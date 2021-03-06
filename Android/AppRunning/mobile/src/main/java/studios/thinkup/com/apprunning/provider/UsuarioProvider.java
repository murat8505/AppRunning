package studios.thinkup.com.apprunning.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Vector;

import studios.thinkup.com.apprunning.model.entity.IEntity;
import studios.thinkup.com.apprunning.model.entity.UsuarioApp;

/**
 * Created by FaQ on 28/05/2015.
 * Implementacion local de IUsuarioProvider
 */
public class UsuarioProvider extends GenericProvider<UsuarioApp> implements IUsuarioProvider {

    public UsuarioProvider(Context c) {
        super(c);
    }

    @Override

        protected String[] getFields(Class<? extends IEntity> clazz) {
            String[] fields = {
                    UsuarioApp.EMAIL,
                    UsuarioApp.ID,
                    UsuarioApp.TIPO_CUENTA,
                    UsuarioApp.NOMBRE
            };
            return fields;
        }


    @Override
    protected String getTableName(Class<? extends IEntity> clazz) {
        return "USUARIO_APP";
    }

    @Override
    public UsuarioApp getUsuarioByEmail(String email) {

        SQLiteDatabase db = null;
        Cursor c = null;

        try {
            db = this.dbProvider.getReadableDatabase();
            String[] params = {email};
           c =  db.query(this.getTableName(UsuarioApp.class), this.getFields(UsuarioApp.class),
                    UsuarioApp.EMAIL + " = ? ", params, null, null, null);
            return toEntity(c);
        } catch (Exception e) {
            return  null;
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }

        }

    }

    @Override
    protected UsuarioApp toEntity(Cursor c) {

        if (c.getCount() <= 0) {
            return null;
        } else {
            c.moveToFirst();
            return getUsuarioApp(c);

        }

    }

    private UsuarioApp getUsuarioApp(Cursor c) {
        UsuarioApp ua = new UsuarioApp();
        try {
            ua.setEmail(c.getString(c.getColumnIndexOrThrow(UsuarioApp.EMAIL)));
            ua.setId(c.getInt(c.getColumnIndexOrThrow(UsuarioApp.ID)));
            ua.setNombre(c.getString(c.getColumnIndexOrThrow(UsuarioApp.NOMBRE)));
            ua.setTipoCuenta(c.getString(c.getColumnIndexOrThrow(UsuarioApp.TIPO_CUENTA)));
            return ua;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    protected List<UsuarioApp> toList(Cursor c) {
        List<UsuarioApp> results = new Vector<>();

        if (c.getCount() <= 0) {
            return results;
        } else {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                results.add(getUsuarioApp(c));
                c.moveToNext();
            }

            return results;
        }

    }
}
