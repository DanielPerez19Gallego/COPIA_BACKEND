package Equipo3.TIComo_project.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import Equipo3.TIComo_project.model.Plate;

public interface PlateRepository extends MongoRepository <Plate, String> {
	
	@Query("select nombre from Plates p where nombre=?1 and nombreRestaurante=?2")
	Plate findPlato(String nombre, String nombreRes);
	
	@Query("select nombre, nombreRestaurante from Plates p where nombre=?1 and nombreRestaurante=?2")
	List<Plate> findByNombreAndRestaurante(String nombre, String nombreRes);

	@Query("delete from Plates p where nombre=?1 and nombreRestaurante=?2")
	void deletePlato(String nombrePlato, String nombreRestaurante);
	
	List <Plate> findBynombreRestaurante(String nombreRes);

	void deleteBynombreRestaurante(String nombreRes);

}
