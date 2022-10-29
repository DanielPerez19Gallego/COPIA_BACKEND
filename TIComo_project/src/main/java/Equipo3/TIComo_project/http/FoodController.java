package Equipo3.TIComo_project.http;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import Equipo3.TIComo_project.services.FoodService;
import Equipo3.TIComo_project.services.SecurityService;

@RestController
@RequestMapping("food")
public class FoodController {

	@Autowired
	private FoodService foodService;
	
	@Autowired
	private SecurityService secService;

	private String nombre = "nombre";

	private String sinAcceso = "No tienes acceso a este servicio";

	@CrossOrigin
	@PostMapping("/crearRestaurante")
	public ResponseEntity<String> crearRestaurante(@RequestBody Map<String, Object> info, HttpSession session) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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

	@CrossOrigin
	@PostMapping("/eliminarRestaurante")
	public ResponseEntity<String> eliminarUsuario(@RequestBody Map<String, Object> info, HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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

	@CrossOrigin
	@PostMapping("/actualizarRestaurante")
	public ResponseEntity<String> actualizarRestaurante(@RequestBody Map<String, Object> info, HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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

	@CrossOrigin
	@GetMapping("/consultarRestaurantes")
	public ResponseEntity<String> consultarRestaurantes() {
		try {	
			String response = this.foodService.consultarRestaurantes();
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/crearPlato")
	public ResponseEntity<String> crearPlato(@RequestBody Map<String, Object> info, HttpSession session){
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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
	
	@CrossOrigin
	@PostMapping("/actualizarPlato")
	public ResponseEntity<String> actualizarPlato(@RequestBody Map<String, Object> info, HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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

	@CrossOrigin
	@GetMapping("/getCarta/{nombreRestaurante}")
	public ResponseEntity <String> consultarCarta(@PathVariable String nombreRestaurante){//Nombre de restaurantes y me devuelve un array de platos en formato String.
		try {
			String listaPlatos= this.foodService.platosParaEnviar(nombreRestaurante);
			if(listaPlatos.length()==0)
				return new ResponseEntity<>("", HttpStatus.OK);
			return new ResponseEntity<>(listaPlatos, HttpStatus.OK);
		}catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@CrossOrigin
	@PostMapping("/eliminarCarta/{restaurante}")
	public ResponseEntity<String> eliminarCarta(@PathVariable String restaurante, HttpSession session) {
		if (!this.secService.accesoAdmin(session))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.sinAcceso);
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
