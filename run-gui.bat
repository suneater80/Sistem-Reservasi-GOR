@echo off
echo ====================================
echo  RUN GUI APPLICATION
echo ====================================
echo.

REM Check if compiled
if not exist "bin\gui\GorAppGUI.class" (
    echo [ERROR] GUI not compiled yet!
    echo Please run: compile-gui.bat first
    pause
    exit /b 1
)

echo Launching GUI...
echo.
java -cp bin gui.GorAppGUI

echo.
pause
