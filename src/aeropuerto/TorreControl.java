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

    // Constructor: inicializa pistas, meteorología y semáforo de pistas
    public TorreControl(int numPistas) {
        this.numPistasTotales = numPistas;
        this.meteorologia = HerramientasAuxiliares.getMeteorologiaRandom();
        this.numPistasActuales = Math.max(0, numPistasTotales - meteorologia.getPistasCerradas());
        this.pistas = new Semaphore(this.numPistasActuales, true);
    }

    // Devuelve la meteorología actual
    public Meteorologia getMeteorologia() {
        return meteorologia;
    }

    // Ordena una lista de aviones por porcentaje de combustible restante (prioridad)
    private void ordenarPorPrioridadCombustible(List<Avion> lista) {
        lista.sort((a1, a2) -> {
            double porc1 = a1.getCombustibleRestante() / a1.getTanqueCombustibleCapacidadLitros();
            double porc2 = a2.getCombustibleRestante() / a2.getTanqueCombustibleCapacidadLitros();
            return Double.compare(porc1, porc2);
        });
    }

    // Comprueba si un avión está en emergencia de combustible
    private boolean estaEnEmergencia(Avion a) {
        return a.getCombustibleRestante() <= a.getConsumoLitrosPorHoraCombustible();
    }

    // Reordena la lista de aterrizaje dando prioridad a emergencias
    private void reordenarConEmergencia() {
        List<Avion> emergencia = new ArrayList<>();
        List<Avion> normales = new ArrayList<>();

        for (Avion avion : listaAterrizaje) {
            if (estaEnEmergencia(avion)) emergencia.add(avion);
            else normales.add(avion);
        }

        emergencia.sort(Comparator.comparingDouble(Avion::getCombustibleRestante));
        ordenarPorPrioridadCombustible(normales);

        listaAterrizaje.clear();
        listaAterrizaje.addAll(emergencia);
        listaAterrizaje.addAll(normales);
    }

    // Solicita permiso para aterrizar: espera su turno según prioridad
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

    // Solicita permiso para despegar: espera su turno según prioridad y emergencias
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

    // Notifica que un avión ha liberado la pista (aterrizó o despegó)
    public void notificarLiberacionPista(Avion a) {
        HerramientasAuxiliares.imprimirYGuardar("*************************");
        String accion = (a.getEstado() == Estado.ATERRIZANDO) ? "HA ATERRIZADO🛬" : "HA DESPEGADO🛫";
        String emergencia = estaEnEmergencia(a) ? " ¡¡¡EMERGENCIA COMBUSTIBLE!!! " : "";
        HerramientasAuxiliares.imprimirYGuardar("[" + a.getId() + "] " + accion + emergencia);
        HerramientasAuxiliares.imprimirYGuardar("*************************");

        pistas.release(); // liberar la pista
        lock.lock();
        try { condition.signalAll(); } finally { lock.unlock(); }
    }

    // Mensaje si no hay pistas disponibles
    public void comprobarSiHayPistasDisponibles() {
        if (numPistasActuales == 0) {
            HerramientasAuxiliares.imprimirYGuardar("··············································································");
            HerramientasAuxiliares.imprimirYGuardar("ACTUALMENTE NO HAY PISTAS DISPONIBLES, ESPERE A QUE EL TIEMPO MEJORE");
            HerramientasAuxiliares.imprimirYGuardar("··············································································");
        }
    }

    // Cambia la meteorología y ajusta las pistas disponibles
    public void cambiarMeteorologia() {
        lock.lock();
        try {
            meteorologia = HerramientasAuxiliares.getMeteorologiaRandom();
            int nuevasPistas = Math.max(0, numPistasTotales - meteorologia.getPistasCerradas());
            int diferencia = nuevasPistas - numPistasActuales;

            if (diferencia > 0) pistas.release(diferencia);
            else if (diferencia < 0) {
                for (int i = 0; i < -diferencia; i++) {
                    try { pistas.acquire(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); return; }
                }
            }

            numPistasActuales = nuevasPistas;
            comprobarSiHayPistasDisponibles();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Devuelve el número de pistas libres
    public int pistasDisponibles() { return pistas.availablePermits(); }

    // Devuelve el número total de pistas operativas actualmente
    public int getNumPistasActuales() { return numPistasActuales; }
}
