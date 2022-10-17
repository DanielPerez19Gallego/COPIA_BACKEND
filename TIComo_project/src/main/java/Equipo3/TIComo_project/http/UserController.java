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
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpSession session, @RequestBody Map<String, Object> info) {
		try {
			session.setMaxInactiveInterval(500);
			JSONObject jso = new JSONObject(info);
			JSONObject response = this.userService.login(jso);
			String resp = response.getString("errorMessage");
			if (resp.equals("Response status 400"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario o password desconocidas");
			else {
				session.setAttribute("userName", jso.getString("name"));
				return new ResponseEntity<>("Inicio de sesion correcto", HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	@PutMapping("/register")
	public ResponseEntity<String> register(@RequestBody Map<String, Object> info, HttpSession session) {
		try {
			JSONObject jso = new JSONObject(info);
			String response = this.userService.register(jso);
			
			if (!response.equals("perfe"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese email");
			else {
				return new ResponseEntity<>("Todo perfecto", HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
