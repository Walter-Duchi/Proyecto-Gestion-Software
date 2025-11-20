package org.example.servicio;

import org.example.modelo.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
    }

    @Test
    @DisplayName("El constructor por defecto debe establecer activo=true")
    void constructorPorDefecto_ActivoTrue() {
        assertTrue(paciente.isActivo(), "Un paciente nuevo debe iniciar como activo.");
    }

    @Test
    @DisplayName("Debe permitir asignar y obtener los valores con getters y setters")
    void gettersSetters_DebenFuncionar() {
        paciente.setId(10);
        paciente.setNombre("Carlos");
        paciente.setApellidos("Ruiz");
        paciente.setFechaNacimiento(LocalDate.of(1999, 5, 20));
        paciente.setDniId("1234567890");
        paciente.setTelefono("0999999999");
        paciente.setEmail("correo@test.com");
        paciente.setDireccion("Calle Falsa 123");
        paciente.setActivo(false);
        LocalDateTime fechaReg = LocalDateTime.now();
        LocalDateTime fechaMod = LocalDateTime.now();
        paciente.setFechaRegistro(fechaReg);
        paciente.setFechaModificacion(fechaMod);

        assertEquals(10, paciente.getId());
        assertEquals("Carlos", paciente.getNombre());
        assertEquals("Ruiz", paciente.getApellidos());
        assertEquals(LocalDate.of(1999, 5, 20), paciente.getFechaNacimiento());
        assertEquals("1234567890", paciente.getDniId());
        assertEquals("0999999999", paciente.getTelefono());
        assertEquals("correo@test.com", paciente.getEmail());
        assertEquals("Calle Falsa 123", paciente.getDireccion());
        assertFalse(paciente.isActivo());
        assertEquals(fechaReg, paciente.getFechaRegistro());
        assertEquals(fechaMod, paciente.getFechaModificacion());
    }

    @Test
    @DisplayName("Constructor completo debe inicializar todos los valores correctamente")
    void constructorCompleto_AsignacionCorrecta() {
        Paciente p = new Paciente(
                "Ana",
                "Pérez",
                LocalDate.of(1990, 10, 10),
                "ABC123",
                "0987654321",
                "ana@test.com",
                "Av. Central"
        );

        assertEquals("Ana", p.getNombre());
        assertEquals("Pérez", p.getApellidos());
        assertEquals(LocalDate.of(1990, 10, 10), p.getFechaNacimiento());
        assertEquals("ABC123", p.getDniId());
        assertEquals("0987654321", p.getTelefono());
        assertEquals("ana@test.com", p.getEmail());
        assertEquals("Av. Central", p.getDireccion());
        assertTrue(p.isActivo(), "El constructor debe dejar activo=true por defecto.");
    }

    @Test
    @DisplayName("toString debe contener el id, nombre, apellidos y dniId")
    void toString_DebeContenerCamposClave() {
        paciente.setId(1);
        paciente.setNombre("Juan");
        paciente.setApellidos("Lopez");
        paciente.setDniId("XYZ789");

        String texto = paciente.toString();

        assertTrue(texto.contains("Juan"));
        assertTrue(texto.contains("Lopez"));
        assertTrue(texto.contains("XYZ789"));
        assertTrue(texto.contains("1"));
    }
}
