package bo.edu.ucb.sis213;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    private static int usuarioId;
    private static double saldo;

    private static final String HOST = "localhost";
    private static final int PORT = 3306;
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String DATABASE = "ATM";

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE);
        try {
            // Asegúrate de tener el driver de MySQL agregado en tu proyecto
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("MySQL Driver not found.", e);
        }

        return DriverManager.getConnection(jdbcUrl, USER, PASSWORD);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        Connection connection = null;
        try {
            connection = getConnection(); // Reemplaza esto con tu conexión real
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }
        

        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (validarPIN(connection, pinIngresado)) {
                mostrarMenu();
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
    }

    public static boolean validarPIN(Connection connection, int pin) {
        String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, pin);
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

    public static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN."); // ya esta hecho
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    consultarSaldo();
                    break;
                case 2:
                    realizarDeposito();
                    break;
                case 3:
                    realizarRetiro();
                    break;
                case 4:
                    cambiarPIN();
                    break;
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

   public static void consultarSaldo() {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
        // Establecer conexión a la base de datos
        connection = getConnection();

        // Consultar el saldo del usuario en la base de datos
        String consultarSaldoQuery = "SELECT saldo FROM usuarios WHERE id = ?";
        preparedStatement = connection.prepareStatement(consultarSaldoQuery);
        preparedStatement.setInt(1, usuarioId); // Reemplaza idUsuario con el identificador adecuado del usuario
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            double saldo = resultSet.getDouble("saldo");
            System.out.println("Su saldo actual es: $" + saldo);
        } else {
            System.out.println("Usuario no encontrado.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Cerrar recursos
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    public static void realizarDeposito() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese la cantidad a depositar: $");
    double cantidad = scanner.nextDouble();

    if (cantidad <= 0) {
        System.out.println("Cantidad no válida.");
        return;
    }

    Connection connection = null;
    PreparedStatement depositoStatement = null;
    PreparedStatement historicoStatement = null;

    try {
        // Establecer conexión a la base de datos
        connection = getConnection();

        // Iniciar una transacción
        connection.setAutoCommit(false);

        // Actualizar el saldo en la base de datos
        String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo + ? WHERE id = ?";
        depositoStatement = connection.prepareStatement(actualizarSaldoQuery);
        depositoStatement.setDouble(1, cantidad);
        depositoStatement.setInt(2, usuarioId); // Reemplaza idUsuario con el identificador adecuado del usuario
        depositoStatement.executeUpdate();

        // Registrar el depósito en el historial
        String registrarDepositoQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        historicoStatement = connection.prepareStatement(registrarDepositoQuery);
        historicoStatement.setInt(1, usuarioId);
        historicoStatement.setString(2, "Deposito");
        historicoStatement.setDouble(3, cantidad);
        historicoStatement.executeUpdate();

        // Confirmar la transacción
        connection.commit();

        System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + (saldo + cantidad));
    } catch (SQLException e) {
        // Si hay un error, deshacer la transacción
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
        e.printStackTrace();
    } finally {
        // Cerrar recursos y restablecer la autoconfirmación
        try {
            if (depositoStatement != null) depositoStatement.close();
            if (historicoStatement != null) historicoStatement.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
public static void realizarRetiro() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese la cantidad a retirar: $");
    double cantidad = scanner.nextDouble();

    if (cantidad <= 0) {
        System.out.println("Cantidad no válida.");
        return;
    }

    if (cantidad > saldo) {
        System.out.println("Saldo insuficiente.");
        return;
    }

    Connection connection = null;
    PreparedStatement retiroStatement = null;
    PreparedStatement historicoStatement = null;

    try {
        // Establecer conexión a la base de datos
        connection = getConnection();

        // Iniciar una transacción
        connection.setAutoCommit(false);

        // Actualizar el saldo en la base de datos
        String actualizarSaldoQuery = "UPDATE usuarios SET saldo = saldo - ? WHERE id = ?";
        retiroStatement = connection.prepareStatement(actualizarSaldoQuery);
        retiroStatement.setDouble(1, cantidad);
        retiroStatement.setInt(2, usuarioId); // Reemplaza idUsuario con el identificador adecuado del usuario
        retiroStatement.executeUpdate();

        // Registrar el retiro en el historial
        String registrarRetiroQuery = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad) VALUES (?, ?, ?)";
        historicoStatement = connection.prepareStatement(registrarRetiroQuery);
        historicoStatement.setInt(1, usuarioId);
        historicoStatement.setString(2, "Retiro");
        historicoStatement.setDouble(3, cantidad);
        historicoStatement.executeUpdate();

        // Confirmar la transacción
        connection.commit();

        saldo -= cantidad; // Actualizar saldo local
        System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
    } catch (SQLException e) {
        // Si hay un error, deshacer la transacción
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
        }
        e.printStackTrace();
    } finally {
        // Cerrar recursos y restablecer la autoconfirmación
        try {
            if (retiroStatement != null) retiroStatement.close();
            if (historicoStatement != null) historicoStatement.close();
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    public static void cambiarPIN() {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Ingrese su PIN actual: ");
    int pinIngresado = scanner.nextInt();

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    try {
        // Establecer conexión a la base de datos
        connection = getConnection();
        // Verificar si el PIN actual es correcto
        String verificarPINQuery = "SELECT * FROM usuarios WHERE pin = ?";
        preparedStatement = connection.prepareStatement(verificarPINQuery);
        preparedStatement.setInt(1, pinIngresado);
        resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            System.out.print("Ingrese su nuevo PIN: ");
            int nuevoPin = scanner.nextInt();
            System.out.print("Confirme su nuevo PIN: ");
            int confirmacionPin = scanner.nextInt();

            if (nuevoPin == confirmacionPin) {
                // Actualizar el PIN en la base de datos
                String actualizarPINQuery = "UPDATE usuarios SET pin = ? WHERE pin = ?";
                preparedStatement = connection.prepareStatement(actualizarPINQuery);
                preparedStatement.setInt(1, nuevoPin);
                preparedStatement.setInt(2, pinIngresado);
                preparedStatement.executeUpdate();
                System.out.println("PIN actualizado con éxito.");
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Cerrar recursos
        try {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
}
