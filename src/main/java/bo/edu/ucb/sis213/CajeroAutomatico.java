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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        try {
            connection = ConexionBD.getConnection();
        } catch (SQLException ex) {
            System.err.println("No se puede conectar a la Base de Datos");
            ex.printStackTrace();
            System.exit(1);
        }

        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (validarPIN(pinIngresado)) {
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

   public static boolean validarPIN(int pinIngresado) {
    String query = "SELECT id, saldo FROM usuarios WHERE pin = ?";
    try {
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
        double saldoActual = Operaciones.consultarSaldo(connection, usuarioId);
        System.out.println("Su saldo actual es: $" + saldoActual);
    }

    public static void realizarDeposito() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a depositar: $");
        double cantidad = scanner.nextDouble();
        Operaciones.realizarDeposito(connection, usuarioId, cantidad, saldo);
    }

    public static void realizarRetiro() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la cantidad a retirar: $");
        double cantidad = scanner.nextDouble();
        Operaciones.realizarRetiro(connection, usuarioId, cantidad);
    }

    public static void cambiarPIN() {
        // Código para cambiar el PIN similar a tu implementación actual
    }
}
