package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Welcome Screen - Pilih Login sebagai Admin atau Customer
 */
public class WelcomeScreen extends JPanel {
    
    private GorAppGUI mainApp;
    
    public WelcomeScreen(GorAppGUI mainApp) {
        this.mainApp = mainApp;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Alice Blue
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        headerPanel.setPreferredSize(new Dimension(0, 200)); // Diperbesar dari 150 ke 200
        headerPanel.setLayout(new GridBagLayout()); // Ganti ke GridBagLayout untuk center vertical
        
        JLabel titleLabel = new JLabel("SISTEM RESERVASI GOR");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Center Panel with buttons
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Info Label
        JLabel infoLabel = new JLabel("Silakan pilih mode login:");
        infoLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        centerPanel.add(infoLabel, gbc);
        
        // Admin Button
        gbc.gridy = 1;
        JButton adminButton = new JButton("Login sebagai ADMIN");
        adminButton.setFont(new Font("Poppins", Font.BOLD, 22)); // Lebih besar dari 20
        adminButton.setPreferredSize(new Dimension(380, 85)); // Lebih besar
        adminButton.setBackground(new Color(46, 125, 50)); // Material Design Green (lebih gelap)
        adminButton.setForeground(Color.BLACK);
        adminButton.setFocusPainted(false);
        adminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminButton.setBorder(BorderFactory.createLineBorder(new Color(27, 94, 32), 2)); // Border hijau gelap
        adminButton.addActionListener(e -> mainApp.showAdminDashboard());
        centerPanel.add(adminButton, gbc);
        
        // Customer Button
        gbc.gridy = 2;
        JButton customerButton = new JButton("Login sebagai CUSTOMER");
        customerButton.setFont(new Font("Poppins", Font.BOLD, 22)); // Lebih besar dari 20
        customerButton.setPreferredSize(new Dimension(380, 85)); // Lebih besar
        customerButton.setBackground(new Color(25, 118, 210)); // Material Design Blue (lebih gelap)
        customerButton.setForeground(Color.BLACK);
        customerButton.setFocusPainted(false);
        customerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerButton.setBorder(BorderFactory.createLineBorder(new Color(13, 71, 161), 2)); // Border biru gelap
        customerButton.addActionListener(e -> mainApp.showCustomerDashboard());
        centerPanel.add(customerButton, gbc);
        
        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 248, 255));
        footerPanel.setPreferredSize(new Dimension(0, 100)); // Lebih besar dari 80
        footerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 30)); // Margin top 30px
        
        JLabel footerLabel = new JLabel("© 2025 Sistem Reservasi GOR - Made with love");
        footerLabel.setFont(new Font("Poppins", Font.ITALIC, 13)); // Sedikit lebih besar
        footerLabel.setForeground(new Color(100, 100, 100)); // Abu-abu lebih gelap
        footerPanel.add(footerLabel);
        
        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
}
