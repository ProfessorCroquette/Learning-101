@echo off
cd /d I:\REPO\Learning-101\project-sephirah

echo 🚀 Starting Project Sephirah...
echo.

echo [1/2] Packaging project...
mvn clean package -DskipTests -q

if %ERRORLEVEL% NEQ 0 (
    echo ❌ Build failed!
    exit /b 1
)

echo [2/2] Running Project Sephirah...
echo.
echo ═════════════════════════════════════════════════════════════
echo 📚 PROJECT SEPHIRAH - CHESED MODULE CONNECTED
echo ═════════════════════════════════════════════════════════════
echo.
echo ✅ Features Integrated:
echo    • ProjectSephirah.java (Main Framework)
echo    • ChesedSephirah.java (OOP & Algorithms Module)
echo.
echo 📌 CHESED SUBMENU OPTIONS:
echo    1 = OOP Concepts Demo
echo    2 = Sorting Algorithms Demo
echo    3 = Search Algorithms Demo
echo    4 = Comparators ^& Strategies
echo    5 = Full Demonstration
echo    0 = Back to Main Menu
echo.
echo ═════════════════════════════════════════════════════════════
echo.

java -jar target/project-sephirah-1.0.0-all.jar
pause


echo.
echo Running Project Sephirah...
echo Selecting Chesed Module (option 1)...
echo.

REM Run with piped input to select option 1
echo 1 | java -cp target/classes;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.16.1\jackson-databind-2.16.1.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.16.1\jackson-core-2.16.1.jar;%USERPROFILE%\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.16.1\jackson-datatype-jsr310-2.16.1.jar;%USERPROFILE%\.m2\repository\com\squareup\okhttp3\okhttp\4.12.0\okhttp-4.12.0.jar;%USERPROFILE%\.m2\repository\com\squareup\okio\okio\3.8.1\okio-3.8.1.jar;%USERPROFILE%\.m2\repository\org\slf4j\slf4j-api\2.0.9\slf4j-api-2.0.9.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-classic\1.4.12\logback-classic-1.4.12.jar;%USERPROFILE%\.m2\repository\ch\qos\logback\logback-core\1.4.12\logback-core-1.4.12.jar com.atziluth.ProjectSephirah

pause
