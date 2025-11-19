import aeropuerto.Aeropuerto;
import aeropuerto.Avion;
import aeropuerto.TorreControl;
import aeropuerto.Estado;
import utils.HerramientasAuxiliares;

void main() {

    // Leer parámetros del usuario
    Scanner scan = new Scanner(System.in);
    int numAviones = 0, numPistas = 0;
    List<Avion> aviones = new ArrayList<>();
    List<Estado> estadosIniciales = new ArrayList<>();
    TorreControl torreControl;
    Aeropuerto aeropuerto;

    // Pedir número de aviones (mínimo 10)
    while (numAviones < 10) {
        System.out.print("Número de aviones (>=10): ");
        numAviones = scan.nextInt();
    }

    // Pedir número de pistas (mínimo 2)
    while (numPistas < 2) {
        System.out.print("\nNúmero de pistas (>=2): ");
        numPistas = scan.nextInt();
    }

    // Crear torre de control
    torreControl = new TorreControl(numPistas);

    // Crear aviones, guardar estado inicial y lanzar hilos
    for (int i = 0; i < numAviones; i++) {
        aviones.add(new Avion(HerramientasAuxiliares.crearIdRandomAvion(), torreControl));
        estadosIniciales.add(aviones.get(i).getEstado());
        new Thread(aviones.get(i)).start(); // Ejecutar cada avión en un hilo
    }

    // Crear aeropuerto y mostrar estados iniciales
    aeropuerto = new Aeropuerto(aviones);
    HerramientasAuxiliares.mostrarEstadosIniciales(aviones, estadosIniciales, torreControl);

    // Guardar tiempo de inicio
    double incio = System.currentTimeMillis();

    try {
        int i = 0;

        // Bucle principal hasta que todos los aviones terminen
        while (!aeropuerto.todosFinalizados()) {

            aeropuerto.mostrarEstados(torreControl); // Mostrar estados actualizados
            Thread.sleep(1000); // Esperar 1 segundo

            for (Avion avion : aeropuerto.getAviones()) {

                // Reducir combustible si está esperando y hay pistas libres
                if (avion.getEstado().equals(Estado.ESPERANDO)
                        && torreControl.getNumPistasActuales() > 0
                        && i % 2 == 0) {
                    avion.reducirCombustible();
                }

                // Cambiar clima si no hay pistas operativas
                if (torreControl.getNumPistasActuales() == 0) {
                    torreControl.cambiarMeteorologia();
                }
            }

            System.out.println();

            // Cambiar clima cada 5 ciclos
            if (i == 5) {
                torreControl.cambiarMeteorologia();
                i = 0;
            }
            i++;
        }

        aeropuerto.mostrarEstados(torreControl); // Mostrar estados finales

    } catch (InterruptedException e) {
        System.out.println("Error al mostrar estadosIniciales: " + e.getMessage());
    }

    // Guardar tiempo final
    double fin = System.currentTimeMillis();

    // Log final en archivo + consola
    HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
    HerramientasAuxiliares.imprimirYGuardar("                                        ⚡RESULTADO FINAL⚡                                     ");
    HerramientasAuxiliares.imprimirYGuardar("════════════════════════════════════════════════════════════════════════════════════════════");
    HerramientasAuxiliares.imprimirYGuardar("🚦 Operaciones de aterrizaje y despegue realizadas: " + aviones.size() * 2);
    HerramientasAuxiliares.imprimirYGuardar("⏱️ Tiempo total de simulación: " + (fin - incio) / 1000 + " segundos");
    HerramientasAuxiliares.imprimirYGuardar("🌟 ¡Simulación finalizada correctamente! 🌟");

    scan.close(); // Cerrar scanner
}
