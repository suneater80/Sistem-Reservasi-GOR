# LAPORAN TUGAS BESAR PEMROGRAMAN BERORIENTASI OBJEK
## SISTEM RESERVASI GELANGGANG OLAHRAGA RAKYAT (GOR)

---

## 📋 IDENTITAS KELOMPOK

**Nama Kelompok:** [Isi nama kelompok]

**Anggota:**
1. [Nama Lengkap] - [NIM]
2. [Nama Lengkap] - [NIM]
3. [Nama Lengkap] - [NIM]

**Kelas:** [Isi kelas]

**Dosen:** [Isi nama dosen]

**Tanggal:** Desember 2025

---

## 1. PENDAHULUAN

### 1.1 Latar Belakang
Sistem Reservasi GOR adalah aplikasi berbasis Command Line Interface (CLI) yang dikembangkan untuk memenuhi kebutuhan pengelolaan reservasi lapangan olahraga. Aplikasi ini dirancang dengan menerapkan konsep Object-Oriented Programming (OOP) yang baik serta mengimplementasikan minimal 3 Design Pattern dari kategori yang berbeda.

### 1.2 Tujuan
Tujuan dari pembuatan aplikasi ini adalah:
1. Menerapkan konsep OOP secara komprehensif
2. Mengimplementasikan Design Patterns (Creational, Structural, Behavioral)
3. Menerapkan Generic Programming dan Java Collections Framework
4. Melakukan Unit Testing dengan JUnit
5. Menerapkan prinsip Clean Code

### 1.3 Tools dan Teknologi
- **Bahasa Pemrograman:** Java (JDK 8+)
- **IDE:** Visual Studio Code / IntelliJ IDEA
- **Testing Framework:** JUnit 5
- **Version Control:** Git & GitHub

---

## 2. DESIGN PATTERNS

### 2.1 Factory Method Pattern (Creational)

**Deskripsi:**
Factory Method Pattern digunakan untuk membuat objek Field (lapangan) tanpa mengekspos logika pembuatan kepada client. Pattern ini memungkinkan client untuk membuat objek melalui interface tanpa perlu mengetahui class konkret yang akan diinstansiasi.

**Implementasi:**

```
[Screenshot: src/factory/FieldFactory.java]
Caption: Implementasi Factory Method Pattern - FieldFactory
```

**Penjelasan Kode:**
- Method `createField()` menerima parameter type ("FUTSAL" atau "BADMINTON")
- Berdasarkan type, factory akan membuat instance yang sesuai
- Client tidak perlu tahu detail konstruksi FutsalCourt atau BadmintonCourt

**Penggunaan:**

```
[Screenshot: Penggunaan FieldFactory di GorAppCli.java]
Caption: Contoh penggunaan Factory Pattern untuk membuat lapangan
```

**Keuntungan:**
1. Mengurangi coupling antara client dan class konkret
2. Mudah menambahkan jenis lapangan baru (Tennis, Basketball, dll)
3. Single Responsibility: pembuatan objek terpisah dari logika bisnis

---

### 2.2 Strategy Pattern (Behavioral)

**Deskripsi:**
Strategy Pattern digunakan untuk mengimplementasikan algoritma perhitungan tarif yang berbeda (weekday vs weekend) dan dapat dipilih saat runtime. Pattern ini memungkinkan algoritma bervariasi secara independen dari client yang menggunakannya.

**Implementasi:**

```
[Screenshot: src/strategy/TariffStrategy.java - Interface]
Caption: Interface TariffStrategy
```

```
[Screenshot: src/strategy/WeekdayTariff.java]
Caption: Concrete Strategy - WeekdayTariff
```

```
[Screenshot: src/strategy/WeekendTariff.java]
Caption: Concrete Strategy - WeekendTariff (15% surcharge)
```

**Penjelasan:**
- **Interface TariffStrategy**: Mendefinisikan method `calculatePrice()`
- **WeekdayTariff**: Implementasi untuk hari kerja (tanpa surcharge)
- **WeekendTariff**: Implementasi untuk akhir pekan (+ 15%)

**Penggunaan:**

```
[Screenshot: Penggunaan Strategy di ReservationService.java]
Caption: Pemilihan strategi tarif berdasarkan hari
```

**Keuntungan:**
1. Algoritma dapat di-swap saat runtime
2. Mudah menambahkan strategi baru (Holiday tariff, Member discount)
3. Menghindari conditional statements yang kompleks

---

### 2.3 Decorator Pattern (Structural)

**Deskripsi:**
Decorator Pattern digunakan untuk menambahkan fitur tambahan (Tax dan Insurance) pada Payment secara dinamis tanpa mengubah struktur class Payment yang sudah ada. Pattern ini mengikuti prinsip Open/Closed Principle.

**Implementasi:**

```
[Screenshot: src/decorator/PaymentDecorator.java]
Caption: Abstract Decorator - PaymentDecorator
```

```
[Screenshot: src/decorator/TaxPaymentDecorator.java]
Caption: Concrete Decorator - TaxPaymentDecorator (PPN 10%)
```

```
[Screenshot: src/decorator/InsurancePaymentDecorator.java]
Caption: Concrete Decorator - InsurancePaymentDecorator (Rp15.000)
```

**Penjelasan:**
- **PaymentDecorator**: Abstract class yang extends Payment dan menyimpan reference ke Payment
- **TaxPaymentDecorator**: Menambahkan pajak 10% pada payment
- **InsurancePaymentDecorator**: Menambahkan asuransi Rp15.000

**Penggunaan:**

```
[Screenshot: Penggunaan Decorator di ReservationService.java]
Caption: Menambahkan Tax dan Insurance secara optional
```

**Keuntungan:**
1. Dapat menambahkan fitur tanpa mengubah class original
2. Dapat dikombinasikan (stacking) - Payment + Tax + Insurance
3. Menghindari class explosion (PaymentWithTax, PaymentWithInsurance, dll)

---

## 3. KONSEP OBJECT-ORIENTED PROGRAMMING

### 3.1 Inheritance (Pewarisan)

**Implementasi:**

```
[Screenshot: src/model/Field.java - Abstract class]
Caption: Abstract class Field sebagai parent
```

```
[Screenshot: src/model/FutsalCourt.java]
Caption: FutsalCourt extends Field
```

```
[Screenshot: src/model/BadmintonCourt.java]
Caption: BadmintonCourt extends Field
```

**Penjelasan:**
- `Field` adalah abstract class dengan property dasar (id, name, basePricePerHour)
- `FutsalCourt` dan `BadmintonCourt` mewarisi dari Field
- Setiap subclass menambahkan attribute spesifik (capacity untuk Futsal, isIndoor untuk Badminton)

---

### 3.2 Polymorphism (Polimorfisme)

**Implementasi:**

```
[Screenshot: Method getPricePerHour() di Field, FutsalCourt, BadmintonCourt]
Caption: Polymorphism - Override method getPricePerHour()
```

**Penjelasan:**
- Method abstrak `getPricePerHour()` di Field
- Di-override di FutsalCourt: menambahkan surcharge Rp5.000
- Di-override di BadmintonCourt: return base price tanpa surcharge
- Client dapat memanggil method yang sama, tapi behavior berbeda

---

### 3.3 Encapsulation

**Implementasi:**

```
[Screenshot: Private fields + getter/setter di class User atau Field]
Caption: Encapsulation - Private fields dengan accessor methods
```

**Penjelasan:**
- Semua field di model classes menggunakan modifier `private`
- Akses ke field hanya melalui public getter/setter
- Data hiding untuk menjaga integritas data

---

### 3.4 Abstraction

**Implementasi:**

```
[Screenshot: Abstract class Field dan Interface TariffStrategy]
Caption: Abstraction melalui abstract class dan interface
```

**Penjelasan:**
- `Field` sebagai abstract class mendefinisikan template
- `TariffStrategy` sebagai interface mendefinisikan kontrak
- Client berinteraksi dengan abstraction, bukan implementation detail

---

## 4. GENERIC PROGRAMMING

### 4.1 GenericRepository<T, K>

**Implementasi:**

```
[Screenshot: src/repository/GenericRepository.java]
Caption: Generic Repository dengan type parameters T dan K
```

**Penjelasan:**
- `T`: Type parameter untuk entity (User, Field, Reservation)
- `K`: Type parameter untuk key/ID (String dalam kasus ini)
- Generic methods: `save(T)`, `findById(K)`, `findAll()`, dll

**Penggunaan:**

```
[Screenshot: UserRepository extends GenericRepository<User, String>]
Caption: UserRepository menggunakan Generic Repository
```

```
[Screenshot: FieldRepository extends GenericRepository<Field, String>]
Caption: FieldRepository menggunakan Generic Repository
```

**Keuntungan:**
1. **DRY (Don't Repeat Yourself)**: CRUD operations tidak perlu diulang
2. **Type Safety**: Compiler akan check type mismatch
3. **Reusable**: Dapat digunakan untuk entity baru tanpa code duplication

---

## 5. JAVA COLLECTIONS FRAMEWORK (JCF)

### 5.1 HashMap

**Implementasi:**

```
[Screenshot: Penggunaan HashMap di GenericRepository]
Caption: HashMap untuk penyimpanan in-memory
```

**Penjelasan:**
- `Map<K, T> dataMap` untuk menyimpan entity dengan key-value pair
- Operasi O(1) untuk insert, find, delete
- Thread-safe tidak diperlukan karena single-threaded CLI

---

### 5.2 List & Stream API

**Implementasi:**

```
[Screenshot: Method findAll() dan penggunaan Stream]
Caption: List collection dan Stream API untuk filtering
```

**Penjelasan:**
- `List<T>` sebagai return type untuk multiple results
- Stream API untuk operasi filter, map, collect
- Contoh: filtering reservasi by user ID, field ID, date

---

### 5.3 Optional

**Implementasi:**

```
[Screenshot: Method findById() dengan Optional return type]
Caption: Optional untuk null-safe operations
```

**Penjelasan:**
- `Optional<T>` untuk method yang mungkin tidak return value
- Menghindari NullPointerException
- Client harus explicitly handle empty case dengan `isPresent()` atau `orElse()`

---

## 6. JUNIT TESTING

### 6.1 Test Suite Overview

**Test Explorer:**

```
[Screenshot: VS Code Test Explorer showing all 5 tests]
Caption: JUnit Test Suite - 5 Test Cases
```

**Test Results:**

```
[Screenshot: All tests passed (green checkmarks)]
Caption: All Tests Passed Successfully
```

**Test Coverage:**
- ✅ testPriceCalculationStrategy (Strategy Pattern)
- ✅ testTimeSlotConflict (Business Logic)
- ✅ testCancellationAndRefund (CRUD Operations)
- ✅ testPaymentDecorator (Decorator Pattern)
- ✅ testGenericRepository (Generic Programming)

---

### 6.2 Test Case Details

#### Test 1: testPriceCalculationStrategy

```
[Screenshot: Test code untuk testPriceCalculationStrategy]
Caption: Unit test untuk Strategy Pattern - Weekday vs Weekend pricing
```

**Penjelasan:**
- Test perhitungan harga weekday (no surcharge)
- Test perhitungan harga weekend (+15%)
- Verify bahwa strategy pattern berfungsi dengan benar

**Expected Result:**
- Weekday: 105,000 x 2 jam = 210,000
- Weekend: 210,000 x 1.15 = 241,500

---

#### Test 2: testTimeSlotConflict

```
[Screenshot: Test code untuk testTimeSlotConflict]
Caption: Unit test untuk validasi bentrok jadwal
```

**Penjelasan:**
- Test deteksi konflik jadwal (overlap)
- Test bahwa slot yang tidak overlap tidak terdeteksi konflik
- Test bahwa lapangan berbeda tidak konflik

---

#### Test 3: testCancellationAndRefund

```
[Screenshot: Test code untuk testCancellationAndRefund]
Caption: Unit test untuk pembatalan dan refund
```

**Penjelasan:**
- Test proses pembatalan reservasi
- Test bahwa refund 50% diberikan
- Test bahwa status berubah menjadi CANCELED
- Test bahwa pembatalan kedua kali ditolak

---

#### Test 4: testPaymentDecorator

```
[Screenshot: Test code untuk testPaymentDecorator]
Caption: Unit test untuk Decorator Pattern
```

**Penjelasan:**
- Test base payment amount
- Test TaxPaymentDecorator (10% tax)
- Test InsurancePaymentDecorator (Rp15,000)
- Test stacking decorators

---

#### Test 5: testGenericRepository

```
[Screenshot: Test code untuk testGenericRepository]
Caption: Unit test untuk Generic Repository CRUD
```

**Penjelasan:**
- Test save operation
- Test findById operation
- Test existsById operation
- Test delete operation
- Test count operation

---

## 7. CLEAN CODE PRACTICES

### 7.1 Meaningful Names

```
[Screenshot: Contoh variable/method names yang deskriptif]
Caption: Meaningful names - self-documenting code
```

**Contoh:**
- `calculatePrice()` lebih baik dari `calc()`
- `userRepository` lebih baik dari `ur`
- `isTimeSlotConflicted()` jelas menunjukkan fungsinya

---

### 7.2 Single Responsibility Principle

```
[Screenshot: Class dengan single responsibility]
Caption: SRP - Setiap class punya satu tanggung jawab
```

**Contoh:**
- `UserRepository`: hanya handle data persistence User
- `ReservationService`: hanya handle business logic reservasi
- `FieldFactory`: hanya handle pembuatan Field objects

---

### 7.3 DRY (Don't Repeat Yourself)

```
[Screenshot: GenericRepository menghilangkan code duplication]
Caption: DRY principle dengan Generic Repository
```

**Penjelasan:**
Tanpa Generic Repository, kita harus copy-paste CRUD methods di setiap repository. Dengan Generic Repository, code ditulis sekali dan di-reuse.

---

## 8. DEMO APLIKASI

### 8.1 Menu Utama

```
[Screenshot: Menu utama aplikasi]
Caption: Tampilan menu utama - Login Admin atau Customer
```

---

### 8.2 Fitur Admin

#### Menu Admin

```
[Screenshot: Menu admin]
Caption: Menu Admin - Kelola Lapangan, User, Reservasi
```

#### CRUD Lapangan

```
[Screenshot: Tambah lapangan baru]
Caption: Admin - Menambahkan lapangan Futsal baru
```

```
[Screenshot: Edit harga lapangan]
Caption: Admin - Mengubah harga lapangan
```

```
[Screenshot: Hapus lapangan]
Caption: Admin - Menghapus lapangan
```

---

### 8.3 Fitur Customer

#### Menu Customer

```
[Screenshot: Menu customer]
Caption: Menu Customer - Booking, Cancel, Riwayat
```

#### Booking Normal

```
[Screenshot: Proses booking tanpa tax & insurance]
Caption: Customer - Booking lapangan Futsal (weekday)
```

```
[Screenshot: Konfirmasi booking]
Caption: Detail reservasi dan total biaya
```

#### Booking dengan Tax & Insurance (Decorator Pattern)

```
[Screenshot: Input pilihan Tax]
Caption: Customer - Memilih tambah Pajak PPN 10%
```

```
[Screenshot: Input pilihan Insurance]
Caption: Customer - Memilih tambah Asuransi Rp15,000
```

```
[Screenshot: Total biaya final dengan tax & insurance]
Caption: Total biaya = Base + Tax + Insurance
```

**Perhitungan:**
- Base price (Futsal 2 jam weekday): 210,000
- Tax (10%): 21,000
- Insurance: 15,000
- **Total: 246,000**

#### Booking Weekend (Strategy Pattern)

```
[Screenshot: Booking di weekend]
Caption: Customer - Booking lapangan di akhir pekan
```

```
[Screenshot: Harga dengan surcharge 15%]
Caption: Tarif weekend +15% dari weekday
```

#### Pembatalan Reservasi

```
[Screenshot: Input ID reservasi untuk dibatalkan]
Caption: Customer - Membatalkan reservasi
```

```
[Screenshot: Konfirmasi refund 50%]
Caption: Refund 50% berhasil diproses
```

#### Riwayat Reservasi

```
[Screenshot: List reservasi customer]
Caption: Customer - Melihat riwayat reservasi
```

---

## 9. CLASS DIAGRAM

### 9.1 Factory Pattern Diagram

```
[Diagram: Field hierarchy dengan Factory]

        ┌─────────────────┐
        │  FieldFactory   │
        │  <<factory>>    │
        └────────┬────────┘
                 │ creates
                 ▼
        ┌─────────────────┐
        │   <<abstract>>  │
        │      Field      │
        ├─────────────────┤
        │ - id            │
        │ - name          │
        │ - basePrice     │
        ├─────────────────┤
        │ + getPricePerHour() │
        └────────▲────────┘
                 │
         ┌───────┴───────┐
         │               │
   ┌─────┴─────┐   ┌────┴────────┐
   │  Futsal   │   │  Badminton  │
   │  Court    │   │    Court    │
   └───────────┘   └─────────────┘
```

Caption: Class Diagram - Factory Method Pattern

---

### 9.2 Strategy Pattern Diagram

```
[Diagram: Strategy Pattern]

   ┌────────────────────────┐
   │   <<interface>>        │
   │   TariffStrategy       │
   ├────────────────────────┤
   │ + calculatePrice()     │
   └──────────▲─────────────┘
              │
       ┌──────┴──────┐
       │             │
┌──────┴─────┐ ┌────┴────────┐
│  Weekday   │ │   Weekend   │
│  Tariff    │ │   Tariff    │
│            │ │   (+15%)    │
└────────────┘ └─────────────┘
```

Caption: Class Diagram - Strategy Pattern

---

### 9.3 Decorator Pattern Diagram

```
[Diagram: Decorator Pattern]

      ┌────────────────┐
      │    Payment     │
      ├────────────────┤
      │ - id           │
      │ - amount       │
      └────────▲───────┘
               │
      ┌────────┴───────────┐
      │ PaymentDecorator   │
      │ <<abstract>>       │
      ├────────────────────┤
      │ - decoratedPayment │
      └────────▲───────────┘
               │
        ┌──────┴──────┐
        │             │
┌───────┴────┐ ┌──────┴──────┐
│    Tax     │ │  Insurance  │
│ Decorator  │ │  Decorator  │
│  (+10%)    │ │  (+15k)     │
└────────────┘ └─────────────┘
```

Caption: Class Diagram - Decorator Pattern

---

### 9.4 Overall Architecture

```
[Diagram: Overall system architecture]

┌─────────────────────────────────────────┐
│           GorAppCli (Main)              │
│              <<CLI>>                    │
└────────────────┬────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────┐
│       ReservationService                │
│         <<Service Layer>>               │
└────┬────────────────────────┬───────────┘
     │                        │
     ▼                        ▼
┌─────────────┐      ┌─────────────────┐
│ Repository  │      │  Factory &      │
│   Layer     │      │  Strategies     │
└─────────────┘      └─────────────────┘
     │                        │
     ▼                        ▼
┌─────────────────────────────────────────┐
│            Model Layer                  │
│  (User, Field, Reservation, Payment)    │
└─────────────────────────────────────────┘
```

Caption: Overall Architecture Diagram

---

## 10. STRUKTUR PROJECT

```
Sistem-Reservasi-GOR/
├── src/
│   ├── app/
│   │   └── GorAppCli.java           # Main application
│   ├── decorator/                    # Decorator Pattern
│   │   ├── PaymentDecorator.java
│   │   ├── TaxPaymentDecorator.java
│   │   └── InsurancePaymentDecorator.java
│   ├── factory/                      # Factory Pattern
│   │   └── FieldFactory.java
│   ├── model/                        # Domain Models
│   │   ├── Field.java
│   │   ├── FutsalCourt.java
│   │   ├── BadmintonCourt.java
│   │   ├── User.java
│   │   ├── Reservation.java
│   │   └── Payment.java
│   ├── repository/                   # Data Access Layer
│   │   ├── GenericRepository.java   # Generic Programming
│   │   ├── UserRepository.java
│   │   ├── FieldRepository.java
│   │   └── ReservationRepository.java
│   ├── service/                      # Business Logic
│   │   └── ReservationService.java
│   ├── strategy/                     # Strategy Pattern
│   │   ├── TariffStrategy.java
│   │   ├── WeekdayTariff.java
│   │   └── WeekendTariff.java
│   └── util/
│       └── IdGenerator.java
├── test/
│   └── ReservationServiceTest.java  # JUnit Tests
├── compile.bat                       # Compile script
├── run.bat                          # Run script
└── README.md                        # Documentation
```

---

## 11. PENILAIAN YANG DIPENUHI

| No | Kriteria Penilaian | Score | Status | Bukti |
|----|-------------------|-------|--------|-------|
| 1 | Penerapan Minimal 3 Design Pattern | 20 | ✅ | Factory, Strategy, Decorator |
| 2 | Penerapan JUnit | 15 | ✅ | 5 test cases, all passed |
| 3 | Penerapan JCF | 10 | ✅ | HashMap, List, Optional, Stream |
| 4 | Penerapan Clean Code | 15 | ✅ | Meaningful names, SRP, DRY |
| 5 | Penerapan Generic Programming | 15 | ✅ | GenericRepository<T, K> |
| 6 | Penerapan GUI / CLI | 15 | ✅ | Interactive CLI |
| 7 | Design Diagram Kelas | 10 | ✅ | Class diagrams |
| | **TOTAL** | **100** | ✅ | |

---

## 12. KESIMPULAN

### 12.1 Pencapaian

Dalam pengembangan Sistem Reservasi GOR ini, tim kami berhasil:

1. **Implementasi OOP**: Menerapkan 4 pilar OOP (Inheritance, Polymorphism, Encapsulation, Abstraction) secara komprehensif
2. **Design Patterns**: Mengimplementasikan 3 Design Pattern dari kategori berbeda:
   - Factory Method (Creational)
   - Strategy (Behavioral)
   - Decorator (Structural)
3. **Generic Programming**: Membuat GenericRepository yang mengurangi code duplication
4. **JCF**: Menggunakan HashMap, List, Optional, dan Stream API
5. **Unit Testing**: Membuat 5 test cases dengan JUnit 5, semua passed
6. **Clean Code**: Menerapkan prinsip-prinsip clean code (meaningful names, SRP, DRY)
7. **Aplikasi Fungsional**: CLI yang berfungsi dengan baik untuk Admin dan Customer

### 12.2 Challenges

Beberapa tantangan yang dihadapi:
1. Memahami kapan menggunakan design pattern yang tepat
2. Setup JUnit testing environment
3. Implementasi Generic programming dengan type parameters
4. Mengelola kompleksitas dengan menerapkan Clean Code

### 12.3 Lessons Learned

Hal-hal yang dipelajari:
1. Design patterns membuat code lebih maintainable dan extensible
2. Generic programming sangat powerful untuk mengurangi code duplication
3. Unit testing penting untuk memastikan code quality
4. Clean Code bukan hanya soal syntax, tapi juga architecture

### 12.4 Future Improvements

Pengembangan di masa depan:
1. Implementasi GUI dengan JavaFX atau Swing
2. Persistent storage dengan database (MySQL/PostgreSQL)
3. Additional features: payment gateway integration, report generation
4. More design patterns: Observer (untuk notifications), Singleton (untuk configurations)

---

## LAMPIRAN

### A. Source Code Repository
- GitHub: [URL Repository]
- Branch: main
- Last Updated: [Tanggal]

### B. Referensi
1. Design Patterns: Elements of Reusable Object-Oriented Software - Gang of Four
2. Effective Java (3rd Edition) - Joshua Bloch
3. Clean Code - Robert C. Martin
4. Java Documentation: https://docs.oracle.com/javase/8/docs/

### C. Video Demo (Optional)
- YouTube Link: [URL]
- Duration: [X minutes]

---

**Terima kasih!**

[Tanda tangan ketiga anggota kelompok]

---

