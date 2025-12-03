# Sistem Reservasi GOR (Gelanggang Olahraga Rakyat)

## 📋 Deskripsi Proyek
Sistem reservasi lapangan olahraga berbasis CLI (Command Line Interface) yang dibangun dengan konsep Object-Oriented Programming (OOP) dan menerapkan 3 Design Pattern dari kategori berbeda.

## 👥 Kelompok
- Anggota 1: Hafiz Zulhakim
- Anggota 2: Idham Khalid
- Anggota 3: Micky Ridho Pratama

## 🎯 Fitur Utama
1. **Admin Panel**
   - Kelola Lapangan (CRUD)
   - Kelola Pengguna (CRUD)
   - Lihat Semua Reservasi

2. **Customer Panel**
   - Lihat Daftar Lapangan
   - Buat Reservasi Baru (dengan opsi Pajak & Asuransi)
   - Batalkan Reservasi (dengan Refund 50%)
   - Lihat Riwayat Reservasi

3. **Perhitungan Tarif Dinamis**
   - Weekday (Senin-Jumat): Tarif Normal
   - Weekend (Sabtu-Minggu): Tarif +15%
   - Opsi Pajak PPN 10%
   - Opsi Asuransi Rp15.000

## 🏗️ Design Pattern yang Diterapkan

### 1. Factory Method Pattern (Creational)
**Lokasi:** `factory/FieldFactory.java`

**Fungsi:** Membuat objek Field (FutsalCourt, BadmintonCourt) tanpa mengekspos logika pembuatan ke client.

**Contoh Penggunaan:**
```java
Field futsal = FieldFactory.createField("FUTSAL", "FUT01", "Lapangan Futsal 1", 120000.0, "12");
Field badminton = FieldFactory.createField("BADMINTON", "BAD01", "Lapangan Badminton A", 65000.0, "true");
```

**Keuntungan:**
- Mengurangi coupling antara kode client dan kelas konkret
- Mudah menambahkan jenis lapangan baru tanpa mengubah kode client

### 2. Strategy Pattern (Behavioral)
**Lokasi:** `strategy/TariffStrategy.java`, `WeekdayTariff.java`, `WeekendTariff.java`

**Fungsi:** Mengimplementasikan algoritma perhitungan tarif yang berbeda berdasarkan hari.

**Contoh Penggunaan:**
```java
TariffStrategy strategy = getTariffStrategy(date.getDayOfWeek());
double totalFee = strategy.calculatePrice(field, dayOfWeek, startHour, duration);
```

**Keuntungan:**
- Algoritma perhitungan tarif dapat diubah saat runtime
- Mudah menambahkan strategi tarif baru (misalnya: holiday tariff, special event tariff)

### 3. Decorator Pattern (Structural)
**Lokasi:** `decorator/PaymentDecorator.java`, `TaxPaymentDecorator.java`, `InsurancePaymentDecorator.java`

**Fungsi:** Menambahkan fitur tambahan pada Payment secara dinamis (Pajak, Asuransi) tanpa mengubah struktur kelas Payment.

**Contoh Penggunaan:**
```java
Payment basePayment = new Payment("PAY001", 100000.0, "CASH");
TaxPaymentDecorator withTax = new TaxPaymentDecorator(basePayment);
InsurancePaymentDecorator withInsurance = new InsurancePaymentDecorator(basePayment);
```

**Keuntungan:**
- Fleksibel menambahkan fitur baru tanpa mengubah kelas yang sudah ada
- Dapat dikombinasikan (stacked) untuk menambahkan multiple features

## 💻 Konsep OOP yang Diterapkan

### 1. Inheritance & Polymorphism
- Kelas `Field` sebagai abstract class
- `FutsalCourt` dan `BadmintonCourt` extends `Field`
- Method `getPricePerHour()` di-override untuk perhitungan harga spesifik

### 2. Encapsulation
- Semua field di kelas model menggunakan `private`
- Akses hanya melalui getter/setter

### 3. Abstraction
- Interface `TariffStrategy` untuk abstraksi strategi tarif
- Abstract class `GenericRepository<T, K>` untuk abstraksi repository operations

## 📦 Generic Programming
**Lokasi:** `repository/GenericRepository.java`

Implementasi base repository dengan Generic Type untuk mengurangi code duplication:

```java
public abstract class GenericRepository<T, K> {
    protected final Map<K, T> dataMap;
    
    public void save(T entity) { ... }
    public Optional<T> findById(K id) { ... }
    public List<T> findAll() { ... }
    protected abstract K getId(T entity);
}
```

Repository lain (UserRepository, FieldRepository, ReservationRepository) extends dari GenericRepository.

## 🧪 Java Collections Framework (JCF)
- **HashMap**: Penyimpanan data in-memory di Repository
- **List**: Pengembalian multiple entities
- **Optional**: Handling null-safe pada operasi findById
- **Stream API**: Filtering dan sorting data

## ✅ JUnit Testing
**Lokasi:** `test/ReservationServiceTest.java`

**Test Cases:**
1. `testPriceCalculationStrategy()` - Test Strategy Pattern (Weekday vs Weekend)
2. `testTimeSlotConflict()` - Test validasi bentrok jadwal
3. `testCancellationAndRefund()` - Test pembatalan dan refund
4. `testPaymentDecorator()` - Test Decorator Pattern (Tax & Insurance)
5. `testGenericRepository()` - Test Generic Repository CRUD operations

**Menjalankan Test:**
```bash
# Jika menggunakan Maven
mvn test

# Jika menggunakan IDE (Eclipse/IntelliJ)
Right-click pada ReservationServiceTest.java → Run As → JUnit Test
```

## 🎨 Clean Code Practices
1. **Meaningful Names**: Variable dan method names yang deskriptif
2. **Single Responsibility**: Setiap class memiliki satu tanggung jawab
3. **DRY (Don't Repeat Yourself)**: Generic Repository mengurangi duplikasi
4. **Error Handling**: Optional untuk null-safety, try-catch pada input parsing
5. **Formatting**: Konsisten indentation dan spacing
6. **Comments**: Dokumentasi untuk design patterns dan business logic

## 🚀 Cara Menjalankan Aplikasi

### Prasyarat
- Java JDK 8 atau lebih tinggi
- JUnit 5 (untuk running tests)

### Langkah Menjalankan
1. **Compile semua file Java:**
```bash
javac -d bin -sourcepath src src/app/GorAppCli.java
```

2. **Jalankan aplikasi:**
```bash
java -cp bin app.GorAppCli
```

3. **Login sebagai Admin:**
   - ID Admin: `ADMIN001`

4. **Login sebagai Customer:**
   - ID Customer: `CUST001` (Ani Wulandari)
   - ID Customer: `CUST002` (Budi Doremi)

### Contoh Flow Reservasi
1. Login sebagai Customer (CUST001)
2. Pilih menu: **2. Buat Reservasi Baru**
3. Pilih lapangan (contoh: FUT01)
4. Input tanggal (format: YYYY-MM-DD)
5. Input jam mulai (0-23)
6. Input durasi jam
7. Pilih opsi Pajak (YA/TIDAK)
8. Pilih opsi Asuransi (YA/TIDAK)
9. Konfirmasi booking (YA)
10. Input metode pembayaran (CASH/TRANSFER)

## 📊 Struktur Project
```
src/
├── app/
│   └── GorAppCli.java              # Main class (Entry point)
├── factory/
│   └── FieldFactory.java           # Factory Method Pattern
├── model/
│   ├── Field.java                  # Abstract class
│   ├── FutsalCourt.java
│   ├── BadmintonCourt.java
│   ├── User.java
│   ├── Reservation.java
│   └── Payment.java
├── repository/
│   ├── GenericRepository.java      # Generic base repository
│   ├── UserRepository.java
│   ├── FieldRepository.java
│   └── ReservationRepository.java
├── service/
│   └── ReservationService.java     # Business logic (Facade)
├── strategy/
│   ├── TariffStrategy.java         # Strategy Pattern Interface
│   ├── WeekdayTariff.java
│   └── WeekendTariff.java
├── decorator/
│   ├── PaymentDecorator.java       # Decorator Pattern Abstract
│   ├── TaxPaymentDecorator.java
│   └── InsurancePaymentDecorator.java
└── util/
    └── IdGenerator.java            # ID generation utility

test/
└── ReservationServiceTest.java     # JUnit Test Cases
```

## 📝 Penilaian yang Dipenuhi

| No | Kriteria Penilaian | Score | Status |
|----|-------------------|-------|--------|
| 1 | Penerapan Minimal 3 Design Pattern | 20 | ✅ Factory, Strategy, Decorator |
| 2 | Penerapan JUnit | 15 | ✅ 5 Test Cases |
| 3 | Penerapan JCF | 10 | ✅ HashMap, List, Optional, Stream |
| 4 | Penerapan Clean Code | 15 | ✅ Meaningful names, SRP, DRY |
| 5 | Penerapan Generic Programming | 15 | ✅ GenericRepository<T, K> |
| 6 | Penerapan GUI / CLI | 15 | ✅ CLI dengan menu interaktif |
| 7 | Design Diagram Kelas | 10 | ⚠️ Lihat section berikut |
| | **TOTAL** | **100** | |

## 📐 Diagram Kelas

### Class Diagram (Simplified)
```
┌─────────────────────┐
│   <<abstract>>      │
│       Field         │
├─────────────────────┤
│ - id: String        │
│ - name: String      │
│ - basePrice: double │
├─────────────────────┤
│ + getPricePerHour() │
│ + getType()         │
└──────────▲──────────┘
           │
    ┌──────┴──────┐
    │             │
┌───┴────┐   ┌───┴────────┐
│Futsal  │   │ Badminton  │
│Court   │   │  Court     │
└────────┘   └────────────┘

┌──────────────────────────┐
│  <<interface>>           │
│   TariffStrategy         │
├──────────────────────────┤
│ + calculatePrice()       │
└─────────────▲────────────┘
              │
       ┌──────┴──────┐
       │             │
┌──────┴─────┐ ┌────┴────────┐
│ Weekday    │ │  Weekend    │
│  Tariff    │ │   Tariff    │
└────────────┘ └─────────────┘

┌──────────────────────────┐
│     Payment              │
├──────────────────────────┤
│ - id: String             │
│ - amount: double         │
└──────────▲───────────────┘
           │
┌──────────┴───────────────┐
│  PaymentDecorator        │
├──────────────────────────┤
│ - decoratedPayment       │
└──────────▲───────────────┘
           │
    ┌──────┴──────┐
    │             │
┌───┴────────┐ ┌─┴──────────┐
│Tax         │ │ Insurance  │
│Decorator   │ │ Decorator  │
└────────────┘ └────────────┘
```

## 🔧 Troubleshooting

### Error: ClassNotFoundException
Pastikan semua file sudah dikompilasi dan path classpath benar.

### Error: Cannot find symbol (JUnit)
Pastikan JUnit 5 library sudah ada di classpath untuk running tests.

### Input tidak valid
Ikuti format input yang diminta (contoh: tanggal harus YYYY-MM-DD, jam harus 0-23).

## 📚 Referensi
- Design Patterns: Elements of Reusable Object-Oriented Software (Gang of Four)
- Effective Java by Joshua Bloch
- Clean Code by Robert C. Martin
- Java Documentation: https://docs.oracle.com/javase/8/docs/

## 📄 Lisensi
Project ini dibuat untuk keperluan akademis (Tugas Besar Praktikum OOP).

---
**Dibuat dengan ❤️ menggunakan Java & Design Patterns**
