@echo off
echo ====================================
echo  COMPILE GUI APPLICATION
echo ====================================
echo.

echo [1/1] Compiling all source files (including GUI)...
javac -d bin -sourcepath src src/gui/*.java src/app/*.java

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Compilation failed!
    pause
    exit /b 1
)

echo [SUCCESS] GUI compiled successfully.
echo.
echo ====================================
echo  COMPILATION COMPLETE!
echo ====================================
echo.
echo All .class files in: bin\
echo Next step: run-gui.bat
echo.
pause
