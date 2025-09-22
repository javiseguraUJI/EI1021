package reservas;

import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class UsuarioLocal {

    /**
     * Muestra el menu de opciones y lee repetidamente de teclado hasta obtener una opción válida
     *
     * @param teclado	stream para leer la opción elegida de teclado
     * @return			opción elegida
     */
    public static int menu(Scanner teclado) {
        int opcion;
        System.out.println("\n\n");
        System.out.println("=====================================================");
        System.out.println("============            MENU        =================");
        System.out.println("=====================================================");
        System.out.println("0. Salir");
        System.out.println("1. Listar las reservas");
        System.out.println("2. Listar plazas disponibles de una actividad");
        System.out.println("3. Hacer una reserva");
        System.out.println("4. Modificar una reserva");
        System.out.println("5. Cancelar una reserva");
        do {
            System.out.print("\nElige una opcion (0..5): ");
            opcion = teclado.nextInt();
        } while ( (opcion<0) || (opcion>5) );
        teclado.nextLine(); // Elimina retorno de carro del buffer de entrada
        return opcion;
    }

    
    /**
     * Programa principal. Muestra el menú repetidamente y atiende las peticiones del usuario.
     *
     * @param args	no se usan argumentos de entrada al programa principal
     */
    public static void main(String[] args)  {

        Scanner teclado = new Scanner(System.in);

        // Crea un gestor de reservas
        GestorReservas gestor = new GestorReservas();

        System.out.print("Introduce tu código de usuario: ");
        String codUsuario = teclado.nextLine();

        
        int opcion;
        do {
            opcion = menu(teclado);
            switch (opcion) {
                case 0 -> { // Guardar los datos en el fichero y salir del programa
                    // Llamamos a guardaDatos() y como opcion es igual a 0 se sale del while y finaliza el programa
                	gestor.guardaDatos();
                	System.out.println("Saliendo del programa...");
                	System.out.println("Adiós :)");
                }

                case 1 -> { // Listar los paquetes enviados por el cliente
                    // Obtenemos un JSONArray con los JSONObjects que representan las reservas del usuario
                	JSONArray jsonArrayReservas = gestor.listaReservasUsuario(codUsuario);

                	// Recorremos el JSONArray y mostramos por pantalla la información pedida
                	int i = 1;
                	for (Object o : jsonArrayReservas) {
                		JSONObject jsonReserva = (JSONObject) o;
                		System.out.println(i + "- Actividad: " + jsonReserva.get("actividad") + " Código: " + jsonReserva.get("codReserva")
                		+ "\nEl día " + jsonReserva.get("dia") + " a las " + jsonReserva.get("hora") + ".");
                		i++;
                	}
                	if (i == 1) System.out.println("No tienes reservas realizadas.");
                }

                case 2 -> { // Listar los plazas disponibles de una actividad
                	// Pedimos el nombre de la actividad de la cuál queremos obtener un array con todas sus sesiones disponibles
                	String nombreActividad = pedirNombreActividad(teclado);
                	JSONArray jsonArraySesiones= gestor.listaPlazasDisponibles(nombreActividad);
                	
                	// Recorremos el JSONArray devuelto con las JSONobjects que representan las sesiones mostrando por pantalla la información necesaria
                	int i = 1;
                	for (Object o : jsonArraySesiones) {
                		JSONObject jsonSesion = (JSONObject) o;
                		System.out.println(i + "- Día " + jsonSesion.get("dia") + " a las " + jsonSesion.get("hora")
                		+ ". Plazas: " + jsonSesion.get("plazas"));
                		i++;
                	}
                }

                case 3 -> { // Hacer una reserva !!!!!!! He puesto prints dentro de hazReserva, creo que se puede pero no lo sé seguro
                    // Pedimos la información necesaria para realizar la reserva
                	String nombreActividad = pedirNombreActividad(teclado);
                    DiaSemana dia = DiaSemana.leerDia(teclado);
                    long hora = pedirHora(teclado, "¿A qué hora quieres reservar?\n");
                    

                     // Intentamos realizar la reserva
                    JSONObject jsonReserva = gestor.hazReserva(codUsuario, nombreActividad, dia, hora);
                    	
                   	if (!jsonReserva.isEmpty()) {
                   		System.out.println("Reserva realizada con éxito. Código: " + jsonReserva.get("codReserva"));
                  	}
                }

                case 4 -> { // Cambiar de día y hora una reserva
                    // Pedimos la información necesaria para modificar la reserva
                	long codReserva = pedirCodReserva(teclado, "Introduce el código de la reserva que quieres modificar: ");
                	DiaSemana dia = DiaSemana.leerDia(teclado);
                	long hora = pedirHora(teclado, "¿A que hora cambiar tu reserva?\n");
                	
                	// Intentamos modificarla
                	JSONObject jsonReserva = gestor.modificaReserva(codUsuario, codReserva, dia, hora);
                	
                	if (jsonReserva == null) { // Si no se ha podido modificar, lo indicamos !!! Faltaría mostrar por qué no se ha podido, quizás se puede hacer dentro de GestorReservas
                		System.out.println("No se ha podido modificar la reserva.");
                	} else { // Si se ha podido modificar, lo indicamos y mostramos la información de la nueva reserva
                		System.out.println("Reserva modificada con éxito. Nueva reserva:\n");
                		System.out.println("Actividad: " + jsonReserva.get("actividad") + " Código: " + jsonReserva.get("codReserva")
                		+ "\nEl día " + jsonReserva.get("dia") + " a las " + jsonReserva.get("hora") + ".");
                	}
                }
                case 5 -> { // Cancelar una reserva
                    // Pedimos la información necesaria para eliminar la reserva
                	long codReserva = pedirCodReserva(teclado, "Introduce el código de la reserva que quieres eliminar: ");
                	
                	// Intentamos cancelarla
                	JSONObject jsonReserva = gestor.cancelaReserva(codUsuario, codReserva);
                	
                	if (jsonReserva.isEmpty()) { // Si no se ha podido reservar lo indicar !!! Mirar si podemos mostrar mejor la razón de por qué no se ha podido
                		System.out.println("No se ha podido eliminar, reserva no existente o no es tuya");
                	} else { // Si se ha podido eliminar, lo indicamos !!! no creo que sea necesario mostrar la información sobre la reserva eliminada, pero se puede hacer
                		System.out.println("Reserva eliminada con exito.");
                	}
                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

    private static String pedirNombreActividad(Scanner teclado) { // pide el nombre de la actividad por teclado
    	String actividad;
    	System.out.println("¿Qué actividad quieres reservar?");
   		actividad = teclado.nextLine();
    	return actividad;
    }
    
    private static long pedirHora(Scanner teclado, String pregunta) { // pide hora por teclado, param pregunta es para reutilizar la función en contextos diferentes
    	long hora;
    	System.out.println(pregunta);
    	do {
    		System.out.println("Dame una hora (0-23): ");
    		hora  = teclado.nextLong();
    	} while (hora < 0 || 23 < hora);
    	return hora;
    }
    
    private static long pedirCodReserva(Scanner teclado, String pregunta) { // pide codReserva por teclado, param pregunta es para reutilizar la función en contextos diferentes
    	long codReserva;
    	System.out.println(pregunta);
    	codReserva = teclado.nextLong();
    	teclado.nextLine();
    	return codReserva;
    }
    
} // fin class
