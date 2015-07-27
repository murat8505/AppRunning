package com.thinkup.ranning.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.thinkup.ranning.dtos.CarreraDTO;
import com.thinkup.ranning.dtos.Filtro;
import com.thinkup.ranning.entities.Carrera;
import com.thinkup.ranning.exceptions.PersistenciaException;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CarreraDAO {

	@PersistenceContext(unitName = "appRunningPostgreDS")
	private EntityManager entityManager;

	public Carrera findCarreraWithNumero(int nro) throws PersistenciaException {
		try {
			Carrera c = this.entityManager
					.createNamedQuery(Carrera.QUERY_BY_NRO_CARRERA,
							Carrera.class)
					.setParameter(Carrera.PARAM_NRO_CARRERA, nro)
					.getSingleResult();

			return c;
		} catch (NoResultException e) {
			String mensaje = "No existe carrera con el numero" + nro;
			throw new PersistenciaException(mensaje, e);
		}
	}

	public List<Carrera> getAllCarreras() {
		List<Carrera> carreras = this.entityManager.createNamedQuery(
				Carrera.QUERY_ALL, Carrera.class).getResultList();
		return carreras;
	}

	public void saveCarrera(Carrera carrera) throws PersistenciaException {
		try {
			this.entityManager.persist(carrera);
		} catch (Exception e) {
			throw new PersistenciaException("No se pude persistir", e);
		}
	}

	public List<CarreraDTO> getCarrerasDTO(Filtro filtro) {
		String query = this.crearQuery();
		@SuppressWarnings("unchecked")
		List<CarreraDTO> carreras = this.entityManager.createNativeQuery(
				query, CarreraDTO.class).getResultList();
		
		return carreras;
	}

	private String crearQuery() {
		
		StringBuffer  select = new StringBuffer();
		select.append(" SELECT c.id as codigoCarrera, c.nombre as nombre, c.fecha_inicio as fechaInicio, EXTRACT(HOUR FROM c.fecha_inicio)||':'||EXTRACT(MINUTE  FROM c.fecha_inicio) AS hora, c.distancia_disponible as distanciaDisponible, c.descripcion as descripcion, c.url_imagen as urlImagen, ");
		select.append(" c.provincia as provincia, c.ciudad as ciudad, uc.id as usuarioCarrera, uc.distancia as distancia, uc.me_gusta as meGusta, uc.anotado as estoyAnotado, uc.corrida as corrida ");
		select.append(" FROM public.carrera c ");
		select.append(" inner join public.usuario_carrera uc on uc.carrera_id = c.id ");
		
		return select.toString();
	}
}