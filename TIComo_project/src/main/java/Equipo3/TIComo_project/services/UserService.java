package Equipo3.TIComo_project.services;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.model.User;
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
	private String rider = "rider";

	public String[] comprobarPassword(JSONObject info) {

		String[] list = new String[2];

		String pwd1 = info.getString("pwd1");
		String pwd2 = info.getString("pwd2");
		list[0] = "false";
		if (!pwd1.equals(pwd2)) {
			list[1] = "Las contraseñas tienen que ser iguales";
		}
		else {
			if(pwd1.length() < 8) {
				list[1] = "La contraseña debe tener al menos 8 caracteres";
			}
			else if (!pwd1.matches(".*[0-9].*")) {
				list[1] = "La contraseña debe contener al menos un digito";
			}
			else if (pwd1.equals(pwd1.toLowerCase())) {
				list[1] = "La contraseña debe tener al menos una mayúscula";
			}
			else if (pwd1.equals(pwd1.toUpperCase())) {
				list[1] = "La contraseña debe tener al menos una minúscula";
			}
			else {
				list[0] = "true";
				list[1] = "OK";
			}
		}
		return list;
	}

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
					rol = this.rider;
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
		return "Registro completado";
	}

	public String crearUsuario(JSONObject jso) {

		User userEmail = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (userEmail != null) 
			return this.correo;

		String rol = jso.getString("rol");

		User user = crearUsuarioAux(jso);
		user.setRol(rol);

		if (rol.equals(this.rider)) {
			Rider riderr = new Rider();
			riderr.setCarnet(Boolean.valueOf(jso.getString("carnet")));
			riderr.setCorreo(jso.getString(this.correo));
			riderr.setMatricula(jso.getString("matricula"));
			riderr.setTipovehiculo(jso.getString("tipovehiculo"));
			this.riderDAO.save(riderr);
		} else {
			Admin admin = new Admin();
			admin.setCorreo(jso.getString(this.correo));
			admin.setZona(jso.getString("zona"));
			this.adminDAO.save(admin);
		}
		this.userDAO.save(user);
		return rol + " creado correctamente";
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
			if (rol.equals(this.rider)) {
				this.riderDAO.deleteByCorreo(correoUsuario);
			}else if (rol.equals(this.client)) {
				this.clientDAO.deleteByCorreo(correoUsuario);
			}else {
				this.adminDAO.deleteByCorreo(correoUsuario);
			}
		}else return this.correo;

		this.userDAO.deleteByCorreo(correoUsuario);
		return "Usuario eliminado correctamente";
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
			this.userDAO.deleteByCorreo(correo);
			this.userDAO.save(nuevo);

			return nuevo;
		}else 
			return nuevo;
	}

	public JSONObject userRider(Rider rid) {
		User user = this.userDAO.findByCorreo(rid.getCorreo());
		JSONObject jso = new JSONObject();
		jso.put("nombre", user.getNombre());
		jso.put("contraseña", user.getPassword());
		jso.put("apellidos", user.getApellidos());
		jso.put(this.correo, correo);
		jso.put("tipoVehiculo", rid.getTipovehiculo());
		jso.put("nif", user.getNif());
		jso.put("carnet", rid.isCarnet());
		jso.put("matricula", rid.getMatricula());
		return jso;	
	}

	public String userRiders(List<Rider> list) {
		String riders = "";
		for (int i = 0; i<list.size(); i++) {
			Rider rid = list.get(i);
			JSONObject jso = this.userRider(rid);
			if (i == list.size() - 1)
				riders = riders + jso.toString();
			else
				riders = riders + jso.toString() + ";";
		}
		riders = riders.replace(" ", "");
		riders = riders.replace("[", "");
		riders = riders.replace("]", "");
		return riders;
	}

	public JSONObject userAdmin(Admin admin) {
		User user = this.userDAO.findByCorreo(admin.getCorreo());
		JSONObject jso = new JSONObject();
		jso.put("nombre", user.getNombre());
		jso.put("contraseña", user.getPassword());
		jso.put("apellidos", user.getApellidos());
		jso.put(this.correo, correo);
		jso.put("nif", user.getNif());
		jso.put("zona",admin.getZona());
		return jso;    
	}

	public String userAdmins(List<Admin> list) {
		String admins = "";
		for (int i = 0; i<list.size(); i++) {
			Admin admin = list.get(i);
			JSONObject jso = this.userAdmin(admin);
			if (i == list.size() - 1)
				admins = admins + jso.toString();
			else
				admins = admins + jso.toString() + ";";
		}
		admins = admins.replace(" ", "");
		admins = admins.replace("[", "");
		admins = admins.replace("]", "");
		return admins;
	}

	public JSONObject userClient(Client client) {
		User user = this.userDAO.findByCorreo(client.getCorreo());
		JSONObject jso = new JSONObject();
		jso.put("nombre", user.getNombre());
		jso.put("contraseña", user.getPassword());
		jso.put("apellidos", user.getApellidos());
		jso.put(this.correo, correo);
		jso.put("nif", user.getNif());
		jso.put("direccion", client.getDireccion());
		jso.put("telefono", client.getTelefono());
		return jso;    
	}

	public String userClients(List<Client> list) {
		String clients = "";
		for (int i = 0; i<list.size(); i++) {
			Client clientt= list.get(i);
			JSONObject jso = this.userClient(clientt);
			if (i == list.size() - 1)
				clients = clients + jso.toString();
			else
				clients = clients + jso.toString() + ";";
		}
		clients = clients.replace(" ", "");
		clients = clients.replace("[", "");
		clients = clients.replace("]", "");
		return clients;
	}

	public List<Rider> consultarRiders(){
		return this.riderDAO.findAll();
	}

	public List<Admin> consultarAdmins(){
		return this.adminDAO.findAll();
	}

	public List<Client> consultarClients(){
		return this.clientDAO.findAll();
	}
}

