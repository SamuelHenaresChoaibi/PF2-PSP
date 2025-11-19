package aeropuerto;

import utils.HerramientasAuxiliares;

import java.util.List;

public class Aeropuerto {
    private List<Avion> aviones;

    // Constructor: recibe la lista de aviones del aeropuerto
    public Aeropuerto(List<Avion> aviones) {
        this.aviones = aviones;
    }

    // Devuelve la lista de aviones
    public List<Avion> getAviones() {
        return aviones;
    }

    // Muestra por pantalla y guarda en el log el estado actual de todos los aviones
    public void mostrarEstados(TorreControl torreControl) {
        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
        HerramientasAuxiliares.imprimirYGuardar("                           ESTADO DE LOS AVIONES                                    ");
        HerramientasAuxiliares.imprimirYGuardar("                   ✈️Aviones: " + aviones.size() + "    ||   ✔️Pistas disponibles: " + torreControl.pistasDisponibles() + "/" + torreControl.getNumPistasActuales());
        HerramientasAuxiliares.imprimirYGuardar("            ❄️Meteorologia: " + HerramientasAuxiliares.escribirtextoMeteorologia(torreControl.getMeteorologia()) + "  ||  ✖️Pistas cerradas temporalmente: " + torreControl.getMeteorologia().getPistasCerradas());
        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");

        for (Avion avione : aviones) {
            HerramientasAuxiliares.imprimirYGuardar(" [" + avione.getId() + "] (COMBUSTIBLE INICIAL: " + avione.getCombustibleRestante() + "/" + avione.getTanqueCombustibleCapacidadLitros() + "L || CONSUMO: " + avione.getConsumoLitrosPorHoraCombustible() + "L/h)⛽ → " + HerramientasAuxiliares.escribirTextoEstado(avione.getEstado()));
        }

        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
    }

    // Comprueba si todos los aviones han terminado sus operaciones
    public boolean todosFinalizados() {
        return aviones.stream().allMatch(Avion::haFinalizado);
    }
}

