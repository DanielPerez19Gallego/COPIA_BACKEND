package Equipo3.TIComo_project.dao;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import Equipo3.TIComo_project.model.Plate;

public interface PlateRepository extends MongoRepository <Plate, String> {

	@Query(value = "{ 'nombre' : ?0, 'nombreRestaurante' : ?1 }")
	List<Plate> findByNombreAndnombreRestaurante(String nombre, String nombreRes);

	@Query(value = "{ 'nombre' : ?0, 'nombreRestaurante' : ?1 }", delete=true)
	void deleteByNombreAndnombreRestaurante(String nombrePlato, String nombreRestaurante);

	List <Plate> findBynombreRestaurante(String nombreRes);

	void deleteBynombreRestaurante(String nombreRes);
}
