package Equipo3.TIComo_project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import Equipo3.TIComo_project.dao.UserRepository;
import Equipo3.TIComo_project.services.UserService;


@SpringBootTest
class TiComoProjectApplicationTests {
	private UserService us;
	
	public void escenario() {
		us = new UserService();
		 UserRepository userDAO;
	}
	
	
	
	
	
	@Test
	public void testLogin() throws JSONException {
		JSONObject jso = new JSONObject();
		jso.put("correo","dani@gmail");
		jso.put("rol", "rider");
		escenario();
		assertEquals("rider",us.login(jso));
	}

}
