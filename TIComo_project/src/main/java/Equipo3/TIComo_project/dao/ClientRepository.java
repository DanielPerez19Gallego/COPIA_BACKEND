package Equipo3.TIComo_project.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import Equipo3.TIComo_project.model.Client;



@Repository
public interface ClientRepository extends MongoRepository <Client, String>{
	Optional<Client> findByCorreo(String correo);

}
