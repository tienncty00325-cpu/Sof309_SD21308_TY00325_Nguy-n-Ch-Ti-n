# Cấu hình CI/CD với GitHub Actions - Dự án V-SPORT

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **File workflow**: [`.github/workflows/maven-ci.yml`](../../.github/workflows/maven-ci.yml)

---

## 1. Mục đích
Mỗi khi push code hoặc tạo Pull Request vào nhánh `main`, GitHub Actions sẽ tự động:
1. Checkout mã nguồn.
2. Cài đặt **JDK 17 (Temurin)** và bật cache cho thư viện Maven (`~/.m2`).
3. Chạy `mvn -B test-compile` để đảm bảo toàn bộ source code chính và source test biên dịch không lỗi.
4. Chạy bộ Unit Test lõi (`CourtPricingServiceTest`, `PaymentCalculatorTest`, `PhoneUtilTest`, `AuthPortalPolicyTest`, `PaginationUtilsTest`, `CustomerReputationServiceClampTest`, `ReputationLabelTest`, `PasswordResetChallengeTest`).

---

## 2. Lưu ý cấu hình
- Không chạy các class `*IT` (Selenium) và các class test tích hợp DB (`ListTablesTest`, `FindTestAccountsTest`) trên runner Ubuntu của GitHub vì trên CI không có sẵn Tomcat và SQL Server/MySQL local.
- Kiểm tra kết quả chạy tại tab **Actions** trên repository GitHub sau khi push.
