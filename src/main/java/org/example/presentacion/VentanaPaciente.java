package org.example.presentacion;

import org.example.modelo.Paciente;
import org.example.servicio.ServicioPaciente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class VentanaPaciente extends JFrame {

    private final ServicioPaciente servicio;
    private JTable tablaPaciente;
    private DefaultTableModel modeloTabla;

    private JTextField txtIdBuscar, txtNombreBuscar, txtApellidosBuscar, txtDniBuscar;

    private JTextField txtIdModificar, txtNombre, txtApellidos, txtFechaNacimiento, txtDni, txtTelefono, txtEmail, txtDireccion;
    private JButton btnRegistrar, btnBuscar, btnModificar, btnInactivar;

    public VentanaPaciente() {
        super("Gestión de Pacientes");
        this.servicio = new ServicioPaciente();

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(crearPanelFormulario(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridLayout(5, 4, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Formulario de Paciente"));

        txtIdModificar = new JTextField(); txtIdModificar.setEnabled(false);
        txtNombre = new JTextField();
        txtApellidos = new JTextField();
        txtFechaNacimiento = new JTextField();
        txtDni = new JTextField();
        txtTelefono = new JTextField();
        txtEmail = new JTextField();
        txtDireccion = new JTextField();

        panel.add(new JLabel("ID:")); panel.add(txtIdModificar);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Apellidos:")); panel.add(txtApellidos);
        panel.add(new JLabel("Fecha Nac (YYYY-MM-DD):")); panel.add(txtFechaNacimiento);
        panel.add(new JLabel("DNI/ID:")); panel.add(txtDni);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTelefono);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Dirección:")); panel.add(txtDireccion);

        return panel;
    }

    private JScrollPane crearPanelTabla() {
        String[] columnas = {"ID", "Nombre", "Apellidos", "DNI", "Teléfono", "Email", "Activo"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPaciente = new JTable(modeloTabla);
        tablaPaciente.getSelectionModel().addListSelectionListener(e -> seleccionarPaciente());
        return new JScrollPane(tablaPaciente);
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JPanel panelBuscar = new JPanel();
        JPanel panelAcciones = new JPanel();

        txtIdBuscar = new JTextField(5);
        txtNombreBuscar = new JTextField(8);
        txtApellidosBuscar = new JTextField(8);
        txtDniBuscar = new JTextField(8);

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::buscarPacientes);

        panelBuscar.add(new JLabel("ID:")); panelBuscar.add(txtIdBuscar);
        panelBuscar.add(new JLabel("Nombre:")); panelBuscar.add(txtNombreBuscar);
        panelBuscar.add(new JLabel("Apellidos:")); panelBuscar.add(txtApellidosBuscar);
        panelBuscar.add(new JLabel("DNI:")); panelBuscar.add(txtDniBuscar);
        panelBuscar.add(btnBuscar);

        btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(this::registrarPaciente);

        btnModificar = new JButton("Modificar");
        btnModificar.addActionListener(this::modificarPaciente);

        btnInactivar = new JButton("Inactivar");
        btnInactivar.addActionListener(this::inactivarPaciente);

        panelAcciones.add(btnRegistrar);
        panelAcciones.add(btnModificar);
        panelAcciones.add(btnInactivar);

        panel.add(panelBuscar);
        panel.add(panelAcciones);

        return panel;
    }

    private void registrarPaciente(ActionEvent e) {
        Paciente p = leerFormulario(false);
        if (p != null && servicio.registrarPaciente(p)) {
            JOptionPane.showMessageDialog(this, "Paciente registrado exitosamente.");
            limpiarFormulario(); buscarPacientes(null);
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar paciente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarPacientes(ActionEvent e) {
        buscarPacientes(null);
    }

    private void buscarPaciente(String mensaje) {
        modeloTabla.setRowCount(0);
        List<Paciente> lista = servicio.buscarPacientes(
                txtIdBuscar.getText(),
                txtNombreBuscar.getText(),
                txtApellidosBuscar.getText(),
                txtDniBuscar.getText()
        );
        for (Paciente p : lista) {
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getApellidos(),
                    p.getDniId(), p.getTelefono(), p.getEmail(),
                    p.isActivo() ? "Sí" : "No"
            });
        }
        if (mensaje != null) JOptionPane.showMessageDialog(this, mensaje);
    }

    private void seleccionarPaciente() {
        int fila = tablaPaciente.getSelectedRow();
        if (fila >= 0) {
            txtIdModificar.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
            txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
            txtApellidos.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
            txtDni.setText(String.valueOf(modeloTabla.getValueAt(fila, 3)));
            txtTelefono.setText(String.valueOf(modeloTabla.getValueAt(fila, 4)));
            txtEmail.setText(String.valueOf(modeloTabla.getValueAt(fila, 5)));
        }
    }

    private void modificarPaciente(ActionEvent e) {
        Paciente p = leerFormulario(true);
        if (p != null && servicio.modificarPaciente(p)) {
            JOptionPane.showMessageDialog(this, "Paciente modificado exitosamente.");
            limpiarFormulario(); buscarPacientes(null);
        } else {
            JOptionPane.showMessageDialog(this, "Error al modificar paciente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inactivarPaciente(ActionEvent e) {
        int id = Integer.parseInt(txtIdModificar.getText());
        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas inactivar al paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && servicio.inactivarPaciente(id)) {
            JOptionPane.showMessageDialog(this, "Paciente inactivado correctamente.");
            limpiarFormulario(); buscarPacientes(null);
        }
    }

    private Paciente leerFormulario(boolean requiereId) {
        try {
            String nombre = txtNombre.getText().trim();
            String apellidos = txtApellidos.getText().trim();
            String fechaTexto = txtFechaNacimiento.getText().trim();
            LocalDate fechaNacimiento = fechaTexto.isBlank() ? null : LocalDate.parse(fechaTexto);

            Paciente p = new Paciente(
                    nombre, apellidos, fechaNacimiento,
                    txtDni.getText().trim(), txtTelefono.getText().trim(),
                    txtEmail.getText().trim(), txtDireccion.getText().trim()
            );

            if (requiereId) p.setId(Integer.parseInt(txtIdModificar.getText()));
            return p;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Datos inválidos. Verifica los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void limpiarFormulario() {
        txtIdModificar.setText(""); txtNombre.setText(""); txtApellidos.setText("");
        txtFechaNacimiento.setText(""); txtDni.setText("");
        txtTelefono.setText(""); txtEmail.setText(""); txtDireccion.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaPaciente ventana = new VentanaPaciente();
            ventana.setVisible(true);
        });
    }
}