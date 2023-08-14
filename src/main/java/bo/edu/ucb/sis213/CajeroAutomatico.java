package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class CajeroAutomatico {
    private static int usuarioId;
    private static double saldo;
    private static Connection connection;
    
    
   public boolean validarPIN(int pinIngresado) {
    String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
    try {
        connection = ConexionBD.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, pinIngresado);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            usuarioId = resultSet.getInt("id");
            saldo = resultSet.getDouble("saldo");
            return true;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public static double getSaldo() {
    return saldo;
}
    public Double consultarSaldo() {
        double saldoActual = Operaciones.consultarSaldo(connection, usuarioId);
        return(saldoActual);
    }

    public String realizarDeposito(double cantidad) {
        return Operaciones.realizarDeposito(connection, usuarioId, cantidad);  
    }

    public String realizarRetiro(double cantidad) {
        return Operaciones.realizarRetiro(connection, usuarioId, cantidad);
    }

    public void cambiarPIN(int nuevoPin) {
    
       
   
            
                String actualizarPINQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(actualizarPINQuery)) {
                    preparedStatement.setInt(1, nuevoPin);
                    preparedStatement.setInt(2, usuarioId);
                    preparedStatement.executeUpdate();
                    System.out.println("PIN actualizado con éxito.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } }
       