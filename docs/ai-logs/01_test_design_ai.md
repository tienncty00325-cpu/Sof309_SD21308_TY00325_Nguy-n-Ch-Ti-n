# Thiết kế kịch bản kiểm thử (Test Design) - Dự án PetCare

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Môn**: SOF309 - Kiểm thử nâng cao
- **Dự án**: PetCare (Servlet/JSP + SQL Server)

---

## 1. Phạm vi thiết kế Unit Test (Y3)

Các lớp Unit Test được xây dựng cho tầng Controller (`com.petcare.controllers` và `com.petcare.controller`) sử dụng **JUnit 5** và **Mockito**, không phụ thuộc kết nối cơ sở dữ liệu thật:

1. **`DangXuatServletTest`** (`src/test/java/com/petcare/controllers/DangXuatServletTest.java`):
   - Kiểm tra `doGet` và `doPost` xóa session hiện tại (`session.invalidate()`) và chuyển hướng về `/login`.
   - Kiểm tra trường hợp chưa có session (`getSession(false) == null`) không phát sinh `NullPointerException`.
2. **`TrangChuServletTest`** (`src/test/java/com/petcare/controllers/TrangChuServletTest.java`):
   - Kiểm tra `doGet` forward về `/index.jsp`.
3. **`ControllerFormAndExceptionTest`** (`src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java`):
   - Sử dụng `@ParameterizedTest` + `@CsvSource` để kiểm tra biên dữ liệu đầu vào Form đăng nhập (`LoginServlet`) và Form đăng ký (`RegisterServlet`).
   - Sử dụng `assertThrows(IllegalArgumentException.class, ...)` để kiểm thử ngoại lệ nghiệp vụ trong `AuthService` (tài khoản bị khóa, SĐT không hợp lệ, SĐT đã tồn tại, mật khẩu hiện tại không đúng).
4. **`HomeServletTest`** (`src/test/java/com/petcare/controller/HomeServletTest.java`):
   - Kiểm tra `doGet` nạp danh sách dịch vụ (`services`), đánh giá (`reviews`), xử lý flash attributes (`profileSuccess`, `profileError`) và forward `/index.jsp`.
   - Kiểm tra `doPost` tạo `BookingRequest` và trả về `bookingSuccess = true`.
5. **`LoginServletTest`** (`src/test/java/com/petcare/controller/LoginServletTest.java`):
   - Kiểm tra `doGet` lấy và xóa flash attributes (`successMessage`, `registeredIdentifier`) từ session.
   - Kiểm tra `doPost` với dữ liệu rỗng, mật khẩu < 6 ký tự, đăng nhập thành công (`remember=on` -> timeout 7 ngày), sai thông tin (`IllegalArgumentException`), và lỗi quyền SQL (`SQLException` mã `229`).
6. **`RegisterServletTest`** (`src/test/java/com/petcare/controller/RegisterServletTest.java`):
   - Kiểm tra `doGet` forward `/register.jsp`.
   - Kiểm tra `doPost` thiếu trường bắt buộc, mật khẩu < 8 ký tự, mật khẩu xác nhận không khớp, đăng ký thành công (flash + redirect `/login`), và lỗi trùng khóa SQL (`2627`).
7. **`ProfileServletTest`** (`src/test/java/com/petcare/controller/ProfileServletTest.java`):
   - Kiểm tra chuyển hướng `/login` khi chưa đăng nhập.
   - Kiểm tra `doGet` làm mới thông tin user từ `AuthService`, xử lý user không còn tồn tại trong DB.
   - Kiểm tra `doPost` cập nhật hồ sơ, đổi mật khẩu không khớp, và đổi mật khẩu thành công với `redirectTo=home`.

---

## 2. Kỹ thuật thiết kế áp dụng

- **Phân tích giá trị biên (Boundary Value Analysis) với `@ParameterizedTest` + `@CsvSource`**:
  - Form đăng nhập: `identifier` rỗng / khoảng trắng; `password` độ dài `0`, `1`, `5` (dưới ngưỡng tối thiểu `6`).
  - Form đăng ký: `fullName` rỗng, `phone` rỗng, `password` độ dài `7` (dưới ngưỡng tối thiểu `8`), `confirmPassword` không khớp.
- **Kiểm thử ngoại lệ nghiệp vụ với `assertThrows()`**:
  - Xác minh `AuthService` ném `IllegalArgumentException` kèm thông báo tiếng Việt chính xác khi vi phạm quy tắc nghiệp vụ.
- **Mocking với Mockito**:
  - Giả lập `HttpServletRequest`, `HttpServletResponse`, `HttpSession`, `RequestDispatcher`, `AuthService`, `AccountDAO`.

---

## 3. Lệnh chạy kiểm thử

```bash
mvn test
```
