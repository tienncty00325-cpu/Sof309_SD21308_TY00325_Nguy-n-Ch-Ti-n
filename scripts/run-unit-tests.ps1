$ErrorActionPreference = "Stop"

# Nếu máy có cài Maven trên PATH thì ưu tiên chạy mvn test
if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Running: mvn test" -ForegroundColor Cyan
    mvn test
    exit $LASTEXITCODE
}

# Fallback: biên dịch và chạy trực tiếp bằng JDK 17 + thư viện JUnit/Mockito trong .m2
$javac = if (Test-Path "C:\Program Files\Java\jdk-17\bin\javac.exe") { "C:\Program Files\Java\jdk-17\bin\javac.exe" } else { "javac" }
$java = if (Test-Path "C:\Program Files\Java\jdk-17\bin\java.exe") { "C:\Program Files\Java\jdk-17\bin\java.exe" } else { "java" }

$m2 = Join-Path $env:USERPROFILE ".m2\repository"
$agent = "$m2\net\bytebuddy\byte-buddy-agent\1.14.12\byte-buddy-agent-1.14.12.jar"
$jars = @(
    "$m2\jakarta\servlet\jakarta.servlet-api\6.0.0\jakarta.servlet-api-6.0.0.jar",
    "$m2\org\junit\jupiter\junit-jupiter-api\5.12.2\junit-jupiter-api-5.12.2.jar",
    "$m2\org\junit\jupiter\junit-jupiter-engine\5.12.2\junit-jupiter-engine-5.12.2.jar",
    "$m2\org\junit\jupiter\junit-jupiter-params\5.12.2\junit-jupiter-params-5.12.2.jar",
    "$m2\org\junit\platform\junit-platform-commons\1.12.2\junit-platform-commons-1.12.2.jar",
    "$m2\org\junit\platform\junit-platform-engine\1.12.2\junit-platform-engine-1.12.2.jar",
    "$m2\org\junit\platform\junit-platform-launcher\1.12.2\junit-platform-launcher-1.12.2.jar",
    "$m2\org\opentest4j\opentest4j\1.3.0\opentest4j-1.3.0.jar",
    "$m2\org\apiguardian\apiguardian-api\1.1.2\apiguardian-api-1.1.2.jar",
    "$m2\org\mockito\mockito-core\5.11.0\mockito-core-5.11.0.jar",
    "$m2\net\bytebuddy\byte-buddy\1.14.12\byte-buddy-1.14.12.jar",
    $agent,
    "$m2\org\objenesis\objenesis\3.3\objenesis-3.3.jar",
    "$m2\at\favre\lib\bcrypt\0.10.2\bcrypt-0.10.2.jar",
    "$m2\at\favre\lib\bytes\1.5.0\bytes-1.5.0.jar",
    "$m2\org\slf4j\slf4j-api\2.0.20\slf4j-api-2.0.20.jar"
)

New-Item -ItemType Directory -Force -Path "target\classes", "target\test-classes" | Out-Null
$cp = "target\classes;target\test-classes;" + ($jars -join ";")
$testFiles = (Get-ChildItem -Path "src\test\java" -Recurse -Filter "*.java").FullName
$runnerFile = "scripts\AllUnitTestsRunner.java"

Write-Host "Compiling test classes..." -ForegroundColor Cyan
& $javac -encoding UTF-8 -cp $cp -d "target\test-classes" $testFiles $runnerFile
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }

Write-Host "Running all PetCare Unit Tests..." -ForegroundColor Cyan
& $java "-javaagent:$agent" -cp $cp com.petcare.AllUnitTestsRunner
exit $LASTEXITCODE
