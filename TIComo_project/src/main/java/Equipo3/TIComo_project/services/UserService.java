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
	private String admin = "admin";
	private String direccion = "direccion";
	private String carnet = "carnet";
	private String telefono = "telefono";
	private String nombre = "nombre";
	private String matricula = "matricula";
	private String apellidos = "apellidos";
	private String password = "contraseña";

	public String login(JSONObject jso){
		String rol = "nulo";
		User user = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (user != null) {
			if (user.getPassword().equals(jso.getString("pwd"))) {
				if (user.getRol().equals(this.client))
					rol = this.client;
				else if (user.getRol().equals(this.admin))
					rol = this.admin;
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
		clientt.setDireccion(jso.getString(this.direccion));
		clientt.setTelefono(jso.getString(this.telefono));

		this.clientDAO.save(clientt);
		this.userDAO.save(user);
		return "Registro completado";
	}

	public String crearUsuario(JSONObject jso){

		User userEmail = this.userDAO.findByCorreo(jso.getString(this.correo));
		if (userEmail != null) 
			return this.correo;

		String rol = jso.getString("rol");

		User user = crearUsuarioAux(jso);
		user.setRol(rol);

		if (rol.equals(this.rider)) {
			Rider riderr = new Rider();
			riderr.setCarnet(Boolean.valueOf(jso.getString(this.carnet)));
			riderr.setCorreo(jso.getString(this.correo));
			riderr.setMatricula(jso.getString(this.matricula ));
			riderr.setTipovehiculo(jso.getString("tipovehiculo"));
			this.riderDAO.save(riderr);
		} else {
			Admin adminn = new Admin();
			adminn.setCorreo(jso.getString(this.correo));
			adminn.setZona(jso.getString("zona"));
			this.adminDAO.save(adminn);
		}
		this.userDAO.save(user);
		return rol + " creado correctamente";
	}

	public User crearUsuarioAux(JSONObject jso){

		User user = new User();
		user.setCorreo(jso.getString(this.correo));
		user.setPassword(jso.getString("pwd1"));
		user.setApellidos(jso.getString(this.apellidos));
		user.setNif(jso.getString("nif"));
		user.setNombre(jso.getString(this.nombre));
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

	public boolean actualizarUsuario(String correo,JSONObject json){
		if (this.actualizarUser(correo, json)) {
			if (json.getString("rol").equals(this.admin)) {
				Admin adminn = this.adminDAO.findByCorreo(correo);
				adminn.setZona(json.getString("zona"));
			}else if (json.getString("rol").equals(this.rider)) {
				Rider riderr = this.riderDAO.findByCorreo(correo);
				riderr.setCarnet(Boolean.valueOf(json.getString(this.carnet)));
				riderr.setMatricula(json.getString(this.matricula));
				riderr.setTipovehiculo(json.getString("tipoVehiculo"));
			}else {
				this.actualizarCli(correo, json);
			}
			return true;
		}
		return false;
	}

	public boolean actualizarCliente(String correo, JSONObject json){
		if (this.actualizarUser(correo, json)) {
			this.actualizarCli(correo, json);
			return true;
		}
		return false;
	}

	public void actualizarCli(String correo, JSONObject json) {
		Client clientt = this.clientDAO.findByCorreo(correo);
		clientt.setDireccion(json.getString(this.direccion));
		clientt.setTelefono(json.getString(this.telefono));
		this.clientDAO.deleteByCorreo(correo);
		this.clientDAO.save(clientt);
	}

	public boolean actualizarUser(String correo, JSONObject json){
		User nuevo = this.userDAO.findByCorreo(correo);
		if (nuevo!=null) {
			nuevo.setPassword(json.getString(this.password));
			nuevo.setNombre(json.getString(this.nombre));
			nuevo.setApellidos(json.getString(this.apellidos));
			nuevo.setRol(json.getString("rol"));
			this.userDAO.deleteByCorreo(correo);
			this.userDAO.save(nuevo);
			return true;
		} return false;
	}

	public JSONObject userRider(Rider rid) {
		User user = this.userDAO.findByCorreo(rid.getCorreo());
		JSONObject jso = new JSONObject();
		jso.put(this.nombre, user.getNombre());
		jso.put(this.password, user.getPassword());
		jso.put(this.apellidos, user.getApellidos());
		jso.put(this.correo, rid.getCorreo());
		jso.put("tipoVehiculo", rid.getTipovehiculo());
		jso.put("nif", user.getNif());
		jso.put(this.carnet, rid.isCarnet());
		jso.put(this.matricula, rid.getMatricula());
		return jso;	
	}

	public String userRiders(List<Rider> list) {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i<list.size(); i++) {
			Rider rid = list.get(i);
			JSONObject jso = this.userRider(rid);
			if (i == list.size() - 1)
				bld.append(jso.toString());
			else
				bld.append(jso.toString() + ";");
		}
		return bld.toString().replace(" ", "");
	}

	public JSONObject userAdmin(Admin admin) {
		User user = this.userDAO.findByCorreo(admin.getCorreo());
		JSONObject jso = new JSONObject();
		jso.put(this.nombre, user.getNombre());
		jso.put(this.password, user.getPassword());
		jso.put(this.apellidos, user.getApellidos());
		jso.put(this.correo, correo);
		jso.put("nif", user.getNif());
		jso.put("zona",admin.getZona());
		return jso;    
	}

	public String userAdmins(List<Admin> list) {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i<list.size(); i++) {
			Admin adminn = list.get(i);
			JSONObject jso = this.userAdmin(adminn);
			if (i == list.size() - 1)
				bld.append(jso.toString());
			else
				bld.append(jso.toString() + ";");
		}
		return bld.toString().replace(" ", "");
	}

	public JSONObject userClient(Client client) {
		User user = this.userDAO.findByCorreo(client.getCorreo());
		JSONObject jso = new JSONObject();
		jso.put(this.nombre, user.getNombre());
		jso.put(this.password, user.getPassword());
		jso.put(this.apellidos, user.getApellidos());
		jso.put(this.correo, correo);
		jso.put("nif", user.getNif());
		jso.put(this.direccion, client.getDireccion());
		jso.put(this.telefono, client.getTelefono());
		return jso;    
	}

	public String userClients(List<Client> list) {
		StringBuilder bld = new StringBuilder();
		for (int i = 0; i<list.size(); i++) {
			Client clientt= list.get(i);
			JSONObject jso = this.userClient(clientt);
			if (i == list.size() - 1)
				bld.append(jso.toString());
			else
				bld.append(jso.toString() + ";");
		}
		return bld.toString().replace(" ", "");
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

