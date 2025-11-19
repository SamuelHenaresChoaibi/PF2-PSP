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
        System.out.println("\n\n════════════════════════════════════════════════════════════════════════════════════════════");
        System.out.println("                               ESTADO DE LOS AVIONES                                        ");
        System.out.println("                   Aviones: " + aviones.size() + "    ||   Pistas disponibles: " + torreControl.pistasDisponibles() + "/" + torreControl.getNumPistasActuales());
        System.out.println("            Meteorologia: " + HerramientasAuxiliares.escribirtextoMeteorologia(torreControl.getMeteorologia()) + "  ||  Pistas cerradas temporalmente: " + torreControl.getMeteorologia().getPistasCerradas());
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════");
        for (Avion avion : aviones) {
            System.out.println("[" + avion.getId() + "] (COMBUSTIBLE: " + avion.getCombustibleRestante() +"/" + avion.getTanqueCombustibleCapacidadLitros() + ") → " + HerramientasAuxiliares.escribirTextoEstado(avion.getEstado()));
        }
        System.out.println("════════════════════════════════════════════════════════════════════════════════════════════");
    }

    public boolean todosFinalizados() {
        return aviones.stream().allMatch(Avion::haFinalizado);
    }
}
