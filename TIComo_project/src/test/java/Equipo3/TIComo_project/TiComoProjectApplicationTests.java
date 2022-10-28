package Equipo3.TIComo_project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Equipo3.TIComo_project.services.FoodService;

@SpringBootTest
class TiComoProjectApplicationTests {

	
	@Autowired
	private FoodService food;
	
	@Test
	void testFoodService() throws JSONException {
		assertEquals("", food.consultarRestaurantes());
		this.crearRestauranteTest();
		assertNotSame("", food.consultarRestaurantes());
		assertEquals("", food.platosParaEnviar("prueba"));
		this.crearPlatoTest();
		assertNotSame("", food.platosParaEnviar("prueba"));
		this.actualizarPlatoTest();
		this.actualizarRestauranteTest();
		this.eliminarCartaTest();
		this.eliminarRestauranteTest();
	}

	void crearRestauranteTest() throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("nombre", "comoti");
		jso.put("categoria", "hola");
		jso.put("cif", "cif");
		jso.put("direccion", "ticomo");
		jso.put("email", "hola");
		jso.put("razonSocial", "ticomo");
		jso.put("telefono", "hola");
		assertEquals("Restaurante creado correctamente", food.crearRestaurante(jso));
		jso.put("nombre","prueba");
		assertEquals("Restaurante creado correctamente", food.crearRestaurante(jso));
		assertEquals("nombre", food.crearRestaurante(jso));
		
	}

	void crearPlatoTest() throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("nombre", "platoPrueba1");
		jso.put("aptoVegano", "true");
		jso.put("descripcion", "cif");
		jso.put("precio", "ticomo");
		jso.put("nombreRestaurante", "prueba");
		jso.put("foto", "ticomo");
		assertEquals("Plato creado correctamente", food.crearPlato(jso));
		assertEquals("nombre", food.crearPlato(jso));
		jso.put("nombre", "platoPrueba2");
		assertEquals("Plato creado correctamente", food.crearPlato(jso));
	}

	void actualizarPlatoTest() throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("nombreViejo", "noexiste");
		jso.put("aptoVegano", "true");
		jso.put("descripcion", "cif");
		jso.put("precio", "ticomo");
		jso.put("nombreRestaurante", "prueba");
		jso.put("foto", "ticomo");
		jso.put("nombre", "Pizza");
		assertEquals("noexiste", food.actualizarPlato(jso));
		jso.put("nombreViejo", "platoPrueba2");
		jso.put("nombre", "platoPrueba1");
		assertEquals("nombre", food.actualizarPlato(jso));
		jso.put("nombre", "Pizza");
		assertEquals("Plato actualizado correctamente", food.actualizarPlato(jso));
		
	}
	
	void actualizarRestauranteTest() throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("nombre", "noexiste");
		jso.put("categoria", "hola");
		jso.put("cif", "cif");
		jso.put("direccion", "ticomo");
		jso.put("email", "hola");
		jso.put("razonSocial", "ticomo");
		jso.put("telefono", "hola");
		assertEquals("nombre", food.actualizarRestaurante(jso));
		jso.put("nombre", "comoti");
		assertEquals("Restaurante actualizado correctamente", food.actualizarRestaurante(jso));
	}
	
	void eliminarCartaTest() throws JSONException {
		assertEquals("Carta eliminada correctamente", food.eliminarCarta("prueba"));
		assertEquals("nombre", food.eliminarCarta("noexiste"));
	}
	
	void eliminarRestauranteTest() throws JSONException {
		assertEquals("Restaurante eliminado correctamente", food.eliminarRestaurante("prueba"));
		assertEquals("Restaurante eliminado correctamente", food.eliminarRestaurante("comoti"));
		assertEquals("nombre", food.eliminarRestaurante("noexiste"));
	}

}