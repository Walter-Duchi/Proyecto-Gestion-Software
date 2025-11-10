// src/test/java/org/example/servicio/ServicioDiagnosticoTest.java

package org.example.servicio;

import org.example.acceso_datos.DiagnosticoDAOStub;
import org.example.modelo.Diagnostico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServicioDiagnosticoTest {

    private ServicioDiagnostico servicioDiagnostico;
    private DiagnosticoDAOStub daoStub;
    private Diagnostico diagnosticoValido;

    @BeforeEach
    void setUp() {
        // 1. Creamos nuestra implementación falsa (Stub)
        daoStub = new DiagnosticoDAOStub();

        // 2. Inyectamos el Stub en el servicio que vamos a probar
        servicioDiagnostico = new ServicioDiagnostico(daoStub);

        // 3. Preparamos un objeto de diagnóstico base
        diagnosticoValido = new Diagnostico();
        diagnosticoValido.setNotasDiagnostico("Notas válidas.");
        diagnosticoValido.setFechaDiagnostico(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si la cita no existe")
    void registrarDiagnostico_CitaNoEncontrada_LanzaExcepcion() {
        // Nuestro Stub devolverá null para este ID
        int idInexistente = DiagnosticoDAOStub.ID_CITA_INEXISTENTE;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDiagnostico.registrarDiagnosticoServicio(diagnosticoValido, idInexistente);
        });

        assertEquals("La cita con ID " + idInexistente + " no fue encontrada.", exception.getMessage());
        assertFalse(daoStub.fueLlamadoRegistrarDiagnostico(), "El método de registro final no debió ser llamado.");
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException si la cita está cancelada")
    void registrarDiagnostico_CitaCancelada_LanzaExcepcion() {
        // Nuestro Stub devolverá "Cancelada" para este ID
        int idCancelada = DiagnosticoDAOStub.ID_CITA_CANCELADA;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDiagnostico.registrarDiagnosticoServicio(diagnosticoValido, idCancelada);
        });

        assertEquals("No se puede registrar un diagnóstico para una cita cancelada (ID: " + idCancelada + ").", exception.getMessage());
        assertFalse(daoStub.fueLlamadoRegistrarDiagnostico());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Debe lanzar IllegalArgumentException si las notas están vacías o en blanco")
    void registrarDiagnostico_NotasInvalidas_LanzaExcepcion(String notasInvalidas) {
        diagnosticoValido.setNotasDiagnostico(notasInvalidas);
        int idValida = DiagnosticoDAOStub.ID_CITA_VALIDA;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            servicioDiagnostico.registrarDiagnosticoServicio(diagnosticoValido, idValida);
        });

        assertEquals("Las notas del diagnóstico son obligatorias.", exception.getMessage());
        assertFalse(daoStub.fueLlamadoRegistrarDiagnostico());
    }

    @Test
    @DisplayName("Debe registrar exitosamente y establecer la fecha actual si es nula")
    void registrarDiagnostico_FechaNula_EstableceFechaYRegistra() throws SQLException {
        diagnosticoValido.setFechaDiagnostico(null); // Probamos el camino donde la fecha es nula
        int idValida = DiagnosticoDAOStub.ID_CITA_VALIDA;

        int nuevoId = servicioDiagnostico.registrarDiagnosticoServicio(diagnosticoValido, idValida);

        assertEquals(DiagnosticoDAOStub.ID_DIAGNOSTICO_GENERADO, nuevoId);
        assertNotNull(diagnosticoValido.getFechaDiagnostico(), "La fecha debió ser establecida.");
        assertTrue(daoStub.fueLlamadoRegistrarDiagnostico(), "El método de registro debió ser llamado.");
    }

    @Test
    @DisplayName("Debe registrar exitosamente con todos los datos válidos")
    void registrarDiagnostico_DatosCompletos_RegistraExitosamente() throws SQLException {
        LocalDateTime fechaOriginal = LocalDateTime.of(2025, 1, 1, 12, 0);
        diagnosticoValido.setFechaDiagnostico(fechaOriginal);
        int idValida = DiagnosticoDAOStub.ID_CITA_VALIDA;

        int nuevoId = servicioDiagnostico.registrarDiagnosticoServicio(diagnosticoValido, idValida);

        assertEquals(DiagnosticoDAOStub.ID_DIAGNOSTICO_GENERADO, nuevoId);
        assertEquals(fechaOriginal, diagnosticoValido.getFechaDiagnostico(), "La fecha original no debió ser modificada.");
        assertTrue(daoStub.fueLlamadoRegistrarDiagnostico());
    }
}