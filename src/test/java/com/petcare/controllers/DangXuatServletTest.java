package com.petcare.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * =========================================================================
 * [Y3] UNIT TEST JUNIT 5 CHO CONTROLLER - DangXuatServlet
 * =========================================================================
 */
public class DangXuatServletTest {

    private DangXuatServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        servlet = new DangXuatServlet();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        session = Mockito.mock(HttpSession.class);
        when(request.getContextPath()).thenReturn("/petcare");
    }

    @Test
    @DisplayName("TC_LOGOUT_01: Đăng xuất bằng GET khi có session phải hủy session và chuyển hướng /login")
    public void testDoGetWithActiveSession() throws ServletException, IOException {
        // Giả lập đang có session hoạt động
        when(request.getSession(false)).thenReturn(session);

        // Thực hiện gọi doGet để đăng xuất
        servlet.doGet(request, response);

        // Kiểm tra session bị hủy và chuyển hướng về trang đăng nhập
        verify(session).invalidate();
        verify(response).sendRedirect("/petcare/login");
    }

    @Test
    @DisplayName("TC_LOGOUT_02: Đăng xuất bằng GET khi chưa có session vẫn chuyển hướng /login an toàn")
    public void testDoGetWithoutSession() throws ServletException, IOException {
        // Giả lập chưa có session (null)
        when(request.getSession(false)).thenReturn(null);

        // Thực hiện gọi doGet
        servlet.doGet(request, response);

        // Không gọi invalidate và vẫn chuyển hướng về /login
        verify(session, never()).invalidate();
        verify(response).sendRedirect("/petcare/login");
    }

    @Test
    @DisplayName("TC_LOGOUT_03: Đăng xuất bằng POST phải ủy quyền sang doGet, hủy session và chuyển hướng /login")
    public void testDoPostDelegatesToDoGet() throws ServletException, IOException {
        // Giả lập đang có session hoạt động
        when(request.getSession(false)).thenReturn(session);

        // Thực hiện gọi doPost
        servlet.doPost(request, response);

        // Kiểm tra session bị hủy và chuyển hướng về /login
        verify(session).invalidate();
        verify(response).sendRedirect("/petcare/login");
    }
}
