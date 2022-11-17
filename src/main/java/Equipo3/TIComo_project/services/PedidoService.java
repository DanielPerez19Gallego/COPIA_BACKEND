package Equipo3.TIComo_project.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.dao.PedidoRepository;
import Equipo3.TIComo_project.dao.RestaurantRepository;
import Equipo3.TIComo_project.dao.RiderRepository;
import Equipo3.TIComo_project.dao.ValoracionRepository;
import Equipo3.TIComo_project.model.Pedido;
import Equipo3.TIComo_project.model.Valoracion;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pioDAO;
	
	@Autowired
	private ValoracionRepository valDAO;
	
	@Autowired
	private RestaurantRepository resDAO;
	
	@Autowired
	private RiderRepository ridDAO;
	
	@Autowired
	private FoodService foodService;
	
	private String noexiste = "No existe ese pedido";

	public String crearPedido(JSONObject jso) {
		Pedido pedido = new Pedido();
		String res = jso.getString("restaurante");
		if (this.resDAO.findByNombre(res) != null) {
			pedido.setCliente(jso.getString("correoAcceso"));
			pedido.setIdpedido();
			pedido.setFecha(LocalDate.now().toString());
			pedido.setPlatos(jso.getString("platos"));
			pedido.setRestaurante(res);
			pedido.setRider("");
			pedido.setEstado(0);
			this.pioDAO.save(pedido);
			return "Pedido creado correctamente";
		}
		return "No existe restaurante con ese nombre";
	}
	
	public String cancelarPedido(String idPedido, String cliente) {
		Pedido pedi = this.pioDAO.findByidPedido(idPedido);
		if (pedi != null) {
			if(pedi.getEstado() != 0)
				return "Ya no puedes cancelar el pedido";
			if(pedi.getCliente().equals(cliente))
				return "No puedes cancelar el pedido, no es tuyo";
			this.pioDAO.deleteByidPedido(idPedido);
			return "Pedido cancelado";
		}
		return this.noexiste;
	}
	
	public String actualizarPedido(String idPedido, String cliente) {
		Pedido pedi = this.pioDAO.findByidPedido(idPedido);
		if (pedi != null) {
			if(pedi.getEstado() != 0)
				return "Ya no puedes actualizar el pedido";
			if(pedi.getCliente().equals(cliente))
				return "No puedes actualizar el pedido, no es tuyo";
			//implentacion de esto q tiene su miga
		}
		return this.noexiste;
	}
	
	public String consultarPedidosCliente(String cliente) {
		List<Pedido> listaPedidos = this.pioDAO.findAllByCliente(cliente);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarTodosPedidos() {
		List<Pedido> listaPedidos = this.pioDAO.findAll();
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidosPre(String restaurante) {
		if(this.resDAO.findByNombre(restaurante) == null)
			return "No existe ese restaurante";
		List<Pedido> listaPedidos = this.pioDAO.findAllByRestauranteAndEstado(restaurante, 0);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidosRider(String correoRider) {
		List<Pedido> listaPedidos = this.pioDAO.findAllByRider(correoRider);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidosRes(String restaurante) {
		if(this.resDAO.findByNombre(restaurante) == null)
			return "No existe ese restaurante";
		List<Pedido> listaPedidos = this.pioDAO.findAllByRestaurante(restaurante);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidos(List<Pedido> listaPedidos) {
		StringBuilder bld = new StringBuilder();
		if(!listaPedidos.isEmpty()) {
			for (int i = 0; i<listaPedidos.size(); i++) {
				Pedido pedi= listaPedidos.get(i);
				JSONObject jso = pedi.toJSON();
				if (i == listaPedidos.size() - 1)
					bld.append(jso.toString());
				else
					bld.append(jso.toString() + ";;;");
			}
			return bld.toString().replace(String.valueOf((char)92), "");
		}
		return "";
	}
	
	public String asignarRider(String idPedido, String rider) {
		Pedido pedi = this.pioDAO.findByidPedido(idPedido);
		if (pedi!=null) {
			if (pedi.getEstado() == 1)
				return "El pedido ya se ha asignado";
			else if (pedi.getEstado() == 2)
				return "El pedido ya se ha entregado";
			pedi.setEstado(1);
			pedi.setRider(rider);
			this.pioDAO.deleteByidPedido(idPedido);
			this.pioDAO.save(pedi);
			return "Rider asignado";
		}
		return this.noexiste;
	}
	public String ponerEntregado(String idPedido, String rider) {
		Pedido pedi = this.pioDAO.findByidPedido(idPedido);
		if (pedi!=null) {
			if (pedi.getRider().equals(rider)) {
				if(pedi.getEstado() == 0)
					return "Debes asignarte primero";
				else if (pedi.getEstado() == 2)
					return "El pedido ya se ha entregado";
				pedi.setEstado(2);
				this.pioDAO.deleteByidPedido(idPedido);
				this.pioDAO.save(pedi);
				return "Pedido entregado";
			}
			return "No te corresponde entregar este pedido";
		}
		return this.noexiste;
	}
	
	public void hacerValoracion(JSONObject jso) {
		Valoracion valora = new Valoracion();
		valora.setAutor(jso.getString("correoAcceso"));
		valora.setComentario(jso.getString("comentario"));
		valora.setEntidad(jso.getString("entidad"));
		valora.setFecha();
		valora.setValor(Integer.parseInt(jso.getString("valor")));
		this.valDAO.save(valora);
	}
	
	public String consultarValoracionRes(String nombreRes) {
		if(this.resDAO.findByNombre(nombreRes) == null)
			return "No existe ese resturante";
		return consultarValoracion(nombreRes);
	}
	
	public String consultarValoracionRider(String rider) {
		if(this.ridDAO.findByCorreo(rider) == null)
			return "No existe ese rider";
		return consultarValoracion(rider);
	}
	
	public String consultarValoracion(String entidad) {
		List<Valoracion> valoraciones = this.valDAO.findAllByEntidad(entidad);
		if (!valoraciones.isEmpty()) {
			StringBuilder bld = new StringBuilder();
			for(int i=0; i<valoraciones.size();i++) {
				Valoracion val = valoraciones.get(i);
				JSONObject jso = val.toJSON(val.getEntidad());
				if (i == valoraciones.size() - 1)
					bld.append(jso.toString());
				else
					bld.append(jso.toString() + ";;;");
			}
			return bld.toString();
		}
		return entidad + " no tiene valoraciones";
	}
	
	public String consultarFacturacion(JSONObject jso) {
		LocalDate fechaInicio = LocalDate.parse(jso.getString("fechaInicio"));
		LocalDate fechaFinal = LocalDate.parse(jso.getString("fechaFinal"));
		String nombreRes = jso.getString("restaurante");
		if(this.resDAO.findByNombre(nombreRes) != null) {
			List<Pedido> pedidos = this.pioDAO.findAllByRestaurante(nombreRes);
			if (!pedidos.isEmpty()) {
				List<Pedido> pedidosValidos = pedidosEntre(fechaInicio, fechaFinal, pedidos);
				if (!pedidosValidos.isEmpty()) 
					return "Facturacion: " + calcularFacturacion(pedidosValidos);
				return "No hay pedidos entre esas fechas";
			}
			return "El restaurante no tiene pedidos";
		}
		return "No existe ese restaurante";
	}
	
	public float calcularFacturacion(List<Pedido> pedidosValidos) {
		float precio = 0;
		for (int i =0; i<pedidosValidos.size(); i++) {
			Pedido ped = pedidosValidos.get(i);
			String platos = ped.getPlatos();
			if(!platos.equals("")) {
				precio = precio + this.foodService.precioPlatos(platos);
			}
		}
		return precio;
	}

	public List<Pedido> pedidosEntre(LocalDate fechaInicio, LocalDate fechaFinal, List<Pedido> pedidos){
		List<Pedido> pedidosValidos = new ArrayList<>();
		for (int i= 0; i<pedidos.size(); i++) {
			Pedido ped = pedidos.get(i);
			LocalDate fecha = LocalDate.parse(ped.getFecha());
			if ((fecha.isBefore(fechaFinal) || fecha.isEqual(fechaFinal)) && (fecha.isAfter(fechaInicio) || fecha.isEqual(fechaInicio))) {
				pedidosValidos.add(ped);
			}
		}
		return pedidosValidos;
	}

}
