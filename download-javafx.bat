@echo off
echo ====================================
echo  DOWNLOAD JAVAFX SDK
echo ====================================
echo.
echo JavaFX perlu didownload manual karena tidak termasuk di JDK.
echo.
echo Silakan download dari:
echo https://gluonhq.com/products/javafx/
echo.
echo Pilih: JavaFX Windows SDK (versi LTS terbaru, misal 17.0.x atau 21.0.x)
echo.
echo Setelah download:
echo 1. Extract file ZIP
echo 2. Copy folder 'javafx-sdk-XX' ke folder project ini
echo 3. Rename menjadi 'javafx-sdk'
echo.
echo Struktur akhir:
echo   Sistem-Reservasi-GOR\
echo     ├── javafx-sdk\
echo     │   ├── lib\
echo     │   └── ...
echo     └── src\
echo.
echo Alternatif: Gunakan Swing (sudah termasuk di JDK)
echo Apakah Anda ingin saya buatkan versi Swing saja? (y/n)
echo.
pause
