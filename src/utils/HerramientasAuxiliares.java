package utils;

import aeropuerto.*;

import java.util.List;
import java.util.Random;

public class HerramientasAuxiliares {

    public static String crearIdRandomAvion() {
        Random random = new Random();
        int numLetras = random.nextInt(5) + 1;
        int numDigitos = random.nextInt(5) + 1;

        String sb = "";
        for (int i = 0; i < numLetras; i++) {
            sb += (char) ('A' + random.nextInt(26));
        }
        for (int i = 0; i < numDigitos; i++) {
            sb += (random.nextInt(10));
        }
        return sb;
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

    public static String escribirtextoMeteorologia(Meteorologia meteorologia){
        return switch(meteorologia){
            case Meteorologia.DESPEJADO -> "DESPEJADO";
            case Meteorologia.NIEBLA -> "NIEBLA";
            case Meteorologia.VIENTO ->  "VIENTO";
            case Meteorologia.TEMPESTAD ->  "TEMPESTAD";
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

    public static void mostrarEstadosIniciales(List<Avion> aviones, List<Estado> estadosIniciales, TorreControl torreControl) {
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                           ESTADO INICIAL DE LOS AVIONES                                    ");
        System.out.println("                   Aviones: " + aviones.size() + "    ||   Pistas disponibles: " + torreControl.pistasDisponibles() + "/" + torreControl.getNumPistasActuales());
        System.out.println("            Meteorologia: " + HerramientasAuxiliares.escribirtextoMeteorologia(torreControl.getMeteorologia()) + "  ||  Pistas cerradas temporalmente: " + torreControl.getMeteorologia().getPistasCerradas());
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════");

        for (int i = 0; i < aviones.size(); i++) {
            System.out.println(" [" + aviones.get(i).getId() + "] (COMBUSTIBLE INICIAL: " + aviones.get(i).getCombustibleRestante() + "/" + aviones.get(i).getTanqueCombustibleCapacidadLitros() +" || CONSUMO: " + aviones.get(i).getConsumoLitrosPorHoraCombustible() + ")→ " + HerramientasAuxiliares.escribirTextoEstado(estadosIniciales.get(i)));
        }

        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                            ¡SIMULACIÓN DEL AEROPUERTO INICIADA!                            ");
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════");
    }

    public static double asignarCapacidadYCantidadTanqueCombustible() {
        return ((double) Math.round(new Random().nextDouble(100000, 300000 + 1) * 100) / 100);
    }

    public static double asignarConsumoDeCombustible() {
        return ((double) Math.round(new Random().nextDouble(10000, 20000 + 1) * 100) / 100);
    }

    public static Meteorologia getMeteorologiaRandom(){
        int num = new Random().nextInt(1, 6);
        return switch (num){
            case 1 -> Meteorologia.NIEBLA;
            case 2 -> Meteorologia.VIENTO;
            case 3 -> Meteorologia.TEMPESTAD;
            default -> Meteorologia.DESPEJADO;
        };
    }
}
