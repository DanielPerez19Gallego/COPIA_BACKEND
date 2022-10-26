package Equipo3.TIComo_project.services;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.dao.PlateRepository;
import Equipo3.TIComo_project.dao.RestaurantRepository;
import Equipo3.TIComo_project.model.Restaurant;
import Equipo3.TIComo_project.model.Plate;

@Service
public class FoodService {

	@Autowired
	private RestaurantRepository restDAO;

	@Autowired
	private PlateRepository platoDAO;

	private String nombre = "nombre";

	public String crearRestaurante(JSONObject jso) {

		Restaurant resNombre = this.restDAO.findByNombre(jso.getString(this.nombre));
		if (resNombre != null) 
			return this.nombre;
		Restaurant res = new Restaurant();
		res = this.crearRestAux(jso, res);
		this.restDAO.save(res);
		return "Restaurante creado correctamente";
	}

	public String eliminarRestaurante(String nombreRes) {
		Restaurant res = this.restDAO.findByNombre(nombreRes);
		if (res != null) {
			this.platoDAO.deleteBynombreRestaurante(nombreRes);
		}else return this.nombre;

		this.restDAO.deleteByNombre(nombreRes);
		return "Restaurante eliminado correctamente";
	}

	public String actualizarRestaurante(JSONObject jso) {
		String nombreViejo = jso.getString("nombreViejo");
		Restaurant res = this.restDAO.findByNombre(nombreViejo);
		if (res == null) 
			return this.nombre;
		this.restDAO.deleteByNombre(nombreViejo);
		res = this.crearRestAux(jso, res);
		this.restDAO.save(res);
		return "Restaurante actualizado correctamente";
	}

	public Restaurant crearRestAux(JSONObject jso, Restaurant res) {
		res.setCategoria(jso.getString("categoria"));
		res.setCif(jso.getString("cif"));
		res.setDireccion(jso.getString("direccion"));
		res.setEmail(jso.getString("email"));
		res.setNombre(jso.getString(this.nombre));
		res.setRazonSocial(jso.getString("razonSocial"));
		res.setTelefono(jso.getString("telefono"));
		return res;
	}

	public String consultarRestaurantes() {
		StringBuilder bld = new StringBuilder();
		List <Restaurant> restaurantes = this.restDAO.findAll();
		if (!restaurantes.isEmpty()) {
			for (int i=0; i<restaurantes.size(); i++) {
				Restaurant res = restaurantes.get(i);
				JSONObject resJSO = res.toJSON();
				if (i == restaurantes.size() - 1)
					bld.append(resJSO.toString());
				else
					bld.append(resJSO.toString() + ";");
			}
			return bld.toString().replace(" ", "");
		}
		return "";
	}

	public String eliminarCarta(String nombreRes) {
		Restaurant res = this.restDAO.findByNombre(nombreRes);
		if (res == null)
			return this.nombre;
		this.platoDAO.deleteBynombreRestaurante(nombreRes);
		return "Carta eliminada correctamente";

	}

	public String crearPlato(JSONObject jso) {
		String nombrePlato = jso.getString(this.nombre);
		String nombreRestaurante = jso.getString("nombreRestaurante");
		if (this.existePlatoenRestaurante(nombrePlato, nombreRestaurante)) 
			return this.nombre;
		Plate pla = new Plate();
		pla = this.crearPlatoAux(jso, pla);
		pla.setNombreRestaurante(nombreRestaurante);
		platoDAO.save(pla);
		return "Plato creado correctamente";
	}
	
	public boolean existePlatoenRestaurante(String nombreP, String nombreR) {
		List<Plate> platos = this.platoDAO.findByNombreAndRestaurante(nombreP, nombreR);
		return !platos.isEmpty();
	}
	
	public Plate crearPlatoAux(JSONObject jso, Plate pla) {
		pla.setAptoVegano(Boolean.valueOf(jso.getString("aptoVegano")));
		pla.setDescripcion(jso.getString("descripcion"));
		pla.setPrecio(jso.getString("precio"));
		pla.setNombre(jso.getString(this.nombre));
		pla.setFoto(jso.getString("foto"));
		return pla;
	}
	
	public String actualizarPlato(JSONObject jso) {
		String nombreViejo = jso.getString("nombreViejo");
		String nombreNuevo = jso.getString(this.nombre);
		String nombreRestaurante = jso.getString("nombreRestaurante");
		Plate plato = this.platoDAO.findPlato(nombreViejo, nombreRestaurante);
		
		if (plato == null)
			return "noexiste";
		if (this.existePlatoenRestaurante(nombreNuevo, nombreRestaurante)) 
			return this.nombre;
		
		plato = this.crearPlatoAux(jso, plato);
		
		this.platoDAO.deletePlato(nombreViejo, nombreRestaurante);
		this.platoDAO.save(plato);
		
		return "Plato actualizado correctamente";
	}

	public String platosParaEnviar(String nombreRestaurante) {
		StringBuilder bld = new StringBuilder();
		List<Plate> listaPlatos = this.platoDAO.findBynombreRestaurante(nombreRestaurante);
		if(!listaPlatos.isEmpty()) {
			for (int i = 0; i<listaPlatos.size(); i++) {
				Plate plato= listaPlatos.get(i);
				JSONObject jso = plato.toJSON();
				if (i == listaPlatos.size() - 1)
					bld.append(jso.toString());
				else
					bld.append(jso.toString() + ";");
			}
			return bld.toString().replace(" ", "");
		}
		return "";
	}

}
