# ĐẶC TẢ YÊU CẦU PHẦN MỀM (SOFTWARE REQUIREMENTS SPECIFICATION)

## Thông tin dự án
- **Tên dự án**: Hệ thống Quản lý và Đặt sân Thể thao Trực tuyến **V-SPORT**
- **Môn học**: Kiểm thử nâng cao (SOF309)
- **Sinh viên thực hiện**: Nguyễn Chí Tiến — **MSSV**: TY00325 — **Lớp**: SD21308
- **Công nghệ sử dụng**: Java 17, Jakarta Servlet/JSP, Hibernate ORM 6.4, MySQL 8.x / SQL Server, Maven, JUnit 5, Mockito, Selenium WebDriver 4.

---

## 1. Giới thiệu tổng quan
**V-SPORT** là nền tảng web hỗ trợ tìm kiếm, đặt lịch sân thể thao (bóng đá, cầu lông, tennis, pickleball...), quản lý vận hành chi nhánh thể thao, thanh toán trực tuyến qua mã QR (PayOS / VietQR), gọi dịch vụ tại sân qua QR và kết nối cộng đồng thể thao (đội nhóm, ghép kèo).

---

## 2. Phân quyền người dùng (Actors & Roles)

| Vai trò | Khu vực truy cập | Mô tả quyền hạn chính |
|---|---|---|
| **Khách vãng lai (Guest)** | `/`, `/customer/tim-kiem`, `/dangnhap`, `/dang-ky` | Xem trang chủ, tìm kiếm cơ sở/sân, xem chi tiết sân, đăng ký, đăng nhập. |
| **Khách hàng (Customer)** | `/customer/*` | Đặt sân trực quan, thanh toán QR, hủy lịch/yêu cầu hoàn tiền, quản lý hồ sơ, đổi mật khẩu, xem điểm uy tín, tạo đội nhóm & ghép kèo. |
| **Nhân viên (Staff)** | `/staff/*` | Check-in khách đến sân, xử lý yêu cầu dịch vụ qua QR tại sân, xem lịch ca làm việc, in hóa đơn. |
| **Quản lý cơ sở (Manager)** | `/manager/*` | Quản lý sân (CRUD), bảng giá, quản lý lịch đặt sân, hóa đơn, kho dịch vụ, khuyến mãi, nhân sự, ca làm việc, duyệt hoàn tiền, sinh mã QR sân. |
| **Chủ cơ sở (Owner)** | `/ownerLanding`, `/owner-register` | Đăng ký mở chi nhánh mới trên hệ thống V-SPORT. |
| **Quản trị viên (Admin)** | `/admin/*` | Quản trị toàn hệ thống: duyệt Owner/Chi nhánh, quản lý người dùng, cấu hình PayOS, xem Audit Log và Thùng rác hệ thống. |

---

## 3. Đặc tả Yêu cầu Chức năng (Functional Requirements)

### 3.1. Phân hệ Xác thực & Phân quyền (Authentication & Authorization)
- **FR-AUTH-01 (Đăng nhập)**:
  - Cho phép đăng nhập bằng **Email** hoặc **Số điện thoại Việt Nam** kèm mật khẩu.
  - Phân tách cổng đăng nhập Khách hàng (`/dangnhap` - `CUSTOMER` portal) và Cổng nội bộ (`/he-thong/dang-nhap` - `INTERNAL` portal cho Admin/Manager/Staff).
  - Mọi trường hợp đăng nhập thất bại (sai email/SĐT, sai mật khẩu, sai cổng portal, tài khoản khóa) đều trả về thông báo chuẩn duy nhất: `"Email, số điện thoại hoặc mật khẩu không chính xác."` nhằm chống dò tài khoản (user enumeration).
  - Giới hạn tần suất (Rate Limit): Tối đa **5 lần thất bại / 5 phút** trên mỗi định danh hoặc **20 lần / 5 phút** trên mỗi IP.
- **FR-AUTH-02 (Đăng ký tài khoản)**:
  - Kiểm tra định dạng Họ tên, Email hợp lệ, Số điện thoại Việt Nam hợp lệ (10 chữ số đầu `03, 05, 07, 08, 09` hoặc chuẩn hóa từ `+84`/`84`), mật khẩu tối thiểu 6 ký tự và khớp xác nhận mật khẩu.
  - Mã hóa mật khẩu bằng **BCrypt** trước khi lưu CSDL.
- **FR-AUTH-03 (Quên mật khẩu & Đổi mật khẩu)**:
  - Hỗ trợ gửi mã OTP khôi phục mật khẩu qua email với giới hạn số lần thử tối đa và thời gian hết hạn OTP.

### 3.2. Phân hệ Tìm kiếm & Đặt sân (Search & Court Booking)
- **FR-BOOK-01 (Tìm kiếm & Xem chi tiết sân)**:
  - Cho phép lọc sân theo từ khóa, môn thể thao, khu vực, khoảng giá và xem chi tiết cơ sở, danh sách sân con và đánh giá.
- **FR-BOOK-02 (Tính giá thuê sân theo khung giờ — `CourtPricingService`)**:
  - Sân có 2 mức giá: **Giá ban ngày (không đèn)** và **Giá buổi tối (có đèn)** dựa theo khung giờ bật đèn (`lightStart` đến `lightEnd`, ví dụ `18:00` – `23:00`).
  - Nếu khung giờ đặt sân giao giữa giờ ban ngày và giờ bật đèn (ví dụ `17:30` – `18:30`), hệ thống tự động tách số phút không đèn và số phút có đèn để tính tổng tiền chính xác theo từng phút.
  - Từ chối đặt sân nếu thời gian kết thúc `<=` thời gian bắt đầu (`IllegalArgumentException`) hoặc thiếu cấu hình bảng giá.
- **FR-BOOK-03 (Kiểm tra trùng lịch & Giữ chỗ tạm — `SoftHold` / `BookingCreationService`)**:
  - Không cho phép 2 khách hàng đặt trùng cùng một sân trong cùng khung giờ đã được xác nhận hoặc đang trong thời gian giữ chỗ tạm (`SoftHold`).

### 3.3. Phân hệ Thanh toán & Hoàn tiền (Payment & Refund)
- **FR-PAY-01 (Tính toán số tiền thanh toán — `PaymentCalculator`)**:
  - Số tiền còn phải thanh toán (`remainingAmount`) = `Tổng tiền gốc (gross) - Tiền đã cọc (deposit)`.
  - Nếu tiền cọc `>=` tổng tiền gốc, số tiền còn lại được giới hạn về `0` (không bao giờ âm).
  - Trạng thái `isFullyCovered = true` khi số tiền còn lại bằng `0`.
- **FR-PAY-02 (Thanh toán QR PayOS / VietQR)**:
  - Tạo phiên thanh toán gắn với `orderCode = DatSanID`, sinh mã VietQR động và tự động cập nhật trạng thái đơn đặt sân khi nhận Webhook xác nhận từ PayOS.
- **FR-PAY-03 (Hủy đặt sân & Hoàn tiền — `BookingCancellationService` / `RefundService`)**:
  - Tính tỷ lệ hoàn tiền theo chính sách thời gian hủy trước giờ bóng lăn và tạo yêu cầu hoàn tiền cho Manager duyệt.

### 3.4. Phân hệ Điểm uy tín & Tiện ích Hệ thống (Reputation & Utilities)
- **FR-REP-01 (Điểm uy tín khách hàng — `CustomerReputationService`)**:
  - Điểm uy tín luôn được giới hạn (clamp) trong khoảng **[0, 100]**.
  - Phân hạng nhãn uy tín (`ReputationLabel`):
    - `90 – 100`: Xuất sắc (Excellent)
    - `70 – 89`: Tốt (Good)
    - `50 – 69`: Trung bình (Fair)
    - `< 50`: Thấp / Cảnh báo (Low / Warning)
- **FR-UTIL-01 (Chuẩn hóa Số điện thoại — `PhoneUtil`)**:
  - Tự động loại bỏ khoảng trắng, dấu gạch ngang, dấu chấm, ngoặc đơn và chuyển đầu số `+84` hoặc `84` về chuẩn nội địa 10 số `0xxxxxxxxx`.
  - Từ chối các số cố định (`02x`), đầu số cũ 11 số (`01x`), chuỗi chứa chữ cái hoặc ký tự đặc biệt chống SQL Injection.
- **FR-UTIL-02 (Phân trang dữ liệu — `PaginationUtils` / `PageResult`)**:
  - Chuẩn hóa tham số `page` (mặc định `1` nếu âm, bằng 0 hoặc không phải số) và `pageSize` (giới hạn trong ngưỡng an toàn).

---

## 4. Yêu cầu Phi chức năng (Non-Functional Requirements)
1. **Bảo mật (Security)**:
   - Mật khẩu băm bằng BCrypt; chống Timing Attack khi đăng nhập bằng `DUMMY_BCRYPT_HASH`.
   - Bảo vệ chống CSRF (`CsrfFilter`), phân quyền theo URL Filter (`FilterQuyenAdmin`, `FilterQuyenManager`, `FilterCustomerArea`).
2. **Hiệu năng (Performance)**:
   - Sử dụng HikariCP Connection Pool đảm bảo thời gian phản hồi trang trung bình `< 2 giây`.
3. **Khả năng kiểm thử (Testability)**:
   - Tách biệt tầng `service` và `util` độc lập với Servlet để kiểm thử đơn vị (Unit Test với JUnit 5 & Mockito) đạt độ phủ cao, kết hợp kiểm thử giao diện tự động bằng Selenium WebDriver 4.

