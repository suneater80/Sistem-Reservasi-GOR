# 🧪 CARA MENJALANKAN JUNIT TESTS

## ⚠️ PERHATIAN: JANGAN GUNAKAN "RUN CODE"!

**Run Code extension TIDAK BISA** untuk JUnit karena:
- ❌ Tidak include JUnit library
- ❌ Tidak compile dependencies
- ❌ Mencoba run sebagai program biasa (bukan test runner)

## ✅ 3 CARA YANG BENAR:

---

## 🎯 CARA 1: ALL-IN-ONE (PALING MUDAH) ⭐ RECOMMENDED

**Langkah:**
1. Klik kanan di `test-all.bat`
2. Pilih **"Run in Terminal"**
3. Tunggu sampai selesai (download → compile → run tests)
4. Screenshot hasilnya!

**Atau dari terminal:**
```powershell
.\test-all.bat
```

**Script ini akan:**
- Download JUnit otomatis (jika belum ada)
- Compile semua source code
- Compile test code
- Run semua 5 test cases
- Tampilkan hasil (SUCCESS/FAILED)

---

## 🔧 CARA 2: MANUAL STEP-BY-STEP

### Step 1: Download JUnit
```powershell
.\download-junit.bat
```
Akan download `junit-platform-console-standalone-1.10.1.jar` ke folder `lib\`

### Step 2: Compile Project + Tests
```powershell
.\compile-test.bat
```
Compile semua file `.java` ke folder `bin\`

### Step 3: Run Tests
```powershell
.\run-test.bat
```
Jalankan JUnit test runner

---

## 🎨 CARA 3: VS CODE TEST EXPLORER (PALING BAGUS UNTUK SCREENSHOT)

### Prasyarat:
Install extension: **"Extension Pack for Java"** by Microsoft

### Langkah:
1. **Buka VS Code**
2. **Tekan `Ctrl+Shift+X`** (Extensions)
3. **Search:** "Extension Pack for Java"
4. **Install** by Microsoft
5. **Restart VS Code**

### Setelah Install:
1. **Klik icon Testing** di sidebar kiri (icon flask 🧪)
2. **Tunggu beberapa detik** sampai test terdeteksi
3. **Klik tombol ▶️ "Run All Tests"**
4. **Screenshot hasilnya!** ✅ (5/5 tests passed)

### Jika Tidak Muncul:
- Buka file `test/ReservationServiceTest.java`
- Klik **"Run Test"** atau **"Debug Test"** di atas method `@Test`
- Test Explorer akan muncul otomatis

---

## 📊 OUTPUT YANG BENAR

Jika semua test **PASSED**, outputnya seperti ini:

```
╷
├─ JUnit Jupiter ✔
│  └─ ReservationServiceTest ✔
│     ├─ testPriceCalculationStrategy() ✔
│     ├─ testTimeSlotConflict() ✔
│     ├─ testCancellationAndRefund() ✔
│     ├─ testPaymentDecorator() ✔
│     └─ testGenericRepository() ✔
╵

Test run finished after X ms
[         5 containers found      ]
[         0 containers skipped    ]
[         5 containers started    ]
[         0 containers aborted    ]
[         5 containers successful ]
[         0 containers failed     ]
[         5 tests found           ]
[         0 tests skipped         ]
[         5 tests started         ]
[         0 tests aborted         ]
[         5 tests successful      ] ✅
[         0 tests failed          ]
```

---

## 🚫 ERROR YANG SERING TERJADI

### Error: "package org.junit.jupiter.api does not exist"
**Penyebab:** JUnit library belum di-download
**Solusi:** Run `download-junit.bat` dulu

### Error: "cannot find symbol - class User/Field/Reservation"
**Penyebab:** Source code belum di-compile
**Solusi:** Run `compile.bat` atau `compile-test.bat`

### Error: "No tests found"
**Penyebab:** Test class belum di-compile dengan JUnit di classpath
**Solusi:** Pastikan pakai `compile-test.bat`, bukan `javac` manual

### Error di "Run Code"
**Penyebab:** Run Code tidak support JUnit
**Solusi:** **JANGAN pakai Run Code!** Pakai salah satu dari 3 cara di atas.

---

## 📸 SCREENSHOT UNTUK LAPORAN

Ambil screenshot untuk:

1. ✅ **Test Explorer showing 5 tests** (VS Code sidebar)
2. ✅ **All tests passed (green checkmarks)**
3. ✅ **Terminal output** dari `test-all.bat` atau `run-test.bat`
4. ✅ **Individual test code** (5 test methods di `ReservationServiceTest.java`)

**Tips:**
- Pakai **Cara 3 (Test Explorer)** untuk screenshot paling profesional
- Atau pakai **Cara 1 (test-all.bat)** lalu screenshot terminal output

---

## 🎯 QUICK TROUBLESHOOTING

| Problem | Solution |
|---------|----------|
| "Run Code" error | ❌ Don't use it! Use `test-all.bat` |
| Tests not detected in VS Code | Install "Extension Pack for Java" |
| Compilation error | Run `compile.bat` first |
| JUnit not found | Run `download-junit.bat` |
| Test failed | Check error message, fix code, re-run |

---

## 📝 SUMMARY

**UNTUK SCREENSHOT LAPORAN:**
```powershell
# Cara tercepat:
.\test-all.bat

# Atau pakai VS Code Test Explorer (paling bagus)
```

**JANGAN:**
- ❌ Run dari "Run Code" button
- ❌ Compile test pakai `javac` biasa tanpa JUnit di classpath
- ❌ Run test pakai `java ReservationServiceTest` (ini bukan main method!)

**HARUS:**
- ✅ Pakai `test-all.bat` atau batch scripts lainnya
- ✅ Atau pakai VS Code Test Explorer
- ✅ Screenshot hasil test yang passed (5/5)

---

**Good luck! 🚀**
