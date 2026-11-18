# Thiết kế kịch bản kiểm thử (Test Design) - Dự án V-SPORT

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Môn**: SOF309 - Kiểm thử nâng cao
- **Tài liệu liên quan**: [`SPECIFICATIONS.md`](../SPECIFICATIONS.md), [`TEST_PLAN.md`](../TEST_PLAN.md), [`TEST_CASES.md`](../TEST_CASES.md)

---

## 1. Phạm vi thiết kế test
Hệ thống **V-SPORT** có nhiều phân hệ nhưng phần logic dễ phát sinh lỗi nhất nằm ở:
1. **Tính giá sân theo khung giờ (`CourtPricingService`)**: tách phút không đèn và có đèn, kiểm tra trường hợp giờ kết thúc nhỏ hơn hoặc bằng giờ bắt đầu, thiếu đơn giá.
2. **Tính tiền đặt cọc & thanh toán (`PaymentCalculator`)**: đảm bảo tiền còn lại không bị âm khi tiền cọc lớn hơn tổng hóa đơn.
3. **Chuẩn hóa số điện thoại (`PhoneUtil`)**: xử lý đầu số `+84`, `84`, ký tự phân cách và chặn định dạng sai.
4. **Phân quyền & bảo mật đăng nhập (`AuthPortalPolicy`, `PasswordResetChallenge`, `SimpleRateLimiter`)**: tách portal khách hàng và nội bộ, khóa OTP khi nhập sai quá số lần.
5. **Điểm uy tín & phân trang (`CustomerReputationService`, `PaginationUtils`)**: giới hạn điểm trong đoạn `[0, 100]`, xử lý tham số `page` sai định dạng.
6. **Luồng giao diện chính (UI)**: mở trang chủ, form đăng nhập, validation khi để trống, và kiểm tra Filter chặn truy cập trái phép vào `/admin/*`, `/customer/ho-so`.

---

## 2. Kỹ thuật thiết kế áp dụng
- **Phân vùng tương đương (Equivalence Partitioning)**: áp dụng cho SĐT hợp lệ/không hợp lệ, các mốc điểm uy tín (`95`, `75`, `55`, `30`), role theo portal.
- **Phân tích giá trị biên (Boundary Value Analysis)**: áp dụng cho khung giờ giao thoa `18:00` (17:30–18:30, 17:45–18:15), điểm uy tín biên `0` và `100` (`-20`, `150`), tiền cọc bằng và lớn hơn tổng tiền.
- **Đoán lỗi (Error Guessing)**: truyền `null`, chuỗi rỗng, `page="-5"`, `page="abc"`, SĐT chứa dấu nháy đơn `'`.

---

## 3. Phân bổ Test Case
Tổng cộng **30 Test Cases** được quản lý trong [`TEST_CASES.md`](../TEST_CASES.md):
- **18 Unit Test (`TC_UT_01` → `TC_UT_18`)**: chạy bằng JUnit 5 + Mockito, không phụ thuộc DB.
- **12 UI Test (`TC_UI_01` → `TC_UI_12`)**: chạy bằng Selenium WebDriver 4 trên Chrome theo mô hình Page Object Model (POM).
