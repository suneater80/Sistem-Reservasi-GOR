@echo off
echo ========================================
echo   JUNIT TEST - ALL IN ONE
echo ========================================
echo.

REM Step 1: Download JUnit if not exists
if not exist "lib\junit-platform-console-standalone-1.10.1.jar" (
    echo [STEP 1/3] Downloading JUnit library...
    if not exist "lib" mkdir lib
    powershell -Command "& {Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar' -OutFile 'lib\junit-platform-console-standalone-1.10.1.jar'}"
    
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to download JUnit
        pause
        exit /b 1
    )
    echo [SUCCESS] JUnit downloaded
    echo.
) else (
    echo [STEP 1/3] JUnit already exists, skipping download...
    echo.
)

REM Step 2: Compile everything
echo [STEP 2/3] Compiling source and tests...

if exist bin rmdir /s /q bin
mkdir bin

javac -d bin -sourcepath src src\app\GorAppCli.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Source compilation failed!
    pause
    exit /b 1
)

javac -cp "lib\junit-platform-console-standalone-1.10.1.jar;bin" -d bin test\ReservationServiceTest.java
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Test compilation failed!
    pause
    exit /b 1
)

echo [SUCCESS] Compilation complete
echo.

REM Step 3: Run tests
echo [STEP 3/3] Running JUnit tests...
echo ========================================
echo.

java -jar lib\junit-platform-console-standalone-1.10.1.jar --class-path bin --scan-class-path

echo.
echo ========================================
echo   TEST EXECUTION COMPLETE!
echo ========================================
echo.
echo TIP: Scroll up to see test results
echo.
pause
