import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {

        try {
//            ejecutarDir();           // dir -> salida en archivo
//            ejecutarPing();          // ping google.com
            ejecutarProcesos();    // comandos en orden
//            ejecutarParalelo();      // procesos en paralelo
//            ejecutarConError();      // control de errores

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Desarrollar un programa lanzador

    private static void ejecutarDir() throws Exception {

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir"); // Crea el process builder que ejecuta el comando

        pb.redirectOutput(new File("salida_ls.txt")); // Redirige la salida a salida_ls.txt
        pb.redirectError(new File("errores.txt")); // Redirige el error a errores.txt

        Process p = pb.start(); // Abre la consola de windows y ejecuta dir
        p.waitFor(); // Esperar a que termine el proceso

        System.out.println("dir ejecutado > salida_ls.txt"); // Si se ejecuta correctamente devuelve esto
    }

    // Ejecutar un proceso con parámetros (hacer ping a google)

    private static void ejecutarPing() throws Exception {

        // Ejecuta el comando ping 6 veces
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "ping", "-n", "6", "google.com");

        Process p = pb.start();

        mostrarSalida(p); // Lee y muestra en tiempo real la salida
    }

    // Automatizar la ejecución de varios procesos

    private static void ejecutarProcesos() throws Exception { // ESTE NO ME FUNCIONA BIEN ESTOY RESOLVIENDO

        // Imprime el texto "Inicio de la tarea", espera 3s mostrando null y luego muestra la fecha y hora actuales.
        // && es para ejecutar cada comando despues de que el anterior se haya ejecutado bien
        String comando = "echo Inicio de la tarea && timeout /t 3 && echo %date% %time%";

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", comando);

        Process p = pb.start();
        mostrarSalida(p);
    }

    // Ejecutar procesos en paralelo

    private static void ejecutarParalelo() throws Exception {

        ProcessBuilder pb1 = new ProcessBuilder("cmd.exe", "/c", "ping", "localhost"); // Ping a localhost
        ProcessBuilder pb2 = new ProcessBuilder("cmd.exe", "/c", "ping", "127.0.0.1"); // Ping a la ip

        pb1.redirectOutput(new File("ping_localhost.txt")); // Guarda ping de localhost en un archivo .txt
        pb2.redirectOutput(new File("ping_127.txt")); // Guarda ping de 127.0.0.1 en un archivo .txt

        // Lanza los dos procesos a la vez (en paralelo)
        Process p1 = pb1.start();
        Process p2 = pb2.start();

        // Espera a que ambos terminen antes de mostrar que se completaron los 2
        p1.waitFor();
        p2.waitFor();

        // Confirma que se han completado y guardado en los archivos
        System.out.println("Pings paralelos completados > revisar archivos");
    }

    // Extra - Control de errores

    private static void ejecutarConError() throws Exception {

        // Fuerza dos comando erróneos para hacer control de errores
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "comando_que_no_existe");

        // Inicia el proceso y muestra los errores
        Process p = pb.start();
        mostrarErrores(p);

        // Muestra el código de salida (si es 0 está bien , si no, es un error)
        System.out.println("Código de salida: " + p.waitFor());
    }

    // Función para leer salida estándar

    private static void mostrarSalida(Process p) throws IOException {

        // Abre un lector de texto de lo que imprime la consola
        BufferedReader lector = new BufferedReader(new InputStreamReader(p.getInputStream()));

        // Lee línea a línea  y muestra por consola hasta que termine
        String linea;
        while ((linea = lector.readLine()) != null) {
            System.out.println(linea);
        }

        try {
            p.waitFor(); // Espera a que el proceso termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Función para leer errores

    private static void mostrarErrores(Process proceso) throws IOException {

        // Abre un lector de texto para el flujo de errores
        BufferedReader lector = new BufferedReader(new InputStreamReader(proceso.getErrorStream()));

        // Imprime cada línea de error diciendo antes "ERROR: "
        String linea;
        while ((linea = lector.readLine()) != null) {
            System.err.println("ERROR: " + linea);
        }
    }
}