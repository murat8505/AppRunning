package studios.thinkup.com.apprunning.provider.restProviders;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.Vector;

import studios.thinkup.com.apprunning.model.Filtro;
import studios.thinkup.com.apprunning.model.RunningApplication;
import studios.thinkup.com.apprunning.model.entity.CarreraCabecera;
import studios.thinkup.com.apprunning.model.entity.UsuarioCarrera;
import studios.thinkup.com.apprunning.provider.CarreraCabeceraProvider;
import studios.thinkup.com.apprunning.provider.IUsuarioCarreraProvider;
import studios.thinkup.com.apprunning.provider.UsuarioCarreraProvider;

/**
 * Created by Facundo on 29/07/2015.
 * Servicio Para obtener una carrera de Usuario por id
 */
public class UsuarioCarreraService extends AsyncTask<Integer, Integer, UsuarioCarrera> {

    private OnSingleResultHandler<UsuarioCarrera> handler;
    private Context context;
    private Integer idUsuario;

    public UsuarioCarreraService(OnSingleResultHandler<UsuarioCarrera> handler, Context context, Integer idUsuario) {
        this.handler = handler;
        this.context = context;
        this.idUsuario = idUsuario;
    }

    @Override
    protected void onPostExecute(UsuarioCarrera usuarioCarrera) {
        super.onPostExecute(usuarioCarrera);
        if (usuarioCarrera != null) {
            this.handler.actualizarResultado(usuarioCarrera);
        }
    }

    @Override
    protected UsuarioCarrera doInBackground(Integer... id) {
        IUsuarioCarreraProvider cp = new UsuarioCarreraProvider(this.context, idUsuario);
        return cp.getByIdCarrera(id[0]);

    }
}


