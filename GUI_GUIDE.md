# GUI APPLICATION - SISTEM RESERVASI GOR

## 🎨 OVERVIEW

Aplikasi GUI dibuat menggunakan **Java Swing** yang sudah built-in di JDK (tidak perlu download library tambahan). GUI ini menyediakan interface visual yang user-friendly untuk semua fitur sistem reservasi.

---

## 📂 STRUKTUR FILE GUI

```
src/gui/
├── GorAppGUI.java          # Main application dengan CardLayout
├── WelcomeScreen.java      # Screen pemilihan Admin/Customer
├── AdminDashboard.java     # Dashboard admin (3 tabs)
└── CustomerDashboard.java  # Dashboard customer (2 tabs)
```

---

## 🚀 CARA MENJALANKAN

### Quick Start:
```bash
.\compile-gui.bat   # Compile GUI
.\run-gui.bat       # Run GUI
```

### Manual:
```bash
javac -d bin -sourcepath src src/gui/*.java src/app/*.java
java -cp bin gui.GorAppGUI
```

---

## 🎯 FITUR-FITUR

### 1. Welcome Screen
- **Button "Login sebagai ADMIN"** → Masuk ke Admin Dashboard
- **Button "Login sebagai CUSTOMER"** → Masuk ke Customer Dashboard
- Design: Blue gradient header, centered buttons

### 2. Admin Dashboard (3 Tabs)

#### Tab 1: Kelola Lapangan
- **Table** dengan kolom: ID, Tipe, Nama, Harga/Jam, Info Tambahan
- **[+] Tambah Lapangan**: Dialog form untuk tambah lapangan baru
  - Pilih tipe: FUTSAL atau BADMINTON
  - Input: nama, harga, info (kapasitas/indoor)
  - Menggunakan **Factory Pattern** (FieldFactory.createField())
- **[Edit]**: Edit harga lapangan
- **[X] Hapus**: Delete lapangan
- **[Refresh]**: Reload data

#### Tab 2: Kelola User
- **Table** dengan kolom: ID, Nama, Nomor Telepon
- **[+] Tambah User**: Dialog form untuk registrasi user baru
- **[X] Hapus**: Delete user
- **[Refresh]**: Reload data

#### Tab 3: Lihat Reservasi
- **Table** dengan kolom: ID, User, Lapangan, Tanggal, Jam, Durasi, Status, Total
- **[Refresh]**: Reload data
- Read-only view untuk monitoring semua booking

### 3. Customer Dashboard (2 Tabs)

#### Tab 1: [Booking] Lapangan ⭐ PALING PENTING
- **ComboBox Lapangan**: Pilih dari list lapangan yang tersedia
- **Date Input**: Spinner untuk Year, Month, Day
- **Jam Mulai**: Spinner (6-22)
- **Durasi**: Spinner (1-8 jam)
- **Metode Pembayaran**: ComboBox (CASH, TRANSFER, QRIS)
- **✨ CHECKBOX TAX** (Decorator Pattern!) - "Tambahkan Pajak PPN 10%"
- **✨ CHECKBOX INSURANCE** (Decorator Pattern!) - "Tambahkan Asuransi Rp15.000"
- **[Hitung] Harga**: Preview estimasi dengan breakdown:
  ```
  Base: Rp 210,000
  Tax (10%): Rp 21,000
  Insurance: Rp 15,000
  ──────────────────
  TOTAL: Rp 246,000
  ```
- **>>> BOOKING SEKARANG <<<**: Submit booking

**Implementasi Design Pattern:**
- **Strategy Pattern**: Weekday vs Weekend pricing (auto-detect dari tanggal)
- **Decorator Pattern**: Tax & Insurance checkbox menambahkan biaya secara dinamis

#### Tab 2: [Riwayat] Reservasi
- **Table** riwayat booking user yang login
- **[X] Batalkan Reservasi**: Cancel dengan refund 50%
- **[Refresh]**: Reload data

---

## 🎨 DESIGN CHOICES

### Color Scheme:
- **Admin**: Green theme (#228B22 - Forest Green)
- **Customer**: Blue theme (#1E90FF - Dodger Blue)
- **Welcome**: Steel Blue (#4682B4)
- **Delete buttons**: Crimson (#DC143C)
- **Edit buttons**: Orange (#FFA500)

### Layout:
- **CardLayout**: Untuk switching antar screens tanpa buka window baru
- **GridBagLayout**: Untuk form yang flexible
- **BorderLayout**: Untuk struktur dashboard (header/center/footer)
- **FlowLayout**: Untuk button panel

### Component Choices:
- **JTable**: Display data in tabular format
- **JComboBox**: Dropdown selection
- **JSpinner**: Numeric input dengan up/down buttons
- **JCheckBox**: Boolean options (Tax/Insurance)
- **JDialog**: Modal dialogs untuk forms
- **JTabbedPane**: Multiple tabs dalam satu window

---

## 📸 SCREENSHOT GUIDE UNTUK LAPORAN

### WAJIB DIAMBIL:

1. **Welcome Screen**
   - Tampilan awal dengan 2 tombol besar
   - Caption: "Tampilan Welcome Screen - Pilihan Admin atau Customer"

2. **Admin - Kelola Lapangan**
   - Table dengan data lapangan
   - Caption: "Admin Dashboard - CRUD Lapangan (Factory Pattern)"

3. **Admin - Form Tambah Lapangan**
   - Dialog form tambah lapangan
   - Caption: "Dialog Tambah Lapangan - Factory Method Pattern in Action"

4. **Customer - Form Booking (PENTING!)**
   - Screenshot dengan **Tax checkbox DICENTANG**
   - Screenshot dengan **Insurance checkbox DICENTANG**
   - Caption: "Form Booking dengan Decorator Pattern - Tax & Insurance Checkbox"

5. **Customer - Breakdown Harga**
   - Setelah klik "Hitung Harga", tampilan breakdown
   - Caption: "Preview Harga dengan Decorator: Base + Tax + Insurance"

6. **Customer - Booking Success**
   - Dialog konfirmasi "Booking berhasil! ID: RES... Total: Rp..."
   - Caption: "Konfirmasi Booking Berhasil"

7. **Customer - Riwayat Reservasi**
   - Table dengan list booking
   - Caption: "Riwayat Reservasi Customer"

8. **Customer - Cancellation**
   - Dialog konfirmasi cancel dengan refund info
   - Caption: "Pembatalan Reservasi dengan Refund 50%"

---

## 🔍 DESIGN PATTERN DEMONSTRATION

### 1. Factory Method Pattern (Creational)
**Lokasi**: AdminDashboard.java line ~210
```java
Field field = FieldFactory.createField(type, id, name, price, extra);
```
**Terlihat di GUI**: 
- Admin → Kelola Lapangan → [+] Tambah Lapangan
- ComboBox untuk pilih FUTSAL atau BADMINTON
- Factory creates appropriate object based on selection

### 2. Strategy Pattern (Behavioral)
**Lokasi**: CustomerDashboard.java line ~224
```java
DayOfWeek dayOfWeek = date.getDayOfWeek();
var strategy = reservationService.getTariffStrategy(dayOfWeek);
double basePrice = strategy.calculatePrice(field, dayOfWeek, startTime, duration);
```
**Terlihat di GUI**:
- Customer → [Booking] Lapangan
- Pilih tanggal WEEKDAY: harga normal
- Pilih tanggal WEEKEND: harga +15% (auto-detected)
- Klik "Hitung Harga" untuk lihat perbedaan

### 3. Decorator Pattern (Structural) ⭐
**Lokasi**: CustomerDashboard.java line ~140-150 & ReservationService
```java
// Checkbox di GUI
JCheckBox taxCheckbox = new JCheckBox("Tambahkan Pajak PPN 10%");
JCheckBox insuranceCheckbox = new JCheckBox("Tambahkan Asuransi Rp15.000");

// Implementasi decorator di backend
boolean withTax = taxCheckbox.isSelected();
boolean withInsurance = insuranceCheckbox.isSelected();
reservationService.bookField(..., withTax, withInsurance);
```
**Terlihat di GUI**:
- Customer → [Booking] Lapangan
- ✅ Centang "Tambahkan Pajak PPN 10%" → Harga +10%
- ✅ Centang "Tambahkan Asuransi Rp15.000" → Harga +15k
- Bisa distack (centang keduanya)
- Klik "Hitung Harga" untuk preview breakdown

---

## 🧪 SKENARIO TESTING LENGKAP

### Test Scenario 1: Admin CRUD Operations
1. Launch GUI: `.\run-gui.bat`
2. Klik "Login sebagai ADMIN"
3. Tab "Kelola Lapangan"
4. Klik "[+] Tambah Lapangan"
5. Pilih FUTSAL, isi: "Futsal Premium", 150000, kapasitas "14"
6. Simpan
7. **✅ Verify**: Lapangan baru muncul di table
8. Select lapangan → Klik "[Edit]" → Ubah harga → Simpan
9. **✅ Verify**: Harga terupdate
10. Screenshot: Table dengan lapangan baru

### Test Scenario 2: Booking Normal (Tanpa Decorator)
1. Klik "<< Kembali" → "Login sebagai CUSTOMER"
2. Tab "[Booking] Lapangan"
3. Pilih: Futsal A, Tanggal: 2025-01-06 (Senin/Weekday), Jam 14, Durasi 2, CASH
4. **JANGAN CENTANG** Tax dan Insurance
5. Klik "[Hitung] Harga"
6. **✅ Verify**: Hanya base price (misal Rp 210,000)
7. Klik "BOOKING SEKARANG"
8. **✅ Verify**: Dialog konfirmasi muncul
9. Tab "[Riwayat] Reservasi"
10. **✅ Verify**: Booking baru muncul di table

### Test Scenario 3: Booking dengan Tax (Decorator 1) ⭐
1. Tab "[Booking] Lapangan"
2. Pilih: Badminton 1, Tanggal: 2025-01-10 (Jumat/Weekday), Jam 18, Durasi 1, TRANSFER
3. **✅ CENTANG** "Tambahkan Pajak PPN 10%"
4. **JANGAN CENTANG** Insurance
5. Klik "[Hitung] Harga"
6. **✅ Verify**: 
   - Base: Rp 50,000
   - Tax (10%): Rp 5,000
   - TOTAL: Rp 55,000
7. Screenshot: Breakdown harga dengan Tax
8. Klik "BOOKING SEKARANG"
9. **✅ Verify**: Total = 55,000

### Test Scenario 4: Booking dengan Insurance (Decorator 2) ⭐
1. Tab "[Booking] Lapangan"
2. Pilih: Futsal B, Tanggal: 2025-01-11 (Sabtu/Weekend), Jam 20, Durasi 2, QRIS
3. **JANGAN CENTANG** Tax
4. **✅ CENTANG** "Tambahkan Asuransi Rp15.000"
5. Klik "[Hitung] Harga"
6. **✅ Verify**: 
   - Base: Rp 240,000 + 15% weekend = Rp 276,000
   - Insurance: Rp 15,000
   - TOTAL: Rp 291,000
7. Screenshot: Breakdown harga dengan Insurance
8. Klik "BOOKING SEKARANG"

### Test Scenario 5: Booking dengan SEMUA Decorator (Stack) ⭐⭐⭐
1. Tab "[Booking] Lapangan"
2. Pilih: Futsal A, Tanggal: 2025-01-12 (Minggu/Weekend), Jam 16, Durasi 3, CASH
3. **✅ CENTANG** "Tambahkan Pajak PPN 10%"
4. **✅ CENTANG** "Tambahkan Asuransi Rp15.000"
5. Klik "[Hitung] Harga"
6. **✅ Verify Breakdown**: 
   ```
   Base (Weekend +15%): Rp 361,500
   Tax (10%): Rp 36,150
   Insurance: Rp 15,000
   ─────────────────────────────
   TOTAL: Rp 412,650
   ```
7. **Screenshot ini WAJIB untuk laporan!** (Decorator Pattern)
8. Klik "BOOKING SEKARANG"
9. **✅ Verify**: Total di riwayat = 412,650

### Test Scenario 6: Cancellation & Refund
1. Tab "[Riwayat] Reservasi"
2. Select salah satu booking yang statusnya "BOOKED"
3. Klik "[X] Batalkan Reservasi"
4. **✅ Verify**: Dialog konfirmasi muncul "Refund 50% akan diproses"
5. Klik "Yes"
6. **✅ Verify**: 
   - Status berubah jadi "CANCELED"
   - Total amount berkurang 50%
7. Screenshot: Konfirmasi refund
8. Try cancel lagi booking yang sama
9. **✅ Verify**: Error "Reservasi ini sudah dibatalkan!"

---

## 📊 INTEGRATION DENGAN EXISTING CODE

GUI menggunakan **semua class yang sudah ada** tanpa modifikasi:
- ✅ `ReservationService` - Business logic
- ✅ `FieldFactory` - Factory Pattern
- ✅ `TariffStrategy` implementations - Strategy Pattern
- ✅ `PaymentDecorator` hierarchy - Decorator Pattern
- ✅ All repositories (UserRepository, FieldRepository, ReservationRepository)
- ✅ All model classes (User, Field, Reservation, Payment)

**Benefit**: 
- Code reuse 100%
- GUI adalah "view layer" yang wraps existing service
- Semua business logic tetap di service layer (Clean Architecture)

---

## 🐛 TROUBLESHOOTING

### GUI tidak muncul?
```bash
# Check compile
.\compile-gui.bat

# Check error di terminal
java -cp bin gui.GorAppGUI
```

### Error "cannot find symbol"?
- Pastikan semua source compiled: `.\compile-gui.bat`
- Check CLASSPATH includes `bin/`

### Checkbox Tax/Insurance tidak berfungsi?
- Klik "[Hitung] Harga" dulu untuk preview
- Baru klik "BOOKING SEKARANG" untuk submit

### Table kosong?
- Klik "[Refresh]" button
- Atau restart aplikasi

---

## 💡 TIPS UNTUK DEMO/PRESENTASI

1. **Buka GUI dulu**, jangan CLI
2. **Start dari Welcome Screen** - tunjukkan pilihan Admin/Customer
3. **Demo Admin**: Tambah 1 lapangan baru (tunjukkan Factory Pattern)
4. **Demo Customer**: 
   - Booking 1x TANPA decorator (base price only)
   - Booking 1x DENGAN Tax & Insurance (tunjukkan Decorator Pattern!)
   - Klik "Hitung Harga" setiap kali untuk show breakdown
5. **Highlight Design Patterns**:
   - "Ini Factory Pattern - bisa create Futsal atau Badminton dari form yang sama"
   - "Ini Strategy Pattern - harga weekend otomatis +15% berdasarkan tanggal"
   - "Ini Decorator Pattern - checkbox Tax dan Insurance menambahkan fitur ke Payment secara dinamis"

---

## ✅ CHECKLIST SEBELUM SUBMIT

- [ ] GUI bisa dijalankan dengan `.\run-gui.bat`
- [ ] Admin bisa CRUD lapangan
- [ ] Admin bisa CRUD user
- [ ] Customer bisa booking dengan/tanpa Tax
- [ ] Customer bisa booking dengan/tanpa Insurance
- [ ] Customer bisa booking dengan KEDUANYA (stacking)
- [ ] Customer bisa cancel reservation
- [ ] Screenshot semua fitur sudah diambil
- [ ] Decorator Pattern clearly visible di screenshot
- [ ] Source code GUI sudah di-commit ke GitHub

---

**Dibuat oleh**: GitHub Copilot
**Tanggal**: 4 Desember 2025
**Tech Stack**: Java Swing, OOP Design Patterns
