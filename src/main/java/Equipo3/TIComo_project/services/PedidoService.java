package Equipo3.TIComo_project.services;

import java.time.LocalDate;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.dao.PedidoRepository;
import Equipo3.TIComo_project.model.Pedido;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pioDAO;
	
	@Autowired
	private FoodService foodService;
	
	public boolean tienePedidos(String nombreCliente) {
		boolean tiene = true;
		if(this.pioDAO.findAllByCliente(nombreCliente).isEmpty())
			tiene = false;
		return tiene;
	}
	
	public String crearPedido(JSONObject jso) {
		Pedido pedido = new Pedido();
		pedido.setCliente(jso.getString("cliente"));
		pedido.setIdpedido();
		pedido.setFecha(LocalDate.now().toString());
		pedido.setPlatos(jso.getString("platos"));
		pedido.setRestaurante(jso.getString("restaurante"));
		pedido.setRider("rider");
		pedido.setEstado(0);
		this.pioDAO.save(pedido);
		return "Pedido creado correctamente";
	}
	
	public String consultarPedidosCliente(String cliente) {
		List<Pedido> listaPedidos = this.pioDAO.findAllByCliente(cliente);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarTodosPedidos() {
		List<Pedido> listaPedidos = this.pioDAO.findAll();
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidosPre() {
		List<Pedido> listaPedidos = this.pioDAO.findAllByEstado(0);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidosRider(String correoRider) {
		List<Pedido> listaPedidos = this.pioDAO.findAllByRider(correoRider);
		return consultarPedidos(listaPedidos);
	}
	
	public String consultarPedidosRes(String restaurante) {
		List<Pedido> listaPedidos = this.pioDAO.findAllByRestaurante(restaurante);
		return consultarPedidos(listaPedidos);
	}
	
	public void asignarRider(String idPlato, String rider) {
		
	}
	public void ponerEntregado(String idPlato) {
		
	}
	
	public void hacerValoracion(JSONObject jso) {
		
	}
	
	public String consultarValoracion(JSONObject jso) {
		return "";
	}
	
	public String consultarFacturacion(JSONObject jso) {
		return "";
	}
	
	public String consultarPedidos(List<Pedido> listaPedidos) {
		StringBuilder bld = new StringBuilder();
		if(!listaPedidos.isEmpty()) {
			for (int i = 0; i<listaPedidos.size(); i++) {
				Pedido pedi= listaPedidos.get(i);
				JSONObject jso = pedi.toJSON();
				String[] plates = jso.getString("platos").split(",");
				jso.put("platos", this.foodService.listaPlatos(plates));
				if (i == listaPedidos.size() - 1)
					bld.append(jso.toString());
				else
					bld.append(jso.toString() + ";;");
			}
			return bld.toString();
		}
		return "";
	}
}
