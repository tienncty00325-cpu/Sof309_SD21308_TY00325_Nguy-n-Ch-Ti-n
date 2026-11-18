# BỘ KỊCH BẢN KIỂM THỬ — PETCARE

> Các ca dưới đây là bộ kịch bản kiểm thử Unit Test cho tầng Controller & nghiệp vụ xác thực của dự án **PetCare**, được cài đặt bằng **JUnit 5** & **Mockito** theo kế hoạch trong [`TEST_PLAN.md`](TEST_PLAN.md).

## 1. Bảng Test Case & Dẫn chứng mã nguồn

| ID | Chức năng | Loại | Tiền điều kiện | Bước thực hiện / dữ liệu | Kết quả mong đợi | Trạng thái | Dẫn chứng (Class / Method Test) |
|---|---|---|---|---|---|---|---|
| `TC_LOGOUT_01` | Đăng xuất (`DangXuatServlet`) | Pos | Đang có `HttpSession` hoạt động | Gọi `doGet(request, response)` | Hủy session (`invalidate()`) và chuyển hướng `/petcare/login` | Pass | [`DangXuatServletTest`](../src/test/java/com/petcare/controllers/DangXuatServletTest.java#L41-L51) (`testDoGetWithActiveSession`) |
| `TC_LOGOUT_02` | Đăng xuất khi chưa có session | Neg | `getSession(false)` trả về `null` | Gọi `doGet(request, response)` | Không lỗi NPE, chuyển hướng `/petcare/login` | Pass | [`DangXuatServletTest`](../src/test/java/com/petcare/controllers/DangXuatServletTest.java#L55-L65) (`testDoGetWithoutSession`) |
| `TC_LOGOUT_03` | Đăng xuất bằng POST | Pos | Đang có `HttpSession` hoạt động | Gọi `doPost(request, response)` | Ủy quyền `doGet`, hủy session và chuyển hướng `/petcare/login` | Pass | [`DangXuatServletTest`](../src/test/java/com/petcare/controllers/DangXuatServletTest.java#L69-L79) (`testDoPostDelegatesToDoGet`) |
| `TC_HOME_01` | Trang chủ (`TrangChuServlet`) | Pos | Có `RequestDispatcher` `/index.jsp` | Gọi `doGet(request, response)` | Forward đúng về `/index.jsp` | Pass | [`TrangChuServletTest`](../src/test/java/com/petcare/controllers/TrangChuServletTest.java#L39-L49) (`testDoGetForwardsToIndexJsp`) |
| `TC_FORM_LOGIN_1..5` | Biên Form đăng nhập (`LoginServlet`) | Boundary | Mock `request`, `dispatcher` | `@ParameterizedTest` + `@CsvSource` với `identifier` rỗng/khoảng trắng, `password` rỗng / 1 / 5 ký tự | Gán `errorMessage` tương ứng và forward `/login.jsp` | Pass | [`ControllerFormAndExceptionTest`](../src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java#L53-L78) (`testLoginFormBoundary`) |
| `TC_FORM_REG_1..4` | Biên Form đăng ký (`RegisterServlet`) | Boundary | Mock `request`, `dispatcher` | `@ParameterizedTest` + `@CsvSource` với thiếu họ tên/SĐT, mật khẩu 7 ký tự (< 8), `confirmPassword` không khớp | Gán `errorMessage` tương ứng và forward `/register.jsp` | Pass | [`ControllerFormAndExceptionTest`](../src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java#L80-L110) (`testRegisterFormBoundary`) |
| `TC_EX_01` | Đăng nhập tài khoản bị khóa | Neg | User tồn tại với `active = false` | Gọi `authService.authenticate(...)` | `assertThrows(IllegalArgumentException.class)` thông báo tài khoản bị khóa | Pass | [`ControllerFormAndExceptionTest`](../src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java#L112-L131) (`testAuthenticateLockedAccountThrowsException`) |
| `TC_EX_02` | Đăng ký SĐT sai định dạng | Neg | Mock `AccountDAO` | Gọi `authService.registerCustomer(..., "123", ...)` | `assertThrows(IllegalArgumentException.class)` thông báo SĐT không hợp lệ | Pass | [`ControllerFormAndExceptionTest`](../src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java#L133-L144) (`testRegisterInvalidPhoneThrowsException`) |
| `TC_EX_03` | Đăng ký trùng SĐT | Neg | `existsByPhone("0912345678") == true` | Gọi `authService.registerCustomer(...)` | `assertThrows(IllegalArgumentException.class)` thông báo SĐT đã tồn tại | Pass | [`ControllerFormAndExceptionTest`](../src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java#L146-L159) (`testRegisterDuplicatePhoneThrowsException`) |
| `TC_EX_04` | Đổi mật khẩu sai mật khẩu cũ | Neg | User tồn tại với `passwordHash = "correctPass"` | Gọi `authService.changePassword(5, "wrongPass", ...)` | `assertThrows(IllegalArgumentException.class)` thông báo mật khẩu hiện tại không đúng | Pass | [`ControllerFormAndExceptionTest`](../src/test/java/com/petcare/controllers/ControllerFormAndExceptionTest.java#L161-L176) (`testChangePasswordWrongCurrentThrowsException`) |
| `TC_LOGIN_01..07` | Luồng `LoginServlet` | Pos/Neg | Mock `AuthService` & `HttpSession` | Kiểm tra `doGet` flash attributes, `doPost` thành công (`remember=on`), sai thông tin, lỗi SQL `229` | Xử lý session, redirect `/home` hoặc forward `/login.jsp` kèm lỗi đúng | Pass | [`LoginServletTest`](../src/test/java/com/petcare/controller/LoginServletTest.java) |
| `TC_REG_01..06` | Luồng `RegisterServlet` | Pos/Neg | Mock `AuthService` & `HttpSession` | Kiểm tra `doGet`, validation, đăng ký thành công, lỗi trùng khóa SQL `2627` | Lưu flash session & redirect `/login` hoặc forward `/register.jsp` | Pass | [`RegisterServletTest`](../src/test/java/com/petcare/controller/RegisterServletTest.java) |
| `TC_PROFILE_01..07` | Luồng `ProfileServlet` | Pos/Neg | Mock `AuthService` & `SessionUtil` | Kiểm tra chưa đăng nhập, tải hồ sơ, user bị xóa, cập nhật hồ sơ, đổi mật khẩu | Redirect `/login`, `/profile` hoặc `/home#ho-so-nguoi-dung` | Pass | [`ProfileServletTest`](../src/test/java/com/petcare/controller/ProfileServletTest.java) |
| `TC_HOME_SERVLET_01..05` | Luồng `HomeServlet` | Pos | Mock `request`, `session`, `dispatcher` | Kiểm tra nạp `services` (6), `reviews` (3), pop flash attributes, tạo `BookingRequest` | Nạp đủ dữ liệu và forward `/index.jsp` | Pass | [`HomeServletTest`](../src/test/java/com/petcare/controller/HomeServletTest.java) |

---

## 2. Dẫn chứng kết quả chạy thực tế

- **Lệnh thực thi**: `.\run-unit-tests.bat` (hoặc `mvn test`)
- **Kết quả JUnit Platform Summary**:

```text
[        10 containers found      ]
[         0 containers skipped    ]
[        10 containers started    ]
[         0 containers aborted    ]
[        10 containers successful ]
[         0 containers failed     ]
[        42 tests found           ]
[         0 tests skipped         ]
[        42 tests started         ]
[         0 tests aborted         ]
[        42 tests successful      ]
[         0 tests failed          ]
```
