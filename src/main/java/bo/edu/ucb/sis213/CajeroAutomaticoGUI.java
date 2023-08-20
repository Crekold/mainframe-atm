package bo.edu.ucb.sis213;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CajeroAutomaticoGUI {
    private JFrame loginFrame;
    private JFrame menuPrincipalFrame;
    private CajeroAutomatico cajero;
    
    public CajeroAutomaticoGUI() {
        cajero = new CajeroAutomatico(); 
        // Inicialización de los frames y otros componentes
        loginFrame = new JFrame("Inicio de Sesión");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null); // Centrar en la pantalla

        JPanel loginPanel = new JPanel(new GridBagLayout()); // Usamos GridBagLayout
        loginPanel.setBackground(new Color(189, 215, 238)); // Color celeste suave

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Agregar el logo arriba del label de PIN
        ImageIcon logoIcon = new ImageIcon("src\\images\\logo.png"); // Cambia por la ruta de tu imagen
        Image scaledLogo = logoIcon.getImage().getScaledInstance(300, 75, Image.SCALE_SMOOTH);
        ImageIcon scaledLogoIcon = new ImageIcon(scaledLogo);
        JLabel logoLabel = new JLabel();
        logoLabel.setIcon(scaledLogoIcon);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(logoLabel, gbc);

        JLabel userLabel = new JLabel("Usuario:");
gbc.gridx = 0;
gbc.gridy = 1;
gbc.gridwidth = 1;
loginPanel.add(userLabel, gbc);

JTextField userField = new JTextField();
gbc.gridx = 1;
gbc.gridy = 1;
loginPanel.add(userField, gbc);

JLabel pinLabel = new JLabel("PIN:");
gbc.gridx = 0;
gbc.gridy = 2;
gbc.gridwidth = 1;
loginPanel.add(pinLabel, gbc);

JPasswordField pinField = new JPasswordField();
gbc.gridx = 1;
gbc.gridy = 2;
loginPanel.add(pinField, gbc);;

JButton loginButton = new JButton("Iniciar Sesión");
gbc.gridx = 0;
gbc.gridy = 3;
gbc.gridwidth = 2;
loginPanel.add(loginButton, gbc);

        loginButton.setBackground(new Color(100, 149, 237)); // Color de fondo del botón
        loginButton.setForeground(Color.white); // Color de texto del botón
        loginButton.setFocusPainted(false); // Eliminar resaltado de enfoque
        loginButton.setBorder(BorderFactory.createEmptyBorder()); // Sin borde

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                int pin = Integer.parseInt(new String(pinField.getPassword()));
                
                if (cajero.validarUsuarioYPIN(username, pin)) {

                    mostrarMenuPrincipalFrame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Usuario o PIN incorrecto. Intente nuevamente.");
                }
            }
        });
        
        loginFrame.add(loginPanel);
        menuPrincipalFrame = new JFrame("Menú Principal");
        menuPrincipalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuPrincipalFrame.setSize(600, 400);
        menuPrincipalFrame.setLocationRelativeTo(null); // Centrar en la pantalla

        // Instancia del CajeroAutomatico (ajusta según tu código)
    
        MenuPrincipalPanel menuPrincipalPanel = new MenuPrincipalPanel(cajero); // Agrega el panel del menú principal

        menuPrincipalFrame.add(menuPrincipalPanel);
        // Mostrar la vista de inicio de sesión por defecto
        mostrarLoginFrame();
    }

    public void mostrarLoginFrame() {
        loginFrame.setVisible(true);
        menuPrincipalFrame.setVisible(false);
    }

    public void mostrarMenuPrincipalFrame() {
        loginFrame.setVisible(false);
        menuPrincipalFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CajeroAutomaticoGUI();
            }
        });
    }
}
