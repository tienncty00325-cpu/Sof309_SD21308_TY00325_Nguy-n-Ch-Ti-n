# Cấu hình CI/CD với GitHub Actions - Dự án PetCare

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **File workflow**: [`.github/workflows/maven-ci.yml`](../../.github/workflows/maven-ci.yml)

---

## 1. Quy trình tự động trên GitHub Actions (`PetCare CI`)

Workflow tự động kích hoạt khi `push` hoặc `pull_request` vào nhánh `main`:

1. **Checkout source**: `actions/checkout@v4`.
2. **Set up JDK 17**: `actions/setup-java@v4` (Temurin JDK 17, bật cache Maven).
3. **Compile project**: `mvn -B test-compile`.
4. **Run Unit Tests**: `mvn -B test` — chạy toàn bộ Unit Test trong `src/test/java/com/petcare/controllers/` và `src/test/java/com/petcare/controller/`.
