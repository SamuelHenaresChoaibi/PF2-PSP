package aeropuerto;

import utils.HerramientasAuxiliares;

import java.util.Random;

public class Avion implements Runnable{
    private String id;
    private Estado estado;
    private TorreControl torreControl;
    private boolean haAterrizado = false;
    private boolean haDespegado = false;
    private double tanqueCombustibleCapacidadLitros;
    private double combustibleRestante;
    private double consumoLitrosPorHoraCombustible;

    public Avion(String id, TorreControl torreControl) {
        this.id = id;
        this.torreControl = torreControl;
        this.estado = HerramientasAuxiliares.getEstadoRandom();
        this.tanqueCombustibleCapacidadLitros = HerramientasAuxiliares.asignarCapacidadYCantidadTanqueCombustible();
        this.combustibleRestante = (double) (Math.round(tanqueCombustibleCapacidadLitros * (0.50 + Math.random() * 0.50) * 100)) / 100;
        this.consumoLitrosPorHoraCombustible = HerramientasAuxiliares.asignarConsumoDeCombustible();
    }

    public String getId() {
        return id;
    }

    public Estado getEstado() {
        return estado;
    }

    public double getTanqueCombustibleCapacidadLitros() {
        return tanqueCombustibleCapacidadLitros;
    }

    public double getConsumoLitrosPorHoraCombustible() {
        return consumoLitrosPorHoraCombustible;
    }

    public double getCombustibleRestante() {
        return combustibleRestante;
    }

    public boolean isHaAterrizado() {
        return haAterrizado;
    }

    public boolean isHaDespegado() {
        return haDespegado;
    }

    public void cambiarEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public void run() {
        try {
            //Primer Ciclo
            if (estado == Estado.EN_VUELO) {
                // Solo aterriza
                cambiarEstado(Estado.ESPERANDO);
                torreControl.solicitarAterrizar(this);
                cambiarEstado(Estado.ATERRIZANDO);
                Thread.sleep(2000 + new Random().nextInt(6000));
                torreControl.notificarLiberacionPista(this);
                cambiarEstado(Estado.EN_TERMINAL);
                haAterrizado = true;

            } else if (estado == Estado.EN_TERMINAL) {
                // Solo despega
                cambiarEstado(Estado.ESPERANDO);
                torreControl.solicitarDespegar(this);
                cambiarEstado(Estado.DESPEGANDO);
                Thread.sleep(2000 + new Random().nextInt(6000));
                torreControl.notificarLiberacionPista(this);
                cambiarEstado(Estado.EN_VUELO);
                haDespegado = true;
            }

            // Tiempo en tierra o en vuelo antes del segundo ciclo
            Thread.sleep(2000 + new Random().nextInt(3000));

            //Segundo ciclo
            if (haAterrizado && !haDespegado) {
                //Ahora despega
                cambiarEstado(Estado.ESPERANDO);
                torreControl.solicitarDespegar(this);
                cambiarEstado(Estado.DESPEGANDO);
                Thread.sleep(2000 + new Random().nextInt(6000));
                torreControl.notificarLiberacionPista(this);
                cambiarEstado(Estado.EN_VUELO);
                haDespegado = true;

            } else if (haDespegado && !haAterrizado) {
                //Ahora aterriza
                cambiarEstado(Estado.ESPERANDO);
                torreControl.solicitarAterrizar(this);
                cambiarEstado(Estado.ATERRIZANDO);
                Thread.sleep(2000 + new Random().nextInt(6000));
                torreControl.notificarLiberacionPista(this);
                cambiarEstado(Estado.EN_TERMINAL);
                haAterrizado = true;
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public boolean haFinalizado() {
        return haAterrizado && haDespegado;
    }

    public void reducirCombustible() {
        if (estado == Estado.EN_VUELO || estado == Estado.ESPERANDO) {
            if (combustibleRestante > 0) {
                combustibleRestante -= consumoLitrosPorHoraCombustible;
                if (combustibleRestante < 0) {
                    combustibleRestante = 0.0;
                }
                combustibleRestante = Math.round(combustibleRestante * 100.0) / 100.0;
            }
        }
    }

}
