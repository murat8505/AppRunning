package studios.thinkup.com.apprunning.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import studios.thinkup.com.apprunning.fragment.BusquedaFormulario;
import studios.thinkup.com.apprunning.fragment.RecomendadosFragment;
import studios.thinkup.com.apprunning.model.Filtro;

/**
 * Created by fcostazini on 21/05/2015.
 * Adapter de Fragmentos para Busqueda
 */
public class BusquedaPagerAdapter extends FragmentPagerAdapter {
    private Filtro filtro;
    public BusquedaPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "RECOMENDADAS";
            case 1: return "FORMULARIO";
            default: return "";
        }
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        // Crear un FoodFragment con el nombre como argumento
        if(i==0){
            fragment = RecomendadosFragment.newInstance();
        }else{
            fragment = BusquedaFormulario.newInstance();
        }


        return fragment;



    }
}
