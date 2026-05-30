package trimestrepractica;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    public static void escribir(String usuario, String accion) {

        try (FileWriter fw = new FileWriter("src/movimientos.log", true);
             PrintWriter pw = new PrintWriter(fw)) {

            DateTimeFormatter formato =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String fecha = LocalDateTime.now().format(formato);

            pw.println("[" + fecha + "][" + usuario + "][" + accion + "]");

        } catch (Exception e) {
            System.out.println("Error en log: " + e.getMessage());
        }
    }
}