# Nhật ký sửa lỗi (Debug Log) - Dự án V-SPORT

- **Người thực hiện**: Nguyễn Chí Tiến (TY00325 - SD21308)
- **Môn**: SOF309 - Kiểm thử nâng cao

---

## 1. Lỗi thiếu thư viện `org.slf4j` khi build dự án

**Lỗi gặp phải:**
Khi build project trên IntelliJ IDEA, trình biên dịch báo lỗi không tìm thấy package `org.slf4j` ở 3 file (`BookingLifecycleService.java`, `AuditLogDAOImpl.java`, `AuditLogService.java`):
```text
java: package org.slf4j does not exist
java: cannot find symbol class Logger
java: cannot find symbol variable LoggerFactory
```

**Nguyên nhân:**
Trong `pom.xml` của source web cũ chỉ cài sẵn Log4j2 (`log4j-api`, `log4j-core`, `log4j-web`), chưa khai báo `slf4j-api`. Trong khi đó 3 class trên lại import `org.slf4j.Logger` thay vì dùng `Log4j2` như các class khác trong project.

**Cách sửa:**
- Chuyển 3 file `BookingLifecycleService.java`, `AuditLogDAOImpl.java`, `AuditLogService.java` sang dùng `org.apache.logging.log4j.LogManager` và `org.apache.logging.log4j.Logger` cho đồng bộ với phần còn lại của dự án.
- Thêm thêm `slf4j-api` (2.0.12) và `log4j-slf4j2-impl` (2.20.0) vào `pom.xml` để phòng trường hợp thư viện ngoài gọi tới SLF4J.

---

## 2. Lỗi `package com.cloudinary does not exist` ở `CloudinaryUtil.java`

**Lỗi gặp phải:**
Build Output báo 7 lỗi tại `src/main/java/org/example/util/CloudinaryUtil.java`:
```text
package com.cloudinary does not exist :3
package com.cloudinary.utils does not exist :4
cannot find symbol class Cloudinary :23, :27, :41
cannot find symbol variable ObjectUtils :57, :78
```

**Nguyên nhân:**
- Trong `pom.xml` ban đầu chỉ khai báo `cloudinary-http44` (1.38.0) mà không khai báo trực tiếp `cloudinary-core` (chứa class `Cloudinary` và `ObjectUtils`).
- Sau khi sửa `pom.xml` nhưng chưa bấm đồng bộ lại Maven trong IntelliJ (Reload Maven), IDE vẫn giữ classpath cũ nên báo không tìm thấy package `com.cloudinary`.

**Cách sửa:**
1. Khai báo thêm dependency `cloudinary-core` phiên bản `1.38.0` trực tiếp vào `pom.xml`.
2. Trên IntelliJ IDEA, mở tab **Maven** bên phải (hoặc mở `pom.xml`), bấm **Reload All Maven Projects** (`Ctrl + Shift + O`) để tải lại thư viện vào classpath, sau đó chọn **Build -> Rebuild Project**.

---

## 3. Lỗi cấu hình `Backend_java.iml` và thư mục trùng lặp

**Lỗi gặp phải:**
File `Backend_java.iml` đi kèm trong file zip cũ cấu hình `src/main/java` là `isTestSource="true"`, làm IntelliJ nhận nhầm thư mục code chính thành thư mục test. Ngoài ra trong workspace có bị copy dư một thư mục con trùng tên dự án.

**Cách sửa:**
- Xóa file `Backend_java.iml` cũ (và giữ quy tắc `*.iml` trong `.gitignore`) để IntelliJ tự tạo lại cấu hình chuẩn từ `pom.xml`.
- Xóa thư mục con bị copy trùng.
