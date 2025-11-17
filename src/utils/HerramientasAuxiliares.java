package utils;

import aeropuerto.Avion;

import java.util.List;
import java.util.Random;

public class HerramientasAuxiliares {

    public static String crearIdRandomAvion() {
        Random random = new Random();
        int numLetras = random.nextInt(5) + 1;
        int numDigitos = random.nextInt(5) + 1;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numLetras; i++) {
            sb.append((char) ('A' + random.nextInt(26)));
        }
        for (int i = 0; i < numDigitos; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static String escribirTextoEstado(Estado estado){
        return switch(estado){
            case Estado.EN_VUELO -> "EN VUELO";
            case Estado.ATERRIZANDO -> "ATERRIZANDO";
            case Estado.EN_TERMINAL ->  "EN TERMINAL";
            case Estado.DESPEGANDO ->  "DESPEGANDO";
            case ESPERANDO ->  "ESPERANDO";
        };
    }

    public static Estado getEstadoRandom(){
        int num = new Random().nextInt(1, 3);

        return switch(num){
            case 1 -> Estado.EN_VUELO;
            case 2 -> Estado.EN_TERMINAL;
            default -> throw new IllegalStateException("Sin estado");
        };
    }

    public static void mostrarEstadosIniciales(List<Avion> aviones, List<Estado> estadosIniciales) {
        System.out.println("══════════════════════════════════════════════");
        System.out.println("      ESTADO INICIAL DE LOS AVIONES      ");
        System.out.println("══════════════════════════════════════════════");

        for (int i = 0; i < aviones.size(); i++) {
            System.out.println(" [" + aviones.get(i).getId() + "] → " + HerramientasAuxiliares.escribirTextoEstado(estadosIniciales.get(i)));
        }

        System.out.println("══════════════════════════════════════════════");
        System.out.println("       ¡SIMULACIÓN DEL AEROPUERTO INICIADA!       ");
        System.out.println("══════════════════════════════════════════════\n");
    }
}
