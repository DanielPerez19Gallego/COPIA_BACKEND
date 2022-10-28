package Equipo3.TIComo_project.model;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.Document;

import Equipo3.TIComo_project.services.SecurityService;

@Document(collection="Users")
public class User {

	private String correo;
	private String password;
	private String nombre;
	private String apellidos;
	private String nif;
	private String rol;
	
	@Autowired
	private SecurityService secService;

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getPassword(){
		try {
			return this.secService.desencriptar(password);
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			return "";
		}
	}

	public void setPassword(String password){
		try {
			this.password = this.secService.encriptar(password);
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
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
