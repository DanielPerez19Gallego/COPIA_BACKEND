package Equipo3.TIComo_project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Equipo3.TIComo_project.model.Admin;
import Equipo3.TIComo_project.model.Client;
import Equipo3.TIComo_project.model.Rider;
import Equipo3.TIComo_project.services.FoodService;
import Equipo3.TIComo_project.services.SecurityService;
import Equipo3.TIComo_project.services.UserService;

@SpringBootTest
class TiComoProjectApplicationTests {
	@Autowired
	private UserService user;

	@Autowired
	private SecurityService secService;


	@Autowired
	private FoodService food;

	@Test
	void testFoodService() throws JSONException {
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


	@Test
	void testUserService() throws JSONException {
		this.registroConSinEliminacionTest();
		this.crearUsuarioAdminTest();
		this.crearUsuarioRiderTest();
		this.comprobarPasswordTest();
		this.userRidersTest();
		this.userAdminsTest();
		this.userClientsTest();

	}
	void registroConSinEliminacionTest() throws JSONException {
		//CrearUsuarioCliente
		JSONObject registro = new JSONObject();
		registro.put("correo", "daniprueba5@gmail");
		registro.put("pwd1", "Clientclient2");
		registro.put("pwd2", "Clientclient2");
		registro.put("apellidos", "client@gmail");
		registro.put("direccion", "direccion");
		registro.put("telefono", "234234");
		registro.put("rol", "client");
		registro.put("nif", "unNif");
		registro.put("nombre", "NombrePrueba");
		assertEquals("Registro completado", user.register(registro));

		JSONObject registro2 = new JSONObject();
		registro2.put("correo", "daniprueba5@gmail");
		registro2.put("pwd1", "Clientclient2");
		registro2.put("pwd2", "Clientclient2");
		registro2.put("apellidos", "client@gmail");
		registro2.put("direccion", "direccion");
		registro2.put("telefono", "234234");
		registro2.put("rol", "client");
		registro2.put("nif", "unNif");
		registro2.put("nombre", "NombrePrueba");
		assertEquals("correo", user.register(registro2));

		JSONObject accesoClient = new JSONObject();
		accesoClient.put("correoAcceso", "daniprueba5@gmail");
		accesoClient.put("passwordAcceso", "Clientclient2");
		assertEquals(true, secService.accesoCliente(accesoClient));
		accesoClient.put("correoAcceso", "sinacceso@gmail");
		assertEquals(false, secService.accesoCliente(accesoClient));

		//Login
		JSONObject jsoooo = new JSONObject();
		jsoooo.put("pwd", "Clientclient2");
		jsoooo.put("correo", "daniprueba5@gmail");
		assertEquals("client", user.login(jsoooo));


		//Actualizar cliente
		String correo4 = "daniprueba5@gmail";
		JSONObject actualizarUsuario4 = new JSONObject();
		actualizarUsuario4.put("rol", "client");
		actualizarUsuario4.put("contraseña", "Clientclient2");
		actualizarUsuario4.put("nif", "asdasd");
		actualizarUsuario4.put("direccion","pilar");
		actualizarUsuario4.put("telefono","12312");
		actualizarUsuario4.put("apellidos", "apellidos");
		actualizarUsuario4.put("nombre", "NombrePrueba");
		assertEquals(true, user.actualizarUsuario(correo4, actualizarUsuario4));

		assertEquals(true, user.actualizarCliente(correo4, actualizarUsuario4));
		assertEquals(false, user.actualizarCliente("amai@gmail", actualizarUsuario4));
		//Eliminar
		String correo2 = "daniprueba5@gmail";
		assertEquals("Usuario eliminado correctamente", user.eliminarUsuario(correo2));

	}

	void crearUsuarioAdminTest() throws JSONException {
		//CrearUsuarioAdmin
		JSONObject registro = new JSONObject();
		registro.put("correo", "adminprueba3@gmail");
		registro.put("pwd1", "Clientclient2");
		registro.put("pwd2", "Clientclient2");
		registro.put("apellidos", "client@gmail");
		registro.put("direccion", "direccion");
		registro.put("telefono", "234234");
		registro.put("rol", "admin");
		registro.put("nif", "unNif");
		registro.put("nombre", "NombrePrueba");
		registro.put("zona", "sanfran");
		assertEquals("admin creado correctamente", user.crearUsuario(registro));
		assertEquals("correo", user.crearUsuario(registro));

		JSONObject accesoAdmin = new JSONObject();
		accesoAdmin.put("correoAcceso", "adminprueba3@gmail");
		accesoAdmin.put("passwordAcceso", "Clientclient2");
		assertEquals(true, secService.accesoAdmin(accesoAdmin));
		accesoAdmin.put("correoAcceso", "sinacceso@gmail");
		assertEquals(false, secService.accesoAdmin(accesoAdmin));
		
		//Login
		JSONObject jsooo = new JSONObject();
		jsooo.put("pwd", "Clientclient2");
		jsooo.put("correo", "adminprueba3@gmail");
		assertEquals("admin", user.login(jsooo));

		//Actualizar admin
		String correo1 = "adminprueba3@gmail";
		JSONObject actualizarUsuario1 = new JSONObject();
		actualizarUsuario1.put("rol", "admin");
		actualizarUsuario1.put("zona", "sanfran2");
		actualizarUsuario1.put("contraseña", "Clientclient2");
		actualizarUsuario1.put("nombre", "nombreTest");
		actualizarUsuario1.put("apellidos","apellidos");
		actualizarUsuario1.put("direccion","direccion");
		actualizarUsuario1.put("telefono","telefono");
		assertEquals(true, user.actualizarUsuario(correo1, actualizarUsuario1));
		assertEquals(false, user.actualizarUsuario("lasmuestras", actualizarUsuario1));
		//Eliminar
		String correo3 = "adminprueba3@gmail";
		assertEquals("Usuario eliminado correctamente", user.eliminarUsuario(correo3));
	}
	void crearUsuarioRiderTest() throws JSONException {
		//CrearUsuarioRider
		JSONObject registroParte2 = new JSONObject();
		registroParte2.put("correo", "riderPrueba2@gmail");
		registroParte2.put("pwd1", "Clientclient2");
		registroParte2.put("pwd2", "Clientclient2");
		registroParte2.put("apellidos", "client@gmail");
		registroParte2.put("direccion", "direccion");
		registroParte2.put("telefono", "234234");
		registroParte2.put("rol", "rider");
		registroParte2.put("nif", "unNif");
		registroParte2.put("nombre", "NombrePrueba");
		registroParte2.put("carnet", "Moto");
		registroParte2.put("matricula", "72722DWR");
		registroParte2.put("tipovehiculo", "coche");
		registroParte2.put("zona", "SanFran");
		assertEquals("rider creado correctamente", user.crearUsuario(registroParte2));

		//Login rider
		JSONObject jso = new JSONObject();
		jso.put("pwd", "Clientclient2");
		jso.put("correo", "riderPrueba2@gmail");
		assertEquals("rider", user.login(jso));
		//Login null
		JSONObject jsoo = new JSONObject();
		jsoo.put("pwd", "1234567890");
		jsoo.put("correo", "dani@");
		assertEquals("nulo", user.login(jsoo));

		//Actualizar rider
		String correoActu = "riderPrueba2@gmail";
		JSONObject actualizarUsuario2 = new JSONObject();
		actualizarUsuario2.put("rol", "rider");
		actualizarUsuario2.put("zona", "sanfran2");
		actualizarUsuario2.put("carnet", "si");
		actualizarUsuario2.put("matricula", "si");
		actualizarUsuario2.put("tipoVehiculo", "si");
		actualizarUsuario2.put("contraseña", "Clientclient2");
		actualizarUsuario2.put("nombre", "asd");
		actualizarUsuario2.put("apellidos", "asd");
		assertEquals(true, user.actualizarUsuario(correoActu, actualizarUsuario2));

		//Eliminar
		String correo2 = "riderPrueba2@gmail";
		assertEquals("Usuario eliminado correctamente", user.eliminarUsuario(correo2));
		assertEquals("correo", user.eliminarUsuario(correo2));


	}

	void comprobarPasswordTest() throws JSONException {
		JSONObject iguales = new JSONObject();
		iguales.put("pwd1", "12");
		iguales.put("pwd2", "21");
		JSONObject size = new JSONObject();
		size.put("pwd1", "12");
		size.put("pwd2", "12");
		JSONObject digit = new JSONObject();
		digit.put("pwd1", "Holaholaa");
		digit.put("pwd2", "Holaholaa");
		JSONObject minus = new JSONObject();
		minus.put("pwd1", "hola4321");
		minus.put("pwd2", "hola4321");
		JSONObject mayus = new JSONObject();
		mayus.put("pwd1", "HOLA1234");
		mayus.put("pwd2", "HOLA1234");
		JSONObject perfe = new JSONObject();
		perfe.put("pwd1", "Hola1234");
		perfe.put("pwd2", "Hola1234");
		assertEquals("false", secService.comprobarPassword(iguales)[0]);
		assertEquals("false", secService.comprobarPassword(size)[0]);
		assertEquals("false", secService.comprobarPassword(minus)[0]);
		assertEquals("false", secService.comprobarPassword(mayus)[0]);
		assertEquals("true", secService.comprobarPassword(perfe)[0]);
		assertEquals("false", secService.comprobarPassword(digit)[0]);

	}

	void userRidersTest()throws JSONException  {
		JSONObject registroParte2 = new JSONObject();
		registroParte2.put("correo", "riderPrueba9@gmail");
		registroParte2.put("pwd1", "Clientclient2");
		registroParte2.put("pwd2", "Clientclient2");
		registroParte2.put("apellidos", "client@gmail");
		registroParte2.put("direccion", "direccion");
		registroParte2.put("telefono", "234234");
		registroParte2.put("rol", "rider");
		registroParte2.put("nif", "unNif");
		registroParte2.put("nombre", "NombrePrueba");
		registroParte2.put("carnet", "Moto");
		registroParte2.put("matricula", "72722DWR");
		registroParte2.put("tipovehiculo", "coche");
		registroParte2.put("zona", "SanFran");
		assertEquals("rider creado correctamente", user.crearUsuario(registroParte2));





		Rider r = new Rider();
		List <Rider> listaRiders = new ArrayList<>();
		r.setCorreo("riderPrueba9@gmail");
		listaRiders.add(r);


		assertNotSame(null, user.userRiders(listaRiders));

		assertNotSame("", user.consultarRiders());

		String correo2 = "riderPrueba9@gmail";
		assertEquals("Usuario eliminado correctamente", user.eliminarUsuario(correo2));
		assertEquals("correo", user.eliminarUsuario(correo2));
	}
	void userAdminsTest()throws JSONException  {
		JSONObject registro = new JSONObject();
		registro.put("correo", "adminPrueba9@gmail");
		registro.put("pwd1", "Clientclient2");
		registro.put("pwd2", "Clientclient2");
		registro.put("apellidos", "client@gmail");
		registro.put("direccion", "direccion");
		registro.put("telefono", "234234");
		registro.put("rol", "admin");
		registro.put("nif", "unNif");
		registro.put("nombre", "NombrePrueba");
		registro.put("zona", "sanfran");
		assertEquals("admin creado correctamente", user.crearUsuario(registro));
		assertEquals("correo", user.crearUsuario(registro));

		Admin ad = new Admin();
		List <Admin> listaAdmins = new ArrayList<>();
		listaAdmins.add(ad);

		ad.setCorreo("adminPrueba9@gmail");
		listaAdmins.add(ad);

		assertNotSame(null, user.userAdmins(listaAdmins));

		assertNotSame("", user.consultarAdmins());

		String correo2 = "adminPrueba9@gmail";
		assertEquals("Usuario eliminado correctamente", user.eliminarUsuario(correo2));
		assertEquals("correo", user.eliminarUsuario(correo2));
	}
	void userClientsTest()throws JSONException  {
		JSONObject registro = new JSONObject();
		registro.put("correo", "daniprueba55@gmail");
		registro.put("pwd1", "Clientclient2");
		registro.put("pwd2", "Clientclient2");
		registro.put("apellidos", "client@gmail");
		registro.put("direccion", "direccion");
		registro.put("telefono", "234234");
		registro.put("rol", "client");
		registro.put("nif", "unNif");
		registro.put("nombre", "NombrePrueba");
		assertEquals("Registro completado", user.register(registro));


		Client cl = new Client();
		List <Client> listaClients = new ArrayList<>();
		listaClients.add(cl);

		cl.setCorreo("daniprueba55@gmail");
		listaClients.add(cl);
		assertNotSame(null, user.userClients(listaClients));

		assertNotSame("", user.consultarClients());

		String correo2 = "daniprueba55@gmail";
		assertEquals("Usuario eliminado correctamente", user.eliminarUsuario(correo2));
		assertEquals("correo", user.eliminarUsuario(correo2));
	}

}