package startup.services;

import java.util.Scanner;

import startup.FileIO;

/**
 * @author Abdieeel
 */
public class OperationsMenu extends MainMenu {

    FileIO userfile = new FileIO();
    private final String OPERATIONMENU = """
            Bievenido %s
            Seleccione la operacion que desea realizar:
                1) Revisar estado de cuenta
                2) Deposito
                3) Retiro
                4) Transferencia
                5) Cerrar sesion
            Ingrese opcion [1-5]:
            """;
    private final String REPORTACCOUNT = """
            ###########################################
            # ESTADO DE CUENTA                        #
            ###########################################
            # USUARIO : %-30s#
            # SALDO RESTANTE : S/ %-20s#
            ###########################################
            """;
    @SuppressWarnings("unused")
    private static String amount;
    @SuppressWarnings("unused")
    private static String destinationAccount;

    public OperationsMenu(String user, String accountnumber, String balance) {
        super(user, accountnumber, balance);
    }

    public OperationsMenu() {
    }

    public String getOPERATIONMENU() {
        return OPERATIONMENU;
    }

    public String getREPORTACCOUNT() {
        return REPORTACCOUNT;
    }

    public String stateAccount() {
        return String.format(getREPORTACCOUNT(), getUser(), getBalance());
    }

    @Override
    public boolean isEmptyBalance(String amount) {
        isEmpty = false;
        if (amount.isEmpty()) {
            System.err.println("Operacion cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

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

    @Override
    public boolean isEmptyAccount(String accountnumber) {
        isEmpty = false;
        if (accountnumber.isEmpty()) {
            System.err.println("Operación cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

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
                    System.err.println("El número de cuenta debe tener exactamente 8 digitos.");
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
