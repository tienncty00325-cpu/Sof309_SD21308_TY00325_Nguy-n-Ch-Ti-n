# BẢNG KỊCH BẢN KIỂM THỬ (TEST CASES)

## Thông tin chung
- **Dự án**: Hệ thống Quản lý và Đặt sân Thể thao Trực tuyến **V-SPORT**
- **Sinh viên thực hiện**: Nguyễn Chí Tiến — **MSSV**: TY00325 — **Lớp**: SD21308
- **Tổng số Test Case**: **30 Test Cases** (Gồm **18 Unit Test Cases - JUnit 5** và **12 UI/Functional Test Cases - Selenium WebDriver 4**)

---

## PHẦN 1: KỊCH BẢN KIỂM THỬ ĐƠN VỊ (UNIT TEST CASES — JUNIT 5)

### 1.1. Module Tính giá thuê sân (`CourtPricingServiceTest`)

| Mã TC | Tên kịch bản | Tiền điều kiện | Các bước thực hiện | Dữ liệu đầu vào | Kết quả mong đợi | Ưu tiên | Trạng thái |
|---|---|---|---|---|---|---|---|
| **TC_UT_01** | Tính giá sân khung giờ ban ngày (không đèn) | Khởi tạo `CourtPricingService`, giờ bật đèn `18:00–23:00` | Gọi `calculate()` cho khung giờ hoàn toàn trước `18:00` | `start = 2026-07-15T17:00`<br>`end = 2026-07-15T18:00`<br>`dayRate = 100,000`<br>`nightRate = 150,000` | `totalCourtAmount = 100,000đ`<br>`minutesWithoutLight = 60`<br>`minutesWithLight = 0` | High | Pass |
| **TC_UT_02** | Tính giá sân khung giờ buổi tối (có đèn) | Khởi tạo `CourtPricingService`, giờ bật đèn `18:00–23:00` | Gọi `calculate()` cho khung giờ nằm trong `18:00–23:00` | `start = 2026-07-15T18:00`<br>`end = 2026-07-15T19:00`<br>`dayRate = 100,000`<br>`nightRate = 150,000` | `totalCourtAmount = 150,000đ`<br>`minutesWithoutLight = 0`<br>`minutesWithLight = 60` | High | Pass |
| **TC_UT_03** | Tính giá sân giao thoa 30p ban ngày + 30p có đèn | Khởi tạo `CourtPricingService`, giờ bật đèn `18:00–23:00` | Gọi `calculate()` cho khung giờ vắt qua mốc `18:00` | `start = 2026-07-15T17:30`<br>`end = 2026-07-15T18:30`<br>`dayRate = 100,000`<br>`nightRate = 150,000` | `amountWithoutLight = 50,000đ`<br>`amountWithLight = 75,000đ`<br>`totalCourtAmount = 125,000đ` | High | Pass |
| **TC_UT_04** | Từ chối tính giá khi giờ kết thúc `<=` giờ bắt đầu | Khởi tạo `CourtPricingService` | Gọi `calculate()` với `end == start` hoặc `end < start` | `start = 2026-07-15T19:00`<br>`end = 2026-07-15T18:00` | Ném ngoại lệ `IllegalArgumentException` | High | Pass |
| **TC_UT_05** | Từ chối tính giá khi thiếu đơn giá ngày hoặc đêm | Khởi tạo `CourtPricingService` | Gọi `calculate()` với `dayRate = null` hoặc `nightRate = null` | `dayRate = null`, `nightRate = 150,000` | Ném ngoại lệ `IllegalArgumentException` | Medium | Pass |

---

### 1.2. Module Tính toán Thanh toán & Đặt cọc (`PaymentCalculatorTest`)

| Mã TC | Tên kịch bản | Tiền điều kiện | Các bước thực hiện | Dữ liệu đầu vào | Kết quả mong đợi | Ưu tiên | Trạng thái |
|---|---|---|---|---|---|---|---|
| **TC_UT_06** | Tính tiền còn lại khi chưa đặt cọc | `PaymentCalculator` sẵn sàng | Gọi `remainingAmount(gross, deposit)` | `gross = 500,000`<br>`deposit = 0` | Trả về `500,000` | High | Pass |
| **TC_UT_07** | Tính tiền còn lại khi đã cọc một phần | `PaymentCalculator` sẵn sàng | Gọi `remainingAmount(gross, deposit)` | `gross = 500,000`<br>`deposit = 200,000` | Trả về `300,000` | High | Pass |
| **TC_UT_08** | Giới hạn tiền còn lại về `0` khi tiền cọc vượt tổng tiền | `PaymentCalculator` sẵn sàng | Gọi `remainingAmount(gross, deposit)` với `deposit > gross` | `gross = 500,000`<br>`deposit = 999,999` | Trả về `0` (không trả về số âm) | High | Pass |
| **TC_UT_09** | Kiểm tra `isFullyCovered` khi đã thanh toán đủ và chưa đủ | `PaymentCalculator` sẵn sàng | Gọi `isFullyCovered(gross, paid)` | TH1: `(500000, 500000)`<br>TH2: `(500000, 100000)` | TH1: `true`<br>TH2: `false` | Medium | Pass |

---

### 1.3. Module Chuẩn hóa Số điện thoại (`PhoneUtilTest`)

| Mã TC | Tên kịch bản | Tiền điều kiện | Các bước thực hiện | Dữ liệu đầu vào | Kết quả mong đợi | Ưu tiên | Trạng thái |
|---|---|---|---|---|---|---|---|
| **TC_UT_10** | Giữ nguyên số điện thoại nội địa 10 số hợp lệ | `PhoneUtil` sẵn sàng | Gọi `PhoneUtil.normalizeVN(input)` | `"0786041209"` | Trả về `"0786041209"` | High | Pass |
| **TC_UT_11** | Chuyển đổi đầu số quốc tế `+84` và `84` về `0xxxxxxxxx` | `PhoneUtil` sẵn sàng | Gọi `PhoneUtil.normalizeVN(input)` | `"+84786041209"` và `"84786041209"` | Đều trả về `"0786041209"` | High | Pass |
| **TC_UT_12** | Loại bỏ ký tự phân cách (khoảng trắng, gạch ngang, ngoặc) | `PhoneUtil` sẵn sàng | Gọi `PhoneUtil.normalizeVN(input)` | `"(078) 604.1209"`, `"078-604-1209"` | Đều trả về `"0786041209"` | Medium | Pass |
| **TC_UT_13** | Từ chối các SĐT sai định dạng hoặc chứa ký tự nguy hiểm | `PhoneUtil` sẵn sàng | Gọi `PhoneUtil.normalizeVN(input)` | `null`, `""`, `"012345678"`, `"0286041209"`, `"0786'041209"` | Trả về `null` cho tất cả trường hợp không hợp lệ | High | Pass |

---

### 1.4. Module Phân quyền Portal, Điểm uy tín & Khôi phục mật khẩu (`AuthPortalPolicyTest`, `ReputationLabelTest`, `PasswordResetChallengeTest`)

| Mã TC | Tên kịch bản | Tiền điều kiện | Các bước thực hiện | Dữ liệu đầu vào | Kết quả mong đợi | Ưu tiên | Trạng thái |
|---|---|---|---|---|---|---|---|
| **TC_UT_14** | Kiểm tra quyền truy cập Portal theo Role (`AuthPortalPolicy`) | `AuthPortalPolicy` sẵn sàng | Gọi `isAllowedOnPortal(role, portal)` | Customer vào `CUSTOMER` vs `INTERNAL`; Admin/Manager/Staff vào `INTERNAL` | Customer chỉ được vào `CUSTOMER`; Admin/Manager/Staff chỉ được vào `INTERNAL` | High | Pass |
| **TC_UT_15** | Giới hạn điểm uy tín trong đoạn `[0, 100]` (`CustomerReputationService`) | `CustomerReputationService` sẵn sàng | Gọi `clampScore(score)` với giá trị biên âm và `> 100` | `-20`, `0`, `85`, `100`, `150` | Trả về lần lượt: `0`, `0`, `85`, `100`, `100` | Medium | Pass |
| **TC_UT_16** | Phân loại nhãn uy tín theo điểm số (`ReputationLabel`) | `ReputationLabel` sẵn sàng | Gọi phân loại nhãn theo các mốc điểm `95`, `75`, `55`, `30` | Điểm: `95`, `75`, `55`, `30` | Trả về đúng hạng: Xuất sắc, Tốt, Trung bình, Thấp | Medium | Pass |
| **TC_UT_17** | Kiểm tra xác thực OTP quên mật khẩu & khóa sau khi nhập sai quá số lần (`PasswordResetChallenge`) | Tạo challenge OTP 6 số, hiệu lực 5 phút | Nhập sai OTP liên tiếp quá số lần cho phép rồi nhập lại OTP đúng | OTP đúng: `"123456"`, nhập sai `"000000"` 5 lần | Challenge bị hủy hiệu lực (invalidated), từ chối cả khi nhập lại OTP đúng | High | Pass |
| **TC_UT_18** | Kiểm tra chuẩn hóa phân trang khi `page` âm hoặc chữ (`PaginationUtilsTest`) | Mock `HttpServletRequest` bằng Mockito | Gọi `PaginationUtils` với tham số `page="-5"` và `page="abc"` | `page = "-5"`, `page = "abc"` | Tự động đưa `page` về mặc định là `1`, không ném lỗi `NumberFormatException` | Medium | Pass |

---

## PHẦN 2: KỊCH BẢN KIỂM THỬ GIAO DIỆN & HỆ THỐNG (UI TEST CASES — SELENIUM WEBDRIVER 4)

| Mã TC | Tên kịch bản | Chức năng | Tiền điều kiện | Các bước thực hiện | Dữ liệu đầu vào | Kết quả mong đợi | Ưu tiên | Trạng thái |
|---|---|---|---|---|---|---|---|---|
| **TC_UI_01** | Tải Trang chủ V-SPORT thành công | Trang chủ (`/`) | Web Server đang chạy tại `http://localhost:8080` | 1. Mở trình duyệt Chrome qua Selenium<br>2. Truy cập `http://localhost:8080/`<br>3. Kiểm tra tiêu đề trang và thanh điều hướng | URL: `http://localhost:8080/` | Trang chủ tải thành công (HTTP 200), hiển thị logo/thương hiệu V-SPORT và khu vực tìm kiếm sân | High | Pass |
| **TC_UI_02** | Mở Modal / Trang Đăng nhập khi truy cập `/dangnhap` | Đăng nhập (`/dangnhap`) | Chưa đăng nhập | 1. Truy cập `http://localhost:8080/dangnhap`<br>2. Quan sát URL điều hướng và giao diện form đăng nhập | URL: `http://localhost:8080/dangnhap` | Hệ thống điều hướng về `/index.jsp?auth=login` và hiển thị form nhập Email/SĐT + Mật khẩu | High | Pass |
| **TC_UI_03** | Đăng nhập Khách hàng thất bại khi nhập sai mật khẩu | Đăng nhập (`/dangnhap`) | Chưa đăng nhập | 1. Mở form đăng nhập<br>2. Nhập email hợp lệ và mật khẩu sai<br>3. Bấm nút "Đăng nhập" | Email: `khachhang@test.com`<br>Pass: `SaiMatKhau123` | Hiển thị đúng thông báo lỗi chung: `"Email, số điện thoại hoặc mật khẩu không chính xác."` | High | Pass |
| **TC_UI_04** | Đăng nhập Khách hàng thất bại khi để trống thông tin | Đăng nhập (`/dangnhap`) | Chưa đăng nhập | 1. Mở form đăng nhập<br>2. Để trống trường tài khoản và mật khẩu<br>3. Bấm nút "Đăng nhập" | Tài khoản: `""`<br>Mật khẩu: `""` | Trình duyệt chặn submit (HTML5 validation `required`) hoặc hiển thị thông báo lỗi yêu cầu nhập thông tin | Medium | Pass |
| **TC_UI_05** | Kiểm tra hiển thị Cổng đăng nhập Nội bộ (`/he-thong/dang-nhap`) | Đăng nhập Nội bộ | Chưa đăng nhập | 1. Truy cập `http://localhost:8080/he-thong/dang-nhap`<br>2. Kiểm tra form đăng nhập nội bộ | URL: `http://localhost:8080/he-thong/dang-nhap` | Hiển thị giao diện đăng nhập dành cho Nhân sự / Quản lý / Admin (`DangNhapNoiBo.jsp`) | High | Pass |
| **TC_UI_06** | Chặn tài khoản Khách hàng đăng nhập vào Cổng Nội bộ | Phân quyền Portal (`/he-thong/dang-nhap`) | Có tài khoản role `CUSTOMER` | 1. Mở `/he-thong/dang-nhap`<br>2. Nhập tài khoản `CUSTOMER` và mật khẩu đúng<br>3. Bấm "Đăng nhập" | Tài khoản Customer + Portal `INTERNAL` | Bị từ chối đăng nhập và trả về thông báo lỗi chung `"Email, số điện thoại hoặc mật khẩu không chính xác."` | High | Pass |
| **TC_UI_07** | Đăng ký tài khoản thất bại khi mật khẩu xác nhận không khớp | Đăng ký (`/dang-ky`) | Chưa đăng nhập | 1. Mở form Đăng ký<br>2. Nhập Họ tên, Email, SĐT hợp lệ<br>3. Nhập Mật khẩu và Xác nhận mật khẩu khác nhau<br>4. Bấm "Đăng ký" | Pass: `Abc123456`<br>Confirm: `Abc654321` | Hệ thống từ chối tạo tài khoản và hiển thị thông báo mật khẩu xác nhận không khớp | High | Pass |
| **TC_UI_08** | Đăng ký tài khoản thất bại khi Số điện thoại sai định dạng VN | Đăng ký (`/dang-ky`) | Chưa đăng nhập | 1. Mở form Đăng ký<br>2. Nhập SĐT không hợp lệ (`012345` hoặc `abc`)<br>3. Nhập các trường khác hợp lệ và bấm "Đăng ký" | SĐT: `"012345"` | Hệ thống từ chối đăng ký và báo lỗi số điện thoại không hợp lệ | High | Pass |
| **TC_UI_09** | Tìm kiếm sân thể thao theo từ khóa | Tìm kiếm sân (`/customer/tim-kiem`) | Server hoạt động | 1. Truy cập `/customer/tim-kiem`<br>2. Nhập từ khóa vào ô tìm kiếm và bấm lọc/tìm kiếm | Từ khóa: `"Sân"` | Trang hiển thị danh sách kết quả các cơ sở/sân phù hợp hoặc thông báo danh sách sân rõ ràng, không lỗi 500 | High | Pass |
| **TC_UI_10** | Chặn người dùng chưa đăng nhập truy cập trang Quản trị (`/admin/dashboard`) | Bảo mật Filter (`FilterQuyenAdmin`) | Chưa đăng nhập (Guest) | 1. Xóa toàn bộ cookie/session<br>2. Truy cập trực tiếp `http://localhost:8080/admin/dashboard` | URL: `http://localhost:8080/admin/dashboard` | Hệ thống tự động chặn và redirect người dùng về trang đăng nhập, không cho xem nội dung Admin | High | Pass |
| **TC_UI_11** | Chặn người dùng chưa đăng nhập truy cập trang Quản lý cơ sở (`/manager/dashboard`) | Bảo mật Filter (`FilterQuyenManager`) | Chưa đăng nhập (Guest) | 1. Xóa toàn bộ cookie/session<br>2. Truy cập trực tiếp `http://localhost:8080/manager/dashboard` | URL: `http://localhost:8080/manager/dashboard` | Hệ thống tự động chặn và redirect người dùng về trang đăng nhập | High | Pass |
| **TC_UI_12** | Chặn người dùng chưa đăng nhập truy cập trang Hồ sơ cá nhân (`/customer/ho-so`) | Bảo mật Filter (`FilterBaoMat`) | Chưa đăng nhập (Guest) | 1. Xóa toàn bộ cookie/session<br>2. Truy cập trực tiếp `http://localhost:8080/customer/ho-so` | URL: `http://localhost:8080/customer/ho-so` | Hệ thống tự động redirect về trang đăng nhập kèm tham số `redirect` để quay lại sau khi login | High | Pass |

