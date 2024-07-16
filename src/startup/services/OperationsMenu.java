package startup.services;

import java.util.Scanner;

/**
 * @author Abdieeel
 * @author Cielo
 * @author Sergio
 * @author Milagros
 */
public class OperationsMenu extends MainMenu {
    private static Scanner sc = new Scanner(System.in);
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
            # NUMERO DE CUENTA : %-21s#
            # SALDO DISPONIBLE : S/ %-18s#
            ###########################################
            """;

    @SuppressWarnings("unused")
    private static String amount; // Variable estática para el monto
    @SuppressWarnings("unused")
    private static String destinationAccount; // Variable estática para la cuenta de destino
    private static String destinationUser; // Variable estática para el nombre de usuario destino
    private static String newBalance; // Variable estática para almacenar el nuevo saldo
    private static boolean confirmedOp; // Variable estática para el valor de confirmado

    // Formato de texto para la confirmación
    private static final String CONFIRM_TEXT = """
            El número de cuenta destino pertenece a %s.
            ¿Desea continuar con la operación? [y/n]:
            """;

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

    public static boolean isConfirmedOp() {
        return confirmedOp;
    }

    public static void setConfirmedOp(boolean confirmedOp) {
        OperationsMenu.confirmedOp = confirmedOp;
    }

    public static String getAmount() {
        return amount;
    }

    public static void setAmount(String amount) {
        OperationsMenu.amount = amount;
    }

    public static String getDestinationAccount() {
        return destinationAccount;
    }

    public static void setDestinationAccount(String destinationAccount) {
        OperationsMenu.destinationAccount = destinationAccount;
    }

    public static String getDestinationUser() {
        return destinationUser;
    }

    public static void setDestinationUser(String destinationUser) {
        OperationsMenu.destinationUser = destinationUser;
    }

    public static String getNewBalance() {
        return newBalance;
    }

    public static void setNewBalance(String newBalance) {
        OperationsMenu.newBalance = newBalance;
    }

    // Método para generar el estado de cuenta formateado
    public String stateAccount() {
        if (getNewBalance() == null) {
            setNewBalance(getBalance());
        }
        return String.format(getREPORTACCOUNT(), getUser(), getAccountnumber(), getNewBalance());
    }

    // Sobrescribir el método isEmptyBalance para verificar si el balance está vacío
    @Override
    public boolean isEmptyEntry(String amount) {
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
        if (isEmptyEntry(amount)) {
            return;
        }
        while (true) {
            try {
                @SuppressWarnings("unused")
                Double canBeDouble = Double.valueOf(amount);
                if(canBeDouble<0){
                    System.out.println("El valor ingresado no es un número válido.");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese el monto: ");
                    amount = sc.nextLine();
                    continue;
                }
                setAmount(amount);
                return;
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese el monto: ");
                amount = sc.nextLine();
                if (isEmptyEntry(amount)) {
                    break;
                }
            }
        }
    }

    // Sobrescribir el método verifyAccount para verificar y validar el número de cuenta
    public void verifyAccount(Scanner sc, String accountnumber) {
        if (isEmptyEntry(accountnumber)) {
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
                    if (isEmptyEntry(accountnumber)) {
                        return;
                    }
                    continue;
                } else if (accountnumber.equals(MainMenu.getAccountnumber())) {
                    System.err.println("No puede ingresar su mismo número de cuenta.");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese el número de cuenta destino: ");
                    accountnumber = sc.nextLine();
                    if (isEmptyEntry(accountnumber)) {
                        return;
                    }
                    continue;
                }
                setDestinationAccount(accountnumber);
                return;
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese el número de cuenta destino: ");
                accountnumber = sc.nextLine();
                if (isEmptyEntry(accountnumber)) {
                    return;
                }
            }
        }
    }

    // Método para confirmar la transacción
    public static void confirmTransaction(){
        String option;
        do {
            System.out.printf(CONFIRM_TEXT,getDestinationUser());
            option = sc.nextLine();
            if (option.equalsIgnoreCase("y")){
                OperationsMenu.confirmedOp = true;
                return;
            } else if (option.equalsIgnoreCase("n")){
                OperationsMenu.confirmedOp = false;
                System.err.println("Transferencia cancelada!");
                return;
            } else {
                System.err.println("Por favor ingrese una opción valida");
            }
        } while (!option.equalsIgnoreCase("y") && !option.equalsIgnoreCase("n"));
    }
}
