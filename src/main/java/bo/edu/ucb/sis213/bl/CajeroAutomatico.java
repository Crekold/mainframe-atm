package bo.edu.ucb.sis213.bl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bo.edu.ucb.sis213.pl.ConexionBD;
import bo.edu.ucb.sis213.pl.Operaciones;
public class CajeroAutomatico {
    private static int usuarioId;
    private static String alias;
    private static Connection connection;
    
    public boolean validarUsuarioYPIN(String usuarioIngresado, int pinIngresado) {
        String query = "SELECT id, alias FROM usuarios WHERE alias = ? AND pin = ?";
        try {
            connection = ConexionBD.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usuarioIngresado);
            preparedStatement.setInt(2, pinIngresado);
            ResultSet resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
                alias = resultSet.getString("alias");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

    public String cambiarPIN(int nuevoPin) {
                return Operaciones.cambiarPIN(connection, usuarioId, nuevoPin);
            }
    public static String getAlias(){
    return alias;
}
}
       