package gui;

import javax.swing.*;
import java.awt.*;

/**
 * Main GUI Application untuk Sistem Reservasi GOR
 * Menggunakan Java Swing
 */
public class GorAppGUI extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Screens
    private WelcomeScreen welcomeScreen;
    private AdminDashboard adminDashboard;
    private CustomerDashboard customerDashboard;
    
    public GorAppGUI() {
        setTitle("Sistem Reservasi GOR - GUI");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Setup CardLayout for screen switching
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Initialize screens
        welcomeScreen = new WelcomeScreen(this);
        adminDashboard = new AdminDashboard(this);
        customerDashboard = new CustomerDashboard(this);
        
        // Add screens to CardLayout
        mainPanel.add(welcomeScreen, "WELCOME");
        mainPanel.add(adminDashboard, "ADMIN");
        mainPanel.add(customerDashboard, "CUSTOMER");
        
        add(mainPanel);
        
        // Show welcome screen
        showWelcomeScreen();
    }
    
    public void showWelcomeScreen() {
        cardLayout.show(mainPanel, "WELCOME");
    }
    
    public void showAdminDashboard() {
        adminDashboard.refreshData();
        cardLayout.show(mainPanel, "ADMIN");
    }
    
    public void showCustomerDashboard() {
        customerDashboard.refreshData();
        cardLayout.show(mainPanel, "CUSTOMER");
    }
    
    public static void main(String[] args) {
        // Set Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            GorAppGUI gui = new GorAppGUI();
            gui.setVisible(true);
        });
    }
}
