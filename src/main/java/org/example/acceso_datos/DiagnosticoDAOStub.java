package org.example.acceso_datos;

import org.example.modelo.Diagnostico;
import java.sql.SQLException;

/**
 * Esta es una implementación "Stub" de DiagnosticoDAO para usar en pruebas.
 * No se conecta a la base de datos; en su lugar, devuelve valores predefinidos
 * para simular diferentes escenarios de prueba.
 */
public class DiagnosticoDAOStub extends DiagnosticoDAO {

    public static final int ID_CITA_VALIDA = 15;
    public static final int ID_CITA_CANCELADA = 10;
    public static final int ID_CITA_INEXISTENTE = 999;
    public static final int ID_DIAGNOSTICO_GENERADO = 101;

    private boolean registrarLlamado = false;

    @Override
    public String getEstadoCita(int idCita) throws SQLException {
        if (idCita == ID_CITA_VALIDA) {
            return "Programada";
        }
        if (idCita == ID_CITA_CANCELADA) {
            return "Cancelada";
        }
        // Para cualquier otro ID, simulamos que no existe.
        return null;
    }

    @Override
    public int registrarDiagnostico(Diagnostico diagnostico, int idCita) throws SQLException {
        // Simulamos que el registro fue exitoso si la cita es válida
        if (idCita == ID_CITA_VALIDA) {
            this.registrarLlamado = true; // Guardamos un registro de que este método fue llamado
            return ID_DIAGNOSTICO_GENERADO;
        }
        return 0;
    }

    // Método auxiliar para nuestras pruebas para verificar si se llamó al registro
    public boolean fueLlamadoRegistrarDiagnostico() {
        return registrarLlamado;
    }
}