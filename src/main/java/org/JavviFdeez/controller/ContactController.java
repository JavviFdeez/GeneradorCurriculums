package org.JavviFdeez.controller;

import javafx.fxml.Initializable;
import org.JavviFdeez.model.connection.ConnectionMariaDB;
import org.JavviFdeez.model.dao.ContactDAO;
import org.JavviFdeez.model.entity.Contact;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ContactController implements Initializable {
    // =============
    // Atributos
    // =============

    private ContactDAO contactDAO;

    private Connection conn;

    // ==============
    // Constructor
    // ==============
    public ContactController() {
        this.contactDAO = new ContactDAO(ConnectionMariaDB.getConnection());
        this.conn = ConnectionMariaDB.getConnection();
    }

    /**
     * @param contact el contacto que se va a guardar
     * @Author: JavviFdeez
     * Metodo para mostrar un mensaje de INSERTAR una nuevo contacto en la base de datos
     */
    public void saveContact(Contact contact) {
        try {
            // ==========================================
            // Guardar el contacto en la base de datos
            // ==========================================
            contactDAO.save(contact);
            // ======================================================
            // Si el guardado es exitoso, mostrar mensaje de éxito.
            // ======================================================
            System.out.println("✅ Contacto guardada exitosamente.");
        } catch (SQLException e) {
            // =============================================
            // En caso de error, mostrar mensaje de error.
            // =============================================
            System.err.println("❌ Error al guardar el contacto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param id el contacto que se va a actualizar
     * @Author: JavviFdeez
     * Metodo para mostrar un mensaje de ACTUALIZAR un contacto en la base de datos
     */
    public void updateContact(int id, Contact updatedContact) {
        try {
            // ==========================================
            // Actualizar el contacto en la base de datos
            // ==========================================
            contactDAO.update(id, updatedContact);
            // ======================================================
            // Si la actualizacion es exitosa, mostrar mensaje de exito.
            // ======================================================
            System.out.println("✅ Contacto actualizado exitosamente.");
        } catch (SQLException e) {
            // =============================================
            // En caso de error, mostrar mensaje de error.
            // =============================================
            System.err.println("❌ Error al actualizar el contacto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param id el contacto que se va a eliminar
     * @Author: JavviFdeez
     * Metodo para mostrar un mensaje de ELIMINAR un contacto en la base de datos
     */
    public void deleteContact(int id) {
        try {
            // ==========================================
            // Eliminar el contacto de la base de datos
            // ==========================================
            contactDAO.delete(id);
            // ======================================================
            // Si la eliminación es exitosa, mostrar mensaje de exito.
            // ======================================================
            System.out.println("✅ Contacto eliminado exitosamente.");
        } catch (SQLException e) {
            // =============================================
            // En caso de error, mostrar mensaje de error.
            // =============================================
            System.err.println("❌ Error al eliminar el contacto: " + e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * @param email
     * @return
     * @throws SQLException
     * @Author: JavviFdeez
     * Metodo para mostrar un mensaje de buscar un contacto por email en la base de datos
     */
    public Integer getContactIdByEmail(String email) throws SQLException {
        try {
            // ==========================================
            // Buscar el contactId en la base de datos
            // ==========================================
            Integer contactId = contactDAO.getContactIdByEmail(email);
            if (contactId != null) {
                // ======================================================
                // Si la búsqueda es exitosa, mostrar mensaje de éxito.
                // ======================================================
                System.out.println("✅ Contacto encontrado exitosamente: contactId = " + contactId);
                return contactId;
            } else {
                // ======================================================
                // Si no se encuentra el contacto, mostrar mensaje de advertencia.
                // ======================================================
                System.out.println("⚠️ No se encontró ningún contacto con el email proporcionado.");
            }
        } catch (SQLException e) {
            // =============================================
            // En caso de error, mostrar mensaje de error.
            // =============================================
            System.err.println("❌ Error al buscar el contactId por email: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Error al obtener el contactId del usuario por correo electrónico: " + e.getMessage());
        }
        return null;
    }


    /**
     * @Author: JavviFdeez
     * Metodo para mostrar un mensaje de buscar todos los contactos en la base de datos
     */
    public void getAllContact() {
        try {
            // ==========================================
            // Buscar todos los contactos en la base de datos
            // ==========================================
            contactDAO.findAll();
            // ======================================================
            // Si la busqueda es exitosa, mostrar mensaje de exito.
            // ======================================================
            System.out.println("✅ Contactos encontrados exitosamente.");
        } catch (SQLException e) {
            // =============================================
            // En caso de error, mostrar mensaje de error.
            // =============================================
            System.err.println("❌ Error al buscar los contactos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean saveDataToDatabase(String name, String lastname, String image, String occupation, String mobile, String email, String linkedin, String location, String extra) throws SQLException {
        // Guardar los datos en la base de datos
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionMariaDB.getConnection();
            }

            // Preparar la consulta SQL para insertar los datos
            String query = "INSERT INTO cvv_contact (name, lastname, image, occupation, mobile, email, linkedin, location, extra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, name);
                pst.setString(2, lastname);
                pst.setString(3, image);
                pst.setString(4, occupation);
                pst.setString(5, mobile);
                pst.setString(6, email);
                pst.setString(7, linkedin);
                pst.setString(8, location);
                pst.setString(9, extra);
                // Ejecutar la inserción
                pst.executeUpdate();

                // Si se ejecuta sin excepciones, devuelve true
                return true;
            }
        } catch (SQLException e) {
            // Manejar cualquier excepción de SQL aquí
            throw e;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
