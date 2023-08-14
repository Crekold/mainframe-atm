package bo.edu.ucb.sis213;
import java.awt.GridLayout;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CajeroAutomaticoGUI {
    private JFrame loginFrame;
    private JFrame menuPrincipalFrame;
    private CajeroAutomatico cajero;
    public CajeroAutomaticoGUI() {
        // Inicialización de los frames y otros componentes
        loginFrame = new JFrame("Inicio de Sesión");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);
        loginFrame.setLocationRelativeTo(null); // Centrar en la pantalla
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel pinLabel = new JLabel("PIN:");
        JPasswordField pinField = new JPasswordField();
        JButton loginButton = new JButton("Iniciar Sesión");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pin = Integer.parseInt(new String(pinField.getPassword()));
                CajeroAutomatico cajero = new CajeroAutomatico();
                if (cajero.validarPIN(pin)) {
                    mostrarMenuPrincipalFrame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "PIN incorrecto. Intente nuevamente.");
                }
            }
        });

        loginPanel.add(pinLabel);
        loginPanel.add(pinField);
        loginPanel.add(new JLabel()); // Espacio en blanco
        loginPanel.add(new JLabel()); // Espacio en blanco
        loginPanel.add(new JLabel()); // Espacio en blanco
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        menuPrincipalFrame = new JFrame("Menú Principal");
        menuPrincipalFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuPrincipalFrame.setSize(600, 400);
        menuPrincipalFrame.setLocationRelativeTo(null); // Centrar en la pantalla

        cajero = new CajeroAutomatico(); // Instancia del CajeroAutomatico (ajusta según tu código)

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

