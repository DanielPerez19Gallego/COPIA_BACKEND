package Equipo3.TIComo_project.http;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import Equipo3.TIComo_project.services.UserService;


@RestController
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;
	
	private String correo = "correo";
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpSession session, @RequestBody Map<String, Object> info) {
		try {
			//session.setMaxInactiveInterval(500);
			JSONObject jso = new JSONObject(info);
			String response = this.userService.login(jso);
			
			if (response.equals("nulo"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o password desconocidas");
			else {
				session.setAttribute(this.correo, jso.getString(this.correo));
				return new ResponseEntity<>("Inicio de sesion correcto como "+ response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@PutMapping("/register")
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
			
			if (response.equals("correo"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo");
			else {
				return new ResponseEntity<>("Todo perfecto", HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
