package com.petcare.controllers;

import com.petcare.controller.LoginServlet;
import com.petcare.controller.RegisterServlet;
import com.petcare.daos.AccountDAO;
import com.petcare.models.Users;
import com.petcare.services.AuthService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * =========================================================================
 * [Y3] UNIT TEST JUNIT 5 - KIỂM TRA BIÊN FORM & NGOẠI LỆ NGHIỆP VỤ
 * - Sử dụng @ParameterizedTest + @CsvSource kiểm tra biên Form
 * - Sử dụng assertThrows() kiểm thử ngoại lệ bắt lỗi nghiệp vụ
 * =========================================================================
 */
public class ControllerFormAndExceptionTest {

    private static class TestableLoginServlet extends LoginServlet {
        void invokePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            doPost(req, resp);
        }
    }

    private static class TestableRegisterServlet extends RegisterServlet {
        void invokePost(HttpServletRequest req, HttpServletResponse resp) throws Exception {
            doPost(req, resp);
        }
    }

    private AccountDAO accountDAO;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        accountDAO = Mockito.mock(AccountDAO.class);
        authService = new AuthService(accountDAO);
    }

    @ParameterizedTest(name = "TC_FORM_LOGIN_{index}: identifier=\"{0}\", password=\"{1}\"")
    @DisplayName("TC_FORM_LOGIN: Kiểm tra biên Form đăng nhập với @ParameterizedTest + @CsvSource")
    @CsvSource({
            "'', '123456', 'Vui lòng nhập đầy đủ số điện thoại/email và mật khẩu.'",
            "'   ', '123456', 'Vui lòng nhập đầy đủ số điện thoại/email và mật khẩu.'",
            "'user@petcare.vn', '', 'Vui lòng nhập đầy đủ số điện thoại/email và mật khẩu.'",
            "'user@petcare.vn', '1', 'Mật khẩu phải có tối thiểu 6 ký tự.'",
            "'user@petcare.vn', '12345', 'Mật khẩu phải có tối thiểu 6 ký tự.'"
    })
    public void testLoginFormBoundary(String identifier, String password, String expectedError)
            throws Exception {
        // Chuẩn bị dữ liệu giả lập cho request/response và dispatcher của LoginServlet
        TestableLoginServlet servlet = new TestableLoginServlet();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getParameter("identifier")).thenReturn(identifier);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(dispatcher);

        // Gọi doPost với dữ liệu biên từ @CsvSource
        servlet.invokePost(request, response);

        // Kiểm tra thông báo lỗi và chuyển tiếp lại trang login.jsp
        verify(request).setAttribute("errorMessage", expectedError);
        verify(dispatcher).forward(request, response);
    }

    @ParameterizedTest(name = "TC_FORM_REG_{index}: fullName=\"{0}\", phone=\"{1}\"")
    @DisplayName("TC_FORM_REG: Kiểm tra biên Form đăng ký với @ParameterizedTest + @CsvSource")
    @CsvSource({
            "'', '0912345678', '12345678', '12345678', 'Vui lòng điền đầy đủ họ tên, số điện thoại và mật khẩu.'",
            "'Nguyen A', '', '12345678', '12345678', 'Vui lòng điền đầy đủ họ tên, số điện thoại và mật khẩu.'",
            "'Nguyen A', '0912345678', '1234567', '1234567', 'Mật khẩu tạo mới phải có tối thiểu 8 ký tự.'",
            "'Nguyen A', '0912345678', '12345678', '12345679', 'Mật khẩu nhập lại không khớp với mật khẩu vừa tạo.'"
    })
    public void testRegisterFormBoundary(
            String fullName, String phone, String password, String confirmPassword, String expectedError)
            throws Exception {
        // Chuẩn bị dữ liệu giả lập cho form đăng ký
        TestableRegisterServlet servlet = new TestableRegisterServlet();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        RequestDispatcher dispatcher = Mockito.mock(RequestDispatcher.class);

        when(request.getParameter("fullName")).thenReturn(fullName);
        when(request.getParameter("phone")).thenReturn(phone);
        when(request.getParameter("email")).thenReturn("test@petcare.vn");
        when(request.getParameter("password")).thenReturn(password);
        when(request.getParameter("confirmPassword")).thenReturn(confirmPassword);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(dispatcher);

        // Gọi doPost với các giá trị biên (thiếu trường, mật khẩu < 8 ký tự, không khớp)
        servlet.invokePost(request, response);

        // Kiểm tra lỗi trả về đúng thông điệp và forward về /register.jsp
        verify(request).setAttribute("errorMessage", expectedError);
        verify(dispatcher).forward(request, response);
    }

    @Test
    @DisplayName("TC_EX_01: Đăng nhập thất bại khi tài khoản đang bị khóa (assertThrows)")
    public void testAuthenticateLockedAccountThrowsException() throws Exception {
        // Giả lập tài khoản hợp lệ mật khẩu nhưng trạng thái active = false (bị khóa)
        Users lockedUser = new Users();
        lockedUser.setUserId(1);
        lockedUser.setEmail("locked@petcare.vn");
        lockedUser.setPasswordHash("secret123");
        lockedUser.setActive(false);

        when(accountDAO.findByIdentifier("locked@petcare.vn")).thenReturn(lockedUser);

        // Kiểm tra AuthService.authenticate phải ném IllegalArgumentException
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.authenticate("locked@petcare.vn", "secret123"),
                "Phải ném IllegalArgumentException khi tài khoản bị khóa"
        );
        assertEquals("Tài khoản của bạn hiện đang bị khóa. Vui lòng liên hệ quản trị viên.", ex.getMessage());
    }

    @Test
    @DisplayName("TC_EX_02: Đăng ký thất bại khi số điện thoại không hợp lệ (assertThrows)")
    public void testRegisterInvalidPhoneThrowsException() {
        // Truyền SĐT "123" sai định dạng và kiểm tra ngoại lệ nghiệp vụ
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.registerCustomer("Nguyen A", "123", "a@petcare.vn", "password123"),
                "Phải ném IllegalArgumentException khi SĐT sai định dạng"
        );
        assertEquals("Số điện thoại không hợp lệ (ví dụ: 0912345678).", ex.getMessage());
    }

    @Test
    @DisplayName("TC_EX_03: Đăng ký thất bại khi số điện thoại đã tồn tại (assertThrows)")
    public void testRegisterDuplicatePhoneThrowsException() throws Exception {
        // Giả lập SĐT đã tồn tại trong CSDL
        when(accountDAO.existsByPhone("0912345678")).thenReturn(true);

        // Kiểm tra hệ thống từ chối đăng ký trùng SĐT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.registerCustomer("Nguyen A", "0912345678", "a@petcare.vn", "password123"),
                "Phải ném IllegalArgumentException khi SĐT đã được đăng ký"
        );
        assertEquals("Số điện thoại này đã được đăng ký tài khoản.", ex.getMessage());
    }

    @Test
    @DisplayName("TC_EX_04: Đổi mật khẩu thất bại khi mật khẩu hiện tại không đúng (assertThrows)")
    public void testChangePasswordWrongCurrentThrowsException() throws Exception {
        // Giả lập user tồn tại với mật khẩu cũ "correctPass"
        Users user = new Users();
        user.setUserId(5);
        user.setPasswordHash("correctPass");
        when(accountDAO.findById(5)).thenReturn(user);

        // Nhập sai mật khẩu hiện tại ("wrongPass") -> phải ném ngoại lệ
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.changePassword(5, "wrongPass", "newPassword123"),
                "Phải ném IllegalArgumentException khi mật khẩu hiện tại không đúng"
        );
        assertEquals("Mật khẩu hiện tại không chính xác.", ex.getMessage());
    }
}
