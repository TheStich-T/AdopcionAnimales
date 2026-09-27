package org.ap.dao.impl;

import org.ap.model.Animal;
import org.ap.util.Conexion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.ap.dao.AnimalDAO;

public class AnimalDAOImpl implements AnimalDAO {

    @Override
    public List<Animal> listarTodos() {
        List<Animal> animales = new ArrayList<>();
        String sql = "SELECT id_animal, nombre, especie, raza, edad, estado FROM animales ORDER BY id_animal";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Animal animal = new Animal(
                    rs.getInt("id_animal"),
                    rs.getString("nombre"),
                    rs.getString("especie"),
                    rs.getString("raza"),
                    rs.getInt("edad"),
                    rs.getString("estado")
                );
                animales.add(animal);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return animales;
    }

    @Override
    public boolean insertar(Animal animal) {
        String sql = "INSERT INTO animales (nombre, especie, raza, edad, estado) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, animal.getNombre());
            stmt.setString(2, animal.getEspecie());
            stmt.setString(3, animal.getRaza());
            stmt.setInt(4, animal.getEdad());
            stmt.setString(5, animal.getEstado());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Animal animal) {
        String sql = "UPDATE animales SET nombre = ?, especie = ?, raza = ?, edad = ?, estado = ? WHERE id_animal = ?";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, animal.getNombre());
            stmt.setString(2, animal.getEspecie());
            stmt.setString(3, animal.getRaza());
            stmt.setInt(4, animal.getEdad());
            stmt.setString(5, animal.getEstado());
            stmt.setInt(6, animal.getIdAnimal());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int idAnimal) {
        String sql = "DELETE FROM animales WHERE id_animal = ?";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idAnimal);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean cambiarEstado(int idAnimal, String estado) {
        String sql = "{CALL sp_cambiar_estado(?, ?)}";

        try (Connection conn = Conexion.getInstancia().getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, idAnimal);
            stmt.setString(2, estado);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Animal> buscar(String texto) {
        List<Animal> animales = new ArrayList<>();
        String sql = "SELECT id_animal, nombre, especie, raza, edad, estado FROM animales " +
                     "WHERE nombre LIKE ? OR especie LIKE ? OR raza LIKE ? ORDER BY id_animal";

        try (Connection conn = Conexion.getInstancia().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String busqueda = "%" + texto + "%";
            stmt.setString(1, busqueda);
            stmt.setString(2, busqueda);
            stmt.setString(3, busqueda);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Animal animal = new Animal(
                        rs.getInt("id_animal"),
                        rs.getString("nombre"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        rs.getInt("edad"),
                        rs.getString("estado")
                    );
                    animales.add(animal);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return animales;
    }
}