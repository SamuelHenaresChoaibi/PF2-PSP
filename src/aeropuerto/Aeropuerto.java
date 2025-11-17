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

    public void setAviones(List<Avion> aviones) {
        this.aviones = aviones;
    }

    public void mostrarEstados() {
        System.out.println("══════════════════════════════════════════════");
        System.out.println("      ESTADO DE LOS AVIONES      ");
        System.out.println("══════════════════════════════════════════════");
        for (Avion avion : aviones) {
            System.out.println("[" + avion.getId() + "] → " + HerramientasAuxiliares.escribirTextoEstado(avion.getEstado()));
        }
        System.out.println("══════════════════════════════════════════════");
    }

    public boolean todosFinalizados() {
        return aviones.stream().allMatch(Avion::haFinalizado);
    }
}
