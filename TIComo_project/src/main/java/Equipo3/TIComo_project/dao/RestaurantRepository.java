package Equipo3.TIComo_project.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import Equipo3.TIComo_project.model.Restaurant;

public interface RestaurantRepository extends MongoRepository <Restaurant, String> {
	
	Restaurant findByCorreo(String nombre);

	void deleteByNombre(String nombreRestaurante);

}
