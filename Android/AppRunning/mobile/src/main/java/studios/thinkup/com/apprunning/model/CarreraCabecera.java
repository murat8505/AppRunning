package studios.thinkup.com.apprunning.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by fcostazini on 22/05/2015.
 * Cabecera de de carrera
 */
public class CarreraCabecera implements Serializable{
    private String nombre;
    private Date fechaInicio;
    private String distancia;
    private String descripcion;
    private String urlImage;

    public CarreraCabecera(String nombre, Date fechaInicio, String distancia, String descripcion, String urlImage) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.distancia = distancia;
        this.descripcion = descripcion;
        this.urlImage = urlImage;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
