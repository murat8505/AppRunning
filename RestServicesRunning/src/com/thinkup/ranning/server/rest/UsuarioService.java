package com.thinkup.ranning.server.rest;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.thinkup.ranning.dao.UsuarioDAO;
import com.thinkup.ranning.dtos.Respuesta;
import com.thinkup.ranning.dtos.TipoCuenta;
import com.thinkup.ranning.dtos.UsuarioDTO;
import com.thinkup.ranning.entities.Usuario;
import com.thinkup.ranning.exceptions.PersistenciaException;
import com.thinkup.ranning.server.app.EmailService;
import com.thinkup.ranning.server.app.TokenGenerator;

/**
 * Servicio de usuarios.
 * 
 * @author Dario Camarro
 *
 */
@Path("/usuarios")
public class UsuarioService {

	@Inject
	UsuarioDAO service;

	@Inject
	EmailService emailService;

	/**
	 * Este servicio permite obtener la lista de carreras que se encuentra en la
	 * base de datos.
	 * 
	 * @return Lista de carreras de la base de datos.
	 */
	@Path("/usuariosAll")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Respuesta<List<UsuarioDTO>> getAll() {

		List<UsuarioDTO> usuariosDto = new Vector<>();
		List<Usuario> usuarios = service.getAllUsuarios();
		for (Usuario usuario : usuarios) {
			usuariosDto.add(new UsuarioDTO(usuario));
		}

		Respuesta<List<UsuarioDTO>> r = new Respuesta<List<UsuarioDTO>>();
		r.addMensaje("Operacion ejecutada con éxito.");

		if (usuarios.isEmpty()) {
			r.setCodigoRespuesta(Respuesta.CODIGO_SIN_RESULTADOS);
		} else
			r.setCodigoRespuesta(Respuesta.CODIGO_OK);

		r.setDto(usuariosDto);

		return r;
	}

	/**
	 * Este servicio permite obtener la lista de carreras que se encuentra en la
	 * base de datos.
	 * 
	 * @return Lista de carreras de la base de datos.
	 */
	@Path("/usuariosById/{id}")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Respuesta<UsuarioDTO> findById(@PathParam("id") int id) {

		UsuarioDTO usuarioDto = null;
		Usuario usuario;
		try {
			usuario = service.getById(id);
			if (usuario != null) {
				usuarioDto = new UsuarioDTO(usuario);
			}
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje("Operacion ejecutada con éxito.");
			r.setCodigoRespuesta(Respuesta.CODIGO_OK);
			r.setDto(usuarioDto);
			return r;
		} catch (PersistenciaException e) {
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje(e.getMessage());
			r.setCodigoRespuesta(Respuesta.CODIGO_SIN_RESULTADOS);
			r.setDto(usuarioDto);
			return r;
		}

	}

	/**
	 * Este servicio permite obtener la lista de carreras que se encuentra en la
	 * base de datos.
	 * 
	 * @return Lista de carreras de la base de datos.
	 */
	@Path("/usuariosByEmail/{email}")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public Respuesta<UsuarioDTO> findByEmail(@PathParam("email") String email) {

		UsuarioDTO usuarioDto = null;
		Usuario usuario;
		try {
			usuario = service.getByEmail(email);
			if (usuario != null) {
				usuarioDto = new UsuarioDTO(usuario);
			}
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje("Operacion ejecutada con éxito.");
			r.setCodigoRespuesta(Respuesta.CODIGO_OK);
			r.setDto(usuarioDto);
			return r;
		} catch (Exception e) {
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje(e.getMessage());
			r.setCodigoRespuesta(Respuesta.CODIGO_SIN_RESULTADOS);
			r.setDto(usuarioDto);
			return r;
		}

	}

	/**
	 * Este servicio permite modificar un usuario.
	 * 
	 * @param carrera
	 * @return La respuesta del servicio contiene el codigo de respuesta y los
	 *         mensajes asociados.
	 */
	@Path("/updateUsuario")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Respuesta<UsuarioDTO> updateUsuario(UsuarioDTO usuariosDTO) {
		try {
			service.modificarUsuario(usuariosDTO);
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje("El usuario se modificó con éxito.");
			r.setCodigoRespuesta(Respuesta.CODIGO_CREACION_MODIFICACION_OK);
			r.setDto(usuariosDTO);
			return r;

		} catch (PersistenciaException e) {
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje(e.getMessage());
			r.setCodigoRespuesta(Respuesta.CODIGO_SOLICITUD_INCORRECTA);
			return r;
		}
	}

	/**
	 * Este servicio permite modificar un usuario.
	 * 
	 * @param carrera
	 * @return La respuesta del servicio contiene el codigo de respuesta y los
	 *         mensajes asociados.
	 */
	@Path("/saveUsuario")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Respuesta<UsuarioDTO> saveUsuario(UsuarioDTO usuariosDTO) {
		try {
			if (this.service.getByEmail(usuariosDTO.getEmail()) != null) {
				Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
				r.addMensaje("Usuario Existente");
				r.setCodigoRespuesta(Respuesta.CODIGO_SOLICITUD_INCORRECTA);
				return r;
			}
			Usuario u = null;
			if (usuariosDTO.getTipoCuenta().equals(TipoCuenta.PROPIA.getTipo())) {

				usuariosDTO.setVerificado(false);
				String token = TokenGenerator.getInstance().nextTokenId();
				service.saveUsuario(usuariosDTO, token);
				new EmailSenderTask(usuariosDTO, token).start();

			} else {
				usuariosDTO.setVerificado(true);
				service.saveUsuario(usuariosDTO);

			}

			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje("El usuario se creo con éxito.");
			r.setCodigoRespuesta(Respuesta.CODIGO_OK);
			r.setDto(usuariosDTO);
			return r;

		} catch (PersistenciaException e) {
			Respuesta<UsuarioDTO> r = new Respuesta<UsuarioDTO>();
			r.addMensaje(e.getMessage());
			r.setCodigoRespuesta(Respuesta.CODIGO_SOLICITUD_INCORRECTA);
			return r;
		}
	}

	/**
	 * Este servicio permite obtener la lista de carreras que se encuentra en la
	 * base de datos.
	 * 
	 * @return Lista de carreras de la base de datos.
	 */
	@Path("/token/{email}/{token}")
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	@PermitAll
	public String verificarUsuario(
			@PathParam("email") String email,
			@PathParam("token") String token) {

		UsuarioDTO usuarioDto = null;
		Usuario usuario;
		try {
			usuario = service.getByEmail(email);
			if(usuario!= null && usuario.getFechaVigencia().compareTo(new Date())>0 && usuario.getToken().equals(token)){
				usuario.setVerificado(true);
				service.save(usuario);
				
				return "cofirmado.hmtl";
				
			}else{
				
				return "no_cofirmado.hmtl";
			}
	
		} catch (Exception e) {
			
			return "no_cofirmado.hmtl";
		}

	}

	private class EmailSenderTask extends Thread {
		private UsuarioDTO usuario;
		private String token;

		public EmailSenderTask(UsuarioDTO usuario, String token) {
			super();
			this.usuario = usuario;
			this.token = token;
		}

		@Override
		public void run() {

			emailService.sendConfirmacion(usuario, token);
			// TODO Auto-generated method stub

		}

	}

}
