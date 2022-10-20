package Equipo3.TIComo_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import Equipo3.TIComo_project.model.Plate;

public interface PlateRepository extends MongoRepository <Plate, String> {
	Plate findByCorreo(String nombre);

	void deleteByNombre(String nombrePlato);

}
