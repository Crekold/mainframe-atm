package bo.edu.ucb.sis213.pl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Operaciones {
    public static double consultarSaldo(Connection connection, int usuarioId) {
        double saldo = 0;

        String consultarSaldoQuery = "SELECT saldo FROM usuarios WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(consultarSaldoQuery)) {
            preparedStatement.setInt(1, usuarioId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    saldo = resultSet.getDouble("saldo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return saldo;
    }
    

    public static String realizarDeposito(Connection connection, int usuarioId, double cantidad) {
        double saldo = Operaciones.consultarSaldo(connection, usuarioId);

        String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
        String registrarDepositoQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";

        try (PreparedStatement depositoStatement = connection.prepareStatement(actualizarSaldoQuery);
             PreparedStatement historicoStatement = connection.prepareStatement(registrarDepositoQuery)) {

            connection.setAutoCommit(false);

            depositoStatement.setDouble(1, cantidad);
            depositoStatement.setInt(2, usuarioId);
            depositoStatement.executeUpdate();

            historicoStatement.setInt(1, usuarioId);
            historicoStatement.setString(2, "Deposito");
            historicoStatement.setDouble(3, cantidad);
            historicoStatement.executeUpdate();

            connection.commit();

            return("Depósito realizado con éxito. Su nuevo saldo es: $" + (saldo + cantidad));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "hola";
    }

    public static String realizarRetiro(Connection connection, int usuarioId, double cantidad) {
        
        double saldo = Operaciones.consultarSaldo(connection, usuarioId);
    
        String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
        String registrarRetiroQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
    
        try (PreparedStatement retiroStatement = connection.prepareStatement(actualizarSaldoQuery);
             PreparedStatement historicoStatement = connection.prepareStatement(registrarRetiroQuery)) {
    
            connection.setAutoCommit(false);
    
            retiroStatement.setDouble(1, cantidad);
            retiroStatement.setInt(2, usuarioId);
            retiroStatement.executeUpdate();
    
            historicoStatement.setInt(1, usuarioId);
            historicoStatement.setString(2, "Retiro");
            historicoStatement.setDouble(3, cantidad);
            historicoStatement.executeUpdate();
    
            connection.commit();
    
            saldo -= cantidad;
            return("Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "hola";
    }
    public static String cambiarPIN(Connection connection, int usuarioId, int nuevoPin) {
        String actualizarPINQuery = "UPDATE usuarios SET pin = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(actualizarPINQuery)) {
            preparedStatement.setInt(1, nuevoPin);
            preparedStatement.setInt(2, usuarioId);
            preparedStatement.executeUpdate();
            return("PIN actualizado con éxito.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "error";
    }
    
    
}
