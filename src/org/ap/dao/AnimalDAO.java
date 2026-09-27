package org.ap.dao;
import org.ap.model.Animal;
import java.util.List;

public interface AnimalDAO {
    List<Animal> listarTodos();
    boolean insertar(Animal animal);
    boolean actualizar(Animal animal);
    boolean eliminar(int idAnimal);
    boolean cambiarEstado(int idAnimal, String estado);
    List<Animal> buscar(String texto);
}