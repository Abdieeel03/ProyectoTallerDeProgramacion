package startup.services;

import java.util.Scanner;
import startup.FileIO;

/**
 * @author Abdieeel
 */
public class MainMenu {

    // Declaración de variables
    private final String MAINMENU = """
            Bienvenido a *NAMEBANK* (Simulador de app bancaria)
            Opciones:
                1) Iniciar Sesión
                2) Registrarse
                3) Salir
            Seleccione una opcion [1-3]:
            """;
    protected final String DEFAULTTEXT_1 = "**DEJE EL APARTADO EN BLANCO SI DESEA CANCELAR LA OPERACION**\n";
    private static String user; // Nombre de usuario
    private static String pass; // Contraseña
    private static String accountnumber; // Número de cuenta
    private static String balance; // Saldo
    protected static boolean isEmpty; // Variables de estado
    private static boolean isAuth;
    private static boolean userFound;
    private static boolean accountFound;
    private FileIO userfile; // Objeto para manejar la interacción con el archivo de usuarios

    // Constructor con parámetros
    public MainMenu(String user, String accountnumber, String balance) {
        MainMenu.user = user;
        MainMenu.accountnumber = accountnumber;
        MainMenu.balance = balance;
    }

    // Constructor que inicializa el archivo de usuarios
    public MainMenu(FileIO userfile) {
        this.userfile = userfile;
    }

    // Constructor por defecto
    public MainMenu() {
    }

    // Métodos para obtener los textos de los menús
    public String getMAINMENU() {
        return MAINMENU;
    }

    public String getDEFAULTTEXT_1() {
        return DEFAULTTEXT_1;
    }

    // Métodos para verificar y establecer estados
    public boolean isIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        MainMenu.isEmpty = isEmpty;
    }

    public boolean isUserFound() {
        return userFound;
    }

    public void setUserFound(boolean userFound) {
        MainMenu.userFound = userFound;
    }

    public boolean isAccountFound() {
        return accountFound;
    }

    public void setAccountFound(boolean accountFound) {
        MainMenu.accountFound = accountFound;
    }

    public void setIsAuth(boolean isAuth) {
        MainMenu.isAuth = isAuth;
    }

    // Métodos para obtener y establecer los datos del usuario
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        MainMenu.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        if (isEmptyPass(pass)) {
            MainMenu.isEmpty = true;
            return;
        }
        MainMenu.pass = pass;
    }

    public static String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        MainMenu.accountnumber = accountnumber;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        MainMenu.balance = balance;
    }

    // Métodos para verificar si las entradas del usuario están vacías
    public boolean isEmptyUser(String user) {
        isEmpty = false;
        if (user.isEmpty()) {
            System.err.println("Operacion cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmptyPass(String pass) {
        isEmpty = false;
        if (pass.isEmpty()) {
            System.err.println("Operacion cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmptyAccount(String accountnumber) {
        isEmpty = false;
        if (accountnumber.isEmpty()) {
            System.err.println("Operacion cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    public boolean isEmptyBalance(String balance) {
        isEmpty = false;
        if (balance.isEmpty()) {
            System.err.println("Operacion cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    // Método para verificar la disponibilidad del usuario
    public void verifyUser(Scanner sc, String user) {
        MainMenu.isEmpty = false;
        if (isEmptyUser(user)) {
            MainMenu.isEmpty = true;
            return;
        }
        while (true) {
            userfile.readOnlyUser(user);
            if (!isUserFound()) {
                MainMenu.user = user;
                return;
            } else {
                System.err.println("El usuario ingresado ya existe. Porfavor ingrese otro!");
                System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo usuario: ");
                user = sc.nextLine();
                if (isEmptyUser(user)) {
                    MainMenu.isEmpty = true;
                    return;
                }
            }
        }
    }

    // Método para verificar la disponibilidad del número de cuenta
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
                    System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo número de cuenta (8 digitos): ");
                    accountnumber = sc.nextLine();
                    if (isEmptyAccount(accountnumber)) {
                        MainMenu.isEmpty = true;
                        return;
                    }
                    continue;
                }
                userfile.readOnlyAccount(accountnumber);
                if (!isAccountFound()) {
                    MainMenu.accountnumber = accountnumber;
                    return;
                } else {
                    System.err.println("El número de cuenta ya existe. Porfavor ingrese otro!");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo número de cuenta (8 digitos): ");
                    accountnumber = sc.nextLine();
                    if (isEmptyAccount(accountnumber)) {
                        MainMenu.isEmpty = true;
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo número de cuenta (8 digitos): ");
                accountnumber = sc.nextLine();
                if (isEmptyAccount(accountnumber)) {
                    MainMenu.isEmpty = true;
                    return;
                }
            }
        }
    }

    // Método para verificar la validez del saldo inicial
    public void verifyBalance(Scanner sc, String balance) {
        MainMenu.isEmpty = false;
        if (isEmptyBalance(balance)) {
            MainMenu.isEmpty = true;
            return;
        }
        while (true) {
            try {
                @SuppressWarnings("unused")
                Double canBeDouble = Double.valueOf(balance);
                MainMenu.balance = balance;
                return;
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese su saldo inicial: ");
                balance = sc.nextLine();
                if (isEmptyBalance(balance)) {
                    MainMenu.isEmpty = true;
                    break;
                }
            }
        }
    }

    // Método para registrar un nuevo usuario
    public void register() {
        if (user.isEmpty()) {
            return;
        }
        if (accountnumber.isEmpty()) {
            return;
        }
        if (balance.isEmpty()) {
            return;
        }
        userfile.saveData(user, pass, accountnumber, balance);
    }

    // Método para iniciar sesión
    public boolean login(String user, String pass) {
        userfile.readData(user, pass);
        return MainMenu.isAuth;
    }

}
