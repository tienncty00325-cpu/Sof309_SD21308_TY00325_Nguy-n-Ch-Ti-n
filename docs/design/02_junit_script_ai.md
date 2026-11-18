# Kịch bản Unit Test (JUnit 5 & Mockito) - Dự án V-SPORT

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Framework**: JUnit 5 (`5.10.2`), Mockito (`5.11.0`)

---

## 1. Danh sách các file Unit Test chính

| Class Test | Đường dẫn | Nội dung kiểm thử |
|---|---|---|
| `CourtPricingServiceTest` | `src/test/java/org/example/service/pricing/CourtPricingServiceTest.java` | Tính giá ban ngày, buổi tối có đèn, khung giờ giao thoa, ngoại lệ đầu vào |
| `PaymentCalculatorTest` | `src/test/java/org/example/service/payment/PaymentCalculatorTest.java` | Tính tiền còn lại sau cọc, giới hạn không âm, `isFullyCovered` |
| `PhoneUtilTest` | `src/test/java/org/example/util/PhoneUtilTest.java` | Chuẩn hóa SĐT VN (`0x`, `+84`, `84`), loại bỏ ký tự thừa, từ chối SĐT sai |
| `AuthPortalPolicyTest` | `src/test/java/org/example/util/AuthPortalPolicyTest.java` | Kiểm tra phân quyền role theo cổng `CUSTOMER` và `INTERNAL` |
| `CustomerReputationServiceClampTest` | `src/test/java/org/example/service/reputation/CustomerReputationServiceClampTest.java` | Giới hạn điểm uy tín trong đoạn `[0, 100]` |
| `ReputationLabelTest` | `src/test/java/org/example/service/reputation/ReputationLabelTest.java` | Phân loại nhãn uy tín theo khoảng điểm |
| `PasswordResetChallengeTest` | `src/test/java/org/example/service/reset/PasswordResetChallengeTest.java` | Xác thực OTP và vô hiệu hóa khi nhập sai quá số lần |
| `PaginationUtilsTest` | `src/test/java/org/example/util/PaginationUtilsTest.java` | Mock `HttpServletRequest` bằng Mockito để kiểm tra chuẩn hóa phân trang |

---

## 2. Lệnh chạy Unit Test

Chạy toàn bộ các class Unit Test độc lập (không cần kết nối DB):
```bash
mvn test -Dtest="CourtPricingServiceTest,PaymentCalculatorTest,PhoneUtilTest,AuthPortalPolicyTest,PaginationUtilsTest,CustomerReputationServiceClampTest,ReputationLabelTest,PasswordResetChallengeTest"
```

> Lưu ý: Một số file test cũ trong `src/test/java/org/example/` như `ListTablesTest`, `FindTestAccountsTest` có gọi `DBUtil.getConnection()` nên chỉ chạy khi máy đã bật sẵn Database cục bộ.
