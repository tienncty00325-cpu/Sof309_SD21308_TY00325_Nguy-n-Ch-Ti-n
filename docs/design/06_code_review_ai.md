# Rà soát mã nguồn & Kết quả kiểm thử (Code Review) - Dự án V-SPORT

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)

---

## 1. Những điểm đã làm tốt
- Các lớp nghiệp vụ tính toán (`CourtPricingService`, `PaymentCalculator`, `PhoneUtil`, `CustomerReputationService`) được tách riêng khỏi tầng Servlet nên dễ viết Unit Test bằng JUnit 5 mà không cần dựng server.
- Sử dụng Mockito trong `PaginationUtilsTest` để giả lập `HttpServletRequest`, tránh phụ thuộc vào môi trường Tomcat.
- Cấu trúc Selenium áp dụng Page Object Model (`BasePage`, `HomePage`, `LoginPage`) giúp code test gọn và dễ bảo trì.
- Đã xử lý triệt để lỗi biên dịch ở `pom.xml` (thiếu `cloudinary-core`, `slf4j-api`) và đồng bộ logging sang `Log4j2` trong `AuditLogDAOImpl`, `AuditLogService`, `BookingLifecycleService`.

---

## 2. Những điểm cần cải thiện thêm
- Một số class test cũ trong `src/test/java/org/example/` (như `ListTablesTest`, `RunMigrationTest`) vẫn gọi trực tiếp `DBUtil.getConnection()`, dễ gây nhầm lẫn với Unit Test thuần; nên tách riêng sang nhóm Integration Test hoặc gắn `@Tag("db")` để lọc khi chạy Maven.
- `BookingLifecycleService` vẫn còn viết câu SQL trực tiếp bằng JDBC thay vì đưa xuống tầng DAO; về sau cần refactor xuống DAO để dễ mock và test đơn vị hơn.
- Chưa có dữ liệu tài khoản test cố định trong script seed riêng cho Selenium để test luồng đặt sân End-to-End sau khi đăng nhập thành công.
