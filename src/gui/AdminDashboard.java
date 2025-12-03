package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import repository.*;
import model.*;
import factory.FieldFactory;

/**
 * Admin Dashboard - CRUD Lapangan, User, Lihat Reservasi
 */
public class AdminDashboard extends JPanel {
    
    private GorAppGUI mainApp;
    private FieldRepository fieldRepository;
    private UserRepository userRepository;
    private ReservationRepository reservationRepository;
    
    // Field Management
    private JTable fieldTable;
    private DefaultTableModel fieldTableModel;
    
    // User Management  
    private JTable userTable;
    private DefaultTableModel userTableModel;
    
    // Reservation View
    private JTable reservationTable;
    private DefaultTableModel reservationTableModel;
    
    public AdminDashboard(GorAppGUI mainApp) {
        this.mainApp = mainApp;
        this.fieldRepository = new FieldRepository();
        this.userRepository = new UserRepository();
        this.reservationRepository = new ReservationRepository();
        
        initComponents();
        loadSampleData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        JButton logoutButton = new JButton("<< Kembali");
        logoutButton.addActionListener(e -> mainApp.showWelcomeScreen());
        headerPanel.add(logoutButton);
        
        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Tab 1: Kelola Lapangan
        tabbedPane.addTab("Kelola Lapangan", createFieldManagementPanel());
        
        // Tab 2: Kelola User
        tabbedPane.addTab("Kelola User", createUserManagementPanel());
        
        // Tab 3: Lihat Reservasi
        tabbedPane.addTab("Lihat Reservasi", createReservationViewPanel());
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createFieldManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        String[] columns = {"ID", "Tipe", "Nama", "Harga/Jam", "Info Tambahan"};
        fieldTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fieldTable = new JTable(fieldTableModel);
        fieldTable.setRowHeight(25);
        fieldTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(fieldTable);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("[+] Tambah Lapangan");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addField());
        
        JButton editButton = new JButton("[Edit]");
        editButton.setBackground(new Color(255, 165, 0));
        editButton.setForeground(Color.WHITE);
        editButton.addActionListener(e -> editField());
        
        JButton deleteButton = new JButton("[X] Hapus");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteField());
        
        JButton refreshButton = new JButton("[Refresh]");
        refreshButton.addActionListener(e -> loadFieldData());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        String[] columns = {"ID", "Nama", "Nomor Telepon"};
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(userTableModel);
        userTable.setRowHeight(25);
        userTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(userTable);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("[+] Tambah User");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> addUser());
        
        JButton deleteButton = new JButton("[X] Hapus");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteUser());
        
        JButton refreshButton = new JButton("[Refresh]");
        refreshButton.addActionListener(e -> loadUserData());
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createReservationViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Table
        String[] columns = {"ID", "User", "Lapangan", "Tanggal", "Jam", "Durasi", "Status", "Total"};
        reservationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reservationTable = new JTable(reservationTableModel);
        reservationTable.setRowHeight(25);
        reservationTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton refreshButton = new JButton("[Refresh]");
        refreshButton.addActionListener(e -> loadReservationData());
        
        buttonPanel.add(refreshButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Field CRUD Methods
    private void addField() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah Lapangan", true);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JLabel typeLabel = new JLabel("Tipe:");
        String[] types = {"FUTSAL", "BADMINTON"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        
        JLabel nameLabel = new JLabel("Nama:");
        JTextField nameField = new JTextField();
        
        JLabel priceLabel = new JLabel("Harga/Jam:");
        JTextField priceField = new JTextField();
        
        JLabel extraLabel = new JLabel("Info Tambahan:");
        JTextField extraField = new JTextField();
        JLabel extraHintLabel = new JLabel("(Futsal: kapasitas, Badminton: indoor/outdoor)");
        extraHintLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        
        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String extra = extraField.getText();
                
                if (name.isEmpty() || extra.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String id = "FLD" + (fieldRepository.count() + 1);
                Field field = FieldFactory.createField(type, id, name, price, extra);
                fieldRepository.save(field);
                
                loadFieldData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Lapangan berhasil ditambahkan!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(typeLabel);
        dialog.add(typeCombo);
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(priceLabel);
        dialog.add(priceField);
        dialog.add(extraLabel);
        dialog.add(extraField);
        dialog.add(extraHintLabel);
        dialog.add(new JLabel()); // Spacer
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.setVisible(true);
    }
    
    private void editField() {
        int selectedRow = fieldTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih lapangan yang akan diedit!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) fieldTableModel.getValueAt(selectedRow, 0);
        Field field = fieldRepository.findById(id).orElse(null);
        
        if (field == null) {
            JOptionPane.showMessageDialog(this, "Lapangan tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String newPriceStr = JOptionPane.showInputDialog(this, "Masukkan harga baru:", field.getBasePricePerHour());
        if (newPriceStr != null && !newPriceStr.isEmpty()) {
            try {
                double newPrice = Double.parseDouble(newPriceStr);
                field.setBasePricePerHour(newPrice);
                fieldRepository.save(field);
                loadFieldData();
                JOptionPane.showMessageDialog(this, "Harga berhasil diupdate!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteField() {
        int selectedRow = fieldTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih lapangan yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) fieldTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus lapangan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            fieldRepository.deleteById(id);
            loadFieldData();
            JOptionPane.showMessageDialog(this, "Lapangan berhasil dihapus!");
        }
    }
    
    // User CRUD Methods
    private void addUser() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah User", true);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JLabel nameLabel = new JLabel("Nama:");
        JTextField nameField = new JTextField();
        
        JLabel phoneLabel = new JLabel("Nomor Telepon:");
        JTextField phoneField = new JTextField();
        
        JButton saveButton = new JButton("Simpan");
        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            
            if (name.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String id = "USR" + (userRepository.count() + 1);
            User user = new User(id, name, phone);
            userRepository.save(user);
            
            loadUserData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "User berhasil ditambahkan!");
        });
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(phoneLabel);
        dialog.add(phoneField);
        dialog.add(saveButton);
        dialog.add(cancelButton);
        
        dialog.setVisible(true);
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih user yang akan dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String id = (String) userTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus user ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            userRepository.deleteById(id);
            loadUserData();
            JOptionPane.showMessageDialog(this, "User berhasil dihapus!");
        }
    }
    
    // Load Data Methods
    private void loadFieldData() {
        fieldTableModel.setRowCount(0);
        for (Field field : fieldRepository.findAll()) {
            String type = field instanceof model.FutsalCourt ? "FUTSAL" : "BADMINTON";
            String extra = "";
            if (field instanceof model.FutsalCourt) {
                extra = "Kapasitas: " + ((model.FutsalCourt) field).getCapacity();
            } else if (field instanceof model.BadmintonCourt) {
                extra = ((model.BadmintonCourt) field).isIndoor() ? "Indoor" : "Outdoor";
            }
            
            fieldTableModel.addRow(new Object[]{
                field.getId(),
                type,
                field.getName(),
                String.format("Rp %.0f", field.getPricePerHour()),
                extra
            });
        }
    }
    
    private void loadUserData() {
        userTableModel.setRowCount(0);
        for (User user : userRepository.findAll()) {
            userTableModel.addRow(new Object[]{
                user.getId(),
                user.getName(),
                user.getPhone()
            });
        }
    }
    
    private void loadReservationData() {
        reservationTableModel.setRowCount(0);
        for (Reservation res : reservationRepository.findAll()) {
            User user = res.getUser();
            Field field = res.getField();
            
            reservationTableModel.addRow(new Object[]{
                res.getId(),
                user != null ? user.getName() : "-",
                field != null ? field.getName() : "-",
                res.getDate(),
                res.getStartTimeHour() + ":00",
                res.getDurationHours() + " jam",
                res.getStatus(),
                String.format("Rp %.0f", res.getPayment().getAmount())
            });
        }
    }
    
    private void loadSampleData() {
        // Sample Fields
        if (fieldRepository.count() == 0) {
            fieldRepository.save(FieldFactory.createField("FUTSAL", "FLD001", "Futsal A", 100000, "10"));
            fieldRepository.save(FieldFactory.createField("FUTSAL", "FLD002", "Futsal B", 120000, "12"));
            fieldRepository.save(FieldFactory.createField("BADMINTON", "FLD003", "Badminton 1", 50000, "true"));
            fieldRepository.save(FieldFactory.createField("BADMINTON", "FLD004", "Badminton 2", 45000, "false"));
        }
        
        // Sample Users
        if (userRepository.count() == 0) {
            userRepository.save(new User("USR001", "Joko Santoso", "08123456789"));
            userRepository.save(new User("USR002", "Siti Nurhaliza", "08198765432"));
            userRepository.save(new User("USR003", "Budi Doremi", "08111222333"));
        }
    }
    
    public void refreshData() {
        loadFieldData();
        loadUserData();
        loadReservationData();
    }
}
