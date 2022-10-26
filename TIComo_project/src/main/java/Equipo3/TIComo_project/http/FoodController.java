package Equipo3.TIComo_project.http;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<String> crearRestaurante(@RequestBody Map<String, Object> info) {
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
	public ResponseEntity<String> eliminarUsuario(@RequestBody Map<String, Object> info) {
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

	

	@PostMapping("/actualizarRestaurante")
	public ResponseEntity<String> actualizarRestaurante(@RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);	
			String response = this.foodService.actualizarRestaurante(jso);
			if (response.equals(this.nombre))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un restaurante con ese nombre");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/consultarRestaurantes")
	public ResponseEntity<String> consultarRestaurantes() {
		try {	
			String response = this.foodService.consultarRestaurantes();
			if (response.length() == 0)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existen restaurantes");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/crearPlato")
	public ResponseEntity<String> crearPlato(@RequestBody Map<String, Object> info){
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
	
	@PostMapping("/actualizarPlato")
	public ResponseEntity<String> actualizarPlato(@RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);	
			String response = this.foodService.actualizarPlato(jso);
			if (response.equals(this.nombre))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un plato con ese nombre");
			else if (response.equals("noexiste"))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un plato con ese nombre");
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/getCarta/{nombreRestaurante}")
	public ResponseEntity <String> consultarCarta(@PathVariable String nombreRestaurante){//Nombre de restaurantes y me devuelve un array de platos en formato String.
		try {
			String listaPlatos= this.foodService.platosParaEnviar(nombreRestaurante);
			if(listaPlatos.length()==0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El restaurante "+nombreRestaurante+" no tiene carta");
			}
			return new ResponseEntity<>(listaPlatos, HttpStatus.OK);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@PostMapping("/eliminarCarta/{restaurante}")
	public ResponseEntity<String> eliminarCarta(@PathVariable String restaurante) {
		try {
			String response = this.foodService.eliminarCarta(restaurante);

			if (response.equals(this.nombre))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un restaurante llamado " + restaurante);
			else {
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
