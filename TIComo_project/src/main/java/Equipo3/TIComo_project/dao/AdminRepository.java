package Equipo3.TIComo_project.dao;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import Equipo3.TIComo_project.model.Admin;
import Equipo3.TIComo_project.model.User;


public interface AdminRepository extends MongoRepository <Admin, String> {
	Optional<Admin> findByCorreo(String correo);

}
