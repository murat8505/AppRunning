package com.thinkup.ranning.dtos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum Modalidad {

	TODOS("Todas las Modalidades"), 
	EQUIPOS("Equipos"), 
	INDIVIDUAL("Individual"), 
	KIDS("Kids");
	
	private String nombre;

	Modalidad(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	@Override
	public String toString() {
		return nombre;
	}

	public static Modalidad getByName(String nombre) {
		for (Modalidad g : Modalidad.values()) {
			if (g.getNombre().equals(nombre)) {
				return g;
			}
		}
		return null;
	}

	public static String[] getNombres() {
		return new String[] { "Todas las Modalidades", "Equipos", "Individual",
				"Kids" };
	}

}