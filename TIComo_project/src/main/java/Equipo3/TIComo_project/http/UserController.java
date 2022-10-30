package Equipo3.TIComo_project.http;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<String> login(@RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String response = this.userService.login(jso);

			if (response.equals("nulo"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o password desconocidas");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@CrossOrigin
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, Object> info) {
		JSONObject jso = new JSONObject(info);
		
		if (!this.secService.accesoCliente(jso))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		try {
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
	public ResponseEntity<String> crearUsuario(@RequestBody Map<String, Object> info) {
		JSONObject jso = new JSONObject(info);
		
		if (!this.secService.accesoAdmin(jso))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		try {
			String response = "";
			String [] comprobar = this.secService.comprobarPassword(jso);
			if (Boolean.TRUE.equals(Boolean.valueOf(comprobar[0])))
				response = this.userService.crearUsuario(jso);

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
	public ResponseEntity<String> eliminarUsuario(@RequestBody Map<String, Object> info) {
		JSONObject jso = new JSONObject(info);
		
		if (!this.secService.accesoAdmin(jso))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		try {
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
	@PostMapping("/actualizarUsuario/{correo}")
	public ResponseEntity<String> actualizarUsuario(@PathVariable("correo") String correo,@RequestBody Map<String, Object> info) {
		JSONObject json = new JSONObject(info);
		
		if (!this.secService.accesoAdmin(json))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		boolean userUpdate = false;
		userUpdate= this.userService.actualizarUsuario(correo,json);
		if (userUpdate) {
			return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
		}
	}

	@CrossOrigin
	@PostMapping("/actualizarCliente/{correo}")
	public ResponseEntity<String> actualizarCliente(@PathVariable("correo") String correo,@RequestBody Map<String, Object> info){
		JSONObject json = new JSONObject(info);
		
		if (!this.secService.accesoCliente(json))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		boolean userUpdate = false;
		userUpdate= this.userService.actualizarCliente(correo,json);
		if (userUpdate) {
			return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
		}else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.noExiste);
		}
	}

	@CrossOrigin
	@PostMapping("/getRiders")
	public ResponseEntity<String> consultarRiders(@RequestBody Map<String, Object> info) {
		JSONObject json = new JSONObject(info);
		
		if (!this.secService.accesoAdmin(json))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		List<Rider> listaResponse;
		try {
			listaResponse = this.userService.consultarRiders();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				return new ResponseEntity<>("", HttpStatus.OK);
			}else {
				return new ResponseEntity<>(this.userService.userRiders(listaResponse), HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/getAdmins")
	public ResponseEntity<String> consultarAdmins(@RequestBody Map<String, Object> info) {
		JSONObject json = new JSONObject(info);
		
		if (!this.secService.accesoAdmin(json))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		List<Admin> listaResponse;
		try {
			listaResponse = this.userService.consultarAdmins();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				return new ResponseEntity<>("", HttpStatus.OK);
			}else {
				return new ResponseEntity<>(this.userService.userAdmins(listaResponse), HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/getClients")
	public ResponseEntity<String> consultarClients(@RequestBody Map<String, Object> info) {
		JSONObject json = new JSONObject(info);
		
		if (!this.secService.accesoAdmin(json))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
		List<Client> listaResponse;
		try {
			listaResponse = this.userService.consultarClients();  //Recojo la lista en una variable.
			if(listaResponse.isEmpty()) {
				return new ResponseEntity<>("", HttpStatus.OK);
			}else {
				return new ResponseEntity<>(this.userService.userClients(listaResponse), HttpStatus.OK);
			}

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
