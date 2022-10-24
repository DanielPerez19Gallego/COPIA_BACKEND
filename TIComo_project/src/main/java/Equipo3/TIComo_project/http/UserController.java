package Equipo3.TIComo_project.http;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import Equipo3.TIComo_project.model.Admin;
import Equipo3.TIComo_project.model.Client;
import Equipo3.TIComo_project.model.Rider;
import Equipo3.TIComo_project.model.User;
import Equipo3.TIComo_project.services.UserService;
import ch.qos.logback.core.recovery.ResilientSyslogOutputStream;


@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	private String correo = "correo";

	private String rol = "rol";
	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpSession session, @RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String response = this.userService.login(jso);

			if (response.equals("nulo"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o password desconocidas");
			else {
				session.setAttribute(this.correo, jso.getString(this.correo));
				System.out.println("gñe");
				return new ResponseEntity<>("Inicio de sesion correcto como "+ response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, Object> info, HttpSession session) {
		try {
			JSONObject jso = new JSONObject(info);
			String pwd1 = jso.getString("pwd1");
			String pwd2 = jso.getString("pwd2");

			if (!pwd1.equals(pwd2))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas tienen que ser iguales");
			else {
				if(pwd1.length() < 8)
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña tiene que tener minimo 8 caracteres");
			}

			String response = this.userService.register(jso);

			if (response.equals(this.correo))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo");
			else {
				return new ResponseEntity<>("Registro completado", HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/crearUsuario")
	public ResponseEntity<String> crearUsuario(@RequestBody Map<String, Object> info, HttpSession session) {
		try {
			JSONObject jso = new JSONObject(info);
			String pwd1 = jso.getString("pwd1");
			String pwd2 = jso.getString("pwd2");

			if (!pwd1.equals(pwd2))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las contraseñas tienen que ser iguales");
			else {
				if(pwd1.length() < 8)
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña tiene que tener minimo 8 caracteres");
			}

			String response = this.userService.crearUsuario(jso);

			if (response.equals(this.correo))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo");
			else {
				return new ResponseEntity<>(jso.getString("rol")+" creado correctamente", HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/eliminarUsuario")
	public ResponseEntity<String> eliminarUsuario(@RequestBody Map<String, Object> info, HttpSession session) {
		try {
			JSONObject jso = new JSONObject(info);
			String correoUsuario = jso.getString(this.correo);
			String response = this.userService.eliminarUsuario(correoUsuario);

			if (response.equals(this.correo))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un usuario con ese correo");
			else {
				return new ResponseEntity<>("Usuario eliminado correctamente", HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@CrossOrigin
	@GetMapping("/getRiders")//Devuelve todos los Users
	public ResponseEntity<String> consultarRiders() {
		List<Rider> listaResponse;
		try {
			listaResponse = this.userService.consultarRiders();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe ningun usuario en la base de datos");
			}else {
				JSONObject jso = new JSONObject(); //Mando la lista con todos los users en un JSON.
				return new ResponseEntity<>(jso.put("Riders",listaResponse)+"Todos los usuarios actuales de la base de datos.", HttpStatus.OK);
			}

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@CrossOrigin
	@GetMapping("/getAdmins")//Devuelve todos los Users
	public ResponseEntity<String> consultarAdmins() {
		List<Admin> listaResponse;
		try {
			listaResponse = this.userService.consultarAdmins();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe ningun usuario en la base de datos");
			}else {
				JSONObject jso = new JSONObject(); //Mando la lista con todos los users en un JSON.
				return new ResponseEntity<>(jso.put("Admins",listaResponse)+"Todos los usuarios actuales de la base de datos.", HttpStatus.OK);
			}

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@CrossOrigin
	@GetMapping("/getClients")//Devuelve todos los Users
	public ResponseEntity<String> consultarClients() {
		List<Client> listaResponse;
		try {
			listaResponse = this.userService.consultarClients();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe ningun usuario en la base de datos");
			}else {
				JSONObject jso = new JSONObject(); //Mando la lista con todos los users en un JSON.
				return new ResponseEntity<>(jso.put("Clientes",listaResponse)+"Todos los usuarios actuales de la base de datos.", HttpStatus.OK);
			}

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PutMapping("/actualizarUsuario/{correo}")
	public ResponseEntity<String> actualizarUsuario(@PathVariable("correo") String correo,@RequestBody Map<String, Object> info){
		boolean userUpdate = false;
		JSONObject json = new JSONObject(info);
		 userUpdate= this.userService.actualizarUsuario(correo,json);
		if (userUpdate) {
			return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe ese usuario en la base de datos");
		}

	}
}



