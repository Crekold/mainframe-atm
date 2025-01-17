package bo.edu.ucb.sis213.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import bo.edu.ucb.sis213.bl.CajeroAutomatico;
import bo.edu.ucb.sis213.pl.Transaccion;
 // Asegúrate de importar ArrayList
import java.util.List; 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipalPanel extends JPanel {
    private CajeroAutomatico cajero;
    private Color backgroundColor = new Color(173, 216, 230);
    private Image backgroundImage; 
    

    public MenuPrincipalPanel(CajeroAutomatico cajero) {
        this.cajero = cajero;
        setLayout(new GridBagLayout()); // Usamos GridBagLayout
        setBackground(backgroundColor); // Aplicamos el color de fondo
         // Cargar la imagen de fondo
        backgroundImage = new ImageIcon("src\\images\\background.jpg").getImage();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        
        // Crear el label de bienvenida
        JLabel bienvenidaLabel = new JLabel("Bienvenido");
        bienvenidaLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bienvenidaLabel.setForeground(Color.WHITE); // Color del texto
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(bienvenidaLabel, gbc);
        


        // Botones
        JButton consultarSaldoButton = new JButton("Consultar Saldo");
        JButton realizarDepositoButton = new JButton("Realizar Depósito");
        JButton realizarRetiroButton = new JButton("Realizar Retiro");
        JButton consultarHistorialButton = new JButton("Consultar Historial");
        JButton cambiarPINButton = new JButton("Cambiar PIN");
        JButton salirButton = new JButton("Salir");
        
        // ActionListener para cada botón (sigue el mismo patrón)
        
        consultarSaldoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double saldoActual = cajero.consultarSaldo();
                JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "Saldo actual: $" + saldoActual);
            }
        });

        realizarDepositoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double cantidad = 0.0;
                boolean cantidadValida = false;
        
                do {
                    String cantidadStr = JOptionPane.showInputDialog(MenuPrincipalPanel.this, "Ingrese la cantidad a depositar: $");
                    if (cantidadStr != null) {
                        try {
                            cantidad = Double.parseDouble(cantidadStr);
                            if (cantidad > 0) {
                                String mensaje = cajero.realizarDeposito(cantidad);
                                cantidadValida = true;
                                JOptionPane.showMessageDialog(MenuPrincipalPanel.this, mensaje);
                            } else {
                                JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Si se cancela el cuadro de diálogo, salir del bucle
                        break;
                    }
                } while (!cantidadValida);
            }
        });
        

        realizarRetiroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double cantidad = 0.0;
                boolean cantidadValida = false;
        
                do {
                    String cantidadStr = JOptionPane.showInputDialog(MenuPrincipalPanel.this, "Ingrese la cantidad a retirar: $");
                    if (cantidadStr != null) {
                        try {
                            cantidad = Double.parseDouble(cantidadStr);
                            double saldoActual = cajero.consultarSaldo();
    
                            if (cantidad > 0) {
                                
                                if (cantidad<=saldoActual) {
                                    cantidadValida = true;
                                    String Mensaje= cajero.realizarRetiro(cantidad);
                                    JOptionPane.showMessageDialog(MenuPrincipalPanel.this, Mensaje);}
                                 else {
                                    JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "Saldo insuficiente.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "La cantidad debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        // Si se cancela el cuadro de diálogo, salir del bucle
                        break;
                    }
                } while (!cantidadValida);
            }
        });
        
consultarHistorialButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Transaccion> transacciones = cajero.obtenerHistorialTransacciones();
        
        Object[][] data = new Object[transacciones.size()][3];
        for (int i = 0; i < transacciones.size(); i++) {
            Transaccion transaccion = transacciones.get(i);
            data[i] = new Object[]{transaccion.getFecha(), transaccion.getTipoOperacion(), transaccion.getCantidad()};
        }

        String[] columnNames = {"Fecha", "Tipo de Operación", "Cantidad"};
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JOptionPane.showMessageDialog(MenuPrincipalPanel.this, scrollPane, "Historial de Transacciones", JOptionPane.PLAIN_MESSAGE);
    }
});


        cambiarPINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String pinIngresadoStr = JOptionPane.showInputDialog(MenuPrincipalPanel.this, "Ingrese su PIN actual:");
                if (pinIngresadoStr == null) {
                    return;  // Si se cancela el cuadro de diálogo, salir de la acción
                }
        
                if (pinIngresadoStr.isEmpty()) {
                    JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "El campo PIN no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;  // No procesar la acción si el campo está vacío
                }
        
                int pinIngresado = Integer.parseInt(pinIngresadoStr);
                String alias = CajeroAutomatico.getAlias();
                if (cajero.validarUsuarioYPIN(alias, pinIngresado)) {
                    String nuevoPinStr = JOptionPane.showInputDialog(MenuPrincipalPanel.this, "Ingrese su nuevo PIN:");
                    if (nuevoPinStr == null) {
                        return;  // Si se cancela el cuadro de diálogo, salir de la acción
                    }
        
                    String confirmacionPinStr = JOptionPane.showInputDialog(MenuPrincipalPanel.this, "Confirme su nuevo PIN:");
                    if (confirmacionPinStr == null) {
                        return;  // Si se cancela el cuadro de diálogo, salir de la acción
                    }
        
                    if (nuevoPinStr.isEmpty() || confirmacionPinStr.isEmpty()) {
                        JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "Los campos no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;  // No procesar la acción si algún campo está vacío
                    }
        
                    int nuevoPin = Integer.parseInt(nuevoPinStr);
                    int confirmacionPin = Integer.parseInt(confirmacionPinStr);
        
                    if (nuevoPin == confirmacionPin) {
                        cajero.cambiarPIN(nuevoPin); // Llamar al método de cambiarPIN en la instancia de CajeroAutomatico
                        JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "PIN actualizado con éxito.");
                    } else {
                        JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "Los PINs no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(MenuPrincipalPanel.this, "PIN incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        
        salirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }); 

        UIManager.put("OptionPane.background", new Color(173, 216, 230)); // Cambia por el color que desees
        UIManager.put("Panel.background", new Color(173, 216, 230)); // Cambia por el color que desees

        // Restablecer la apariencia original al cerrar el programa
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            UIManager.put("OptionPane.background", UIManager.get("Panel.background"));
            UIManager.put("Panel.background", UIManager.get("Panel.background"));
        }));
        // Estilo de los botones
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Color buttonColor = new Color(70, 130, 180); // Azul acero
        Color buttonTextColor = Color.WHITE;


        JButton[] buttons = {
            consultarSaldoButton,
            realizarDepositoButton,
            realizarRetiroButton,
            consultarHistorialButton,
            cambiarPINButton,
            salirButton
        };

        for (JButton button : buttons) {
            button.setFont(buttonFont);
            button.setBackground(buttonColor);
            button.setForeground(buttonTextColor);
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            add(button, gbc);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Pintar la imagen de fondo
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
