package org.example.presentacion;

import org.example.modelo.Medico;
import org.example.servicio.ServicioMedico;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class VentanaMedico extends JFrame {

    // Componentes de la UI
    private JPanel panelPrincipal;
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtEspecialidad;
    private JTextField txtTelefono;
    private JTextField txtEmail;
    private JTextField txtTarifa;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnLimpiar;

    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tablaMedicos;
    private DefaultTableModel modeloTabla;
    private JButton btnEliminar;

    // Servicios
    private final ServicioMedico servicioMedico;

    public VentanaMedico() {
        this.servicioMedico = new ServicioMedico();

        setTitle("Gestión de Médicos");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        layoutComponents();
        addEventListeners();

        // Estado inicial
        txtId.setEditable(false);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    private void initComponents() {
        // Panel de Formulario
        panelPrincipal = new JPanel();
        txtId = new JTextField(5);
        txtNombre = new JTextField(20);
        txtApellidos = new JTextField(20);
        txtEspecialidad = new JTextField(20);
        txtTelefono = new JTextField(15);
        txtEmail = new JTextField(20);
        txtTarifa = new JTextField(10);
        btnGuardar = new JButton("Guardar Nuevo (RF-001)");
        btnActualizar = new JButton("Actualizar Datos (RF-003)");
        btnLimpiar = new JButton("Limpiar Formulario");

        // Panel de Búsqueda y Resultados
        txtBuscar = new JTextField(25);
        btnBuscar = new JButton("Buscar Médico (RF-002)");
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Apellidos", "Especialidad", "Email", "Teléfono", "Tarifa"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        tablaMedicos = new JTable(modeloTabla);
        btnEliminar = new JButton("Eliminar Médico (Lógico) (RF-004)");
    }

    private void layoutComponents() {
        panelPrincipal.setLayout(new BorderLayout(10, 10));

        // --- Panel del Formulario (Norte) ---
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos del Médico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; panelFormulario.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; panelFormulario.add(txtId, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; panelFormulario.add(txtNombre, gbc);
        gbc.gridx = 2; gbc.gridy = 1; panelFormulario.add(new JLabel("Apellidos:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; panelFormulario.add(txtApellidos, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panelFormulario.add(new JLabel("Especialidad:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; panelFormulario.add(txtEspecialidad, gbc);
        gbc.gridx = 2; gbc.gridy = 2; panelFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; panelFormulario.add(txtTelefono, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; panelFormulario.add(txtEmail, gbc);
        gbc.gridx = 2; gbc.gridy = 3; panelFormulario.add(new JLabel("Tarifa Cita:"), gbc);
        gbc.gridx = 3; gbc.gridy = 3; panelFormulario.add(txtTarifa, gbc);

        // Panel de botones del formulario
        JPanel panelBotonesForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotonesForm.add(btnGuardar);
        panelBotonesForm.add(btnActualizar);
        panelBotonesForm.add(btnLimpiar);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; panelFormulario.add(panelBotonesForm, gbc);

        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);

        // --- Panel de Búsqueda y Tabla (Centro) ---
        JPanel panelBusqueda = new JPanel(new BorderLayout(10, 10));
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda y Resultados"));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.add(new JLabel("Buscar por ID, Nombre o Especialidad:"));
        panelFiltro.add(txtBuscar);
        panelFiltro.add(btnBuscar);

        panelBusqueda.add(panelFiltro, BorderLayout.NORTH);
        panelBusqueda.add(new JScrollPane(tablaMedicos), BorderLayout.CENTER);

        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAccionesTabla.add(btnEliminar);
        panelBusqueda.add(panelAccionesTabla, BorderLayout.SOUTH);

        panelPrincipal.add(panelBusqueda, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private void addEventListeners() {
        btnGuardar.addActionListener(e -> registrarMedico());
        btnActualizar.addActionListener(e -> modificarMedico());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnBuscar.addActionListener(e -> buscarMedicos());
        btnEliminar.addActionListener(e -> eliminarMedico());

        // Listener para la tabla, para cargar datos en el formulario al seleccionar una fila
        tablaMedicos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaMedicos.getSelectedRow() != -1) {
                cargarMedicoSeleccionadoEnFormulario();
            }
        });
    }

    private void registrarMedico() {
        try {
            Medico medico = new Medico();
            medico.setNombre(txtNombre.getText());
            medico.setApellidos(txtApellidos.getText());
            medico.setEspecialidad(txtEspecialidad.getText());
            medico.setTelefono(txtTelefono.getText());
            medico.setEmail(txtEmail.getText());
            medico.setTarifaCita(new BigDecimal(txtTarifa.getText()));

            int idGenerado = servicioMedico.registrarMedicoServicio(medico);
            JOptionPane.showMessageDialog(this, "Médico registrado con éxito. ID: " + idGenerado, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            buscarMedicos(); // Refrescar la tabla
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La tarifa debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarMedico() {
        try {
            Medico medico = new Medico();
            medico.setId(Integer.parseInt(txtId.getText()));
            medico.setNombre(txtNombre.getText());
            medico.setApellidos(txtApellidos.getText());
            medico.setEspecialidad(txtEspecialidad.getText());
            medico.setTelefono(txtTelefono.getText());
            medico.setEmail(txtEmail.getText());
            medico.setTarifaCita(new BigDecimal(txtTarifa.getText()));

            boolean exito = servicioMedico.modificarMedicoServicio(medico);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Datos del médico actualizados con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                buscarMedicos();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el médico.", "Fallo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID y la tarifa deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al modificar médico: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMedicos() {
        try {
            String criterio = txtBuscar.getText().trim();
            if (criterio.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un criterio de búsqueda.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Medico> medicos = servicioMedico.buscarMedicosServicio(criterio);
            modeloTabla.setRowCount(0); // Limpiar tabla

            if (medicos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron médicos que coincidan con el criterio.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Medico medico : medicos) {
                    modeloTabla.addRow(new Object[]{
                            medico.getId(),
                            medico.getNombre(),
                            medico.getApellidos(),
                            medico.getEspecialidad(),
                            medico.getEmail(),
                            medico.getTelefono(),
                            medico.getTarifaCita()
                    });
                }
            }
        } catch (IllegalArgumentException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar médicos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarMedico() {
        int filaSeleccionada = tablaMedicos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un médico de la tabla para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idMedico = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
        String nombreCompleto = modeloTabla.getValueAt(filaSeleccionada, 1) + " " + modeloTabla.getValueAt(filaSeleccionada, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar (desactivar) al médico: " + nombreCompleto + "?\n" +
                        "El médico no podrá ser asignado a nuevas citas.",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = servicioMedico.eliminarMedicoLogicoServicio(idMedico);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Médico eliminado (desactivado) con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    buscarMedicos();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el médico.", "Fallo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                // Captura la excepción específica lanzada cuando hay dependencias
                if ("23000".equals(ex.getSQLState())) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error de Dependencia", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error de base de datos al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarMedicoSeleccionadoEnFormulario() {
        int filaSeleccionada = tablaMedicos.getSelectedRow();
        if (filaSeleccionada != -1) {
            txtId.setText(modeloTabla.getValueAt(filaSeleccionada, 0).toString());
            txtNombre.setText(modeloTabla.getValueAt(filaSeleccionada, 1).toString());
            txtApellidos.setText(modeloTabla.getValueAt(filaSeleccionada, 2).toString());
            txtEspecialidad.setText(modeloTabla.getValueAt(filaSeleccionada, 3).toString());
            txtEmail.setText(modeloTabla.getValueAt(filaSeleccionada, 4).toString());
            txtTelefono.setText(modeloTabla.getValueAt(filaSeleccionada, 5).toString());
            txtTarifa.setText(modeloTabla.getValueAt(filaSeleccionada, 6).toString());

            // Habilitar/deshabilitar botones
            btnGuardar.setEnabled(false);
            btnActualizar.setEnabled(true);
            btnEliminar.setEnabled(true);
        }
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellidos.setText("");
        txtEspecialidad.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        txtTarifa.setText("");
        tablaMedicos.clearSelection();

        btnGuardar.setEnabled(true);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new VentanaMedico().setVisible(true);
        });
    }
}