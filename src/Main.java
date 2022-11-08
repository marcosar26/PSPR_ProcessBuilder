import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String comando = args[0];
        if (comando.isEmpty() || comando.isBlank()) {
            System.out.println("No se ha especificado ningún comando.");
            return;
        }

        System.out.print("Introduce la ruta: ");
        String ruta = sc.next();
        if (ruta.isEmpty() || ruta.isBlank()) {
            System.out.println("Ruta inválida.");
            return;
        }

        Process process;
        try {
            List<String> cmd = Arrays.asList("cmd.exe", "/c", comando);
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            processBuilder.directory(new File(ruta));
            process = processBuilder.start();
        } catch (IOException e) {
            System.out.println("Error al crear el proceso.");
            e.printStackTrace();
            return;
        }
        long pid = process.pid();
        InputStream stream = process.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(streamReader);

        StringBuilder sb = new StringBuilder();
        reader.lines().forEach(str -> sb.append(str).append(System.lineSeparator()));
        sb.append("PID:").append("\s").append(pid);

        System.out.println(sb);

        System.out.print("¿Desea guardar la información? (si/no): ");
        String res = sc.next();

        if (res.equalsIgnoreCase("si")) {
            File file = new File(ruta + "/" + comando + "_processbuilder" + ".txt");

            if (file.exists()) {
                file.delete();
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Error al crear el fichero.");
                }
            }

            try {
                Files.writeString(file.toPath(), sb.toString(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("Error al escribir en el fichero.");
            }
        }
    }
}