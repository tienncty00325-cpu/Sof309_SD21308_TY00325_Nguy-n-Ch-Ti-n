# Thiết kế Selenium WebDriver 4 (Page Object Model) - Dự án PetCare

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Công cụ**: Selenium WebDriver 4 (`4.18.1`), WebDriverManager (`5.7.0`), JUnit 5

---

## 1. Cấu trúc Page Object Model (POM) dự kiến cho PetCare

| Page Object | Đường dẫn JSP / Route | Thành phần & Hành động chính |
|---|---|---|
| `HomePage` | `/home` (`index.jsp`) | Danh sách dịch vụ, đánh giá khách hàng, form đặt lịch nhanh |
| `LoginPage` | `/login` (`login.jsp`) | Ô nhập `identifier`, `password`, checkbox `remember`, nút Đăng nhập |
| `RegisterPage` | `/register` (`register.jsp`) | Ô nhập `fullName`, `phone`, `email`, `password`, `confirmPassword` |
| `ProfilePage` | `/profile` (`profile.jsp`) | Form cập nhật thông tin cá nhân và form đổi mật khẩu |

---

## 2. Kịch bản kiểm thử UI trọng tâm (Y4)

1. Mở trang chủ `/home`, kiểm tra hiển thị danh sách dịch vụ chăm sóc thú cưng.
2. Kiểm tra đăng nhập thành công tại `/login` và điều hướng về `/home`.
3. Kiểm tra validation form đăng ký `/register` khi bỏ trống trường bắt buộc hoặc mật khẩu không khớp.
4. Kiểm tra chuyển hướng về `/login` khi truy cập `/profile` chưa đăng nhập.
