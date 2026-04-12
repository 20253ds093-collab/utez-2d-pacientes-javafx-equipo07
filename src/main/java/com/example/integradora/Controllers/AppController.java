package com.example.integradora.Controllers;

import com.example.integradora.Models.Paciente;
import com.example.integradora.Services.PacienteService;
import com.example.integradora.Repositores.PacienteRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;

public class AppController {

    @FXML private TableView<Paciente> tblPacientes;
    @FXML private TableColumn<Paciente, String> colCurp;
    @FXML private TableColumn<Paciente, String> colNombre;
    @FXML private TableColumn<Paciente, Integer> colEdad;
    @FXML private TableColumn<Paciente, String> colTelefono;
    @FXML private TableColumn<Paciente, String> colEstatus;

    @FXML private TextField txtCurp;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEdad;
    @FXML private TextField txtTelefono;
    @FXML private TextArea txtAlergias;
    @FXML private ComboBox<String> cbEstatus;

    @FXML private Label lblTotal;
    @FXML private Label lblActivos;
    @FXML private Label lblInactivos;
    @FXML private Label lblMsg;

    @FXML private Button btnGuardar;

    private PacienteService service = new PacienteService();
    private PacienteRepository repo = new PacienteRepository();
    private ObservableList<Paciente> pacientesData = FXCollections.observableArrayList();
    private String curpEdicion = "";

    @FXML
    public void initialize() {
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        cbEstatus.setItems(FXCollections.observableArrayList("ACTIVO", "INACTIVO"));
        cbEstatus.setValue("ACTIVO");

        tblPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                llenarFormulario(newSel);
                curpEdicion = newSel.getCurp();
                btnGuardar.setText("Actualizar");
            }
        });

        tblPacientes.setItems(pacientesData);
        cargarDatos();
    }

    private void cargarDatos() {
        try {
            List<Paciente> lista = repo.findAll();
            pacientesData.setAll(lista);
            actualizarResumen(lista);
        } catch (IOException e) {
            mensaje("Error al cargar datos", false);
        }
    }

    private void actualizarResumen(List<Paciente> lista) {
        int total = lista.size();
        int activos = (int) lista.stream().filter(p -> p.getEstatus().equals("ACTIVO")).count();
        lblTotal.setText("Total: " + total);
        lblActivos.setText("Activos: " + activos);
        lblInactivos.setText("Inactivos: " + (total - activos));
    }

    @FXML
    private void onGuardar() {
        try {
            String curp = txtCurp.getText().trim();
            String nombre = txtNombre.getText().replace(",", " ").trim();

            String edadText = txtEdad.getText().trim();
            if (edadText.isBlank()) throw new IllegalArgumentException("La edad no puede estar vacía.");
            int edad = Integer.parseInt(edadText);

            String tel = txtTelefono.getText().trim();
            String alergias = txtAlergias.getText().replace(",", " ").trim();
            String estatus = cbEstatus.getValue();

            Paciente p = new Paciente(curp, nombre, edad, tel, alergias, estatus);

            if (!curpEdicion.isEmpty()) {
                service.actualizar(curpEdicion, p);
                mensaje("Paciente actualizado", true);
            } else {
                service.registrar(p);
                mensaje("Paciente registrado", true);
            }

            cargarDatos();
            onNuevo();
        } catch (NumberFormatException e) {
            mensaje("La edad debe ser un número", false);
        } catch (Exception e) {
            mensaje(e.getMessage(), false);
        }
    }

    @FXML
    private void onEliminar() {
        Paciente sel = tblPacientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar (inactivo) a " + sel.getNombre() + "?", ButtonType.YES, ButtonType.NO);
            if (alert.showAndWait().get() == ButtonType.YES) {
                try {
                    service.borradoLogico(sel.getCurp());
                    cargarDatos();
                    onNuevo();
                    mensaje("Paciente eliminado lógicamente", true);
                } catch (IOException e) {
                    mensaje("Error", false);
                }
            }
        }
    }

    @FXML
    private void onNuevo() {
        tblPacientes.getSelectionModel().clearSelection();
        txtCurp.clear();
        txtNombre.clear();
        txtEdad.clear();
        txtTelefono.clear();
        txtAlergias.clear();
        cbEstatus.setValue("ACTIVO");
        curpEdicion = "";
        btnGuardar.setText("Guardar");
    }

    private void llenarFormulario(Paciente p) {
        txtCurp.setText(p.getCurp());
        txtNombre.setText(p.getNombre());
        txtEdad.setText(String.valueOf(p.getEdad()));
        txtTelefono.setText(p.getTelefono());
        txtAlergias.setText(p.getAlergias());
        cbEstatus.setValue(p.getEstatus());
    }

    private void mensaje(String m, boolean s) {
        lblMsg.setText(m);
        lblMsg.setStyle("-fx-text-fill: " + (s ? "green" : "red") + "; -fx-font-weight: bold;");
    }
}