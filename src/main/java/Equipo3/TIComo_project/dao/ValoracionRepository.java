package Equipo3.TIComo_project.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import Equipo3.TIComo_project.model.Valoracion;

public interface ValoracionRepository extends MongoRepository <Valoracion, String> {
	
	List<Valoracion> findAllByEntidad(String entidad);

}
