package startup.services;

import java.util.Scanner;

/**
 * @autor Abdieeel
 */
public class Main {

    public static void main(String[] args) {
        // Inicializar objetos
        FileIO userfile = new FileIO(); // Objeto para manejo de archivos de usuario
        MainMenu mainMenu = new MainMenu(userfile); // Menú principal, se le pasa el objeto FileIO
        OperationsMenu op = new OperationsMenu(); // Menú de operaciones

        // Declaración de variables
        Scanner sc = new Scanner(System.in); // Objeto para leer la entrada del usuario
        userfile.init(mainMenu); // Inicializa el menú principal con el objeto FileIO
        String user = null, pass = null, accountnumber, balance, amount; // Variables para datos del usuario

        // Verificar si el archivo de usuarios existe, si no, crear uno nuevo
        if (!userfile.usernames_file.exists()) {
            userfile.createFile(); // Crear archivo
        }

        int option = 0; // Variable para almacenar la opción del menú
        boolean isAuth = false; // Variable para verificar si el usuario está autenticado

        do {
            try {
                // Mostrar el menú principal y obtener la opción del usuario
                System.out.print(mainMenu.getMAINMENU());
                option = sc.nextInt();
                sc.nextLine(); // LIMPIA EL BUFFER
            } catch (Exception e) {
                // Manejo de error si la entrada no es numérica
                System.err.print("Error: Debe ingresar una opción numérica!");
                sc.nextLine(); // LIMPIA EL BUFFER
                continue;
            }

            // Manejo de las opciones del menú principal
            switch (option) {

                case 1 -> {
                    // Opción de login
                    System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese su usuario: ");
                    user = sc.nextLine();

                    if (mainMenu.isEmptyUser(user)) {
                        break;
                    }
                    System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese la contraseña: ");
                    pass = sc.nextLine();
                    if (mainMenu.isEmptyPass(pass)) {
                        break;
                    }

                    // Intentar iniciar sesión
                    isAuth = mainMenu.login(user, pass);
                }

                case 2 -> {
                    // Opción de registro
                    System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese el usuario a registrar: ");
                    user = sc.nextLine();
                    mainMenu.verifyUser(sc, user);
                    if (mainMenu.isIsEmpty()) {
                        break;
                    }

                    System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese la contraseña: ");
                    pass = sc.nextLine();
                    mainMenu.setPass(pass);
                    if (mainMenu.isIsEmpty()) {
                        break;
                    }

                    System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese su nuevo número de cuenta (8 dígitos): ");
                    accountnumber = sc.nextLine();
                    mainMenu.verifyAccount(sc, accountnumber);
                    if (mainMenu.isIsEmpty()) {
                        break;
                    }

                    System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese su saldo inicial: ");
                    balance = sc.nextLine();
                    mainMenu.verifyBalance(sc, balance);
                    if (mainMenu.isIsEmpty()) {
                        break;
                    }

                    // Registrar el usuario
                    mainMenu.register();
                    System.err.print("Presione enter para continuar...");
                    sc.nextLine();
                }

                case 3 -> {
                    // Opción para salir de la aplicación
                    System.err.println("Saliendo de la aplicación");
                    System.err.println("Pulse enter para continuar... ");
                    sc.nextLine();
                    break;
                }

                default -> System.err.println("Opción no reconocida!");
            }

            // Si el usuario está autenticado, mostrar el menú de operaciones
            if (isAuth) {
                do {
                    try {
                        // Mostrar el menú de operaciones y obtener la opción del usuario
                        System.out.printf(op.getOPERATIONMENU(), op.getUser());
                        option = sc.nextInt();
                        sc.nextLine(); // LIMPIA EL BUFFER
                    } catch (Exception e) {
                        // Manejo de error si la entrada no es numérica
                        System.err.print("Error: Debe ingresar una opción numérica!");
                        sc.nextLine(); // LIMPIA EL BUFFER
                        continue;
                    }

                    // Manejo de las opciones del menú de operaciones
                    switch (option) {
                        case 1 -> {
                            // Mostrar el estado de cuenta
                            System.out.println(op.stateAccount());
                            System.out.println("Pulse enter para continuar... ");
                            sc.nextLine();
                        }

                        case 2 -> {
                            // Opción de depósito
                            System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese el monto a depositar: ");
                            amount = sc.nextLine();
                            op.verifyBalance(sc, amount);
                            if (op.isIsEmpty()) {
                                break;
                            }
                            option = 1; // DEPÓSITO
                            userfile.updateBalance(user, pass, amount, option);
                        }

                        case 3 -> {
                            // Opción de retiro
                            System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese el monto a retirar: ");
                            amount = sc.nextLine();
                            op.verifyBalance(sc, amount);
                            if (op.isIsEmpty()) {
                                break;
                            }
                            option = 2; // RETIRO
                            userfile.updateBalance(user, pass, amount, option);
                        }

                        case 4 -> {
                            // Opción de transferencia
                            System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese el número de cuenta destino: ");
                            accountnumber = sc.nextLine();
                            op.verifyAccount(sc, accountnumber);
                            if (op.isIsEmpty()) {
                                break;
                            }

                            System.out.print(mainMenu.getDEFAULTTEXT_1() + "Ingrese el monto a transferir: ");
                            amount = sc.nextLine();
                            op.verifyBalance(sc, amount);
                            if (op.isIsEmpty()) {
                                break;
                            }

                            userfile.transferBalance(user, pass, accountnumber, amount);
                        }

                        case 5 -> {
                            // Cerrar sesión
                            System.err.println("Pulse enter para continuar... ");
                            sc.nextLine();
                            isAuth = false; // Cambia el estado de autenticación
                        }

                        default -> System.err.println("Opción no reconocida!");
                    }
                } while (option != 5); // Continuar mostrando el menú de operaciones hasta que el usuario elija cerrar
                                       // sesión
            }
        } while (option != 3); // Continuar mostrando el menú principal hasta que el usuario elija salir

    }

}
