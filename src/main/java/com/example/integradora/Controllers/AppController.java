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
    @FXML private Button btnInactivar;

    private PacienteService service = new PacienteService();
    private PacienteRepository repo = new PacienteRepository();
    private ObservableList<Paciente> pacientesData = FXCollections.observableArrayList();
    private boolean isEditing = false;

    @FXML
    public void initialize() {
        colCurp.setCellValueFactory(new PropertyValueFactory<>("curp"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEdad.setCellValueFactory(new PropertyValueFactory<>("edad"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEstatus.setCellValueFactory(new PropertyValueFactory<>("estatus"));

        cbEstatus.setItems(FXCollections.observableArrayList("ACTIVO", "INACTIVO"));
        cbEstatus.setValue("ACTIVO");

        tblPacientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                llenarFormulario(newSelection);
                isEditing = true;
                txtCurp.setDisable(true);
                btnGuardar.setText("Actualizar");
                btnInactivar.setDisable(false);
            }
        });

        tblPacientes.setItems(pacientesData);
        cargarDatosDesdeArchivo();
    }

    private void cargarDatosDesdeArchivo() {
        try {
            List<Paciente> lista = repo.findAll();
            pacientesData.setAll(lista);
            actualizarContadoresResumen(lista);
            showFormatMessage("Datos cargados correctamente", true);
        } catch (IOException e) {
            showFormatMessage("Error al leer el archivo", false);
        }
    }

    private void actualizarContadoresResumen(List<Paciente> lista) {
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
            String nombre = txtNombre.getText().trim();
            int edad = txtEdad.getText().isBlank() ? 0 : Integer.parseInt(txtEdad.getText());
            String tel = txtTelefono.getText().trim();
            String alergias = txtAlergias.getText().trim();
            String estatus = cbEstatus.getValue();

            Paciente p = new Paciente(curp, nombre, edad, tel, alergias, estatus);

            if (isEditing) {
                service.actualizar(p);
                showFormatMessage("Paciente actualizado", true);
            } else {
                service.registrar(p);
                showFormatMessage("Paciente registrado", true);
            }

            cargarDatosDesdeArchivo();
            onNuevo();
        } catch (NumberFormatException e) {
            showFormatMessage("La edad debe ser un número", false);
        } catch (IllegalArgumentException e) {
            showFormatMessage(e.getMessage(), false);
        } catch (IOException e) {
            showFormatMessage("Error de persistencia", false);
        }
    }

    @FXML
    private void onInactivar() {
        Paciente sel = tblPacientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar cambio");
            alert.setHeaderText("¿Cambiar estatus de " + sel.getNombre() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    service.cambiarEstatus(sel.getCurp());
                    cargarDatosDesdeArchivo();
                    onNuevo();
                } catch (IOException e) {
                    showFormatMessage("Error al actualizar estatus", false);
                }
            }
        }
    }

    @FXML
    private void onEliminar() {
        Paciente sel = tblPacientes.getSelectionModel().getSelectedItem();
        if (sel != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Eliminar");
            alert.setHeaderText(null);
            alert.setContentText("¿Eliminar a " + sel.getNombre() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    service.eliminarFisico(sel.getCurp());
                    cargarDatosDesdeArchivo();
                    onNuevo();
                    showFormatMessage("Eliminado", true);
                } catch (IOException e) {
                    showFormatMessage("Error", false);
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

        txtCurp.setDisable(false);
        btnGuardar.setText("Guardar");
        btnInactivar.setDisable(true);
        isEditing = false;
    }

    private void llenarFormulario(Paciente p) {
        txtCurp.setText(p.getCurp());
        txtNombre.setText(p.getNombre());
        txtEdad.setText(String.valueOf(p.getEdad()));
        txtTelefono.setText(p.getTelefono());
        txtAlergias.setText(p.getAlergias());
        cbEstatus.setValue(p.getEstatus());
    }

    private void showFormatMessage(String msg, boolean success) {
        lblMsg.setText(msg);
        lblMsg.setStyle("-fx-text-fill: " + (success ? "green" : "red") + "; -fx-font-weight: bold;");
    }
}