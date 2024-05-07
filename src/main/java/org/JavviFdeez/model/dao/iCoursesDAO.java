package org.JavviFdeez.model.dao;

import org.JavviFdeez.model.entity.Courses;
import java.sql.SQLException;
import java.util.List;


public interface iCoursesDAO {
    /**
     * @param course
     * @return
     * @throws SQLException
     * @Author: JavviFdeez
     * Método para GUARDAR un curso
     */
    Courses save(Courses course) throws SQLException;

    /**
     * @param course
     * @return
     * @throws SQLException
     * @Author: JavviFdeez
     * Método para ACTUALIZAR un curso
     */
    Courses update(Courses course) throws SQLException;

    /**
     * @param course
     * @return
     * @throws SQLException
     * @Author: JavviFdeez
     * Método para ELIMINAR un curso
     */
    Courses delete(Courses course) throws SQLException;

    /**
     * @param id
     * @return
     * @throws SQLException
     * @Author: JavviFdeez
     * Método para BUSCAR un curso por su ID
     */
    Courses findById(int id) throws SQLException;

    /**
     * @return
     * @throws SQLException
     * @Author: JavviFdeez
     * Método para BUSCAR todos los cursos
     */
    List<Courses> findAll() throws SQLException;
}