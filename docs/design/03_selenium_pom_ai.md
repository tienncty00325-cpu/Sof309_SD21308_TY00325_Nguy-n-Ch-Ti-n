# Thiết kế Selenium WebDriver 4 (Page Object Model) - Dự án V-SPORT

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Thư viện**: `selenium-java 4.18.1`, `webdrivermanager 5.7.0`, JUnit 5

---

## 1. Cấu trúc thư mục POM
Các file kiểm thử giao diện được đặt trong package `org.example.selenium`:
```text
src/test/java/org/example/selenium/
├── pages/
│   ├── BasePage.java       # Khởi tạo WebDriverWait, các hàm openPath, type, click dùng chung
│   ├── HomePage.java       # Thao tác trên trang chủ (/)
│   └── LoginPage.java      # Thao tác trên form đăng nhập (#loginForm, #loginEmail, #loginPassword)
└── VSportUiIT.java         # Bộ kịch bản test giao diện (TC_UI_01, 02, 04, 10, 12)
```

---

## 2. Lý do tách lớp theo POM
- Tách riêng locator (`By.id("loginEmail")`, `By.id("loginPassword")`, `By.id("loginSubmitBtn")`) vào trong các class Page để khi giao diện JSP thay đổi ID/CSS thì chỉ cần sửa 1 chỗ.
- Dùng `WebDriverWait` trong `BasePage` thay cho `Thread.sleep()` để tránh lỗi do trang tải chậm.
- Đặt tên file test là `VSportUiIT.java` (hậu tố `IT`) để khi chạy `mvn test` thông thường hoặc chạy CI trên GitHub Actions không bị lỗi do chưa bật Tomcat/Chrome.

---

## 3. Cách chạy kiểm thử Selenium
1. Khởi chạy Tomcat ở cổng `8080` (`http://localhost:8080`).
2. Trong IntelliJ IDEA, mở file `src/test/java/org/example/selenium/VSportUiIT.java` và bấm nút **Run** ở cạnh tên class.
3. Hoặc chạy headless bằng Maven:
   ```bash
   mvn test -Dtest=VSportUiIT -Dheadless=true -Dvsport.baseUrl=http://localhost:8080
   ```
