package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.Optional;
import repository.*;
import model.*;
import service.ReservationService;

public class CustomerDashboard extends JPanel {
    
    private GorAppGUI mainApp;
    private ReservationService reservationService;
    private UserRepository userRepository;
    private FieldRepository fieldRepository;
    private ReservationRepository reservationRepository;
    
    private User currentUser;
    
    private JTable reservationTable;
    private DefaultTableModel reservationTableModel;
    
    public CustomerDashboard(GorAppGUI mainApp) {
        this.mainApp = mainApp;
        this.userRepository = new UserRepository();
        this.fieldRepository = new FieldRepository();
        this.reservationRepository = new ReservationRepository();
        this.reservationService = new ReservationService(userRepository, fieldRepository, reservationRepository);
        
        // Load or create default user
        loadOrCreateUser();
        
        // Load sample data if needed
        loadSampleData();
        
        initComponents();
    }
    
    private void loadOrCreateUser() {
        // Try to get first user or create one
        var users = userRepository.findAll();
        if (!users.isEmpty()) {
            currentUser = users.get(0);
        } else {
            currentUser = new User("USR001", "Customer Demo", "08123456789");
            userRepository.save(currentUser);
        }
    }
    
    private void loadSampleData() {
        // Load sample fields if none exist
        if (fieldRepository.count() == 0) {
            fieldRepository.save(factory.FieldFactory.createField("FUTSAL", "FLD001", "Futsal A", 100000, "10"));
            fieldRepository.save(factory.FieldFactory.createField("FUTSAL", "FLD002", "Futsal B", 120000, "12"));
            fieldRepository.save(factory.FieldFactory.createField("BADMINTON", "FLD003", "Badminton 1", 50000, "true"));
            fieldRepository.save(factory.FieldFactory.createField("BADMINTON", "FLD004", "Badminton 2", 45000, "false"));
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setPreferredSize(new Dimension(0, 80)); // Lebih tinggi untuk 2 baris text
        
        // Tombol Kembali di KIRI (dalam wrapper panel dengan GridBagLayout untuk auto-center)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(150, 80)); // Same as header height
        
        JButton logoutButton = new JButton("<< Kembali");
        logoutButton.addActionListener(e -> mainApp.showWelcomeScreen());
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.BLACK);
        logoutButton.setFocusPainted(false);
        logoutButton.setPreferredSize(new Dimension(110, 25)); // Ukuran tombol
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Align ke kiri
        gbc.insets = new Insets(0, 10, 0, 0); // Left margin 10px
        
        leftPanel.add(logoutButton, gbc);
        
        // Dummy panel di KANAN untuk balance (sama lebarnya dengan leftPanel)
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(150, 80)); // Same as header height
        
        // Center panel dengan title dan user info
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0)); // Padding atas bawah
        
        JLabel titleLabel = new JLabel("CUSTOMER DASHBOARD");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel userLabel = new JLabel("User: " + currentUser.getName());
        userLabel.setFont(new Font("Poppins", Font.PLAIN, 13));
        userLabel.setForeground(new Color(240, 240, 240)); 
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(titleLabel);
        centerPanel.add(Box.createVerticalStrut(5)); // Space antara title dan user
        centerPanel.add(userLabel);
        
        // Add ke BorderLayout
        headerPanel.add(leftPanel, BorderLayout.WEST);     // Kiri
        headerPanel.add(centerPanel, BorderLayout.CENTER); // Tengah (2 labels stacked)
        headerPanel.add(rightPanel, BorderLayout.EAST);    // Kanan (untuk balance)
        
        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Poppins", Font.PLAIN, 14));
        
        // Tab 1: Booking Lapangan
        tabbedPane.addTab("[Booking] Lapangan", createBookingPanel());
        
        // Tab 2: Riwayat & Cancel
        tabbedPane.addTab("[Riwayat] Reservasi", createHistoryPanel());
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Booking"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // Field Selection
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Pilih Lapangan:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<String> fieldCombo = new JComboBox<>();
        loadFieldsToCombo(fieldCombo);
        formPanel.add(fieldCombo, gbc);
        
        row++;
        
        // Date
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Tanggal:"), gbc);
        
        gbc.gridx = 1;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2025, 2025, 2030, 1));
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        datePanel.add(new JLabel("Tahun:"));
        datePanel.add(yearSpinner);
        datePanel.add(new JLabel("Bulan:"));
        datePanel.add(monthSpinner);
        datePanel.add(new JLabel("Hari:"));
        datePanel.add(daySpinner);
        formPanel.add(datePanel, gbc);
        
        row++;
        
        // Start Time
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Jam Mulai:"), gbc);
        
        gbc.gridx = 1;
        JSpinner timeSpinner = new JSpinner(new SpinnerNumberModel(8, 6, 22, 1));
        formPanel.add(timeSpinner, gbc);
        
        row++;
        
        // Duration
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Durasi (jam):"), gbc);
        
        gbc.gridx = 1;
        JSpinner durationSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 8, 1));
        formPanel.add(durationSpinner, gbc);
        
        row++;
        
        // Payment Method
        gbc.gridx = 0;
        gbc.gridy = row;
        formPanel.add(new JLabel("Metode Pembayaran:"), gbc);
        
        gbc.gridx = 1;
        String[] paymentMethods = {"CASH", "TRANSFER", "QRIS"};
        JComboBox<String> paymentCombo = new JComboBox<>(paymentMethods);
        formPanel.add(paymentCombo, gbc);
        
        row++;
        
        // Tax Checkbox (DECORATOR PATTERN!)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JCheckBox taxCheckbox = new JCheckBox("Tambahkan Pajak PPN 10% (Tax Decorator)");
        taxCheckbox.setFont(new Font("Poppins", Font.BOLD, 12));
        taxCheckbox.setForeground(new Color(255, 69, 0));
        formPanel.add(taxCheckbox, gbc);
        
        row++;
        
        // Insurance Checkbox (DECORATOR PATTERN!)
        gbc.gridy = row;
        JCheckBox insuranceCheckbox = new JCheckBox("Tambahkan Asuransi Rp15.000 (Insurance Decorator)");
        insuranceCheckbox.setFont(new Font("Poppins", Font.BOLD, 12));
        insuranceCheckbox.setForeground(new Color(255, 69, 0));
        formPanel.add(insuranceCheckbox, gbc);
        
        row++;
        
        // Price Preview
        gbc.gridy = row;
        JLabel priceLabel = new JLabel("Estimasi Harga: -");
        priceLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 100, 0));
        formPanel.add(priceLabel, gbc);
        
        row++;
        
        // Calculate Button
        gbc.gridy = row;
        gbc.gridwidth = 2;
        JButton calculateButton = new JButton("[Hitung] Harga");
        calculateButton.addActionListener(e -> {
            try {
                String fieldId = getFieldIdFromCombo(fieldCombo);
                if (fieldId == null) {
                    JOptionPane.showMessageDialog(this, "Pilih lapangan terlebih dahulu!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                Field field = fieldRepository.findById(fieldId).orElse(null);
                if (field == null) return;
                
                int year = (Integer) yearSpinner.getValue();
                int month = (Integer) monthSpinner.getValue();
                int day = (Integer) daySpinner.getValue();
                LocalDate date = LocalDate.of(year, month, day);
                
                int duration = (Integer) durationSpinner.getValue();
                boolean withTax = taxCheckbox.isSelected();
                boolean withInsurance = insuranceCheckbox.isSelected();
                
                // Calculate price with strategy pattern
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                var strategy = reservationService.getTariffStrategy(dayOfWeek);
                int startTime = (Integer) timeSpinner.getValue();
                double basePrice = strategy.calculatePrice(field, dayOfWeek, startTime, duration);
                
                // Apply decorators
                double finalPrice = basePrice;
                if (withTax) {
                    finalPrice += basePrice * 0.10;
                }
                if (withInsurance) {
                    finalPrice += 15000;
                }
                
                String breakdown = String.format(
                    "<html>Base: Rp %.0f<br>" +
                    (withTax ? "Tax (10%%): Rp %.0f<br>" : "") +
                    (withInsurance ? "Insurance: Rp 15,000<br>" : "") +
                    "<b>TOTAL: Rp %.0f</b></html>",
                    basePrice,
                    withTax ? (basePrice * 0.10) : 0,
                    finalPrice
                );
                
                priceLabel.setText(breakdown);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        formPanel.add(calculateButton, gbc);
        
        row++;
        
        // Book Button
        gbc.gridy = row;
        JButton bookButton = new JButton(">>> BOOKING SEKARANG <<<");
        bookButton.setBackground(new Color(34, 139, 34));
        bookButton.setForeground(Color.BLACK);
        bookButton.setFont(new Font("Poppins", Font.BOLD, 16));
        bookButton.setPreferredSize(new Dimension(0, 50));
        bookButton.addActionListener(e -> {
            try {
                String fieldId = getFieldIdFromCombo(fieldCombo);
                if (fieldId == null) {
                    JOptionPane.showMessageDialog(this, "Pilih lapangan terlebih dahulu!", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                int year = (Integer) yearSpinner.getValue();
                int month = (Integer) monthSpinner.getValue();
                int day = (Integer) daySpinner.getValue();
                LocalDate date = LocalDate.of(year, month, day);
                
                int startTime = (Integer) timeSpinner.getValue();
                int duration = (Integer) durationSpinner.getValue();
                String paymentMethod = (String) paymentCombo.getSelectedItem();
                boolean withTax = taxCheckbox.isSelected();
                boolean withInsurance = insuranceCheckbox.isSelected();
                
                Optional<Reservation> result = reservationService.bookField(
                    currentUser.getId(),
                    fieldId,
                    date,
                    startTime,
                    duration,
                    paymentMethod,
                    withTax,
                    withInsurance
                );
                
                if (result.isPresent()) {
                    Reservation res = result.get();
                    JOptionPane.showMessageDialog(this,
                        String.format("Booking berhasil!\nID: %s\nTotal: Rp %.0f",
                            res.getId(), res.getPayment().getAmount()),
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadReservationData();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Booking gagal! Kemungkinan jadwal bentrok.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        formPanel.add(bookButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        String[] columns = {"ID", "Lapangan", "Tanggal", "Jam", "Durasi", "Status", "Total", "Payment Method"};
        reservationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(reservationTableModel);
        reservationTable.setRowHeight(25);
        reservationTable.setFont(new Font("Poppins", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton cancelButton = new JButton("[X] Batalkan Reservasi");
        cancelButton.setBackground(new Color(220, 20, 60));
        cancelButton.setForeground(Color.BLACK);
        cancelButton.addActionListener(e -> cancelReservation());
        
        JButton refreshButton = new JButton("[Refresh]");
        refreshButton.addActionListener(e -> loadReservationData());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadFieldsToCombo(JComboBox<String> combo) {
        combo.removeAllItems();
        var fields = fieldRepository.findAll();
        if (fields.isEmpty()) {
            combo.addItem("Tidak ada lapangan tersedia");
            return;
        }
        for (Field field : fields) {
            String type = field instanceof model.FutsalCourt ? "[FUTSAL]" : "[BADMINTON]";
            String label = String.format("%s %s - Rp %.0f/jam", type, field.getName(), field.getPricePerHour());
            combo.addItem(field.getId() + "||" + label);
        }
    }
    
    private String getFieldIdFromCombo(JComboBox<String> combo) {
        String selected = (String) combo.getSelectedItem();
        if (selected == null || !selected.contains("||")) return null;
        return selected.split("\\|\\|")[0];
    }
    
    private void loadReservationData() {
        reservationTableModel.setRowCount(0);
        for (Reservation res : reservationRepository.findAll()) {
            if (!res.getUser().getId().equals(currentUser.getId())) continue;
            
            Field field = res.getField();
            
            reservationTableModel.addRow(new Object[]{
                res.getId(),
                field != null ? field.getName() : "-",
                res.getDate(),
                res.getStartTimeHour() + ":00",
                res.getDurationHours() + " jam",
                res.getStatus(),
                String.format("Rp %.0f", res.getPayment().getAmount()),
                res.getPayment().getMethod()
            });
        }
    }
    
    private void cancelReservation() {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih reservasi yang akan dibatalkan!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String resId = (String) reservationTableModel.getValueAt(selectedRow, 0);
        String status = (String) reservationTableModel.getValueAt(selectedRow, 5);
        
        if (status.equals("CANCELED")) {
            JOptionPane.showMessageDialog(this, "Reservasi ini sudah dibatalkan!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin membatalkan reservasi ini?\nRefund 50% akan diproses.",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = reservationService.cancelReservation(resId);
            if (success) {
                loadReservationData();
                JOptionPane.showMessageDialog(this, "Reservasi berhasil dibatalkan!\nRefund 50% telah diproses.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal membatalkan reservasi!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void refreshData() {
        loadReservationData();
    }
}
