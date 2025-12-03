@echo off
echo ====================================
echo  DOWNLOAD JUNIT LIBRARIES
echo ====================================
echo.

REM Create lib directory if not exists
if not exist "lib" mkdir lib

echo [1/2] Downloading JUnit Platform Console Standalone...
powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar' -OutFile 'lib\junit-platform-console-standalone-1.10.1.jar'}"

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] JUnit downloaded to lib\junit-platform-console-standalone-1.10.1.jar
) else (
    echo [ERROR] Failed to download JUnit
    pause
    exit /b 1
)

echo.
echo ====================================
echo  DOWNLOAD COMPLETE!
echo ====================================
echo.
echo JUnit library saved to: lib\
echo You can now run: compile-test.bat
echo.
pause
