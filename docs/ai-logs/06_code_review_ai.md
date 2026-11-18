# Rà soát mã nguồn & Kết quả kiểm thử (Code Review) - Dự án PetCare

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)

---

## 1. Kết quả rà soát mã nguồn & Unit Test

- Tất cả các Controller chính (`DangXuatServlet`, `TrangChuServlet`, `HomeServlet`, `LoginServlet`, `RegisterServlet`, `ProfileServlet`) đều đã có Unit Test tương ứng, cô lập hoàn toàn khỏi SQL Server bằng Mockito.
- Đã áp dụng đầy đủ kỹ thuật theo yêu cầu Y3:
  - `@ParameterizedTest` + `@CsvSource` kiểm tra biên Form đăng nhập và đăng ký (`ControllerFormAndExceptionTest`).
  - `assertThrows()` kiểm tra ngoại lệ nghiệp vụ trong `AuthService`.
  - `@DisplayName` đánh mã Test Case (`TC_*`) rõ ràng và chú thích tiếng Việt chi tiết trong từng hàm test.
- Kết quả chạy thực tế: **42/42 test pass** (`0 failed`, `0 skipped`).
