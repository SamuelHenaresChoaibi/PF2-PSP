package aeropuerto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class TorreControl {
    private Semaphore pistas;
    private List<Avion> listaAterrizaje = new ArrayList<>();
    private List<Avion> listaDespegue = new ArrayList<>();

    public TorreControl(int numPistas) {
        this.pistas = new Semaphore(numPistas);
    }

    public void solicitarAterrizar(Avion a) {
        try {
            listaAterrizaje.add(a);
            while (listaAterrizaje.getFirst() != a || pistas.tryAcquire()) {
                wait();
            }
            listaAterrizaje.remove(a);
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void solicitarDespegar(Avion a) {
        try {
            listaDespegue.add(a);
            while (!listaAterrizaje.isEmpty() || listaDespegue.getFirst() != a || !pistas.tryAcquire()) {
                wait();
            }
            listaDespegue.remove(a);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void notificarLiberacionPista(Avion a) {
        try {
            pistas.release();
            pistas.notifyAll();
        } catch (IllegalMonitorStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int pistasDisponibles() {
        return pistas.availablePermits();
    }
}
