package bo.edu.ucb.sis213;

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

    public static void realizarDeposito(Connection connection, int usuarioId, double cantidad,double saldo) {
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
            return;
        }

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

            System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + (saldo + cantidad));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void realizarRetiro(Connection connection, int usuarioId, double cantidad) {
        if (cantidad <= 0) {
            System.out.println("Cantidad no válida.");
            return;
        }

        double saldo = consultarSaldo(connection, usuarioId);

        if (cantidad > saldo) {
            System.out.println("Saldo insuficiente.");
            return;
        }

        String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
        try (PreparedStatement retiroStatement = connection.prepareStatement(actualizarSaldoQuery)) {
            retiroStatement.setDouble(1, cantidad);
            retiroStatement.setInt(2, usuarioId);
            retiroStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
