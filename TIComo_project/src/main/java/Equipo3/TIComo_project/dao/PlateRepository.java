package Equipo3.TIComo_project.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import Equipo3.TIComo_project.model.Plate;

public interface PlateRepository extends MongoRepository <Plate, String> {
	Plate findByNombre(String nombre);

	void deleteByNombre(String nombrePlato);
	
	List <Plate> findBynombreRestaurante(String nombreRes);

}
