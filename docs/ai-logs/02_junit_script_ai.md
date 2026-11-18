# Kịch bản Unit Test (JUnit 5 & Mockito) - Dự án PetCare

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Framework**: JUnit 5 (`5.12.2`), Mockito (`5.11.0`)

---

## 1. Danh sách các file Unit Test Controller

| Class Test | Đường dẫn | Nội dung kiểm thử |
|---|---|---|
| `DangXuatServletTest` | `src/test/java/com/petcare/controllers/DangXuatServletTest.java` | Kiểm tra `doGet`/`doPost` hủy session và chuyển hướng `/login` |
| `TrangChuServletTest` | `src/test/java/com/petcare/controllers/TrangChuServletTest.java` | Kiểm tra `doGet` forward về `/index.jsp` |
| `ControllerFormAndExceptionTest` | `src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java` | `@ParameterizedTest` + `@CsvSource` kiểm tra biên form và `assertThrows()` bắt lỗi nghiệp vụ |
| `HomeServletTest` | `src/test/java/com/petcare/controller/HomeServletTest.java` | Kiểm tra nạp dịch vụ/đánh giá, flash attributes, và đặt lịch `doPost` |
| `LoginServletTest` | `src/test/java/com/petcare/controller/LoginServletTest.java` | Kiểm tra validation, remember-me, sai thông tin, lỗi SQL `229` |
| `RegisterServletTest` | `src/test/java/com/petcare/controller/RegisterServletTest.java` | Kiểm tra validation đăng ký, flash thành công, trùng khóa SQL `2627` |
| `ProfileServletTest` | `src/test/java/com/petcare/controller/ProfileServletTest.java` | Kiểm tra bảo vệ đăng nhập, cập nhật hồ sơ, đổi mật khẩu |

---

## 2. Lệnh chạy toàn bộ Unit Test

Chạy bằng Maven:
```bash
mvn test
```

Hoặc chạy trực tiếp bằng script có sẵn tại thư mục gốc dự án:
```powershell
.\run-unit-tests.bat
```

Kết quả hiện tại: **42/42 test pass** (`0 failed`, `0 skipped`).
