@echo off
echo ====================================
echo  COMPILE PROJECT + TESTS
echo ====================================
echo.

REM Check if JUnit exists
if not exist "lib\junit-platform-console-standalone-1.10.1.jar" (
    echo [ERROR] JUnit library not found!
    echo Please run: download-junit.bat first
    pause
    exit /b 1
)

REM Clean bin directory
if exist bin rmdir /s /q bin
mkdir bin

echo [1/2] Compiling source code...
javac -d bin -sourcepath src src\app\GorAppCli.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Source compilation failed!
    pause
    exit /b 1
)

echo [SUCCESS] Source code compiled.
echo.

echo [2/2] Compiling test code...
javac -cp "lib\junit-platform-console-standalone-1.10.1.jar;bin" -d bin test\ReservationServiceTest.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Test compilation failed!
    pause
    exit /b 1
)

echo [SUCCESS] Test code compiled.
echo.
echo ====================================
echo  COMPILATION COMPLETE!
echo ====================================
echo.
echo All .class files in: bin\
echo Next step: run-test.bat
echo.
pause
