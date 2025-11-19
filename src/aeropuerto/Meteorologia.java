package aeropuerto;

public enum Meteorologia {
    DESPEJADO(0), NIEBLA(1) ,VIENTO(2), TEMPESTAD(3);

    private final int pistasCerradas;

    Meteorologia(int pistasCerradas) {
        this.pistasCerradas = pistasCerradas;
    }

    public int getPistasCerradas() {
        return pistasCerradas;
    }
}
