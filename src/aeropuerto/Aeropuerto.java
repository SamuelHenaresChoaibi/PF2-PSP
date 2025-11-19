package aeropuerto;

import utils.HerramientasAuxiliares;

import java.util.List;

public class Aeropuerto {
    private List<Avion> aviones;

    public Aeropuerto(List<Avion> aviones) {
        this.aviones = aviones;
    }

    public List<Avion> getAviones() {
        return aviones;
    }

    public void mostrarEstados(TorreControl torreControl) {
        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
        HerramientasAuxiliares.imprimirYGuardar("                           ESTADO DE LOS AVIONES                                    ");
        HerramientasAuxiliares.imprimirYGuardar("                   \uD83D\uDEE9\uFE0FAviones: " + aviones.size() + "    ||   ✔\uFE0FPistas disponibles: " + torreControl.pistasDisponibles() + "/" + torreControl.getNumPistasActuales());
        HerramientasAuxiliares.imprimirYGuardar("            ❄\uFE0FMeteorologia: " + HerramientasAuxiliares.escribirtextoMeteorologia(torreControl.getMeteorologia()) + "  ||  ✖\uFE0FPistas cerradas temporalmente: " + torreControl.getMeteorologia().getPistasCerradas());
        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");

        for (Avion avione : aviones) {
            HerramientasAuxiliares.imprimirYGuardar(" [" + avione.getId() + "] (COMBUSTIBLE INICIAL: " + avione.getCombustibleRestante() + "/" + avione.getTanqueCombustibleCapacidadLitros() + "L || CONSUMO: " + avione.getConsumoLitrosPorHoraCombustible() + "L/h)⛽ → " + HerramientasAuxiliares.escribirTextoEstado(avione.getEstado()));
        }

        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
    }

    public boolean todosFinalizados() {
        return aviones.stream().allMatch(Avion::haFinalizado);
    }
}
