package utils;

import aeropuerto.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class HerramientasAuxiliares {

    private static final String RUTA_LOG = "src/resources/simulacion_aeropuerto.txt";
    private static boolean logInicializado = false;
    private static final DateTimeFormatter FORMATEADOR_HORA =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // Genera un ID aleatorio para un avión (letras + dígitos)
    public static String crearIdRandomAvion() {
        Random random = new Random();
        int numLetras = random.nextInt(5) + 1;
        int numDigitos = random.nextInt(5) + 1;

        String sb = "";
        for (int i = 0; i < numLetras; i++) sb += (char) ('A' + random.nextInt(26));
        for (int i = 0; i < numDigitos; i++) sb += (random.nextInt(10));

        return sb;
    }

    // Devuelve texto con emojis según el estado de un avión
    public static String escribirTextoEstado(Estado estado) {
        return switch (estado) {
            case Estado.EN_VUELO -> "✈\uFE0FEN VUELO✈\uFE0F";
            case Estado.ATERRIZANDO -> "\uD83D\uDEEBATERRIZANDO\uD83D\uDEEB";
            case Estado.EN_TERMINAL -> "\uD83C\uDD7F\uFE0FEN TERMINAL\uD83C\uDD7F\uFE0F";
            case Estado.DESPEGANDO -> "\uD83D\uDEEBDESPEGANDO\uD83D\uDEEB";
            case ESPERANDO -> "⌛ESPERANDO⌛";
        };
    }

    // Devuelve texto con emojis según la meteorología
    public static String escribirtextoMeteorologia(Meteorologia meteorologia) {
        return switch (meteorologia) {
            case Meteorologia.DESPEJADO -> "☀\uFE0FDESPEJADO☀\uFE0F";
            case Meteorologia.NIEBLA -> "\uD83C\uDF2B\uFE0FNIEBLA\uD83C\uDF2B\uFE0F";
            case Meteorologia.VIENTO -> "\uD83C\uDF00VIENTO\uD83C\uDF00";
            case Meteorologia.TEMPESTAD -> "\uD83C\uDF29\uFE0FTEMPESTAD\uD83C\uDF29\uFE0F";
        };
    }

    // Devuelve un estado aleatorio para el avión (EN_VUELO o EN_TERMINAL)
    public static Estado getEstadoRandom() {
        int num = new Random().nextInt(1, 3);
        return switch (num) {
            case 1 -> Estado.EN_VUELO;
            case 2 -> Estado.EN_TERMINAL;
            default -> throw new IllegalStateException("Sin estado");
        };
    }

    // Muestra los estados iniciales de los aviones y escribe en el log
    public static void mostrarEstadosIniciales(List<Avion> aviones, List<Estado> estadosIniciales, TorreControl torreControl) {
        imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
        imprimirYGuardar("                           ESTADO INICIAL DE LOS AVIONES                                    ");
        imprimirYGuardar("                   ✈️Aviones: " + aviones.size() + "    ||   ✔️Pistas disponibles: " + torreControl.pistasDisponibles() + "/" + torreControl.getNumPistasActuales());
        imprimirYGuardar("            ❄️Meteorologia: " + escribirtextoMeteorologia(torreControl.getMeteorologia()) + "  ||  ✖️Pistas cerradas temporalmente: " + torreControl.getMeteorologia().getPistasCerradas());
        imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");

        for (int i = 0; i < aviones.size(); i++) {
            imprimirYGuardar(" [" + aviones.get(i).getId() + "] (COMBUSTIBLE INICIAL: " + aviones.get(i).getCombustibleRestante() + "/" + aviones.get(i).getTanqueCombustibleCapacidadLitros() + "L || CONSUMO: " + aviones.get(i).getConsumoLitrosPorHoraCombustible() + "L/h)⛽ → " + escribirTextoEstado(estadosIniciales.get(i)));
        }

        imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
        imprimirYGuardar("                            ¡SIMULACIÓN DEL AEROPUERTO INICIADA!                            ");
        imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
    }

    // Asigna capacidad de combustible aleatoria
    public static double asignarCapacidadYCantidadTanqueCombustible() {
        return ((double) Math.round(new Random().nextDouble(100000, 300001) * 100) / 100);
    }

    // Asigna consumo de combustible aleatorio por hora
    public static double asignarConsumoDeCombustible() {
        return ((double) Math.round(new Random().nextDouble(10000, 20001) * 100) / 100);
    }

    // Devuelve meteorología aleatoria
    public static Meteorologia getMeteorologiaRandom() {
        int num = new Random().nextInt(1, 6);
        return switch (num) {
            case 1 -> Meteorologia.NIEBLA;
            case 2 -> Meteorologia.VIENTO;
            case 3 -> Meteorologia.TEMPESTAD;
            default -> Meteorologia.DESPEJADO;
        };
    }

    // Imprime en consola y guarda la línea en el log con hora
    public static void imprimirYGuardar(String texto) {
        inicializarLog();
        String lineaConHora = "[" + LocalDateTime.now().format(FORMATEADOR_HORA) + "] " + texto;
        System.out.println(lineaConHora);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(RUTA_LOG, true)))) {
            pw.println(lineaConHora);
        } catch (IOException e) {
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }

    // Inicializa el log: crea carpeta, borra archivo viejo y crea uno nuevo
    private static void inicializarLog() {
        if (logInicializado) return;
        try {
            Files.createDirectories(Paths.get("src/resources"));
            Path p = Paths.get(RUTA_LOG);
            if (Files.exists(p)) Files.delete(p);
            Files.createFile(p);
            logInicializado = true;
        } catch (IOException e) {
            System.err.println("Error al inicializar el log: " + e.getMessage());
        }
    }
}
