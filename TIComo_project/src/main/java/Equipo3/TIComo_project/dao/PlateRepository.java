package Equipo3.TIComo_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Equipo3.TIComo_project.model.Plate;
@Repository
public interface PlateRepository extends MongoRepository <Plate, String> {
	Plate findByCorreo(String nombre);

	void deleteByNombre(String nombrePlato);

}
