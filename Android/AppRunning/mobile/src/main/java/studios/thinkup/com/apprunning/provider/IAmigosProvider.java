package studios.thinkup.com.apprunning.provider;

import java.util.List;

import studios.thinkup.com.apprunning.model.entity.Amigo;

/**
 * Created by Facundo on 11/08/2015.
 * Provider de amigos
 */
public interface IAmigosProvider {
    /**
     * Obtiene todos los amigos de un Amigo
     * @param id del usuario
     * @return lista vacia en caso de no encontrar resultados
     */
    List<Amigo> getAmigosByUsuarioId(Integer id);


    /**
     * Obtiene todos los Amigo que tengan nombre o email que cumplan con "LIKE param%"
     * @param parametro para buscar
     * @return lista vacia en caso de no encontrar resultados
     */
    List<Amigo> getUsuarios(String parametro);

    /**
     * Actualiza el estado de un amigo con relacion al usuario
     * @param param
     * @param idUsuario del usuario que solicita
     * @return
     */
    Integer actualizarEstadoAmigo(Amigo param, Integer idUsuario);

}