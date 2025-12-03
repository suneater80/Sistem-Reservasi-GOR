# PANDUAN MENJALANKAN APLIKASI

## ✅ PROJECT SUDAH LENGKAP!

Aplikasi ini memiliki 2 interface:
1. **CLI (Command Line Interface)** - Versi teks di terminal
2. **GUI (Graphical User Interface)** - Versi window dengan Swing

---

## 🎨 MENJALANKAN GUI (RECOMMENDED)

### 1. Compile GUI:
```bash
.\compile-gui.bat
```

### 2. Run GUI:
```bash
.\run-gui.bat
```

### 3. Cara Manual:
```bash
# Compile
javac -d bin -sourcepath src src/gui/*.java src/app/*.java

# Run
java -cp bin gui.GorAppGUI
```

**Fitur GUI:**
- ✅ Welcome Screen dengan 2 pilihan: Admin / Customer
- ✅ Admin Dashboard: CRUD Lapangan, User, Lihat Reservasi (3 tabs)
- ✅ Customer Dashboard: Booking dengan Tax/Insurance checkbox (Decorator!), Riwayat, Cancel
- ✅ Design Patterns visible: Factory, Strategy, **Decorator**

---

## 📟 MENJALANKAN CLI (Command Line Interface)

### Cara 1: Menggunakan Batch Script (Windows)

1. **Compile aplikasi:**
   ```bash
   .\compile.bat
   ```

2. **Run aplikasi:**
   ```bash
   .\run.bat
   ```

### Cara 2: Manual Command

1. **Compile:**
   ```bash
   javac -d bin -sourcepath src src/app/GorAppCli.java
   ```

2. **Run:**
   ```bash
   java -cp bin app.GorAppCli
   ```

---

## 🧪 MENJALANKAN JUNIT TESTS

### Opsi 1: Via Visual Studio Code (RECOMMENDED)

1. **Install Extension** (jika belum):
   - Extension: "Test Runner for Java" by Microsoft
   - Extension: "Debugger for Java" by Microsoft

2. **Run Test:**
   - Buka file: `test/ReservationServiceTest.java`
   - Klik kanan pada file
   - Pilih: **"Run Java"** atau **"Run Tests"**
   
   ATAU
   
   - Lihat di sidebar kiri icon test (flask/beaker icon)
   - Click run all tests

### Opsi 2: Via Command Line (Butuh JUnit JAR)

**Download JUnit 5 terlebih dahulu:**

1. Download JUnit Platform Console Standalone JAR:
   - Link: https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar
   - Simpan di folder: `lib/junit-platform-console-standalone-1.10.1.jar`

2. **Compile test:**
   ```bash
   javac -cp "lib/junit-platform-console-standalone-1.10.1.jar;bin" -d bin test/ReservationServiceTest.java
   ```

3. **Run test:**
   ```bash
   java -jar lib/junit-platform-console-standalone-1.10.1.jar --class-path bin --scan-class-path
   ```

### Opsi 3: Via IntelliJ IDEA / Eclipse

1. Import project ke IDE
2. Right-click `ReservationServiceTest.java`
3. Select: **Run As → JUnit Test**

---

## 📝 LOGIN CREDENTIALS

### Admin:
- **ID**: `ADMIN001`
- **Password**: (tidak ada, langsung masuk)

### Customer:
- **ID**: `CUST001` - Ani Wulandari
- **ID**: `CUST002` - Budi Doremi

---

## 🎯 TEST SCENARIO

### Scenario 1: Customer Booking dengan Tax & Insurance

1. Run aplikasi
2. Pilih: `2` (Masuk sebagai Customer)
3. Input ID: `CUST001`
4. Pilih: `2` (Buat Reservasi Baru)
5. Pilih Lapangan: `FUT01`
6. Input Tanggal: `2025-12-10` (format: YYYY-MM-DD)
7. Input Jam Mulai: `14` (jam 14:00)
8. Input Durasi: `2` (2 jam)
9. Tambahkan Pajak PPN 10%: `YA`
10. Tambahkan Asuransi: `YA`
11. Konfirmasi: `YA`
12. Metode Pembayaran: `CASH`
13. ✅ Lihat detail reservasi dengan total biaya (base + tax + insurance)

### Scenario 2: Pembatalan Reservasi

1. Login sebagai Customer (CUST001)
2. Pilih: `3` (Batalkan Reservasi)
3. Input Reservation ID dari booking sebelumnya (misal: RES1000)
4. ✅ Lihat refund 50% diproses

### Scenario 3: Admin Kelola Lapangan

1. Login sebagai Admin (ADMIN001)
2. Pilih: `1` (Kelola Lapangan)
3. Pilih: `1` (Tambah Lapangan)
4. Jenis: `FUTSAL`
5. Nama: `Lapangan Futsal Premium`
6. Harga: `150000`
7. Kapasitas: `14`
8. ✅ Lapangan baru berhasil ditambahkan

---

## 🐛 TROUBLESHOOTING

### Error: "cannot find symbol - Optional"
✅ **SUDAH DIPERBAIKI!** Import sudah ditambahkan.

### Error: "cannot find symbol - TariffStrategy"
✅ **SUDAH DIPERBAIKI!** Import sudah ditambahkan.

### JUnit Tests tidak bisa di-run
**Solusi:**
1. Pastikan Extension "Test Runner for Java" sudah terinstall di VS Code
2. Atau download JUnit JAR manually (lihat Opsi 2 di atas)
3. Atau gunakan IDE seperti IntelliJ/Eclipse yang sudah include JUnit

### Aplikasi tidak menerima input
**Solusi:**
- Jangan run via `run.bat` dengan double-click jika tidak muncul input
- Run di terminal PowerShell:
  ```bash
  java -cp bin app.GorAppCli
  ```

---

## 📊 DESIGN PATTERNS YANG DIIMPLEMENTASIKAN

### ✅ 1. Factory Method Pattern (Creational)
- **Location**: `src/factory/FieldFactory.java`
- **Usage**: Membuat objek Field (Futsal/Badminton)

### ✅ 2. Strategy Pattern (Behavioral)
- **Location**: `src/strategy/*.java`
- **Usage**: Perhitungan tarif weekday vs weekend

### ✅ 3. Decorator Pattern (Structural)
- **Location**: `src/decorator/*.java`
- **Usage**: Menambahkan Tax & Insurance pada Payment

---

## 📦 STRUKTUR FILE

```
Sistem-Reservasi-GOR/
├── bin/                    # Compiled .class files (auto-generated)
├── src/
│   ├── app/
│   │   └── GorAppCli.java ✅ FIXED (import added)
│   ├── decorator/         ✨ NEW
│   ├── factory/
│   ├── model/
│   ├── repository/        🔧 UPDATED (Generic)
│   ├── service/
│   ├── strategy/
│   └── util/
├── test/
│   └── ReservationServiceTest.java
├── compile.bat            ✨ NEW
├── run.bat                ✨ NEW
├── clean.bat              ✨ NEW
├── CHECKLIST.md
├── README.md
└── QUICK_START.md         ✨ (file ini)
```

---

## ✅ STATUS PERBAIKAN

| Issue | Status |
|-------|--------|
| Compile Error (Optional) | ✅ FIXED |
| Compile Error (TariffStrategy) | ✅ FIXED |
| Missing Decorator Pattern | ✅ ADDED |
| Missing Generic Programming | ✅ ADDED |
| JUnit Tests | ✅ READY (butuh setup) |
| CLI Application | ✅ WORKING |
| Batch Scripts | ✅ CREATED |

---

## 🎓 NEXT STEPS

1. ✅ **Compile & Run** - Sudah bisa jalan!
2. ⏭️ **Setup JUnit** - Install extension atau download JAR
3. ⏭️ **Test Aplikasi** - Coba semua scenario
4. ⏭️ **GUI (Optional)** - Setelah CLI sempurna
5. ⏭️ **Presentasi** - Siapkan demo & penjelasan

---

**Good luck! 🚀**

Untuk pertanyaan lebih lanjut, check:
- `README.md` - Dokumentasi lengkap
- `CHECKLIST.md` - Checklist tugas
