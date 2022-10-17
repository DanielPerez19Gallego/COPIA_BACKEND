package Equipo3.TIComo_project.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Users")
public class User {

	private String correo;
	private String password;
	private String nombre;
	private String apellidos;
	private String nif;

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
		this.password = org.apache.commons.codec.digest.DigestUtils.sha512Hex(password);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	public String getNif() {
		return nif;
	}
	
	public void setNif(String nif) {
		this.nif = org.apache.commons.codec.digest.DigestUtils.sha256Hex(nif);
	}
}
