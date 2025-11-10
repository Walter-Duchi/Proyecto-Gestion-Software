package org.example.servicio;

import org.example.acceso_datos.MedicoDAO;
import org.example.modelo.Medico;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Gestiona la lógica de negocio para las operaciones relacionadas con los médicos.
 * Se encarga de las validaciones y de coordinar las operaciones con la capa de acceso a datos (DAO).
 */
public class ServicioMedico {

    private final MedicoDAO medicoDAO;

    // Patrón para una validación simple de email.
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public ServicioMedico() {
        this.medicoDAO = new MedicoDAO();
    }

    /**
     * RF-MÉDICOS-001: Registra un nuevo médico tras validar los datos.
     *
     * @param medico El objeto Medico a registrar.
     * @return El ID del médico generado.
     * @throws SQLException Si ocurre un error en la base de datos (ej. email duplicado).
     * @throws IllegalArgumentException Si los datos del médico no son válidos.
     */
    public int registrarMedicoServicio(Medico medico) throws SQLException {
        validarDatosMedico(medico);

        // La lógica de auditoría (quién y cuándo) se gestionaría aquí,
        // pasando quizás un objeto de sesión de usuario.
        // Por simplicidad, el DAO solo maneja las columnas de fecha.

        return medicoDAO.registrarMedico(medico);
    }

    /**
     * RF-MÉDICOS-002: Busca médicos según un criterio.
     *
     * @param criterio El término de búsqueda.
     * @return Una lista de médicos que coinciden con el criterio.
     * @throws SQLException Si ocurre un error en la base de datos.
     * @throws IllegalArgumentException si el criterio de búsqueda está vacío.
     */
    public List<Medico> buscarMedicosServicio(String criterio) throws SQLException {
        if (criterio == null || criterio.isBlank()) {
            throw new IllegalArgumentException("El criterio de búsqueda no puede estar vacío.");
        }
        return medicoDAO.buscarMedicos(criterio);
    }

    /**
     * RF-MÉDICOS-003: Modifica los datos de un médico existente.
     *
     * @param medico El objeto Medico con los datos actualizados.
     * @return true si la modificación fue exitosa.
     * @throws SQLException Si ocurre un error en la base de datos.
     * @throws IllegalArgumentException Si los datos a modificar no son válidos.
     */
    public boolean modificarMedicoServicio(Medico medico) throws SQLException {
        if (medico.getId() <= 0) {
            throw new IllegalArgumentException("El ID del médico para modificar no es válido.");
        }
        validarDatosMedico(medico);

        // Aquí también iría la lógica de auditoría.
        return medicoDAO.modificarMedico(medico);
    }

    /**
     * RF-MÉDICOS-004: Elimina (desactiva) lógicamente a un médico.
     *
     * @param idMedico El ID del médico a eliminar.
     * @return true si la eliminación fue exitosa.
     * @throws SQLException Si el médico tiene dependencias activas o si ocurre otro error de BD.
     * @throws IllegalArgumentException Si el ID del médico no es válido.
     */
    public boolean eliminarMedicoLogicoServicio(int idMedico) throws SQLException {
        if (idMedico <= 0) {
            throw new IllegalArgumentException("El ID del médico a eliminar no es válido.");
        }

        // El DAO ya contiene la lógica para verificar dependencias (tieneDependenciasActivas),
        // y lanzará una SQLException si no se puede eliminar. El servicio simplemente la propaga.
        return medicoDAO.eliminarMedicoLogico(idMedico);
    }

    /**
     * Validador centralizado para los datos de un objeto Médico.
     *
     * @param medico El objeto a validar.
     * @throws IllegalArgumentException si algún campo obligatorio está vacío o es inválido.
     */
    private void validarDatosMedico(Medico medico) throws IllegalArgumentException {
        if (medico.getNombre() == null || medico.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del médico es obligatorio.");
        }
        if (medico.getApellidos() == null || medico.getApellidos().isBlank()) {
            throw new IllegalArgumentException("Los apellidos del médico son obligatorios.");
        }
        if (medico.getEspecialidad() == null || medico.getEspecialidad().isBlank()) {
            throw new IllegalArgumentException("La especialidad es obligatoria.");
        }
        if (medico.getEmail() == null || !EMAIL_PATTERN.matcher(medico.getEmail()).matches()) {
            throw new IllegalArgumentException("El formato del correo electrónico es inválido.");
        }
        if (medico.getTarifaCita() == null || medico.getTarifaCita().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La tarifa de la cita no puede ser negativa.");
        }
    }
}