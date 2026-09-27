package org.ap.controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import org.ap.dao.AnimalDAO;
import org.ap.dao.impl.AnimalDAOImpl;
import org.ap.model.Animal;

public class AdopcionController {

    @FXML
    private TextField txtNombre;
    @FXML
    private ComboBox<String> cmbEspecie;
    @FXML
    private TextField txtRaza;
    @FXML
    private TextField txtEdad;
    @FXML
    private ComboBox<String> cmbEstado;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TableView<Animal> tablaAnimales;
    @FXML
    private TableColumn<Animal, Number> colId;
    @FXML
    private TableColumn<Animal, String> colNombre;
    @FXML
    private TableColumn<Animal, String> colEspecie;
    @FXML
    private TableColumn<Animal, String> colRaza;
    @FXML
    private TableColumn<Animal, Number> colEdad;
    @FXML
    private TableColumn<Animal, String> colEstado;

    private final AnimalDAO animalDAO = new AnimalDAOImpl();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdAnimal()));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colEspecie.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEspecie()));
        colRaza.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRaza()));
        colEdad.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getEdad()));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));

        cmbEspecie.setItems(FXCollections.observableArrayList("Perro", "Gato", "Conejo", "Otro"));
        cmbEstado.setItems(FXCollections.observableArrayList("Disponible", "En proceso", "Adoptado"));

        cargarAnimales();

        tablaAnimales.getSelectionModel().selectedItemProperty().addListener((obs, anterior, seleccionado) -> {
            if (seleccionado != null) {
                cargarFormulario(seleccionado);
            }
        });
    }

    private void cargarAnimales() {
        ObservableList<Animal> lista = FXCollections.observableArrayList(animalDAO.listarTodos());
        tablaAnimales.setItems(lista);
    }

    private void cargarFormulario(Animal animal) {
        txtNombre.setText(animal.getNombre());
        cmbEspecie.setValue(animal.getEspecie());
        txtRaza.setText(animal.getRaza());
        txtEdad.setText(String.valueOf(animal.getEdad()));
        cmbEstado.setValue(animal.getEstado());
    }

    @FXML
    private void registrar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            Animal animal = new Animal(
                txtNombre.getText().trim(),
                cmbEspecie.getValue(),
                txtRaza.getText().trim(),
                Integer.parseInt(txtEdad.getText().trim()),
                cmbEstado.getValue()
            );

            if (animalDAO.insertar(animal)) {
                mostrarMensaje(Alert.AlertType.INFORMATION, "Registro", "Animal registrado correctamente.");
                cargarAnimales();
                limpiar();
            }

        } catch (NumberFormatException e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error", "La edad debe ser un número entero.");
        }
    }

    @FXML
    private void modificar() {
        Animal seleccionado = tablaAnimales.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Modificar", "Seleccione un animal de la tabla.");
            return;
        }

        if (!validarFormulario()) {
            return;
        }

        try {
            seleccionado.setNombre(txtNombre.getText().trim());
            seleccionado.setEspecie(cmbEspecie.getValue());
            seleccionado.setRaza(txtRaza.getText().trim());
            seleccionado.setEdad(Integer.parseInt(txtEdad.getText().trim()));
            seleccionado.setEstado(cmbEstado.getValue());

            if (animalDAO.actualizar(seleccionado)) {
                mostrarMensaje(Alert.AlertType.INFORMATION, "Modificar", "Animal actualizado correctamente.");
                cargarAnimales();
                limpiar();
            }

        } catch (NumberFormatException e) {
            mostrarMensaje(Alert.AlertType.ERROR, "Error", "La edad debe ser un número entero.");
        }
    }

    @FXML
    private void eliminar() {
        Animal seleccionado = tablaAnimales.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Eliminar", "Seleccione un animal.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Desea eliminar a " + seleccionado.getNombre() + "?");

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            if (animalDAO.eliminar(seleccionado.getIdAnimal())) {
                mostrarMensaje(Alert.AlertType.INFORMATION, "Eliminar", "Animal eliminado correctamente.");
                cargarAnimales();
                limpiar();
            }
        }
    }

    @FXML
    private void cambiarEstado() {
        Animal seleccionado = tablaAnimales.getSelectionModel().getSelectedItem();

        if (seleccionado == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Estado", "Seleccione un animal.");
            return;
        }

        String nuevoEstado = cmbEstado.getValue();

        if (nuevoEstado == null) {
            mostrarMensaje(Alert.AlertType.WARNING, "Estado", "Seleccione un estado.");
            return;
        }

        if (animalDAO.cambiarEstado(seleccionado.getIdAnimal(), nuevoEstado)) {
            mostrarMensaje(Alert.AlertType.INFORMATION, "Estado", "Estado actualizado correctamente.");
            cargarAnimales();
            limpiar();
        }
    }

    @FXML
    private void buscar() {
        String texto = txtBuscar.getText().trim();

        if (texto.isEmpty()) {
            cargarAnimales();
            return;
        }

        ObservableList<Animal> lista = FXCollections.observableArrayList(animalDAO.buscar(texto));
        tablaAnimales.setItems(lista);
    }

    @FXML
    private void mostrarTodos() {
        txtBuscar.clear();
        cargarAnimales();
    }

    @FXML
    private void limpiar() {
        txtNombre.clear();
        txtRaza.clear();
        txtEdad.clear();
        cmbEspecie.getSelectionModel().clearSelection();
        cmbEstado.getSelectionModel().clearSelection();
        tablaAnimales.getSelectionModel().clearSelection();
    }

    private boolean validarFormulario() {
        if (txtNombre.getText().trim().isEmpty()
                || cmbEspecie.getValue() == null
                || txtRaza.getText().trim().isEmpty()
                || txtEdad.getText().trim().isEmpty()
                || cmbEstado.getValue() == null) {

            mostrarMensaje(Alert.AlertType.WARNING, "Datos incompletos", "Complete todos los campos.");
            return false;
        }

        try {
            int edad = Integer.parseInt(txtEdad.getText().trim());

            if (edad < 0) {
                mostrarMensaje(Alert.AlertType.WARNING, "Edad inválida", "La edad no puede ser negativa.");
                return false;
            }

        } catch (NumberFormatException e) {
            mostrarMensaje(Alert.AlertType.WARNING, "Edad inválida", "La edad debe ser un número.");
            return false;
        }

        return true;
    }

    private void mostrarMensaje(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}