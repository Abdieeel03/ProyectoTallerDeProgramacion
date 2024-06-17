package startup.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Abdieeel
 * @author Cielo
 * @author Sergio
 * @author Milagros
 * @author Benjamin
 */
// Clase que controla el manejo del archivo que aloja los datos de usuario
public class FileIO {

    // Constantes para el nombre del archivo y el formato de salida
    private final String NAMEFILE = "src\\startup\\files\\usernames.csv";
    private final String FORMAT = "%s,%s,%s,%s\n";
    private String readedLine; // Línea leída del archivo
    private MainMenu mainMenu; // Referencia al menú principal
    File usernames_file = new File(NAMEFILE); // Archivo que contiene los datos de usuario

    // Constructor
    public FileIO() {
    }

    // Método para inicializar MainMenu
    public void init(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    // Método para crear el archivo
    public void createFile() {
        try (PrintWriter printer = new PrintWriter(usernames_file)) {
            // Crea un nuevo archivo o sobreescribe el existente
        } catch (IOException e) {
            System.err.println("Ruta alterada!" + e.getMessage());
        }
    }

    // Método para guardar la información registrada por el usuario
    public void saveData(String user, String pass, String accountnumber, String balance) {
        try (FileWriter fw = new FileWriter(usernames_file, true)) {
            PrintWriter printer = new PrintWriter(fw);
            printer.printf(FORMAT, user, pass, accountnumber, balance);
            System.out.println("Usuario registrado correctamente!");
        } catch (IOException e) {
            System.err.println("Ruta alterada!\n" + e.getMessage());
        }
    }

    // Método para leer la información registrada por el usuario
    public void readData(String user, String pass) {
        boolean userFound = false; // Variable para indicar si el usuario fue encontrado
        try (BufferedReader reader = new BufferedReader(new FileReader(usernames_file))) {
            while ((readedLine = reader.readLine()) != null) {
                String readedData[] = readedLine.split(",");
                if (readedData[0].equals(user) && readedData[1].equals(pass)) {
                    userFound = true;
                    mainMenu.setUser(readedData[0]);
                    mainMenu.setPass(readedData[1]);
                    mainMenu.setAccountnumber(readedData[2]);
                    mainMenu.setBalance(readedData[3]);
                    mainMenu.setIsAuth(userFound);
                    break;
                }
            }
            if (!userFound) {
                System.err.println("Nombre de usuario o contraseña incorrectos.");
                mainMenu.setIsAuth(userFound);
            }
        } catch (IOException e) {
            System.err.println("Ruta alterada!\n" + e.getMessage());
            mainMenu.setIsAuth(userFound);
        }
    }

    // Método que solo lee el usuario
    public void readOnlyUser(String user) {
        boolean userFound = false; // Variable para indicar si el usuario fue encontrado
        try (BufferedReader reader = new BufferedReader(new FileReader(usernames_file))) {
            while ((readedLine = reader.readLine()) != null) {
                String readedData[] = readedLine.split(",");
                if (readedData[0].equals(user)) {
                    userFound = true;
                    mainMenu.setUserFound(userFound);
                    break;
                }
            }
            if (!userFound) {
                mainMenu.setUserFound(userFound);
            }
        } catch (IOException e) {
            System.err.println("Ruta alterada!\n" + e.getMessage());
            mainMenu.setUserFound(userFound);
        }
    }

    // Método que solo lee el número de cuenta
    public void readOnlyAccount(String accountnumber) {
        boolean accountFound = false; // Variable para indicar si la cuenta fue encontrada
        try (BufferedReader reader = new BufferedReader(new FileReader(usernames_file))) {
            while ((readedLine = reader.readLine()) != null) {
                String readedData[] = readedLine.split(",");
                if (readedData[2].equals(accountnumber)) {
                    accountFound = true;
                    mainMenu.setAccountFound(accountFound);
                    break;
                }
            }
            if (!accountFound) {
                mainMenu.setAccountFound(accountFound);
            }
        } catch (IOException e) {
            System.err.println("Ruta alterada!\n" + e.getMessage());
            mainMenu.setAccountFound(accountFound);
        }
    }

    // Método para actualizar el saldo y guardarlo en el archivo
    public void updateBalance(String user, String pass, String amount, int option) {
        double userBalance, newBalance;
        try (BufferedReader reader = new BufferedReader(new FileReader(usernames_file));
                PrintWriter writer = new PrintWriter(new FileWriter(usernames_file + ".tmp"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(user) && data[1].equals(pass)) {
                    userBalance = Double.parseDouble(data[3]);
                    switch (option) {
                        case 1:
                            newBalance = userBalance + (Double.parseDouble(amount));
                            data[3] = String.valueOf(newBalance); // Actualiza el saldo
                            line = String.join(",", data);
                            break;
                        case 2:
                            if (userBalance == 0) {
                                System.err.println("No tiene saldo suficiente para realizar esta operación!");
                                return;
                            }
                            newBalance = (Double.parseDouble(data[3])) - (Double.parseDouble(amount));
                            data[3] = String.valueOf(newBalance); // Actualiza el saldo
                            line = String.join(",", data);
                            break;
                    }
                }
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar el saldo: " + e.getMessage());
        }

        // Eliminar el archivo original y renombrar el archivo temporal
        if (usernames_file.delete()) {
            File tmpFile = new File(usernames_file + ".tmp");
            if (tmpFile.renameTo(usernames_file)) {
                System.out.println("Deposito realizado correctamente!");
            } else {
                System.err.println("No se pudo actualizar el saldo.");
                System.out.println("No se pudo renombrar el archivo temporal.");
            }
        } else {
            System.err.println("No se pudo actualizar el saldo.");
            System.out.println("No se pudo eliminar el archivo original.");
        }
    }

    // Método para transferir saldo entre cuentas y guardar el archivo
    public void transferBalance(String user, String pass, String destinationAccount, String amount) {
        boolean accountFound = false; // Variable para indicar si la cuenta destino fue encontrada
        double transferAmount = Double.parseDouble(amount); // Monto a transferir
        double userBalance = 0.0; // Saldo del usuario
        double destinationBalance = 0.0; // Saldo de la cuenta destino
        @SuppressWarnings("unused")
        String destinationUser = ""; // Usuario de la cuenta destino
        @SuppressWarnings("unused")
        String destinationPass = ""; // Contraseña de la cuenta destino

        // Leer y procesar el archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(usernames_file));
                PrintWriter writer = new PrintWriter(new FileWriter(usernames_file + ".tmp"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(user) && data[1].equals(pass)) {
                    userBalance = Double.parseDouble(data[3]);
                    if (userBalance < transferAmount) {
                        System.err.println("Error: Saldo insuficiente para realizar la transferencia.");
                        return;
                    }
                    userBalance -= transferAmount;
                    data[3] = String.valueOf(userBalance);
                    line = String.join(",", data);
                } else if (data[2].equals(destinationAccount)) {
                    accountFound = true;
                    destinationUser = data[0];
                    destinationPass = data[1];
                    destinationBalance = Double.parseDouble(data[3]);
                    destinationBalance += transferAmount;
                    data[3] = String.valueOf(destinationBalance);
                    line = String.join(",", data);
                }
                writer.println(line);
            }
        } catch (IOException e) {
            System.err.println("Error al realizar la transferencia: " + e.getMessage());
            e.printStackTrace(); // Imprimir la traza de la excepción para depuración
        }

        if (!accountFound) {
            System.err.println("Error: Número de cuenta destino no encontrado.");
            return;
        }

        // Eliminar el archivo original y renombrar el archivo temporal
        if (usernames_file.delete()) {
            File tmpFile = new File(usernames_file + ".tmp");
            if (tmpFile.renameTo(usernames_file)) {
                System.out.println("Transferencia realizada correctamente!");
            } else {
                System.err.println("No se pudo completar la transferencia.");
                System.out.println("No se pudo renombrar el archivo temporal.");
            }
        } else {
            System.err.println("No se pudo completar la transferencia.");
            System.out.println("No se pudo eliminar el archivo original.");
        }
    }

}
