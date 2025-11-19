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

        for (int i = 0; i < aviones.size(); i++) {
            HerramientasAuxiliares.imprimirYGuardar(" [" + aviones.get(i).getId() + "] (COMBUSTIBLE INICIAL: " + aviones.get(i).getCombustibleRestante() + "/" + aviones.get(i).getTanqueCombustibleCapacidadLitros() + "L || CONSUMO: " + aviones.get(i).getConsumoLitrosPorHoraCombustible() + "L/h)⛽ → " + HerramientasAuxiliares.escribirTextoEstado(aviones.get(i).getEstado()));

        }

        HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
    }

    public boolean todosFinalizados() {
        return aviones.stream().allMatch(Avion::haFinalizado);
    }
}
