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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ProfileServletTest {

    private ProfileServlet servlet;
    private AuthService authService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new ProfileServlet();
        authService = Mockito.mock(AuthService.class);

        Field field = ProfileServlet.class.getDeclaredField("authService");
        field.setAccessible(true);
        field.set(servlet, authService);

        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);

        when(request.getContextPath()).thenReturn("/petcare");
        when(request.getRequestDispatcher("/profile.jsp")).thenReturn(dispatcher);
    }

    private Users createSampleUser() {
        Users user = new Users();
        user.setUserId(5);
        user.setFullName("Nguyen Chi Tien");
        user.setPhone("0901234567");
        user.setEmail("tien@petcare.vn");
        user.setRole(Users.ROLE_CUSTOMER);
        return user;
    }

    @Test
    void doGet_whenNotLoggedIn_redirectsToLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(response).sendRedirect("/petcare/login");
        verifyNoInteractions(authService);
    }

    @Test
    void doGet_whenLoggedIn_refreshesUserAndForwardsToProfileJsp() throws Exception {
        Users current = createSampleUser();
        Users fresh = createSampleUser();
        fresh.setFullName("Nguyen Chi Tien Updated");

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute(SessionUtil.ATTR_CURRENT_USER)).thenReturn(current);
        when(session.getAttribute("profileSuccess")).thenReturn("OK");
        when(authService.getUserById(5)).thenReturn(fresh);

        servlet.doGet(request, response);

        verify(request).setAttribute("profileSuccess", "OK");
        verify(session).removeAttribute("profileSuccess");
        verify(session).setAttribute(SessionUtil.ATTR_CURRENT_USER, fresh);
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doGet_whenUserNotFoundInDb_clearsSessionAndRedirectsToLogin() throws Exception {
        Users current = createSampleUser();

        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute(SessionUtil.ATTR_CURRENT_USER)).thenReturn(current);
        when(authService.getUserById(5)).thenReturn(null);

        servlet.doGet(request, response);

        verify(session).invalidate();
        verify(response).sendRedirect("/petcare/login");
    }

    @Test
    void doPost_whenNotLoggedIn_redirectsToLogin() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doPost(request, response);

        verify(response).sendRedirect("/petcare/login");
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_updateProfile_refreshesSessionAndRedirectsToProfile() throws Exception {
        Users current = createSampleUser();
        Users updated = createSampleUser();
        updated.setFullName("New Name");

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute(SessionUtil.ATTR_CURRENT_USER)).thenReturn(current);
        when(request.getParameter("action")).thenReturn(null);
        when(request.getParameter("fullName")).thenReturn("New Name");
        when(request.getParameter("phone")).thenReturn("0901234567");
        when(request.getParameter("email")).thenReturn("tien@petcare.vn");
        when(authService.updateProfile(5, "New Name", "0901234567", "tien@petcare.vn")).thenReturn(updated);

        servlet.doPost(request, response);

        verify(session).setAttribute(SessionUtil.ATTR_CURRENT_USER, updated);
        verify(session).setAttribute("profileSuccess", "Cập nhật hồ sơ người dùng thành công!");
        verify(response).sendRedirect("/petcare/profile");
    }

    @Test
    void doPost_changePasswordMismatch_setsProfileErrorInSession() throws Exception {
        Users current = createSampleUser();

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute(SessionUtil.ATTR_CURRENT_USER)).thenReturn(current);
        when(request.getParameter("action")).thenReturn("changePassword");
        when(request.getParameter("currentPassword")).thenReturn("oldpass123");
        when(request.getParameter("newPassword")).thenReturn("newpass123");
        when(request.getParameter("confirmPassword")).thenReturn("different");

        servlet.doPost(request, response);

        verify(session).setAttribute("profileError", "Mật khẩu mới và mật khẩu xác nhận không khớp.");
        verify(response).sendRedirect("/petcare/profile");
        verifyNoInteractions(authService);
    }

    @Test
    void doPost_changePasswordSuccessAndRedirectHome_redirectsToHomeAnchor() throws Exception {
        Users current = createSampleUser();

        when(request.getSession(false)).thenReturn(session);
        when(request.getSession(true)).thenReturn(session);
        when(session.getAttribute(SessionUtil.ATTR_CURRENT_USER)).thenReturn(current);
        when(request.getParameter("action")).thenReturn("changePassword");
        when(request.getParameter("redirectTo")).thenReturn("home");
        when(request.getParameter("currentPassword")).thenReturn("oldpass123");
        when(request.getParameter("newPassword")).thenReturn("newpass123");
        when(request.getParameter("confirmPassword")).thenReturn("newpass123");

        servlet.doPost(request, response);

        verify(authService).changePassword(5, "oldpass123", "newpass123");
        verify(session).setAttribute("profileSuccess", "Đổi mật khẩu thành công!");
        verify(response).sendRedirect("/petcare/home#ho-so-nguoi-dung");
    }
}
