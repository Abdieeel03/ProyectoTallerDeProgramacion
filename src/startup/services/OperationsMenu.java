package startup.services;

import java.util.Scanner;

/**
 * @author Abdieeel
 * @author Cielo
 * @author Sergio
 * @author Milagros
 * @author Benjamin
 */
public class OperationsMenu extends MainMenu {

    // Instancia del objeto FileIO para manejo de archivos
    FileIO userfile = new FileIO();

    // Constante para el menú de operaciones
    private final String OPERATIONMENU = """
            Bienvenido %s
            Seleccione la operación que desea realizar:
                1) Revisar estado de cuenta
                2) Depósito
                3) Retiro
                4) Transferencia
                5) Cerrar sesión
            Ingrese opción [1-5]:
            """;

    // Constante para el reporte de estado de cuenta
    private final String REPORTACCOUNT = """
            ###########################################
            # ESTADO DE CUENTA                        #
            ###########################################
            # USUARIO : %-30s#
            # SALDO RESTANTE : S/ %-20s#
            ###########################################
            """;

    @SuppressWarnings("unused")
    private static String amount; // Variable estática para el monto
    @SuppressWarnings("unused")
    private static String destinationAccount; // Variable estática para la cuenta de destino

    // Constructor que recibe usuario, número de cuenta y saldo
    public OperationsMenu(String user, String accountnumber, String balance) {
        super(user, accountnumber, balance);
    }

    // Constructor por defecto
    public OperationsMenu() {
    }

    // Método para obtener el menú de operaciones
    public String getOPERATIONMENU() {
        return OPERATIONMENU;
    }

    // Método para obtener el reporte de estado de cuenta
    public String getREPORTACCOUNT() {
        return REPORTACCOUNT;
    }

    // Método para generar el estado de cuenta formateado
    public String stateAccount() {
        return String.format(getREPORTACCOUNT(), getUser(), getBalance());
    }

    // Sobrescribir el método isEmptyBalance para verificar si el balance está vacío
    @Override
    public boolean isEmptyBalance(String amount) {
        isEmpty = false;
        if (amount.isEmpty()) {
            System.err.println("Operación cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    // Sobrescribir el método verifyBalance para verificar y validar el balance
    @Override
    public void verifyBalance(Scanner sc, String amount) {
        MainMenu.isEmpty = false;
        if (isEmptyBalance(amount)) {
            MainMenu.isEmpty = true;
            return;
        }
        while (true) {
            try {
                @SuppressWarnings("unused")
                Double canBeDouble = Double.valueOf(amount);
                OperationsMenu.amount = amount;
                return;
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese el monto: ");
                amount = sc.nextLine();
                if (isEmptyBalance(amount)) {
                    MainMenu.isEmpty = true;
                    break;
                }
            }
        }
    }

    // Sobrescribir el método isEmptyAccount para verificar si el número de cuenta
    // está vacío
    @Override
    public boolean isEmptyAccount(String accountnumber) {
        isEmpty = false;
        if (accountnumber.isEmpty()) {
            System.err.println("Operación cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    // Sobrescribir el método verifyAccount para verificar y validar el número de
    // cuenta
    @Override
    public void verifyAccount(Scanner sc, String accountnumber) {
        MainMenu.isEmpty = false;
        if (isEmptyAccount(accountnumber)) {
            MainMenu.isEmpty = true;
            return;
        }
        while (true) {
            try {
                @SuppressWarnings("unused")
                Integer canBeInteger = Integer.valueOf(accountnumber);
                if (accountnumber.length() != 8) {
                    System.err.println("El número de cuenta debe tener exactamente 8 dígitos.");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese el número de cuenta destino: ");
                    accountnumber = sc.nextLine();
                    if (isEmptyAccount(accountnumber)) {
                        MainMenu.isEmpty = true;
                        return;
                    }
                    continue;
                } else if (accountnumber.equals(MainMenu.getAccountnumber())) {
                    System.err.println("No puede ingresar su mismo número de cuenta.");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese el número de cuenta destino: ");
                    accountnumber = sc.nextLine();
                    if (isEmptyAccount(accountnumber)) {
                        MainMenu.isEmpty = true;
                        return;
                    }
                    continue;
                }
                OperationsMenu.destinationAccount = accountnumber;
                return;
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese el número de cuenta destino: ");
                accountnumber = sc.nextLine();
                if (isEmptyAccount(accountnumber)) {
                    MainMenu.isEmpty = true;
                    return;
                }
            }
        }
    }

}
