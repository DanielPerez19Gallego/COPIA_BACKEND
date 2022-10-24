package Equipo3.TIComo_project.services;


import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.model.User;
import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;

import java.util.Collections;
import Equipo3.TIComo_project.model.Admin;
import Equipo3.TIComo_project.model.Client;
import Equipo3.TIComo_project.model.Rider;
import Equipo3.TIComo_project.dao.UserRepository;
import Equipo3.TIComo_project.dao.AdminRepository;
import Equipo3.TIComo_project.dao.ClientRepository;
import Equipo3.TIComo_project.dao.RiderRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userDAO;

	@Autowired
	private ClientRepository clientDAO;

	@Autowired
	private RiderRepository riderDAO;

	@Autowired
	private AdminRepository adminDAO;

	private String correo = "correo";
	private String client = "client";

	private String rol = "rol";

	public String login(JSONObject jso) {
		String rol = "nulo";
		User user = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (user != null) {
			String pwd = org.apache.commons.codec.digest.DigestUtils.sha512Hex(jso.getString("pwd"));
			if (user.getPassword().equals(pwd)) {
				if (user.getRol().equals(this.client))
					rol = this.client;
				else if (user.getRol().equals("admin"))
					rol = "admin";
				else 
					rol = "rider";
			}
		}
		return rol;
	}

	public String register(JSONObject jso) {

		Client clientt = new Client();
		User userEmail = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (userEmail != null) 
			return this.correo;

		User user = crearUsuarioAux(jso);
		user.setRol(this.client);

		clientt.setCorreo(jso.getString(this.correo));
		clientt.setDireccion(jso.getString("direccion"));
		clientt.setTelefono(jso.getString("telefono"));

		this.clientDAO.save(clientt);
		this.userDAO.save(user);
		return "perfecto";
	}

	public String crearUsuario(JSONObject jso) {

		User userEmail = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (userEmail != null) 
			return this.correo;

		String rol = jso.getString("rol");

		User user = crearUsuarioAux(jso);
		user.setRol(rol);

		if (rol.equals("rider")) {
			Rider rider = new Rider();
			rider.setCarnet(Boolean.valueOf(jso.getString("carnet")));
			rider.setCorreo(jso.getString(this.correo));
			rider.setMatricula(jso.getString("matricula"));
			rider.setTipovehiculo(jso.getString("tipovehiculo"));
			this.riderDAO.save(rider);
		} else {
			Admin admin = new Admin();
			admin.setCorreo(jso.getString(this.correo));
			admin.setZona(jso.getString("zona"));
			this.adminDAO.save(admin);
		}
		userDAO.save(user);
		return "perfecto";
	}

	public User crearUsuarioAux(JSONObject jso) {

		User user = new User();
		user.setCorreo(jso.getString(this.correo));
		user.setPassword(jso.getString("pwd1"));
		user.setApellidos(jso.getString("apellidos"));
		user.setNif(jso.getString("nif"));
		user.setNombre(jso.getString("nombre"));
		return user;
	}

	public String eliminarUsuario(String correoUsuario) {
		User user = this.userDAO.findByCorreo(correoUsuario);
		if (user != null) {
			String rol = user.getRol();
			if (rol.equals("rider")) {
				this.riderDAO.deleteByCorreo(correoUsuario);
			}else if (rol.equals(this.client)) {
				this.clientDAO.deleteByCorreo(correoUsuario);
			}else {
				this.adminDAO.deleteByCorreo(correoUsuario);
			}
		}else return this.correo;

		this.userDAO.deleteByCorreo(correoUsuario);
		return "perfecto";
	}
	public List<User> consultarUsuarios() {
		if(!this.userDAO.findAll().isEmpty()) {
			return this.userDAO.findAll(); //Este tiene todos los users.
		}else {
			return this.userDAO.findAll(); //Lo devuelve tal y como esta, vacío.
		}

	}
	public List <User> consultarUsuario(String correo) {


		List <User> unico = null;
		if(!this.userDAO.findAll().isEmpty()) {
			unico = this.userDAO.findAllByCorreo(correo); //Este tiene el user o los users querido.
			return  unico;
		}
		return  unico;

	}

	public User actualizarUsuario(String correo,JSONObject json) {
		
		User nuevo = this.userDAO.findByCorreo(correo);
		if (nuevo!=null) {
			nuevo.setCorreo(json.getString("correo"));
			nuevo.setPassword(json.getString("password"));
			nuevo.setNombre(json.getString("nombre"));
			nuevo.setApellidos(json.getString("apellidos"));
			nuevo.setNif(json.getString("nif"));
			nuevo.setRol(json.getString("rol"));
			userDAO.deleteByCorreo(correo);
			userDAO.save(nuevo);
			
			return nuevo;
		}else 
			return nuevo;
		
	
	}


}


