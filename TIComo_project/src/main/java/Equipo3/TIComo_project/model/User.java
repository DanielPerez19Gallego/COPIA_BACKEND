package Equipo3.TIComo_project.model;



import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Users")
public class User {

	private String correo;
	private String password;

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
