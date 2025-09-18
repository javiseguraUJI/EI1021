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
                    // POR IMPLEMENTAR
                	gestor.guardaDatos();
                }

                case 1 -> { // Listar los paquetes enviados por el cliente
                    // POR IMPLEMENTAR
                	JSONArray jsonArrayReservas = gestor.listaReservasUsuario(codUsuario);
                	int i = 1;
                	for (Object o : jsonArrayReservas) {
                		JSONObject jsonReserva = (JSONObject) o;
                		System.out.println(i + "- Actividad: " + jsonReserva.get("actividad") + " Código: " + jsonReserva.get("codReserva")
                		+ "\nEl día " + jsonReserva.get("dia") + " a las " + jsonReserva.get("hora") + ".");
                		i++;
                	}
                }

                case 2 -> { // Listar los plazas disponibles de una actividad
                    // POR IMPLEMENTAR
                	String nombreActividad = pedirNombreActividad(teclado);
                	JSONArray jsonArraySesiones= gestor.listaPlazasDisponibles(nombreActividad);
                	int i = 1;
                	for (Object o : jsonArraySesiones) {
                		JSONObject jsonSesion = (JSONObject) o;
                		System.out.println(i + "- Día " + jsonSesion.get("dia") + " a las " + jsonSesion.get("hora")
                		+ ". Plazas: " + jsonSesion.get("plazas"));
                		i++;
                	}
                }

                case 3 -> { // Hacer una reserva
                    // POR IMPLEMENTAR
                    String nombreActividad = pedirNombreActividad(teclado);
                    DiaSemana dia = DiaSemana.leerDia(teclado);
                    long hora = pedirHora(teclado);
                	JSONObject jsonReserva = gestor.hazReserva(codUsuario, nombreActividad, dia, hora);
                	if (jsonReserva.isEmpty()) {
                		System.out.println("No se ha podido realizar la reserva, información introducida o sin plazas.");
                	} else {
                		System.out.println("Reserva realizada con éxito");
                	}
                }
                case 4 -> { // Cambiar de día y hora una reserva


                    // POR IMPLEMENTAR


                }
                case 5 -> { // Cancelar una reserva
                	gestor.cancelaReserva(codUsuario, 0);
                    // POR IMPLEMENTAR



                }

            } // fin switch

        } while (opcion != 0);

    } // fin de main

    private static String pedirNombreActividad(Scanner teclado) {
    	return "";
    }
    
    private static long pedirHora(Scanner teclado) {
    	System.out.println("A que hora quieres reservar: ");
    	long hora  = teclado.nextLong();
    	do {
    		System.out.println("Dame una hora correcta: ");
    		hora  = teclado.nextLong();
    	} while (hora < 0 || 23 < hora);
    	return hora;
    }
    
  
    
} // fin class
