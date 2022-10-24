package Equipo3.TIComo_project.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Plates")

public class Plate {

	private String nombre;
	private String foto;
	private String descripcion;
	private String precio;
	private boolean aptoVegano;
	private String correo;
	public String getNombre() {
		return nombre;
	}
	
	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getFoto() {
		return foto;
	}
	
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getPrecio() {
		return precio;
	}
	
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	
	public boolean isAptoVegano() {
		return aptoVegano;
	}
	
	public void setAptoVegano(boolean aptoVegano) {
		this.aptoVegano = aptoVegano;
	}
}

