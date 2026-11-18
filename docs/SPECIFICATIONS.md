# ĐẶC TẢ HỆ THỐNG — PETCARE

- **Tên dự án**: Hệ thống Quản lý & Đặt lịch Chăm sóc Thú cưng Trực tuyến **PetCare**
- **Công nghệ**: Java 17, Jakarta Servlet 6.0 / JSP, JSTL, SQL Server, Maven, JUnit 5 & Mockito
- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)

---

## 1. Tổng quan hệ thống
**PetCare** là ứng dụng web hỗ trợ khách hàng tìm hiểu dịch vụ spa/grooming, đặt lịch chăm sóc thú cưng, quản lý tài khoản cá nhân và hỗ trợ nhân viên/quản trị viên vận hành cửa hàng.

---

## 2. Danh sách Controller & Chức năng chính

| Controller / Endpoint | URL Pattern | Mô tả chức năng |
|---|---|---|
| `HomeServlet` | `/home`, `/trang-chu`, `/dat-lich` | Hiển thị danh sách dịch vụ, đánh giá khách hàng, tiếp nhận yêu cầu đặt lịch nhanh (`POST`) |
| `TrangChuServlet` | `/` | Forward về trang chủ `/index.jsp` |
| `LoginServlet` | `/login`, `/dang-nhap` | Hiển thị form đăng nhập, kiểm tra dữ liệu đầu vào, xác thực qua `AuthService`, lưu session (`rememberMe`) |
| `RegisterServlet` | `/register`, `/dang-ky` | Kiểm tra form đăng ký, tạo tài khoản khách hàng qua `AuthService.registerCustomer`, chuyển hướng `/login` |
| `ProfileServlet` | `/profile`, `/ho-so` | Bảo vệ truy cập người dùng đã đăng nhập, làm mới thông tin, cập nhật hồ sơ & đổi mật khẩu |
| `DangXuatServlet` | `/logout`, `/dang-xuat` | Hủy session hiện tại và chuyển hướng về `/login` |

---

## 3. Quy tắc nghiệp vụ cốt lõi (`AuthService` & Validation)
1. **Đăng nhập (`LoginServlet` & `AuthService.authenticate`)**:
   - Không được để trống `identifier` (SĐT/Email) hoặc `password`.
   - Mật khẩu tối thiểu **6 ký tự** ở bước kiểm tra form đăng nhập.
   - Tài khoản bị khóa (`isActive = false`) không được phép đăng nhập.
   - Khi chọn `remember=on`, thời gian sống session là **7 ngày** (`7 * 24 * 60 * 60` giây); mặc định **30 phút**.
2. **Đăng ký (`RegisterServlet` & `AuthService.registerCustomer`)**:
   - Bắt buộc nhập `fullName`, `phone`, `password`.
   - Số điện thoại phải đúng định dạng Việt Nam (ví dụ `0912345678`).
   - Mật khẩu tạo mới tối thiểu **8 ký tự** và phải khớp `confirmPassword`.
   - Không cho phép trùng số điện thoại hoặc email đã tồn tại.
3. **Hồ sơ & Đổi mật khẩu (`ProfileServlet` & `AuthService.changePassword`)**:
   - Chưa đăng nhập phải chuyển hướng về `/login`.
   - Đổi mật khẩu yêu cầu mật khẩu hiện tại chính xác, mật khẩu mới tối thiểu 8 ký tự và khớp xác nhận.
