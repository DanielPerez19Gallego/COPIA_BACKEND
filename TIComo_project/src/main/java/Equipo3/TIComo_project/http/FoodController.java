package Equipo3.TIComo_project.http;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import Equipo3.TIComo_project.services.FoodService;

@RestController
@RequestMapping("food")
public class FoodController {

	@Autowired
	private FoodService foodService;
	
	private String nombre = "nombre";
	
	@PostMapping("/crearRestaurante")
	public ResponseEntity<String> crearRestaurante(HttpSession session, @RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String response = this.foodService.crearRestaurante(jso);
			
			if (response.equals(this.nombre))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un restaurante con ese nombre");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@PostMapping("/eliminarRestaurante")
	public ResponseEntity<String> eliminarUsuario(@RequestBody Map<String, Object> info, HttpSession session) {
		try {
			JSONObject jso = new JSONObject(info);
			String nombreRes = jso.getString(this.nombre);
			String response = this.foodService.eliminarRestaurante(nombreRes);
			
			if (response.equals(this.nombre))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un restaurante llamado " + nombreRes);
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	//Prueba para eliminar restaurante
	@PostMapping("/crearPlato")
	public ResponseEntity<String> crearPlato(@RequestBody Map<String, Object> info, HttpSession session) {
		try {
			JSONObject jso = new JSONObject(info);	
			String response = this.foodService.crearPlato(jso);
			if (response.equals(this.nombre))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un plato con ese nombre");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
