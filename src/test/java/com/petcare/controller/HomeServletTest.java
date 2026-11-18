package com.petcare.controller;

import com.petcare.model.BookingRequest;
import com.petcare.model.ReviewItem;
import com.petcare.model.ServiceCard;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HomeServletTest {

    private HomeServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private RequestDispatcher dispatcher;
    private HttpSession session;

    @BeforeEach
    void setUp() {
        servlet = new HomeServlet();
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        dispatcher = Mockito.mock(RequestDispatcher.class);
        session = Mockito.mock(HttpSession.class);
        when(request.getRequestDispatcher("/index.jsp")).thenReturn(dispatcher);
    }

    @Test
    void buildDefaultServices_returnsSixServices() {
        List<ServiceCard> services = HomeServlet.buildDefaultServices();
        assertNotNull(services);
        assertEquals(6, services.size());
    }

    @Test
    void buildDefaultReviews_returnsThreeReviews() {
        List<ReviewItem> reviews = HomeServlet.buildDefaultReviews();
        assertNotNull(reviews);
        assertEquals(3, reviews.size());
    }

    @Test
    void doGet_loadsHomeDataAndForwardsToIndex() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setCharacterEncoding("UTF-8");
        verify(response).setCharacterEncoding("UTF-8");
        verify(request).setAttribute(eq("services"), Mockito.anyList());
        verify(request).setAttribute(eq("reviews"), Mockito.anyList());
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doGet_popsProfileFlashAttributesFromSession() throws ServletException, IOException {
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("profileSuccess")).thenReturn("Cập nhật thành công");
        when(session.getAttribute("profileError")).thenReturn("Có lỗi");

        servlet.doGet(request, response);

        verify(request).setAttribute("profileSuccess", "Cập nhật thành công");
        verify(session).removeAttribute("profileSuccess");
        verify(request).setAttribute("profileError", "Có lỗi");
        verify(session).removeAttribute("profileError");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void doPost_createsBookingRequestAndForwardsWithSuccess() throws ServletException, IOException {
        when(request.getParameter("ownerName")).thenReturn("Nguyen Van A");
        when(request.getParameter("phone")).thenReturn("0912345678");
        when(request.getParameter("petName")).thenReturn("Miu");
        when(request.getParameter("petType")).thenReturn("Mèo");
        when(request.getParameter("weightRange")).thenReturn("Dưới 5kg");
        when(request.getParameter("serviceName")).thenReturn("Tắm spa");
        when(request.getParameter("branch")).thenReturn("Quận 3");
        when(request.getParameter("bookingDate")).thenReturn("2026-11-20");
        when(request.getParameter("timeSlot")).thenReturn("09:00");
        when(request.getParameter("notes")).thenReturn("Bé hơi nhát");

        servlet.doPost(request, response);

        verify(request).setAttribute("bookingSuccess", true);
        ArgumentCaptor<BookingRequest> captor = ArgumentCaptor.forClass(BookingRequest.class);
        verify(request).setAttribute(eq("bookingData"), captor.capture());

        BookingRequest captured = captor.getValue();
        assertEquals("Nguyen Van A", captured.getOwnerName());
        assertEquals("0912345678", captured.getPhone());
        assertEquals("Miu", captured.getPetName());
        assertEquals("09:00", captured.getTimeSlot());

        verify(request).setAttribute(eq("services"), Mockito.anyList());
        verify(request).setAttribute(eq("reviews"), Mockito.anyList());
        verify(dispatcher).forward(request, response);
    }
}
