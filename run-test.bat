@echo off
echo ====================================
echo  RUN JUNIT TESTS
echo ====================================
echo.

REM Check if compiled
if not exist "bin\test\ReservationServiceTest.class" (
    echo [ERROR] Tests not compiled yet!
    echo Please run: compile-test.bat first
    pause
    exit /b 1
)

echo Running all tests...
echo.
java -jar lib\junit-platform-console-standalone-1.10.1.jar --class-path bin --scan-class-path

echo.
echo ====================================
echo  TEST EXECUTION COMPLETE!
echo ====================================
pause
