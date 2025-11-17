package aeropuerto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TorreControl {
    private Semaphore pistas;
    private List<Avion> listaAterrizaje = new ArrayList<>();
    private List<Avion> listaDespegue = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public TorreControl(int numPistas) {
        this.pistas = new Semaphore(numPistas, true);
    }

    public void solicitarAterrizar(Avion a) throws InterruptedException {
        lock.lock();
        try {
            listaAterrizaje.add(a);
            while (listaAterrizaje.getFirst() != a || pistas.availablePermits() == 0) {
                condition.await();
            }
            listaAterrizaje.removeFirst();
            condition.signalAll();
        } finally{
            lock.unlock();
        }
        pistas.acquire();
    }

    public void solicitarDespegar(Avion a) throws InterruptedException {
        lock.lock();
        try {
            listaDespegue.add(a);
            while (!listaAterrizaje.isEmpty() || listaDespegue.getFirst() != a || pistas.availablePermits() == 0) {
                condition.await();
            }
            listaDespegue.removeFirst();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
        pistas.acquire();
    }

    public void notificarLiberacionPista(Avion a) {
        System.out.println("\n*************************");
        if (!a.isHaAterrizado()) {
            System.out.println("[" + a.getId() + "] HA ATERRIZADO");
        } else {
            System.out.println("[" + a.getId() + "] HA DESPEGADO");
        }
        System.out.println("*************************");

        pistas.release();
        lock.lock();
        try {
            condition.signalAll();
        } finally{
            lock.unlock();
        }
    }

    public int pistasDisponibles() {
        return pistas.availablePermits();
    }
}
