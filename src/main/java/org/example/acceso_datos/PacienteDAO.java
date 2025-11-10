package org.example.acceso_datos;

import org.example.modelo.Paciente;
import org.example.acceso_datos.util.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    // RF-PAC-001: Registrar nuevo paciente
    public boolean registrarPaciente(Paciente paciente) {
        String sql = "{CALL sp_paciente_registrar(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnector.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellidos());

            if (paciente.getFechaNacimiento() != null) {
                stmt.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
            } else {
                stmt.setNull(3, Types.DATE);
            }

            stmt.setString(4, paciente.getDniId());
            stmt.setString(5, paciente.getTelefono());
            stmt.setString(6, paciente.getEmail());
            stmt.setString(7, paciente.getDireccion());

            stmt.execute();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: DNI o Email ya existen en otro paciente activo.");
        } catch (SQLException e) {
            System.err.println("Error al registrar paciente: " + e.getMessage());
        }

        return false;
    }

    // RF-PAC-002: Consultar/Buscar pacientes por criterios
    public List<Paciente> buscarPacientes(String id, String nombre, String apellidos, String dniId) {
        List<Paciente> resultados = new ArrayList<>();
        String sql = "{CALL sp_paciente_consultar(?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnector.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            // Parámetros pueden ser nulos o vacíos
            stmt.setString(1, id != null && !id.isBlank() ? id : null);
            stmt.setString(2, nombre != null && !nombre.isBlank() ? nombre : null);
            stmt.setString(3, apellidos != null && !apellidos.isBlank() ? apellidos : null);
            stmt.setString(4, dniId != null && !dniId.isBlank() ? dniId : null);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellidos"));
                p.setDniId(rs.getString("dni_id"));
                p.setTelefono(rs.getString("telefono"));
                p.setEmail(rs.getString("email"));
                p.setDireccion(rs.getString("direccion"));
                p.setActivo(rs.getBoolean("activo"));

                Date fechaNac = rs.getDate("fecha_nacimiento");
                if (fechaNac != null) {
                    p.setFechaNacimiento(fechaNac.toLocalDate());
                }

                Timestamp fechaReg = rs.getTimestamp("fecha_registro");
                if (fechaReg != null) {
                    p.setFechaRegistro(fechaReg.toLocalDateTime());
                }

                Timestamp fechaMod = rs.getTimestamp("fecha_modificacion");
                if (fechaMod != null) {
                    p.setFechaModificacion(fechaMod.toLocalDateTime());
                }

                resultados.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar pacientes: " + e.getMessage());
        }

        return resultados;
    }

    // RF-PAC-003: Modificar datos del paciente
    public boolean modificarPaciente(Paciente paciente) {
        String sql = "{CALL sp_paciente_modificar(?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnector.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, paciente.getId());
            stmt.setString(2, paciente.getNombre());
            stmt.setString(3, paciente.getApellidos());

            if (paciente.getFechaNacimiento() != null) {
                stmt.setDate(4, Date.valueOf(paciente.getFechaNacimiento()));
            } else {
                stmt.setNull(4, Types.DATE);
            }

            stmt.setString(5, paciente.getDniId());
            stmt.setString(6, paciente.getTelefono());
            stmt.setString(7, paciente.getEmail());
            stmt.setString(8, paciente.getDireccion());

            stmt.execute();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Error: DNI o Email ya están en uso por otro paciente activo.");
        } catch (SQLException e) {
            System.err.println("Error al modificar paciente: " + e.getMessage());
        }

        return false;
    }

    // RF-PAC-004: Inactivar (eliminar lógicamente) paciente
    public boolean inactivarPaciente(int idPaciente) {
        String sql = "{CALL sp_paciente_inactivar(?)}";

        try (Connection conn = DatabaseConnector.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idPaciente);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al inactivar paciente: " + e.getMessage());
        }

        return false;
    }
}