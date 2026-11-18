# Nhật ký sửa lỗi (Debug Log) - Dự án PetCare

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)

---

## 1. Các lỗi đã phát hiện & xử lý trong quá trình viết Unit Test

| STT | Vấn đề gặp phải | Nguyên nhân | Cách khắc phục |
|---|---|---|---|
| 1 | `JUnitException: versions of JUnit jars not aligned` khi chạy trực tiếp từ `.m2` | Phiên bản `junit-jupiter-api` (`5.13.4`) và `junit-platform-launcher` (`1.12.2`) lệch nhau trong classpath thủ công | Đồng bộ toàn bộ artifact JUnit Jupiter về `5.12.2` và JUnit Platform về `1.12.2` |
| 2 | ByteBuddy self-attach lỗi pipe trong môi trường bị giới hạn | Mockito tải dynamic agent qua named pipe của JDK | Truyền tường minh `-javaagent:byte-buddy-agent-1.14.12.jar` khi chạy ngoài Maven |
| 3 | `LoginServlet`, `RegisterServlet`, `ProfileServlet` khởi tạo trực tiếp `new AuthService()` | Field `authService` là `private final`, không có constructor truyền mock | Dùng reflection trong `@BeforeEach` để inject `AuthService` mock, đảm bảo test độc lập không cần SQL Server |
| 4 | File tài liệu cũ của dự án V-SPORT còn sót trong `docs/` và `.github/workflows/maven-ci.yml` | Dùng lại khung thư mục từ bài trước | Xóa toàn bộ tài liệu V-SPORT và cập nhật `maven-ci.yml` sang `PetCare CI` |
