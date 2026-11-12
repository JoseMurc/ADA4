/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Juca
 */
package gestor.prestamos.modelo;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa el intervalo de fechas para el periodo de un prestamo.
 * Tecnica 8: Introduce Parameter Object para agrupar las fechas que viajan juntas.
 */
public final class RangoFechas {

    private final LocalDate inicio;
    private final LocalDate limite;

    /**
     * Crea un rango de fechas validando que sean correctas.
     *
     * @param inicio fecha de inicio del prestamo
     * @param limite fecha maxima de devolucion
     */
    public RangoFechas(LocalDate inicio, LocalDate limite) {
        if (inicio == null || limite == null) {
            throw new IllegalArgumentException("Las fechas de inicio y limite son obligatorias");
        }
        if (limite.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha limite no puede ser anterior al prestamo");
        }
        this.inicio = inicio;
        this.limite = limite;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public LocalDate getLimite() {
        return limite;
    }
}