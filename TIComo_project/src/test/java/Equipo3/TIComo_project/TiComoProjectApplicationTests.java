package Equipo3.TIComo_project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Equipo3.TIComo_project.services.UserService;

@SpringBootTest
class TiComoProjectApplicationTests {

	@Autowired
	private UserService user;
	
	@Test
	void loginTest() throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("pwd", "1234567890");
		jso.put("correo", "dani@gmail");
		JSONObject jsoo = new JSONObject();
		jsoo.put("pwd", "1234567890");
		jsoo.put("correo", "dani@");
		assertEquals("rider", user.login(jso));
		assertEquals("nulo", user.login(jsoo));
	}
	
	@Test
	void comprobarPassword() throws JSONException {
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
		assertEquals("false", user.comprobarPassword(iguales)[0]);
		assertEquals("false", user.comprobarPassword(size)[0]);
		assertEquals("false", user.comprobarPassword(minus)[0]);
		assertEquals("false", user.comprobarPassword(mayus)[0]);
		assertEquals("true", user.comprobarPassword(perfe)[0]);
		assertEquals("false", user.comprobarPassword(digit)[0]);
		
	}

}
