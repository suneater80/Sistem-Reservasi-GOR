package app;

import factory.FieldFactory;
import model.*;
import repository.*;
import service.ReservationService;
import strategy.TariffStrategy;
import util.IdGenerator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

// Kelas Utama (Main Class) dengan Antarmuka CLI (Command Line Interface)
public class GorAppCli {
    private static UserRepository userRepository;
    private static FieldRepository fieldRepository;
    private static ReservationRepository reservationRepository;
    private static ReservationService reservationService;
    private static Scanner scanner;
    
    private static final String ADMIN_ID = "ADMIN001"; // ID khusus untuk Admin

    public static void main(String[] args) {
        // Inisialisasi Components
        userRepository = new UserRepository();
        fieldRepository = new FieldRepository();
        reservationRepository = new ReservationRepository();
        reservationService = new ReservationService(userRepository, fieldRepository, reservationRepository);
        scanner = new Scanner(System.in);
        
        // Panggil setup data awal
        setupInitialData();
        
        System.out.println("=================================================");
        System.out.println("  SISTEM RESERVASI GOR (OOP & Design Pattern)");
        System.out.println("=================================================");
        
        // Loop utama aplikasi
        int choice;
        do {
            showMainMenu();
            choice = getIntInput("Pilih menu: ");
            
            try {
                switch (choice) {
                    case 1:
                        handleAdminMenu();
                        break;
                    case 2:
                        handleCustomerMenu();
                        break;
                    case 0:
                        System.out.println("Terima kasih telah menggunakan sistem reservasi GOR.");
                        break;
                    default:
                        System.err.println("Pilihan tidak valid. Silakan coba lagi.");
                }
            } catch (Exception e) {
                System.err.println("\n[ERROR SISTEM] Terjadi kesalahan: " + e.getMessage());
                // e.printStackTrace(); // Uncomment untuk debugging detail
            }
        } while (choice != 0);
        
        scanner.close();
    }
    
    // =======================================================================
    // 1. Setup Data Awal (Refactoring FieldRepository Initialization)
    // =======================================================================
    private static void setupInitialData() {
        // Data Admin
        userRepository.save(new User(ADMIN_ID, "Administrator GOR", "00000000"));
        
        // Data Dummy Customer
        userRepository.save(new User("CUST001", "Ani Wulandari", "081211112222"));
        userRepository.save(new User("CUST002", "Budi Doremi", "081233334444"));

        // Data Dummy Lapangan menggunakan Factory Method (FASE 2)
        Field f1 = FieldFactory.createField("FUTSAL", "FUT01", "Lapangan Futsal 1", 120000.0, "12");
        Field f2 = FieldFactory.createField("FUTSAL", "FUT02", "Lapangan Futsal 2", 110000.0, "10");
        Field b1 = FieldFactory.createField("BADMINTON", "BAD01", "Lapangan Badminton A", 65000.0, "true");
        Field b2 = FieldFactory.createField("BADMINTON", "BAD02", "Lapangan Badminton B", 55000.0, "false");
        
        fieldRepository.save(f1);
        fieldRepository.save(f2);
        fieldRepository.save(b1);
        fieldRepository.save(b2);
        
        System.out.println("Data awal Lapangan (" + fieldRepository.count() + ") dan User (" + userRepository.count() + ") berhasil diinisialisasi.");
    }

    // =======================================================================
    // 2. Menu Utama & Login
    // =======================================================================
    private static void showMainMenu() {
        System.out.println("\n--- MENU UTAMA ---");
        System.out.println("1. Masuk sebagai Admin");
        System.out.println("2. Masuk sebagai Customer");
        System.out.println("0. Keluar");
    }

    private static void handleAdminMenu() {
        // Simulasi Login Admin
        String inputId = getStringInput("Masukkan ID Admin: ");
        if (!inputId.equals(ADMIN_ID)) {
            System.err.println("ID Admin salah.");
            return;
        }

        int choice;
        do {
            System.out.println("\n--- MENU ADMIN ---");
            System.out.println("1. Kelola Lapangan (CRUD)");
            System.out.println("2. Kelola Pengguna (CRUD)");
            System.out.println("3. Lihat Semua Reservasi");
            System.out.println("0. Kembali ke Menu Utama");
            choice = getIntInput("Pilih menu: ");
            
            switch (choice) {
                case 1: crudFieldMenu(); break;
                case 2: crudUserMenu(); break;
                case 3: viewAllReservations(reservationService.getAllReservations()); break;
                case 0: break;
                default: System.err.println("Pilihan tidak valid.");
            }
        } while (choice != 0);
    }
    
    private static void handleCustomerMenu() {
        // Simulasi Login Customer
        String userId = getStringInput("Masukkan ID Customer (Contoh: CUST001): ");
        Optional<User> userOpt = userRepository.findById(userId);

        if (!userOpt.isPresent()) {
            System.err.println("ID Customer tidak ditemukan. Kembali ke menu utama.");
            return;
        }
        
        User currentUser = userOpt.get();
        System.out.println("Selamat datang, " + currentUser.getName() + "!");

        int choice;
        do {
            System.out.println("\n--- MENU CUSTOMER ---");
            System.out.println("1. Lihat Daftar Lapangan");
            System.out.println("2. Buat Reservasi Baru");
            System.out.println("3. Batalkan Reservasi");
            System.out.println("4. Lihat Riwayat Reservasi Saya");
            System.out.println("0. Kembali ke Menu Utama");
            choice = getIntInput("Pilih menu: ");
            
            switch (choice) {
                case 1: viewAllFields(); break;
                case 2: createReservation(currentUser.getId()); break;
                case 3: cancelReservation(); break;
                case 4: viewMyReservations(currentUser.getId()); break;
                case 0: break;
                default: System.err.println("Pilihan tidak valid.");
            }
        } while (choice != 0);
    }
    
    // =======================================================================
    // 3. CRUD Lapangan (Admin)
    // =======================================================================
    private static void crudFieldMenu() {
        System.out.println("\n--- KELOLA LAPANGAN ---");
        viewAllFields();
        System.out.println("1. Tambah Lapangan Baru");
        System.out.println("2. Edit Harga Lapangan");
        System.out.println("3. Hapus Lapangan");
        System.out.println("0. Kembali");
        
        int choice = getIntInput("Pilih aksi: ");
        switch(choice) {
            case 1: addField(); break;
            case 2: editFieldPrice(); break;
            case 3: deleteField(); break;
        }
    }
    
    private static void viewAllFields() {
        List<Field> fields = fieldRepository.findAll();
        if (fields.isEmpty()) {
            System.out.println("Tidak ada data lapangan.");
            return;
        }
        System.out.println("\n--- DAFTAR LAPANGAN TERSEDIA (" + fields.size() + ") ---");
        fields.forEach(System.out::println);
    }
    
    private static void addField() {
        System.out.println("\n--- TAMBAH LAPANGAN BARU ---");
        String type = getStringInput("Jenis Lapangan (FUTSAL/BADMINTON): ");
        String name = getStringInput("Nama Lapangan: ");
        double price = getDoubleInput("Harga Dasar per Jam: ");
        String newId = IdGenerator.generateUniqueId(type.substring(0, 3).toUpperCase());
        
        try {
            Field newField;
            if (type.equalsIgnoreCase("FUTSAL")) {
                int capacity = getIntInput("Kapasitas Pemain: ");
                // Memanggil Factory Method
                newField = FieldFactory.createField(type, newId, name, price, String.valueOf(capacity));
            } else if (type.equalsIgnoreCase("BADMINTON")) {
                boolean isIndoor = getStringInput("Indoor? (true/false): ").equalsIgnoreCase("true");
                // Memanggil Factory Method
                newField = FieldFactory.createField(type, newId, name, price, String.valueOf(isIndoor));
            } else {
                System.err.println("Jenis lapangan tidak didukung.");
                return;
            }
            fieldRepository.save(newField);
            System.out.println("SUCCESS: Lapangan baru berhasil ditambahkan: " + newField.toString());
        } catch (Exception e) {
            System.err.println("Gagal menambahkan lapangan: " + e.getMessage());
        }
    }
    
    private static void editFieldPrice() {
        String id = getStringInput("Masukkan ID Lapangan yang akan diubah harganya: ");
        Optional<Field> fieldOpt = fieldRepository.findById(id);
        
        if (fieldOpt.isPresent()) {
            Field field = fieldOpt.get();
            System.out.printf("Harga lama Lapangan %s: %,.0f\n", field.getName(), field.getBasePricePerHour());
            double newPrice = getDoubleInput("Masukkan Harga Dasar per Jam yang baru: ");
            field.setBasePricePerHour(newPrice); // Encapsulation: menggunakan setter
            fieldRepository.save(field);
            System.out.println("SUCCESS: Harga Lapangan berhasil diperbarui!");
        } else {
            System.err.println("Lapangan tidak ditemukan.");
        }
    }
    
    private static void deleteField() {
        String id = getStringInput("Masukkan ID Lapangan yang akan dihapus: ");
        if (fieldRepository.findById(id).isPresent()) {
            fieldRepository.deleteById(id);
            System.out.println("SUCCESS: Lapangan ID " + id + " berhasil dihapus.");
        } else {
            System.err.println("Lapangan tidak ditemukan.");
        }
    }

    // =======================================================================
    // 4. CRUD Pengguna (Admin)
    // =======================================================================
    private static void crudUserMenu() {
        System.out.println("\n--- KELOLA PENGGUNA ---");
        viewAllUsers();
        System.out.println("1. Tambah Pengguna Baru");
        System.out.println("2. Hapus Pengguna");
        System.out.println("0. Kembali");
        
        int choice = getIntInput("Pilih aksi: ");
        switch(choice) {
            case 1: addUser(); break;
            case 2: deleteUser(); break;
        }
    }
    
    private static void viewAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            System.out.println("Tidak ada data pengguna.");
            return;
        }
        System.out.println("\n--- DAFTAR PENGGUNA (" + users.size() + ") ---");
        users.forEach(System.out::println);
    }
    
    private static void addUser() {
        System.out.println("\n--- TAMBAH PENGGUNA BARU ---");
        String name = getStringInput("Nama Pengguna: ");
        String phone = getStringInput("Nomor HP: ");
        String newId = IdGenerator.generateUniqueId("CUST");
        
        User newUser = new User(newId, name, phone);
        userRepository.save(newUser);
        System.out.println("SUCCESS: Pengguna baru berhasil ditambahkan: " + newUser.toString());
    }
    
    private static void deleteUser() {
        String id = getStringInput("Masukkan ID Pengguna yang akan dihapus: ");
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            System.out.println("SUCCESS: Pengguna ID " + id + " berhasil dihapus.");
        } else {
            System.err.println("Pengguna tidak ditemukan.");
        }
    }

    // =======================================================================
    // 5. Reservasi (Customer)
    // =======================================================================
    private static void createReservation(String userId) {
        System.out.println("\n--- BUAT RESERVASI BARU ---");
        viewAllFields();
        
        String fieldId = getStringInput("Pilih ID Lapangan: ");
        String dateStr = getStringInput("Tanggal Booking (YYYY-MM-DD): ");
        int startHour = getIntInput("Jam Mulai (00-23): ");
        int duration = getIntInput("Durasi Jam: ");
        
        if (duration <= 0 || startHour < 0 || startHour + duration > 24) {
            System.err.println("Input jam atau durasi tidak valid.");
            return;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            
            // Cek harga dan strategi yang akan digunakan
            Optional<Field> fieldOpt = fieldRepository.findById(fieldId);
            if (!fieldOpt.isPresent()) {
                System.err.println("Lapangan tidak ditemukan.");
                return;
            }
            
            Field field = fieldOpt.get();
            TariffStrategy strategy = reservationService.getTariffStrategy(date.getDayOfWeek());
            double estimatedPrice = strategy.calculatePrice(field, date.getDayOfWeek(), startHour, duration);
            
            System.out.println("\n--- DETAIL HARGA ---");
            System.out.println("Jenis Tarif: " + strategy.getName());
            System.out.printf("Total Biaya Estimasi (%.0f/jam x %d jam): Rp%,.0f\n", field.getPricePerHour(), duration, estimatedPrice);
            
            // Tanyakan opsi Pajak dan Asuransi (Decorator Pattern)
            String addTaxStr = getStringInput("Tambahkan Pajak PPN 10%? (YA/TIDAK): ");
            boolean addTax = addTaxStr.equalsIgnoreCase("YA");
            
            String addInsuranceStr = getStringInput("Tambahkan Asuransi Rp15.000? (YA/TIDAK): ");
            boolean addInsurance = addInsuranceStr.equalsIgnoreCase("YA");
            
            double finalPrice = estimatedPrice;
            if (addTax) finalPrice += estimatedPrice * 0.10;
            if (addInsurance) finalPrice += 15000.0;
            
            if (addTax || addInsurance) {
                System.out.printf("Total Biaya Final: Rp%,.0f\n", finalPrice);
            }
            
            String confirm = getStringInput("Konfirmasi Booking dan Bayar (YA/TIDAK): ");
            if (!confirm.equalsIgnoreCase("YA")) {
                System.out.println("Reservasi dibatalkan oleh pengguna.");
                return;
            }
            
            String paymentMethod = getStringInput("Metode Pembayaran (Contoh: CASH/TRANSFER): ");

            // Panggil Facade Method dengan Decorator options
            Optional<Reservation> resOpt = reservationService.bookField(userId, fieldId, date, startHour, duration, paymentMethod, addTax, addInsurance);
            
            if (resOpt.isPresent()) {
                System.out.println("\n[RESERVASI BERHASIL!]");
                System.out.println(resOpt.get());
                System.out.printf("Pembayaran Rp%,.0f berhasil diproses.\n", resOpt.get().getTotalFee());
            } else {
                // Pesan error sudah dicetak di Facade Service
                System.err.println("Reservasi GAGAL.");
            }
            
        } catch (java.time.format.DateTimeParseException e) {
            System.err.println("Format tanggal tidak valid. Gunakan YYYY-MM-DD.");
        }
    }
    
    private static void cancelReservation() {
        String resId = getStringInput("Masukkan ID Reservasi yang akan dibatalkan: ");
        // Panggil Facade Method
        reservationService.cancelReservation(resId);
    }
    
    private static void viewMyReservations(String userId) {
        List<Reservation> myReservations = reservationService.getAllReservations().stream()
            .filter(r -> r.getUser().getId().equals(userId))
            .collect(java.util.stream.Collectors.toList());
            
        viewAllReservations(myReservations);
    }
    
    private static void viewAllReservations(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("\n--- TIDAK ADA DATA RESERVASI ---");
            return;
        }
        
        System.out.println("\n--- DAFTAR RESERVASI (" + reservations.size() + ") ---");
        // Sort by date (Clean Code: Stream API)
        reservations.stream()
            .sorted(java.util.Comparator.comparing(Reservation::getDate).thenComparing(Reservation::getStartTimeHour))
            .forEach(System.out::println);
    }

    // =======================================================================
    // 6. Utility Input
    // =======================================================================
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Input harus berupa angka.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim().replace(",", "."); // Handle koma sebagai desimal
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.err.println("Input harus berupa angka desimal.");
            }
        }
    }
}