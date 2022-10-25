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
			List <Plate> platos = this.platoDAO.findBynombreRestaurante(nombreRes);
			if (!platos.isEmpty()) {
				for (int i = 0 ; i<platos.size() ; i++) {
					this.platoDAO.deleteByNombre(platos.get(i).getNombre());
				}
			}
		}else return this.nombre;

		this.restDAO.deleteByNombre(nombreRes);
		return "Restaurante eliminado correctamente";
	}

	public String crearPlato(JSONObject jso) {
		Plate plaNombre = this.platoDAO.findByNombre(jso.getString(this.nombre));
		if (plaNombre != null) 
			return this.nombre;
		Plate pla = new Plate();
		pla.setAptoVegano(Boolean.valueOf(jso.getString("aptoVegano")));
		pla.setDescripcion(jso.getString("descripcion"));
		pla.setPrecio(jso.getString("precio"));
		pla.setNombreRestaurante(jso.getString("nombreRestaurante"));
		pla.setNombre(jso.getString(this.nombre));
		pla.setFoto(jso.getString("foto"));
		platoDAO.save(pla);
		return "Plato creado correctamente";
	}

	public String actualizarRestaurante(JSONObject jso) {
		String nombreViejo = jso.getString("nombreViejo");
		String nombreNuevo = jso.getString(this.nombre);
		Restaurant res = this.restDAO.findByNombre(nombreViejo);
		if (res == null) 
			return this.nombre;

		List <Plate> platos = this.platoDAO.findBynombreRestaurante(nombreViejo);
		if (!platos.isEmpty()) {
			for (int i = 0 ; i<platos.size() ; i++) {
				Plate plato = platos.get(i);
				this.platoDAO.deleteByNombre(plato.getNombre());
				plato.setNombreRestaurante(nombreNuevo);
				this.platoDAO.save(plato);
			}
		}

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
		List <Restaurant> restaurantes = this.restDAO.findAll();
		String response = "";
		if (!restaurantes.isEmpty()) {
			for (int i=0; i<restaurantes.size(); i++) {
				Restaurant res = restaurantes.get(i);
				JSONObject resJSO = res.toJSON();
				if (i == restaurantes.size() - 1)
					response = response + resJSO.toString();
				else
					response = response + resJSO.toString() + ";";
			}
			response = response.replace(" ", "");
			response = response.replace("[", "");
			response = response.replace("]", "");
		}
		return response;
	}

	public String eliminarCarta(String nombreRes) {
		Restaurant res = this.restDAO.findByNombre(nombreRes);
		if (res == null)
			return this.nombre;
		List <Plate> platos = this.platoDAO.findBynombreRestaurante(nombreRes);
		if (!platos.isEmpty()) {
			for (int i = 0 ; i<platos.size() ; i++) {
				Plate plato = platos.get(i);
				this.platoDAO.deleteByNombre(plato.getNombre());
			}
		}
		return "Carta eliminada correctamente";

	}

	public String actualizarPlato(JSONObject jso) {
		String nombreViejo = jso.getString("nombreViejo");
		String nombreNuevo = jso.getString(this.nombre);
		Plate plato = this.platoDAO.findByNombre(nombreViejo);
		
		if (plato == null) 
			return this.nombre;
		
		plato.setAptoVegano(Boolean.valueOf(jso.getString("aptoVegano")));
		plato.setDescripcion(jso.getString("descripcion"));
		plato.setFoto(jso.getString("foto"));
		plato.setNombreRestaurante(jso.getString("nombreRestaurante"));
		plato.setNombre(nombreNuevo);
		plato.setPrecio(jso.getString("precio"));
		
		this.platoDAO.deleteByNombre(nombreViejo);
		this.platoDAO.save(plato);
		
		return "Plato actualizado correctamente";
	}

	public String platosParaEnviar(String nombreRestaurante) {
		String listPlatos = "";
		List<Plate> listaPlatos = this.platoDAO.findBynombreRestaurante(nombreRestaurante);
		if(!listaPlatos.isEmpty()) {
			for (int i = 0; i<listaPlatos.size(); i++) {
				Plate plato= listaPlatos.get(i);
				JSONObject jso = plato.toJSON();
				if (i == listaPlatos.size() - 1)
					listPlatos = listPlatos + jso.toString();
				else
					listPlatos = listPlatos + jso.toString() + ";";
			}
			listPlatos = listPlatos.replace(" ", "");
			listPlatos = listPlatos.replace("[", "");
			listPlatos = listPlatos.replace("]", "");
		}
		return listPlatos;
	}

}
