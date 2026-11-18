package com.petcare.controller;

import com.petcare.models.Users;
import com.petcare.services.AuthService;
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

class RegisterServletTest {

    private RegisterServlet servlet;
    private AuthService authService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new RegisterServlet();
        authService = Mockito.mock(AuthService.class);

        Field field = RegisterServlet.class.getDeclaredField("authService");
        field.setAccessible(true);
        field.set(servlet, authService);

        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);

        when(request.getContextPath()).thenReturn("/petcare");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);
    }

    @Test
    void doGet_forwardsToRegisterJsp() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setCharacterEncoding("UTF-8");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_missingRequiredFields_setsErrorMessageAndForwards() throws ServletException, IOException {
        when(request.getParameter("fullName")).thenReturn("");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("email")).thenReturn("a@b.com");
        when(request.getParameter("password")).thenReturn("12345678");
        when(request.getParameter("confirmPassword")).thenReturn("12345678");

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Vui lòng điền đầy đủ họ tên, số điện thoại và mật khẩu.");
        verify(dispatcher).forward(request, response);
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_passwordTooShort_setsErrorMessageAndForwards() throws ServletException, IOException {
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("email")).thenReturn("a@b.com");
        when(request.getParameter("password")).thenReturn("1234567");
        when(request.getParameter("confirmPassword")).thenReturn("1234567");

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Mật khẩu tạo mới phải có tối thiểu 8 ký tự.");
        verify(dispatcher).forward(request, response);
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_passwordMismatch_setsErrorMessageAndForwards() throws ServletException, IOException {
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("email")).thenReturn("a@b.com");
        when(request.getParameter("password")).thenReturn("password1");
        when(request.getParameter("confirmPassword")).thenReturn("password2");

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Mật khẩu nhập lại không khớp với mật khẩu vừa tạo.");
        verify(dispatcher).forward(request, response);
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_validRegistration_setsFlashAttributesAndRedirectsToLogin() throws Exception {
        Users created = new Users();
        created.setUserId(1);
        created.setFullName("Nguyen Van A");
        created.setPhone("0912345678");
        created.setEmail("a@petcare.vn");

        when(request.getParameter("fullName")).thenReturn(" Nguyen Van A ");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("email")).thenReturn("a@petcare.vn");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(authService.registerCustomer("Nguyen Van A", "0912345678", "a@petcare.vn", "password123"))
                .thenReturn(created);
        when(request.getSession(true)).thenReturn(session);

        servlet.doPost(request, response);

        verify(session).setAttribute("successMessage", "Đăng ký tài khoản thành công!");
        verify(session).setAttribute("registeredIdentifier", "a@petcare.vn");
        verify(response).sendRedirect("/petcare/login");
    }

    @Test
    void doPost_duplicateKeySqlException_setsDuplicateErrorAndForwards() throws Exception {
        when(request.getParameter("fullName")).thenReturn("Nguyen Van A");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("email")).thenReturn("a@petcare.vn");
        when(request.getParameter("password")).thenReturn("password123");
        when(request.getParameter("confirmPassword")).thenReturn("password123");
        when(authService.registerCustomer("Nguyen Van A", "0912345678", "a@petcare.vn", "password123"))
                .thenThrow(new SQLException("Duplicate key", "23000", 2627));

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Email hoặc số điện thoại này đã được đăng ký.");
        verify(dispatcher).forward(request, response);
    }
}
