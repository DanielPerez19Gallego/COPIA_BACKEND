package Equipo3.TIComo_project.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Admins")
public class Admin {

	private String zona;
	private String departamento;
	
	public String getZona() {
		return zona;
	}
	
	public void setZona(String zona) {
		this.zona = zona;
	}

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
}
