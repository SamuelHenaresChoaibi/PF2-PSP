package aeropuerto;

import utils.Estados;

import java.util.Random;

public class Avion implements Runnable{
    private int id;
    private Estados estados;
    private TorreControl torreControl;
    private boolean haAterrizado = false;
    private boolean haDespegado = false;

    public Avion(int id, TorreControl torreControl) {
        this.id = id;
        this.torreControl = torreControl;
        this.estados = Estados.EN_VUELO;
    }

    public int getId() {
        return id;
    }

    public Estados getEstados() {
        return estados;
    }

    public boolean isHaAterrizado() {
        return haAterrizado;
    }

    public boolean isHaDespegado() {
        return haDespegado;
    }

    public TorreControl getTorreControl() {
        return torreControl;
    }

    public void cambiarEstado(Estados estado) {
        this.estados = estado;
    }

    @Override
    public void run() {
        try {
            //Aterrizaje
            cambiarEstado(Estados.ESPERANDO);
            torreControl.solicitarAterrizar(this);

            cambiarEstado(Estados.ATERRIZANDO);
            Thread.sleep(new Random().nextInt(1000,10000));

            torreControl.notificarLiberacionPista(this);
            cambiarEstado(Estados.EN_TERMINAL);
            haAterrizado = true;

            Thread.sleep(3000);

            //Despegue
            cambiarEstado(Estados.ESPERANDO);
            torreControl.solicitarDespegar(this);

            cambiarEstado(Estados.DESPEGANDO);
            Thread.sleep(new Random().nextInt(1000,10000));

            torreControl.notificarLiberacionPista(this);
            cambiarEstado(Estados.EN_VUELO);
            haDespegado = true;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean haFinalizado() {
        return haAterrizado && haDespegado;
    }

}
