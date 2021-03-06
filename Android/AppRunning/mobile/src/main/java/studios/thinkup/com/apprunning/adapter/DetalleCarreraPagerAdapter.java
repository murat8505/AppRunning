package studios.thinkup.com.apprunning.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.astuetz.PagerSlidingTabStrip;

import studios.thinkup.com.apprunning.R;
import studios.thinkup.com.apprunning.fragment.DetalleCarreraFragment;
import studios.thinkup.com.apprunning.fragment.EstadisticaCarreraFragment;
import studios.thinkup.com.apprunning.fragment.IUsuarioCarreraObservable;

/**
 * Created by fcostazini on 21/05/2015.
 * Pager de Detalle de carrera
 */
public class DetalleCarreraPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {
    private int carrera;
    private IUsuarioCarreraObservable observable;


    public DetalleCarreraPagerAdapter(FragmentManager fm, int idCarrera, IUsuarioCarreraObservable observable) {
        super(fm);
        this.carrera = idCarrera;
        this.observable = observable;

    }

    @Override
    public int getCount() {
        return 2;

    }

    @Override
    public int getPageIconResId(int position) {
        switch (position) {
            case 0:
                return R.drawable.ic_detalle;
            case 1:
                return R.drawable.ic_cronometro;
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "";
            case 1:
                return "";
            default:
                return "";
        }

    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        if (i == 0) {

            DetalleCarreraFragment df = DetalleCarreraFragment.newInstance(this.carrera);

            if( observable!= null){
                observable.registrarObservadorUsuario(df);
                df.setUsuarioObsercable(observable);

            }
            fragment = df;
        } else {
            EstadisticaCarreraFragment ef = EstadisticaCarreraFragment.newInstance(this.carrera);

            if( observable!= null){
                observable.registrarObservadorUsuario(ef);ef.setObservable(observable);
            }
            fragment = ef;
        }

        return fragment;


    }

}
