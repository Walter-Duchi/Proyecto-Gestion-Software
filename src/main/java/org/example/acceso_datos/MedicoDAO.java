package org.example.acceso_datos;

import org.example.acceso_datos.util.DatabaseConnector;
import org.example.modelo.Medico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO {

    /**
     * --- RF-MÉDICOS-001: Registro de información médica ---
     * Registra un nuevo médico en la base de datos.
     * El historial de cambios se maneja a través de las columnas de timestamp.
     * La auditoría del usuario que realiza el cambio debería ser gestionada en la capa de servicio.
     *
     * @param medico El objeto Medico con la información a registrar.
     * @return El ID del médico generado por la base de datos.
     * @throws SQLException Si ocurre un error de acceso a la base de datos, como un email duplicado.
     */
    public int registrarMedico(Medico medico) throws SQLException {
        String sql = "INSERT INTO medicos (nombre, apellidos, especialidad, telefono, email, tarifa_cita, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;
        int idMedicoGenerado = -1;

        try {
            conn = DatabaseConnector.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, medico.getNombre());
            pstmt.setString(2, medico.getApellidos());
            pstmt.setString(3, medico.getEspecialidad());
            pstmt.setString(4, medico.getTelefono());
            pstmt.setString(5, medico.getEmail());
            pstmt.setBigDecimal(6, medico.getTarifaCita());
            pstmt.setBoolean(7, true); // Por defecto, un médico nuevo está activo.

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idMedicoGenerado = generatedKeys.getInt(1);
                    medico.setId(idMedicoGenerado); // Actualiza el ID en el objeto modelo
                }
            } else {
                throw new SQLException("Error al registrar al médico, no se crearon filas.");
            }
            return idMedicoGenerado;

        } catch (SQLException e) {
            System.err.println("Error SQL al registrar médico: " + e.getMessage());
            // Si el error es por una restricción UNIQUE (ej. email duplicado)
            if ("23000".equals(e.getSQLState())) {
                throw new SQLException("El correo electrónico '" + medico.getEmail() + "' ya está en uso.", e.getSQLState(), e);
            }
            throw e;
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException e) { /* Ignored */ }
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { /* Ignored */ }
            if (conn != null) try { conn.close(); } catch (SQLException e) { /* Ignored */ }
        }
    }

    /**
     * --- RF-MÉDICOS-002: Consultar médico por identificación, nombre, especialidad ---
     * Busca médicos que coincidan con los criterios de búsqueda. Permite búsquedas parciales.
     * Solo devuelve médicos activos.
     *
     * @param criterio El término de búsqueda (ID, parte del nombre/apellido o especialidad).
     * @return Una lista de objetos Medico que coinciden con la búsqueda.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public List<Medico> buscarMedicos(String criterio) throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        // El query busca en id, nombre, apellidos o especialidad y solo trae médicos activos.
        String sql = "SELECT * FROM medicos WHERE " +
                "(id = ? OR nombre LIKE ? OR apellidos LIKE ? OR especialidad LIKE ?) " +
                "AND activo = TRUE ORDER BY apellidos, nombre";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Para el ID, intentamos convertir el criterio a número. Si falla, no es un ID válido.
            int idCriterio = -1;
            try {
                idCriterio = Integer.parseInt(criterio);
            } catch (NumberFormatException e) {
                // No es un número, así que no puede ser un ID.
            }

            String criterioLike = "%" + criterio + "%";
            pstmt.setInt(1, idCriterio);
            pstmt.setString(2, criterioLike);
            pstmt.setString(3, criterioLike);
            pstmt.setString(4, criterioLike);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    medicos.add(mapResultSetToMedico(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar médicos: " + e.getMessage());
            throw e;
        }
        return medicos;
    }

    /**
     * --- RF-MÉDICOS-003: Modificación de datos del médico ---
     * Actualiza la información de un médico existente.
     * No permite modificar el ID. La fecha de modificación se actualiza automáticamente por la BD.
     *
     * @param medico El objeto Medico con los datos actualizados. El ID debe ser válido.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException Si ocurre un error de acceso a la base de datos.
     */
    public boolean modificarMedico(Medico medico) throws SQLException {
        String sql = "UPDATE medicos SET nombre = ?, apellidos = ?, especialidad = ?, telefono = ?, email = ?, tarifa_cita = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, medico.getNombre());
            pstmt.setString(2, medico.getApellidos());
            pstmt.setString(3, medico.getEspecialidad());
            pstmt.setString(4, medico.getTelefono());
            pstmt.setString(5, medico.getEmail());
            pstmt.setBigDecimal(6, medico.getTarifaCita());
            pstmt.setInt(7, medico.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al modificar médico: " + e.getMessage());
            if ("23000".equals(e.getSQLState())) {
                throw new SQLException("El correo electrónico '" + medico.getEmail() + "' ya está en uso por otro médico.", e.getSQLState(), e);
            }
            throw e;
        }
    }

    /**
     * --- RF-MÉDICOS-004: Eliminación de médico (Lógica) ---
     * Realiza una eliminación lógica de un médico, cambiando su estado a inactivo.
     *
     * @param idMedico El ID del médico a desactivar.
     * @return true si la desactivación fue exitosa, false en caso contrario.
     * @throws SQLException Si hay un error de BD o si no se puede eliminar por tener dependencias.
     */
    public boolean eliminarMedicoLogico(int idMedico) throws SQLException {
        // Primero, verificar si el médico tiene citas activas o diagnósticos.
        if (tieneDependenciasActivas(idMedico)) {
            // Lanza una excepción específica para que la capa de servicio pueda manejarla.
            throw new SQLException("No se puede eliminar el médico con ID " + idMedico + " porque tiene citas activas o diagnósticos asignados.", "23000");
        }

        String sql = "UPDATE medicos SET activo = FALSE WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idMedico);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar lógicamente al médico: " + e.getMessage());
            throw e;
        }
    }

    /**
     * --- RF-MÉDICOS-004 (Helper) ---
     * Verifica si un médico tiene citas en estado 'Agendada' o 'Confirmada',
     * o si tiene algún diagnóstico asociado.
     * La tabla de la BD tiene restricciones (ON DELETE RESTRICT), por lo que un DELETE fallaría,
     * pero para la eliminación lógica, la validación debe hacerse manualmente.
     *
     * @param idMedico El ID del médico a verificar.
     * @return true si existen dependencias, false de lo contrario.
     * @throws SQLException si ocurre un error de BD.
     */
    private boolean tieneDependenciasActivas(int idMedico) throws SQLException {
        // Verificación de citas activas ('Agendada', 'Confirmada')
        String sqlCitas = "SELECT COUNT(*) FROM citas WHERE id_medico = ? AND estado IN ('Agendada', 'Confirmada')";
        // Verificación de diagnósticos (la existencia de cualquier diagnóstico es una dependencia)
        String sqlDiagnosticos = "SELECT COUNT(*) FROM diagnosticos WHERE id_medico = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmtCitas = conn.prepareStatement(sqlCitas);
             PreparedStatement pstmtDiagnosticos = conn.prepareStatement(sqlDiagnosticos)) {

            // Chequear citas
            pstmtCitas.setInt(1, idMedico);
            try (ResultSet rs = pstmtCitas.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Tiene citas activas
                }
            }

            // Chequear diagnósticos
            pstmtDiagnosticos.setInt(1, idMedico);
            try (ResultSet rs = pstmtDiagnosticos.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; // Tiene diagnósticos asociados
                }
            }
        }
        return false; // No tiene dependencias
    }


    /**
     * Método de utilidad para mapear un ResultSet a un objeto Medico.
     *
     * @param rs El ResultSet de una consulta a la tabla `medicos`.
     * @return un objeto Medico completamente populado.
     * @throws SQLException Si hay un error al leer el ResultSet.
     */
    private Medico mapResultSetToMedico(ResultSet rs) throws SQLException {
        Medico medico = new Medico();
        medico.setId(rs.getInt("id"));
        medico.setNombre(rs.getString("nombre"));
        medico.setApellidos(rs.getString("apellidos"));
        medico.setEspecialidad(rs.getString("especialidad"));
        medico.setTelefono(rs.getString("telefono"));
        medico.setEmail(rs.getString("email"));
        medico.setTarifaCita(rs.getBigDecimal("tarifa_cita"));
        medico.setActivo(rs.getBoolean("activo"));

        Timestamp fechaRegistroTS = rs.getTimestamp("fecha_registro");
        if (fechaRegistroTS != null) {
            medico.setFechaRegistro(fechaRegistroTS.toLocalDateTime());
        }

        Timestamp fechaModificacionTS = rs.getTimestamp("fecha_modificacion");
        if (fechaModificacionTS != null) {
            medico.setFechaModificacion(fechaModificacionTS.toLocalDateTime());
        }
        return medico;
    }
}