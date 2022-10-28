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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import Equipo3.TIComo_project.model.Admin;
import Equipo3.TIComo_project.model.Client;
import Equipo3.TIComo_project.model.Rider;
import Equipo3.TIComo_project.services.SecurityService;
import Equipo3.TIComo_project.services.UserService;


@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService secService;

	private String correo = "correo";

	private String sinAcceso = "No tienes acceso a este servicio"; 

	private String noExiste = "No existe ningun usuario en la base de datos";

	@CrossOrigin
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpSession session , @RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String response = this.userService.login(jso);

			if (response.equals("nulo"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o password desconocidas");
			else {
				session.setAttribute("rol", response);
				session.setAttribute("correo", jso.getString(this.correo));
				session.setAttribute("password", jso.getString("password"));
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@CrossOrigin
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, Object> info, HttpSession session) {
		if (!this.secService.accesoCliente(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		try {
			JSONObject jso = new JSONObject(info);
			String response = "";
			String [] comprobar = this.secService.comprobarPassword(jso);
			if (Boolean.TRUE.equals(Boolean.valueOf(comprobar[0])))
				response = this.userService.register(jso);
			else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, comprobar[1]);

			if (response.equals(this.correo))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/crearUsuario")
	public ResponseEntity<String> crearUsuario(@RequestBody Map<String, Object> info, HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/eliminarUsuario")
	public ResponseEntity<String> eliminarUsuario(@RequestBody Map<String, Object> info, HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		try {
			JSONObject jso = new JSONObject(info);
			String correoUsuario = jso.getString(this.correo);
			String response = this.userService.eliminarUsuario(correoUsuario);

			if (response.equals(this.correo))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PutMapping("/actualizarUsuario/{correo}")
	public ResponseEntity<String> actualizarUsuario( HttpSession session, @PathVariable("correo") String correo,@RequestBody Map<String, Object> info){
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		boolean userUpdate = false;
		JSONObject json = new JSONObject(info);
		userUpdate= this.userService.actualizarUsuario(correo,json);
		if (userUpdate) {
			return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
		}
	}

	@CrossOrigin
	@PutMapping("/actualizarCliente/{correo}")
	public ResponseEntity<String> actualizarCliente( HttpSession session, @PathVariable("correo") String correo,@RequestBody Map<String, Object> info){
		if (!this.secService.accesoCliente(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		boolean userUpdate = false;
		JSONObject json = new JSONObject(info);
		userUpdate= this.userService.actualizarCliente(correo,json);
		if (userUpdate) {
			return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
		}
	}

	@CrossOrigin
	@GetMapping("/getRiders")
	public ResponseEntity<String> consultarRiders(HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		List<Rider> listaResponse;
		try {
			listaResponse = this.userService.consultarRiders();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
			}else {
				return new ResponseEntity<>(this.userService.userRiders(listaResponse), HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@GetMapping("/getAdmins")
	public ResponseEntity<String> consultarAdmins(HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		List<Admin> listaResponse;
		try {
			listaResponse = this.userService.consultarAdmins();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
			}else {
				return new ResponseEntity<>(this.userService.userAdmins(listaResponse), HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@GetMapping("/getClients")
	public ResponseEntity<String> consultarClients(HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		List<Client> listaResponse;
		try {
			listaResponse = this.userService.consultarClients();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
			}else {
				return new ResponseEntity<>(this.userService.userClients(listaResponse), HttpStatus.OK);
			}

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@GetMapping("/logout")
	public void cerrarSesion(HttpSession session) {
		session.invalidate();
	}
}
