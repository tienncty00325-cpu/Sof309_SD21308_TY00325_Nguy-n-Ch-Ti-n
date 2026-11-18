# KẾ HOẠCH KIỂM THỬ (TEST PLAN)



## 1. Mục tiêu kiểm thử (Test Objectives)
1. Đảm bảo các nghiệp vụ cốt lõi của hệ thống **V-SPORT** (Xác thực người dùng, Chuẩn hóa dữ liệu SĐT/Thời gian, Tính giá sân theo khung giờ có đèn/không đèn, Tính tiền cọc & thanh toán, Điểm uy tín khách hàng, Đặt sân & Hoàn tiền) hoạt động chính xác theo đặc tả trong [`SPECIFICATIONS.md`](file:///c:/Users/Admin/Documents/GitHub/Sof309_SD21308_TY00325_Nguy-n-Ch-Ti-n/docs/SPECIFICATIONS.md).
2. Phát hiện sớm các lỗi logic ở tầng `Service` / `Util` thông qua kiểm thử đơn vị (**Unit Testing** với **JUnit 5** & **Mockito**).
3. Đảm bảo luồng tương tác người dùng trên trình duyệt (Đăng nhập, Đăng ký, Tìm kiếm sân, Xem chi tiết sân, Điều hướng phân quyền) hoạt động ổn định thông qua kiểm thử tự động giao diện (**UI Automation Testing** với **Selenium WebDriver 4**).

---

## 2. Phạm vi kiểm thử (Test Scope)

### 2.1. Trong phạm vi (In-Scope)
- **Kiểm thử đơn vị (Unit Test - JUnit 5)**:
  - `CourtPricingService`: Tính giá thuê sân ban ngày, buổi tối (có đèn), khung giờ giao thoa và kiểm tra ngoại lệ đầu vào.
  - `PaymentCalculator`: Tính số tiền còn phải trả sau đặt cọc, kiểm tra trạng thái thanh toán đủ (`isFullyCovered`).
  - `PhoneUtil`: Chuẩn hóa và kiểm tra hợp lệ số điện thoại Việt Nam (`normalizeVN`, `isValidVN`, `lookupVariants`).
  - `AuthPortalPolicy`: Kiểm tra chính sách phân tách cổng đăng nhập Khách hàng (`CUSTOMER`) và Nội bộ (`INTERNAL`).
  - `CustomerReputationService` & `ReputationLabel`: Giới hạn điểm uy tín `[0..100]` và phân loại nhãn uy tín.
  - `PasswordResetChallenge` & `SimpleRateLimiter`: Kiểm tra OTP quên mật khẩu, giới hạn số lần nhập sai và chống brute-force.
  - `PaginationUtils` & `PageResult`: Kiểm tra tính toán phân trang dữ liệu.
- **Kiểm thử giao diện & hệ thống (UI / Functional Test - Selenium WebDriver 4)**:
  - Trang chủ & Điều hướng (`/`, `/index.jsp`).
  - Chức năng Đăng nhập Khách hàng & Đăng nhập Nội bộ (`/dangnhap`, `/he-thong/dang-nhap`).
  - Chức năng Đăng ký tài khoản (`/dang-ky`).
  - Chức năng Tìm kiếm sân & Xem chi tiết sân (`/customer/tim-kiem`, `/customer/chi-tiet-san`).
  - Kiểm tra bảo mật điều hướng khi chưa đăng nhập (Filter chặn truy cập `/admin/*`, `/manager/*`, `/customer/ho-so`).

### 2.2. Ngoài phạm vi (Out-of-Scope)
- Kiểm thử tải nặng / chịu tải (Load/Stress Testing) trên hạ tầng Cloud thực tế.
- Kiểm thử cổng thanh toán ngân hàng thật bên ngoài môi trường Sandbox của PayOS và dịch vụ lưu trữ ảnh bên thứ ba (Cloudinary API).

---

## 3. Chiến lược & Phương pháp kiểm thử (Test Strategy)

| Cấp độ kiểm thử | Phương pháp & Kỹ thuật | Công cụ thực thi | Đối tượng áp dụng |
|---|---|---|---|
| **Unit Testing** | Hộp trắng (White-box), Phân vùng tương đương (Equivalence Partitioning), Phân tích giá trị biên (Boundary Value Analysis) | **JUnit 5 (`5.10.2`)**, **Mockito (`5.11.0`)**, Maven Surefire | Các class trong `org.example.service.*` và `org.example.util.*` |
| **Integration & UI Testing** | Hộp đen (Black-box), Kiểm thử theo kịch bản người dùng (End-to-End User Flow) | **Selenium WebDriver 4 (`4.18.1`)**, **WebDriverManager (`5.7.0`)**, ChromeDriver | Các trang JSP/Servlet chạy trên `http://localhost:8080` |

---

## 4. Môi trường kiểm thử (Test Environment)

| Thành phần | Cấu hình chi tiết |
|---|---|
| **Hệ điều hành** | Windows 10 / Windows 11 (64-bit) |
| **JDK** | Java Development Kit (JDK) 17 |
| **Build Tool** | Apache Maven 3.8+ (`maven-surefire-plugin 3.2.5`) |
| **Web Server** | Apache Tomcat 10.1+ (Jakarta EE 10 / Servlet 6.0) tại `http://localhost:8080` |
| **Cơ sở dữ liệu** | MySQL 8.x / Microsoft SQL Server (Schema `V_sport_script_V2.sql`) |
| **Trình duyệt Test** | Google Chrome (phiên bản mới nhất, điều khiển qua Selenium WebDriver 4) |
| **IDE** | IntelliJ IDEA / VS Code |

---

## 5. Tiêu chí Kiểm thử (Test Criteria)

### 5.1. Tiêu chí bắt đầu (Entry Criteria)
- Mã nguồn dự án V-SPORT đã được đưa vào cấu trúc Maven chuẩn (`src/main/java`, `src/main/webapp`, `src/test/java`) và biên dịch thành công (`mvn test-compile`).
- File `pom.xml` đã tích hợp đầy đủ thư viện **JUnit 5** và **Selenium WebDriver 4**.
- Cơ sở dữ liệu và Web Server khởi chạy ổn định tại `http://localhost:8080`.

### 5.2. Tiêu chí kết thúc (Exit Criteria)
- 100% các kịch bản kiểm thử trong [`TEST_CASES.md`](file:///c:/Users/Admin/Documents/GitHub/Sof309_SD21308_TY00325_Nguy-n-Ch-Ti-n/docs/TEST_CASES.md) được thiết kế và thực thi.
- Tỷ lệ Pass của bộ Unit Test đạt **100%** khi chạy lệnh `mvn test`.
- Không còn lỗi mức độ **Critical** hoặc **High** chưa được khắc phục.

### 5.3. Tiêu chí tạm dừng & Tiếp tục (Suspension & Resumption Criteria)
- **Tạm dừng**: Khi Server `localhost:8080` không phản hồi, lỗi kết nối Database hoặc lỗi biên dịch Maven làm gián đoạn toàn bộ luồng test.
- **Tiếp tục**: Sau khi khắc phục sự cố môi trường/build và xác nhận Smoke Test trang chủ thành công.

---

## 6. Lịch trình thực hiện (Test Schedule)

| Giai đoạn | Công việc cụ thể | Sản phẩm bàn giao |
|---|---|---|
| **Giai đoạn 1 (Y1)** | Đưa mã nguồn Web V-SPORT vào dự án Maven, cấu hình JUnit 5 & Selenium 4, chạy thử Web tại `http://localhost:8080` | Mã nguồn dự án, `pom.xml` |
| **Giai đoạn 2 (Y2)** | Phân tích yêu cầu, lập Đặc tả, Kế hoạch kiểm thử và Bảng kịch bản kiểm thử | `docs/SPECIFICATIONS.md`, `docs/TEST_PLAN.md`, `docs/TEST_CASES.md` |
| **Giai đoạn 3 (Y3)** | Viết và thực thi bộ Unit Test với JUnit 5 & Mockito | Các class `*Test.java` trong `src/test/java` |
| **Giai đoạn 4 (Y4)** | Viết và thực thi bộ kiểm thử tự động giao diện với Selenium WebDriver 4 | Các script Selenium Test & Báo cáo kết quả |

---

## 7. Rủi ro & Biện pháp giảm thiểu (Risks & Mitigation)

| Rủi ro | Mức độ | Biện pháp giảm thiểu |
|---|---|---|
| Phiên bản ChromeDriver không tương thích với trình duyệt Chrome trên máy | Trung bình | Sử dụng `WebDriverManager` (`io.github.bonigarcia:webdrivermanager`) hoặc Selenium Manager tích hợp sẵn trong Selenium 4 để tự động tải driver khớp phiên bản. |
| Phần tử giao diện (DOM) tải chậm gây lỗi `NoSuchElementException` khi chạy Selenium | Cao | Sử dụng `WebDriverWait` và `ExpectedConditions` (Explicit Wait) thay vì dùng `Thread.sleep()` cố định. |
| Phụ thuộc Database khi chạy Unit Test làm chậm tốc độ test | Trung bình | Tách biệt logic tính toán vào các phương thức thuần (pure methods) và dùng **Mockito** để giả lập (mock) `HttpServletRequest` / `DAO`. |

