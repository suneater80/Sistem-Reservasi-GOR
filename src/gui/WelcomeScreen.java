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
        headerPanel.setPreferredSize(new Dimension(0, 150));
        
        JLabel titleLabel = new JLabel("SISTEM RESERVASI GOR");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
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
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        centerPanel.add(infoLabel, gbc);
        
        // Admin Button
        gbc.gridy = 1;
        JButton adminButton = new JButton("Login sebagai ADMIN");
        adminButton.setFont(new Font("Arial", Font.BOLD, 20));
        adminButton.setPreferredSize(new Dimension(350, 80));
        adminButton.setBackground(new Color(34, 139, 34)); // Forest Green
        adminButton.setForeground(Color.WHITE);
        adminButton.setFocusPainted(false);
        adminButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        adminButton.addActionListener(e -> mainApp.showAdminDashboard());
        centerPanel.add(adminButton, gbc);
        
        // Customer Button
        gbc.gridy = 2;
        JButton customerButton = new JButton("Login sebagai CUSTOMER");
        customerButton.setFont(new Font("Arial", Font.BOLD, 20));
        customerButton.setPreferredSize(new Dimension(350, 80));
        customerButton.setBackground(new Color(30, 144, 255)); // Dodger Blue
        customerButton.setForeground(Color.WHITE);
        customerButton.setFocusPainted(false);
        customerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        customerButton.addActionListener(e -> mainApp.showCustomerDashboard());
        centerPanel.add(customerButton, gbc);
        
        // Footer Panel
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(240, 248, 255));
        footerPanel.setPreferredSize(new Dimension(0, 80));
        
        JLabel footerLabel = new JLabel("© 2025 Sistem Reservasi GOR - Tugas Besar OOP");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        footerPanel.add(footerLabel);
        
        // Add all panels
        add(headerPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
}
