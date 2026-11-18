package com.petcare.controller;

import com.petcare.models.Users;
import com.petcare.services.AuthService;
import com.petcare.utils.SessionUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class LoginServletTest {

    private LoginServlet servlet;
    private AuthService authService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LoginServlet();
        authService = Mockito.mock(AuthService.class);

        Field field = LoginServlet.class.getDeclaredField("authService");
        field.setAccessible(true);
        field.set(servlet, authService);

        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);

        when(request.getContextPath()).thenReturn("/petcare");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);
    }

    @Test
    void doGet_withoutSession_forwardsToLoginJsp() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(dispatcher).forward(request, response);
    }

    @Test
    void doGet_withFlashAttributes_popsFromSessionAndForwards() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("successMessage")).thenReturn("Đăng ký thành công!");
        when(session.getAttribute("registeredIdentifier")).thenReturn("user@petcare.vn");

        servlet.doGet(request, response);

        verify(request).setAttribute("successMessage", "Đăng ký thành công!");
        verify(session).removeAttribute("successMessage");
        verify(request).setAttribute("identifier", "user@petcare.vn");
        verify(session).removeAttribute("registeredIdentifier");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_blankIdentifier_setsErrorMessageAndForwards() throws ServletException, IOException {
        when(request.getParameter("identifier")).thenReturn("   ");
        when(request.getParameter("password")).thenReturn("123456");

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Vui lòng nhập đầy đủ số điện thoại/email và mật khẩu.");
        verify(request).setAttribute("identifier", "");
        verify(dispatcher).forward(request, response);
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_shortPassword_setsErrorMessageAndForwards() throws ServletException, IOException {
        when(request.getParameter("identifier")).thenReturn("0912345678");
        when(request.getParameter("password")).thenReturn("12345");

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Mật khẩu phải có tối thiểu 6 ký tự.");
        verify(request).setAttribute("identifier", "0912345678");
        verify(dispatcher).forward(request, response);
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_validCredentialsWithRememberMe_storesSessionAndRedirectsHome() throws Exception {
        Users user = new Users();
        user.setUserId(10);
        user.setFullName("Test User");
        user.setEmail("test@petcare.vn");
        user.setPhone("0912345678");
        user.setRole(Users.ROLE_CUSTOMER);

        when(request.getParameter("identifier")).thenReturn(" test@petcare.vn ");
        when(request.getParameter("password")).thenReturn("secret123");
        when(request.getParameter("remember")).thenReturn("on");
        when(authService.authenticate("test@petcare.vn", "secret123")).thenReturn(user);
        when(request.getSession(false)).thenReturn(null);
        when(request.getSession(true)).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).setAttribute(SessionUtil.ATTR_CURRENT_USER, user);
        verify(session).setAttribute(SessionUtil.ATTR_USER_ID, 10);
        verify(session).setMaxInactiveInterval(7 * 24 * 60 * 60);
        verify(response).sendRedirect("/petcare/home");
    }

    @Test
    void doPost_invalidCredentials_setsErrorMessageAndForwards() throws Exception {
        when(request.getParameter("identifier")).thenReturn("test@petcare.vn");
        when(request.getParameter("password")).thenReturn("wrongpass");
        when(authService.authenticate("test@petcare.vn", "wrongpass"))
                .thenThrow(new IllegalArgumentException("Sai mật khẩu"));

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Sai mật khẩu");
        verify(request).setAttribute("identifier", "test@petcare.vn");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_sqlPermissionError229_setsFriendlySqlPermissionMessage() throws Exception {
        when(request.getParameter("identifier")).thenReturn("test@petcare.vn");
        when(request.getParameter("password")).thenReturn("secret123");
        when(authService.authenticate("test@petcare.vn", "secret123"))
                .thenThrow(new SQLException("SELECT permission denied", "42000", 229));

        servlet.doPost(request, response);

        verify(request).setAttribute(
                "errorMessage",
                "Tài khoản SQL 'petcare_backend' chưa được cấp quyền SELECT trên bảng dbo.Users. "
                        + "Vui lòng chạy file database/grant_auth_permissions.sql trên SQL Server."
        );
        verify(dispatcher).forward(request, response);
    }
}
