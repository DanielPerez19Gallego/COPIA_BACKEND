package Equipo3.TIComo_project.services;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.model.User;
import Equipo3.TIComo_project.model.Client;
import Equipo3.TIComo_project.dao.UserRepository;
import Equipo3.TIComo_project.dao.ClientRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userDAO;
	
	@Autowired
	private ClientRepository clientDAO;
	
	private String correo = "correo";
	private String client = "client";
	
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
		User user = new User();
		Client client = new Client();
		User userEmail = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (userEmail != null) 
			return "correo";
		
		user.setCorreo(jso.getString(this.correo));
		user.setPassword(jso.getString("pwd1"));
		user.setApellidos(jso.getString("apellidos"));
		user.setNif(jso.getString("nif"));
		user.setNombre(jso.getString("nombre"));
		user.setRol("client");
		
		client.setCorreo(jso.getString(this.correo));
		client.setDireccion(jso.getString("direccion"));
		client.setTelefono(jso.getString("telefono"));
		
		this.clientDAO.save(client);
		this.userDAO.save(user);
		return "perfecto";
	}
}

