package org.JavviFdeez.controller;

import javafx.fxml.Initializable;
import org.JavviFdeez.model.connection.ConnectionMariaDB;
import org.JavviFdeez.model.dao.UsersDAO;
import org.JavviFdeez.model.entity.Session;
import org.JavviFdeez.model.entity.User;
import org.JavviFdeez.utils.PasswordHasher;
import org.JavviFdeez.utils.PasswordValidator;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    // ============
    // Atributes
    // ============
    private UsersDAO usersDAO;

    // ===============================================
    // Atributo para la conexión a la base de datos
    // ===============================================
    private Connection conn;

    // ==============
    // Constructor
    // ==============
    public UsersController() {
        this.conn = conn;
        this.usersDAO = new UsersDAO(ConnectionMariaDB.getConnection());
    }

    /**
     * @param user el usuario a ser guardado
     * @return el usuario guardado, incluyendo su ID generado
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     * @Author: JavviFdeez
     * Método para GUARDAR un usuario en la base de datos.
     */
    public void saveUser(User user) throws SQLException, IllegalArgumentException {
        try {
            if (!PasswordValidator.isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Contraseña no válida. \n" +
                        "Debe tener un mínimo de 10 caracteres y que incluya: MINUSCULAS, MAYUSCULAS, NUMEROS");
            }

            user.setPassword(user.getPassword());
            User savedUser = usersDAO.save(user);

            // Establecer el contactId en la sesión
            Session.getInstance().setContactId(savedUser.getContactId());
        } catch (SQLException e) {
            throw new SQLException("Error al guardar el usuario: " + e.getMessage());
        }
    }

    /**
     * @param id el usuario a ser actualizado
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     * @Author: JavviFdeez
     * Método para ACTUALIZAR un usuario en la base de datos y mostrar un mensaje de exito o error.
     */
    public void updateUser(int id, User updatedUser) throws SQLException {
        try {
            // =========================================
            // Actualizar el usuario en la base de datos
            // =========================================
            usersDAO.update(id, updatedUser);

            // ======================================================
            // Si el guardado es exitoso, mostrar mensaje de exito.
            // ======================================================
            System.out.println("Usuario actualizado exitosamente.");

        } catch (SQLException e) {
            // En caso de error SQL, registrar el error y mostrar un mensaje al usuario
            System.err.println("Error al actualizar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param id el usuario a ser eliminado
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     * @Author: JavviFdeez
     * Método para ELIMINAR un usuario en la base de datos y mostrar un mensaje de exito o error.
     */
    public void deleteUser(int id) throws SQLException {
        try {
            // =========================================
            // Eliminar el usuario en la base de datos
            // =========================================
            usersDAO.delete(id);

            // ======================================================
            // Si el guardado es exitoso, mostrar mensaje de exito.
            // ======================================================
            System.out.println("Usuario eliminado exitosamente.");
        } catch (SQLException e) {
            // En caso de error SQL, registrar el error y mostrar un mensaje al usuario
            System.err.println("Error al eliminar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @param id el usuario a ser consultado
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     * @Author: JavviFdeez
     * Método para CONSULTAR un usuario en la base de datos y mostrar un mensaje de exito o error.
     */
    public void findUserbyID(int id) throws SQLException {
        try {
            // =========================================
            // Consultar el usuario en la base de datos
            // =========================================
            User foundUser = usersDAO.findById(id);

            if (foundUser != null) {
                // ======================================================
                // Si el guardado es exitoso, mostrar mensaje de exito.
                // ======================================================
                System.out.println("Usuario encontrado exitosamente.");
            } else {
                // ======================================================
                // Si el guardado es exitoso, mostrar mensaje de exito.
                // ======================================================
                System.out.println("⚠️ No se encontró ninguna academia con el ID " + id + ".");
            }

        } catch (SQLException e) {
            // En caso de error SQL, registrar el error y mostrar un mensaje al usuario
            System.err.println("Error al encontrar el usuario: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @throws SQLException si ocurre un error al ejecutar la consulta SQL
     * @Author: JavviFdeez
     * Método para CONSULTAR todos los usuarios en la base de datos y mostrar un mensaje de exito o error.
     */
    public void findAllUsers() throws SQLException {
        try {
            // ===================================================
            // Consultar todos los usuarios en la base de datos
            // ===================================================
            List<User> userList = usersDAO.findAll();

            if (!userList.isEmpty()) {
                // ======================================================
                // Si el guardado es exitoso, mostrar mensaje de exito.
                // ======================================================
                System.out.println("✅ Usuario guardado exitosamente");
            } else {
                // ======================================================
                // Si el guardado es exitoso, mostrar mensaje de exito.
                // ======================================================
                System.out.println("No se encontró ninguém usuario.");
            }

        } catch (SQLException e) {
            // En caso de error SQL, registrar el error y mostrar un mensaje al usuario
            System.err.println("Error al encontrar los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método para autenticar un usuario en la base de datos.
     *
     * @param email    El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return El usuario si la autenticación es exitosa, o null si las credenciales son incorrectas.
     * @throws SQLException Si ocurre un error al ejecutar la consulta SQL.
     */
    public boolean authenticate(String email, String password) throws SQLException {
        try {
            if (conn == null || conn.isClosed()) {
                conn = ConnectionMariaDB.getConnection();
            }

            // Obtener el hash SHA-256 de la contraseña ingresada
            String hashedPassword = PasswordHasher.hashPassword(password);

            String query = "SELECT * FROM cvv_users WHERE email = ? AND password = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, email);
                pst.setString(2, hashedPassword);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        int contactId = rs.getInt("contact_id");
                        // Establecer el contact_id en la sesión
                        Session.getInstance().setContactId(contactId);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al autenticar el usuario: " + e.getMessage());
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}