package org.JavviFdeez.controller.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.JavviFdeez.controller.ContactController;
import org.JavviFdeez.model.connection.ConnectionMariaDB;
import org.JavviFdeez.model.entity.Contact;
import org.JavviFdeez.model.entity.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ResourceBundle;

public class FormDataContactController implements Initializable {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailText;

    @FXML
    private TextField LastNameTextField;

    @FXML
    private TextField occupationTextField;

    @FXML
    private TextField locationTextField;

    @FXML
    private TextField linkedinTextField;

    @FXML
    private TextField extraTextField;

    @FXML
    private TextField mobileTextField;

    @FXML
    private ImageView profileImageView;

    @FXML
    private ImageView addImage;

    @FXML
    private Button buttonSignoff;

    private String imageRelativePath;

    @FXML
    private Button buttonSaveData;

    private ContactController contactController;
    private LogInController logInController;



    public FormDataContactController() {
        this.contactController = new ContactController();
    }

    public void setLogInController(LogInController logInController) {
        this.logInController = logInController;
        loadContactData();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Asegurarse de que ningún campo de texto esté seleccionado al inicio con una pequeña demora
        Platform.runLater(() -> nameTextField.getParent().requestFocus());
        handleFormData();
        buttonSaveData.setOnAction(event -> handleFormaDataSave());
        buttonSignoff.setOnAction(event -> handleSignoff());
    }

    private void handleSignoff() {
        try {
            // Cargar la nueva escena desde el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/JavviFdeez/fxml/LogIn.fxml"));
            Parent root = loader.load();

            // Obtener el escenario actual desde el emailTextField
            Stage stage = (Stage) occupationTextField.getScene().getWindow();

            // Establecer la nueva escena en el escenario
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar cualquier error de carga del archivo FXML
            showAlert("Error", "No se pudo cargar la pantalla de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }


    private void loadContactData() {
        try {
            if (logInController != null) {
                // Obtener el contactId del usuario autenticado desde la sesión
                int contactId = Session.getInstance().getContactId();

                // Obtener el contacto utilizando el contactId
                Contact contact = logInController.getIDContact();

                if (contact != null) {
                    // Mostrar los datos del contacto en los campos de texto
                    nameTextField.setText(contact.getName());
                    LastNameTextField.setText(contact.getLastname());
                    occupationTextField.setText(contact.getOccupation());
                    mobileTextField.setText(contact.getMobile());
                    emailText.setText(contact.getEmail());
                    linkedinTextField.setText(contact.getLinkedin());
                    locationTextField.setText(contact.getLocation());
                    extraTextField.setText(contact.getExtra());
                    if (contact.getImage() != null) {
                        // Si hay una imagen asociada, cargarla en el ImageView
                        Image image = new Image(new FileInputStream(contact.getImage()));
                        profileImageView.setImage(image);
                    }
                } else {
                    // Mostrar un mensaje de advertencia si no se encuentra el contacto
                    showAlert("Advertencia", "No se encontró ningún contacto con el ID proporcionado.", Alert.AlertType.WARNING);
                }
            } else {
                // Mostrar un mensaje de advertencia si LogInController es null
                showAlert("Advertencia", "No se pudo obtener la instancia de LogInController.", Alert.AlertType.WARNING);
            }
        } catch (IOException e) {
            // Manejar cualquier excepción de E/S que pueda ocurrir al cargar la imagen
            showAlert("Error", "No se pudo cargar la información del contacto.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void handleFormData() {
        // Cargar datos del contacto
        addImage.setOnMouseClicked(event -> handleUploadImage());
        nameTextField.setOnMouseClicked(event -> handleTextFieldClick(nameTextField));
        LastNameTextField.setOnMouseClicked(event -> handleTextFieldClick(LastNameTextField));
        occupationTextField.setOnMouseClicked(event -> handleTextFieldClick(occupationTextField));
        mobileTextField.setOnMouseClicked(event -> handleTextFieldClick(mobileTextField));
        emailText.setOnMouseClicked(event -> handleTextFieldClick(emailText));
        linkedinTextField.setOnMouseClicked(event -> handleTextFieldClick(linkedinTextField));
        extraTextField.setOnMouseClicked(event -> handleTextFieldClick(extraTextField));
        locationTextField.setOnMouseClicked(event -> handleTextFieldClick(locationTextField));
        extraTextField.setOnMouseClicked(event -> handleTextFieldClick(extraTextField));
    }


    private void handleTextFieldClick(TextField textField) {
        textField.clear();
    }

    private void handleFormaDataSave() {
        // Obtener los valores de los campos de texto
        String name = nameTextField.getText().trim();
        String lastName = LastNameTextField.getText().trim();
        String occupation = occupationTextField.getText().trim();
        String mobile = mobileTextField.getText().trim();
        String email = emailText.getText().trim();
        String linkedin = linkedinTextField.getText().trim();
        String location = locationTextField.getText().trim();
        String extra = extraTextField.getText().trim();

        // Verificar si los campos obligatorios están vacíos
        if (name.isEmpty() || lastName.isEmpty() || addImage.getImage() == null || occupation.isEmpty() || mobile.isEmpty() || email.isEmpty() || linkedin.isEmpty() || location.isEmpty()) {
            showAlert("Campos incompletos", "Por favor, rellene todos los campos obligatorios.", Alert.AlertType.WARNING);
            return;
        }

        try {
            boolean saveDataToDatabase = contactController.saveDataToDatabase(name, lastName, imageRelativePath, occupation, mobile, email, linkedin, location, extra);

                if (saveDataToDatabase) {
                    showAlert("Exito", "Los datos se han guardado exitosamente", Alert.AlertType.INFORMATION);
                    // Cambiar a la escena de inicio de sesión después de guardar el usuario
                    changeSceneToFormData();
                } else {
                    showAlert("Error", "No se han podido guardar los datos", Alert.AlertType.ERROR);
                }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void changeSceneToFormData() {
        try {
            // Cargar la nueva escena desde el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/JavviFdeez/fxml/FormDataAcademies.fxml"));
            Parent root = loader.load();



            // Obtener el escenario actual desde el emailTextField (o cualquier otro nodo)
            Stage stage = (Stage) nameTextField.getScene().getWindow();

            // Establecer la nueva escena en el escenario
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejar cualquier error de carga del archivo FXML
            showAlert("Error", "No se pudo cargar la pantalla de Academies.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String error, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(error);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Leer la imagen seleccionada
                Image image = new Image(new FileInputStream(selectedFile));
                profileImageView.setImage(image);

                // Guardar la imagen en la ruta especificada
                String userImagesDir = "src/main/resources/org/imagesUsers";
                Path outputPath = saveImageToDirectory(selectedFile, userImagesDir);

                // Guardar la ruta relativa
                imageRelativePath = outputPath.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Path saveImageToDirectory(File imageFile, String userImagesDir) throws IOException {
        // Crear el directorio si no existe
        Path userImagesPath = Paths.get(userImagesDir);
        if (!Files.exists(userImagesPath)) {
            Files.createDirectories(userImagesPath);
        }

        // Crear una nueva ruta para la imagen
        String fileName = imageFile.getName();
        Path outputPath = userImagesPath.resolve(fileName);

        // Copiar el archivo a la nueva ubicación
        try (FileInputStream fis = new FileInputStream(imageFile);
             FileOutputStream fos = new FileOutputStream(outputPath.toFile())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }

        return outputPath;
    }
}