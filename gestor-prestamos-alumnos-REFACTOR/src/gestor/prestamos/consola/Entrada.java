package gestor.prestamos.consola;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lectura de datos desde la consola con validación. Repite la petición hasta
 * obtener un valor correcto.
 */
public final class Entrada {

    /*
     * Patron AAAA-MM-DD. Lo agregamos para la tecnica 7 (Replace Exception with Test):
     * antes la unica forma de saber si el texto era fecha era intentar parsearlo y
     * atrapar el DateTimeParseException, ahora se revisa primero con este patron.
     */
    private static final Pattern PATRON_FECHA = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");

    private final Scanner scanner;

    /**
     * Crea una entrada que lee del escáner indicado.
     *
     * @param scanner fuente de las líneas introducidas por el usuario; no puede ser nula
     */
    public Entrada(Scanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "scanner");
    }

    /**
     * Pide un texto no vacío.
     *
     * @param mensaje texto que se muestra antes de leer
     * @return el texto introducido, sin espacios sobrantes
     */
    public String pedirTexto(String mensaje) {
        while (true) {
            String valor = leer(mensaje).trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("  El campo no puede estar vacío.");
        }
    }

    /**
     * Pide un número entero que debe pertenecer a un conjunto de opciones válidas.
     *
     * @param mensaje texto que se muestra antes de leer
     * @param validas números aceptados
     * @return el número elegido
     */
    public int pedirOpcion(String mensaje, Collection<Integer> validas) {
        while (true) {
            String texto = leer(mensaje).trim();
            try {
                int numero = Integer.parseInt(texto);
                if (validas.contains(numero)) {
                    return numero;
                }
            } catch (NumberFormatException e) {
                // El texto no es un número: se informa abajo y se vuelve a pedir.
            }
            System.out.println("  Opción no válida.");
        }
    }

    /**
     * Pide una fecha en formato {@code AAAA-MM-DD}. Si el usuario pulsa Enter y hay un valor
     * por defecto, se devuelve ese valor.
     *
     * @param mensaje texto que se muestra antes de leer
     * @param valorPorDefecto fecha que se usa al pulsar Enter, o {@code null} si no hay
     * @return la fecha introducida o el valor por defecto
     */
    public LocalDate pedirFecha(String mensaje, LocalDate valorPorDefecto) {
        while (true) {
            String texto = leer(mensaje).trim();
            if (texto.isEmpty() && valorPorDefecto != null) {
                return valorPorDefecto;
            }
            /*
             * Tecnica 7: Replace Exception with Test.
             * El equipo analizo y vio que aqui el try/catch se usaba como si fuera un if,
             * porque una fecha mal escrita es algo que pasa normal y no un error raro del
             * programa. Lo mejor aca es meter la tecnica y preguntar antes con
             * esFechaValida(); si da true el parse ya no puede fallar, y si da false se
             * muestra el mismo mensaje de antes y se vuelve a pedir.
             */
            if (esFechaValida(texto)) {
                return LocalDate.parse(texto);
            }
            System.out.println("  Formato inválido. Usa AAAA-MM-DD.");
        }
    }

    /*
     * Prueba (test) que sustituye a la excepcion de pedirFecha(): revisa que el texto
     * tenga forma AAAA-MM-DD y que el mes y el dia existan de verdad (por ejemplo
     * 2026-02-30 o 2026-13-01 no valen). Se usa YearMonth para saber cuantos dias tiene
     * el mes y asi tomar en cuenta los anios bisiestos.
     */
    private static boolean esFechaValida(String texto) {
        Matcher partes = PATRON_FECHA.matcher(texto);
        if (!partes.matches()) {
            return false;
        }
        int anio = Integer.parseInt(partes.group(1));
        int mes = Integer.parseInt(partes.group(2));
        int dia = Integer.parseInt(partes.group(3));
        if (mes < 1 || mes > 12) {
            return false;
        }
        return dia >= 1 && dia <= YearMonth.of(anio, mes).lengthOfMonth();
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
}
