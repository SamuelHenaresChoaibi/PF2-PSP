import aeropuerto.Aeropuerto;
import aeropuerto.Avion;
import aeropuerto.TorreControl;
import utils.Estado;
import utils.HerramientasAuxiliares;

void main() {
    Scanner scan = new Scanner(System.in);
    int numAviones = 0, numPistas = 0;
    List<Avion> aviones = new ArrayList<>();
    List<Estado> estadosIniciales = new ArrayList<>();
    TorreControl torreControl;
    Aeropuerto aeropuerto;

    while (numAviones < 10) {
        System.out.print("Número de aviones (>=10): ");
        numAviones = scan.nextInt();
    }

    while (numPistas < 2) {
        System.out.print("\nNúmero de pistas (>=2): ");
        numPistas = scan.nextInt();
    }

    torreControl = new TorreControl(numPistas);

    for (int i = 0; i < numAviones; i++) {
        aviones.add(new Avion(HerramientasAuxiliares.crearIdRandomAvion(), torreControl));
        estadosIniciales.add(aviones.get(i).getEstado());
        new Thread(aviones.get(i)).start();
    }

    aeropuerto = new Aeropuerto(aviones);
    HerramientasAuxiliares.mostrarEstadosIniciales(aviones, estadosIniciales);
    double incio = System.currentTimeMillis();

    try{
        while (!aeropuerto.todosFinalizados()) {
            aeropuerto.mostrarEstados();
            Thread.sleep(1000);
            System.out.println();
        }
        aeropuerto.mostrarEstados();
    } catch (InterruptedException e) {
        System.out.println("Error al mostrar estadosIniciales: " + e.getMessage());
    }

    double fin = System.currentTimeMillis();

    System.out.println("\n══════════════════════════════════════════════");
    System.out.println("                RESULTADO FINAL               ");
    System.out.println("══════════════════════════════════════════════");
    System.out.println("Operaciones de aterrizaje y despegue realizadas: " + aviones.size() * 2);
    System.out.println("Tiempo total de simulación: " + (fin - incio) + " segundos");
    System.out.println("¡Simulación finalizada correctamente!");

    scan.close();



}