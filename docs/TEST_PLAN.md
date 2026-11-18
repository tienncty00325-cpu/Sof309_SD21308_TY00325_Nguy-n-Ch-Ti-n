# KẾ HOẠCH KIỂM THỬ (TEST PLAN) — PETCARE

- **Dự án**: PetCare
- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Môn học**: SOF309 - Kiểm thử nâng cao

---

## 1. Mục tiêu kiểm thử (Test Objectives)
1. Đảm bảo các Controller cốt lõi của hệ thống **PetCare** (`HomeServlet`, `TrangChuServlet`, `LoginServlet`, `RegisterServlet`, `ProfileServlet`, `DangXuatServlet`) xử lý đúng request/response, validation form và điều hướng theo đặc tả trong [`SPECIFICATIONS.md`](SPECIFICATIONS.md).
2. Kiểm tra đầy đủ các trường hợp biên của Form bằng `@ParameterizedTest` + `@CsvSource` và ngoại lệ nghiệp vụ bằng `assertThrows()`.
3. Đảm bảo toàn bộ Unit Test chạy độc lập bằng **JUnit 5** & **Mockito** không phụ thuộc SQL Server, đạt **100% Pass**.

---

## 2. Phạm vi kiểm thử (Test Scope)
- **Trong phạm vi (In-Scope)**:
  - Unit Test tầng Controller trong `src/test/java/com/petcare/controllers/` và `src/test/java/com/petcare/controller/`.
  - Kiểm thử ngoại lệ nghiệp vụ tầng `AuthService` (khóa tài khoản, SĐT sai định dạng, trùng SĐT, sai mật khẩu cũ).
- **Ngoài phạm vi (Out-of-Scope)**:
  - Kiểm thử tải nặng (Load/Stress Testing) và kết nối SQL Server trực tiếp trong Unit Test.

---

## 3. Chiến lược & Môi trường kiểm thử

| Thành phần | Chi tiết |
|---|---|
| **Ngôn ngữ & JDK** | Java 17 |
| **Build / Runner** | Apache Maven (`mvn test`) / `run-unit-tests.bat` |
| **Framework Unit Test** | JUnit 5 (`5.12.2`), Mockito (`5.11.0`) |
| **Kỹ thuật thiết kế** | Phân vùng tương đương, Phân tích giá trị biên (`@CsvSource`), Đoán lỗi, Kiểm thử ngoại lệ (`assertThrows`) |
| **CI/CD** | GitHub Actions (`PetCare CI` trong `.github/workflows/maven-ci.yml`) |

---

## 4. Tiêu chí nghiệm thu (Exit Criteria)
- `mvn test` (hoặc `.\run-unit-tests.bat`) đạt **100% Pass** (42/42 test cases), không có test lỗi hoặc bị bỏ qua.
- Hồ sơ minh chứng AI trong `docs/ai-logs/` đầy đủ 6 tài liệu theo quy định.
