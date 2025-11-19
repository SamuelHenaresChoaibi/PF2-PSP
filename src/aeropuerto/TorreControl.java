package aeropuerto;

import utils.HerramientasAuxiliares;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TorreControl {
    private final Semaphore pistas;
    private final List<Avion> listaAterrizaje = new ArrayList<>();
    private final List<Avion> listaDespegue = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int numPistasActuales;
    private final int numPistasTotales;
    private Meteorologia meteorologia;

    public TorreControl(int numPistas) {
        this.numPistasTotales = numPistas;
        this.meteorologia = HerramientasAuxiliares.getMeteorologiaRandom();
        this.numPistasActuales = Math.max(0, numPistasTotales - meteorologia.getPistasCerradas());
        this.pistas = new Semaphore(this.numPistasActuales, true);
    }

    public Meteorologia getMeteorologia() {
        return meteorologia;
    }

    private void ordenarPorPrioridadCombustible(List<Avion> lista) {
        lista.sort((a1, a2) -> {
            double porc1 = a1.getCombustibleRestante() / a1.getTanqueCombustibleCapacidadLitros();
            double porc2 = a2.getCombustibleRestante() / a2.getTanqueCombustibleCapacidadLitros();
            return Double.compare(porc1, porc2);
        });
    }

    private boolean estaEnEmergencia(Avion a) {
        return a.getCombustibleRestante() <= a.getConsumoLitrosPorHoraCombustible();
    }

    private void reordenarConEmergencia() {
        List<Avion> emergencia = new ArrayList<>();
        List<Avion> normales = new ArrayList<>();

        for (Avion avion : listaAterrizaje) {
            if (estaEnEmergencia(avion)) {
                emergencia.add(avion);
            } else {
                normales.add(avion);
            }
        }

        emergencia.sort(Comparator.comparingDouble(Avion::getCombustibleRestante));
        ordenarPorPrioridadCombustible(normales);

        listaAterrizaje.clear();
        listaAterrizaje.addAll(emergencia);
        listaAterrizaje.addAll(normales);
    }

    public void solicitarAterrizar(Avion a) throws InterruptedException {
        lock.lock();
        try {
            listaAterrizaje.add(a);
            reordenarConEmergencia();

            while (listaAterrizaje.getFirst() != a || pistas.availablePermits() == 0) {
                condition.await();
                reordenarConEmergencia();
            }
            listaAterrizaje.removeFirst();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
        pistas.acquire();
    }

    public void solicitarDespegar(Avion a) throws InterruptedException {
        lock.lock();
        try {
            listaDespegue.add(a);
            ordenarPorPrioridadCombustible(listaDespegue);
            while (listaDespegue.getFirst() != a ||
                    pistas.availablePermits() == 0 ||
                    (!listaAterrizaje.isEmpty() && !estaEnEmergencia(listaAterrizaje.getFirst()))) {

                condition.await();
                ordenarPorPrioridadCombustible(listaDespegue);
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
        String accion = (a.getEstado() == Estado.ATERRIZANDO) ? "HA ATERRIZADO" : "HA DESPEGADO";
        String emergencia = estaEnEmergencia(a) ? " ¡¡¡EMERGENCIA COMBUSTIBLE!!! " : "";
        System.out.println("[" + a.getId() + "] " + accion + emergencia);
        System.out.println("*************************");

        pistas.release();
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void comprobarSiHayPistasDisponibles() {
        if (numPistasActuales == 0) {
            System.out.println("\n·····················································································");
            System.out.println("ACTUALMENTE NO HAY PISTAS DISPONIBLES, ESPERE A QUE EL TIEMPO MEJORE PARA PODER SEGUIR");
            System.out.println("······················································································");
        }
    }

    public void cambiarMeteorologia() {
        lock.lock();
        try {
            meteorologia = HerramientasAuxiliares.getMeteorologiaRandom();
            int nuevasPistas = Math.max(0, numPistasTotales - meteorologia.getPistasCerradas());
            int diferencia = nuevasPistas - numPistasActuales;

            if (diferencia > 0) {
                pistas.release(diferencia);
            } else if (diferencia < 0) {
                for (int i = 0; i < -diferencia; i++) {
                    try { pistas.acquire(); }
                    catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                }
            }

            numPistasActuales = nuevasPistas;
            comprobarSiHayPistasDisponibles();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int pistasDisponibles() { return pistas.availablePermits(); }
    public int getNumPistasActuales() { return numPistasActuales; }
}