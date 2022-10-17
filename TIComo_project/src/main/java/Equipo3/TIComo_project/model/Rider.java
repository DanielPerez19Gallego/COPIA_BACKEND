package Equipo3.TIComo_project.model;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="Riders")
public class Rider {

	private String tipovehiculo;
	@Field("Matricula")
	private String matricula;
	private boolean carnet;
	
	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getTipovehiculo() {
		return tipovehiculo;
	}
	
	public void setTipovehiculo(String tipovehiculo) {
		this.tipovehiculo = tipovehiculo;
	}
	
	public boolean isCarnet() {
		return carnet;
	}
	
	public void setCarnet(boolean carnet) {
		this.carnet = carnet;
	}
}
