package aeropuerto;

import utils.Estados;

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

    public void mostrarEstado() {
        System.out.println("-------- ESTADO DE LOS AVIONES --------");
        for (Avion avion : aviones) {
            System.out.println("[" + avion.getId() + "] -> " + avion.getEstado());
        }
        System.out.println("---------------------------------------");
    }
}
