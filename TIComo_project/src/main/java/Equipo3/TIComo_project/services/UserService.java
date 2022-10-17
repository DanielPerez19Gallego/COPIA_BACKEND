package Equipo3.TIComo_project.services;

import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Equipo3.TIComo_project.model.User;
import Equipo3.TIComo_project.dao.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userDAO;
	
	public JSONObject login(JSONObject jso) {
		JSONObject json = new JSONObject();
		return json;
	}
	
	public String register(JSONObject jso) {
		User user = new User();/*
		User userEmail = this.userDAO.findByEmail(jso.getString(this.emaill));
		User userName = this.userDAO.findByuserName(jso.getString("userName"));
		if (userEmail != null) 
			return "email";
		else if (userName != null)
			return "name";
		else {
			user.setUserName(jso.getString("userName"));
			user.setEmail(jso.getString(this.emaill));
			user.setPwd(jso.getString("pwd1"));
			user.setToken(UUID.randomUUID().toString());
			user.setHora(System.currentTimeMillis());
			user.setConfirmado(false);
			*/
			user.setCorreo(jso.getString("userName"));
			user.setPassword(jso.getString("pwd1"));
			this.userDAO.save(user);
			return "perfe";
		}
}

