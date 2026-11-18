package com.petcare.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * =========================================================================
 * [Y3] UNIT TEST JUNIT 5 CHO CONTROLLER - TrangChuServlet
 * =========================================================================
 */
public class TrangChuServletTest {

    private TrangChuServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        servlet = new TrangChuServlet();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
    }

    @Test
    @DisplayName("TC_HOME_01: Truy cập TrangChuServlet bằng GET phải forward về /index.jsp")
    public void testDoGetForwardsToIndexJsp() throws ServletException, IOException {
        // Giả lập dispatcher cho trang chủ /index.jsp
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);

        // Gọi doGet trên TrangChuServlet
        servlet.doGet(request, response);

        // Kiểm tra servlet forward đúng tới /index.jsp
        verify(request).getRequestDispatcher("/index.jsp");
        verify(dispatcher).forward(request, response);
    }
}
