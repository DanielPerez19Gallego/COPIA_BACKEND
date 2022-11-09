package Equipo3.TIComo_project.http;

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

import Equipo3.TIComo_project.services.PedidoService;
import Equipo3.TIComo_project.services.SecurityService;

@RestController
@RequestMapping("pedido")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private SecurityService secService;

	private String nombre = "nombre";

	private String sinAcceso = "No tienes acceso a este servicio";
	
	private String inActivo = "Tu cuenta no se encuentra activa";

	@CrossOrigin
	@PostMapping("/consultarPedidos{cliente}")
	public ResponseEntity<String> consultarPedidos(@RequestBody Map<String, Object> info, @PathVariable String cliente) {
		try {
			JSONObject jso = new JSONObject(info);
			if (this.secService.accesoCliente(jso)) {
				if(this.secService.isActivo(jso.getString("correoAcceso"))){
					String response = this.pedidoService.consultarPedidosCliente(cliente);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
				return new ResponseEntity<>(this.inActivo, HttpStatus.OK);
			}
			return new ResponseEntity<>(this.sinAcceso, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@CrossOrigin
	@PostMapping("/crearPedido")
	public ResponseEntity<String> crearPedido(@RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			if (this.secService.accesoCliente(jso)) {
				if(this.secService.isActivo(jso.getString("correoAcceso"))){
					return new ResponseEntity<>(this.pedidoService.crearPedido(jso), HttpStatus.OK);
				}
				return new ResponseEntity<>(this.inActivo, HttpStatus.OK);
			}
			return new ResponseEntity<>(this.sinAcceso, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	@CrossOrigin
	@PostMapping("/consultarTodosPedidos")
	public ResponseEntity<String> consultarTodosPedidos(@RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			if (this.secService.accesoAdmin(jso)) {
				String response = this.pedidoService.consultarTodosPedidos();
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			return new ResponseEntity<>(this.sinAcceso, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
}
