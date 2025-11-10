package org.example.servicio;

import org.example.acceso_datos.PacienteDAO;
import org.example.modelo.Paciente;

import java.util.List;

public class ServicioPaciente {

    private final PacienteDAO pacienteDAO;

    public ServicioPaciente() {
        this.pacienteDAO = new PacienteDAO();
    }

    // RF-PAC-001: Registrar paciente
    public boolean registrarPaciente(Paciente paciente) {
        // Validaciones mínimas obligatorias
        if (paciente.getNombre() == null || paciente.getNombre().isBlank()) {
            System.err.println("Error: El nombre es obligatorio.");
            return false;
        }

        if (paciente.getApellidos() == null || paciente.getApellidos().isBlank()) {
            System.err.println("Error: Los apellidos son obligatorios.");
            return false;
        }

        // Puede agregarse validación de email y DNI aquí (opcional)
        return pacienteDAO.registrarPaciente(paciente);
    }

    // RF-PAC-002: Buscar pacientes con múltiples criterios
    public List<Paciente> buscarPacientes(String id, String nombre, String apellidos, String dni) {
        return pacienteDAO.buscarPacientes(id, nombre, apellidos, dni);
    }

    // RF-PAC-003: Modificar datos del paciente
    public boolean modificarPaciente(Paciente paciente) {
        if (paciente.getId() == null) {
            System.err.println("Error: El ID del paciente a modificar no puede ser nulo.");
            return false;
        }

        return pacienteDAO.modificarPaciente(paciente);
    }

    // RF-PAC-004: Inactivar paciente
    public boolean inactivarPaciente(int idPaciente) {
        if (idPaciente <= 0) {
            System.err.println("Error: ID de paciente inválido.");
            return false;
        }

        return pacienteDAO.inactivarPaciente(idPaciente);
    }
}