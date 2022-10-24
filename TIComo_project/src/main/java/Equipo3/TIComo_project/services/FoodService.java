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
		res.setCategoria(jso.getString("categoria"));
		res.setCif(jso.getString("cif"));
		res.setDireccion(jso.getString("direccion"));
		res.setEmail(jso.getString("email"));
		res.setNombre(jso.getString(this.nombre));
		res.setRazonSocial(jso.getString("razonSocial"));
		res.setTelefono(jso.getString("telefono"));
		this.restDAO.save(res);
		return "Restaurante creado correctamente";
	}

	public String eliminarRestaurante(String nombreRes) {
		Restaurant res = this.restDAO.findByNombre(nombreRes);
		if (res != null) {
			List <Plate> platos = this.platoDAO.findBynombreRestaurante(nombreRes);
			for (int i = 0 ; i<platos.size() ; i++) {
				this.platoDAO.deleteByNombre(platos.get(i).getNombre());
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

}
