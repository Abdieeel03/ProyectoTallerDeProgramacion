package startup.services;

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
        return String.format(getREPORTACCOUNT(),getUser(),getBalance());
    }

}
