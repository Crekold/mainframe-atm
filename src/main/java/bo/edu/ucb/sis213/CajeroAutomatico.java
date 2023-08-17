package bo.edu.ucb.sis213;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class CajeroAutomatico {
    private static int usuarioId;
    private static double saldo;
    private static String alias;
    private static Connection connection;
    
    public boolean validarUsuarioYPIN(String usuarioIngresado, int pinIngresado) {
        String query = "SELECT id, saldo, nombre, alias FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            connection = ConexionBD.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usuarioIngresado);
            preparedStatement.setInt(2, pinIngresado);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
                saldo = resultSet.getDouble("saldo");
                
                alias = resultSet.getString("alias");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getNombre(){
        String nombreQuery = "SELECT nombre FROM usuarios WHERE id = ?";
        String nombre="";
        try {
            connection = ConexionBD.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(nombreQuery);
            preparedStatement.setInt(1, usuarioId);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                nombre = resultSet.getString("nombre");
                return nombre;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ola";
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
            }
    public static String getAlias(){
    return alias;
}

        }
       