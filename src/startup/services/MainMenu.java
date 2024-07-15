package startup.services;

import java.util.Random;
import java.util.Scanner;

/**
 * @author Abdieeel
 * @author Cielo
 * @author Sergio
 * @author Milagros
 */
public class MainMenu {

    // Declaración de variables
    private final String MAINMENU = """
            Bienvenido a BancoUTP (Simulador de app bancaria)
            Opciones:
                1) Iniciar Sesión
                2) Registrarse
                3) Salir
            Seleccione una opción [1-3]:
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

    // Método para verificar si las entradas están vacías
    public boolean isEmptyEntry(String entry) {
        isEmpty = false;
        if (entry.isEmpty()) {
            System.err.println("Operacion cancelada!");
            isEmpty = true;
        }
        return isEmpty;
    }

    // Métodos para verificar las restricciones de cada entrada de dato
    public void verifyUser(Scanner sc, String user) {
        if (isEmptyEntry(user)) {
            return;
        }
        while (true) {
            userfile.readOnlyUser(user);
            if (!isUserFound()) {
                MainMenu.user = user;
                return;
            } else {
                System.err.println("El usuario ingresado ya existe. Por favor ingrese otro!");
                System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo usuario: ");
                user = sc.nextLine();
                if (isEmptyEntry(user)) {
                    return;
                }
            }
        }
    }

    public void verifyPass(Scanner sc, String pass) {
        while (pass.length() > 12 || pass.length() < 5) {
            System.err.println("La contraseña debe contener como mínimo 5 dígitos y como máximo 12.");
            System.out.print(getDEFAULTTEXT_1() + "Ingrese la contraseña: ");
            pass = sc.nextLine();
            if (isEmptyEntry(pass)) {
                return;
            }
        }
        MainMenu.pass = pass;
    }

    // Método para verificar la disponibilidad del número de cuenta
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
                    System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo número de cuenta (8 dígitos): ");
                    accountnumber = sc.nextLine();
                    if (isEmptyEntry(accountnumber)) {
                        return;
                    }
                    continue;
                }
                userfile.readOnlyAccount(accountnumber);
                if (!isAccountFound()) {
                    MainMenu.accountnumber = accountnumber;
                    return;
                } else {
                    System.err.println("El número de cuenta ya existe. Por favor ingrese otro!");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo número de cuenta (8 dígitos): ");
                    accountnumber = sc.nextLine();
                    if (isEmptyEntry(accountnumber)) {
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese su nuevo número de cuenta (8 dígitos): ");
                accountnumber = sc.nextLine();
                if (isEmptyEntry(accountnumber)) {
                    return;
                }
            }
        }
    }

    // Método para verificar la validez del saldo inicial
    public void verifyBalance(Scanner sc, String balance) {
        if (isEmptyEntry(balance)) {
            return;
        }
        while (true) {
            try {
                @SuppressWarnings("unused")
                Double canBeDouble = Double.valueOf(balance);
                if(canBeDouble<0){
                    System.out.println("El valor ingresado no es un número válido.");
                    System.out.print(DEFAULTTEXT_1 + "Ingrese su saldo inicial: ");
                    balance = sc.nextLine();
                    continue;
                }
                MainMenu.balance = balance;
                return;
            } catch (NumberFormatException e) {
                System.err.println("El valor ingresado no es un número válido.");
                System.out.print(DEFAULTTEXT_1 + "Ingrese su saldo inicial: ");
                balance = sc.nextLine();
                if (isEmptyEntry(balance)) {
                    break;
                }
            }
        }
    }

    public int generateAccount() {
        int randomAccountNumber = 89800000;
        Random random = new Random();
        randomAccountNumber += random.nextInt(99999);
        return randomAccountNumber;
    }

    // Método para registrar un nuevo usuario
    public void register() {
        if (user.isEmpty()) {
            return;
        }

        if (balance.isEmpty()) {
            return;
        }

        setAccountnumber(String.valueOf(generateAccount()));
        userfile.saveData(user, pass, accountnumber, balance);
    }

    // Método para iniciar sesión
    public boolean login(String user, String pass) {
        userfile.readData(user, pass);
        return MainMenu.isAuth;
    }

}
