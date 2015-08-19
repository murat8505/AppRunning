package studios.thinkup.com.apprunning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import studios.thinkup.com.apprunning.model.DefaultSettings;
import studios.thinkup.com.apprunning.model.Filtro;

/**
 * Created by Facundo on 21/07/2015.
 * Funcionalidad para filtrar carreras
 */
public abstract class ResultadosFiltrablesActivity extends ActivityConFiltro {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = this.getFragment();
            Bundle b = new Bundle();
            b.putSerializable(Filtro.FILTRO_ID, this.filtro);
            fragment.setArguments(b);
            fragmentTransaction.replace(R.id.content_fragment, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_filtro_carrera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnu_filtrar) {
            if (this.filtro == null) {

                DefaultSettings ds = this.getDefaultSettings();
                this.filtro = new Filtro(ds);
            }
            Intent intent = new Intent(this, FiltroCarreraActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(Filtro.FILTRO_ID, filtro);
            b.putString("caller", this.getClass().getName());
            intent.putExtras(b);
            this.startActivity(intent);
            overridePendingTransition(R.anim.aparece_desde_la_derecha, R.anim.desaparece_hacia_la_derecha);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void defineContentView() {
        setContentView(R.layout.activity_resultados);
    }

    protected abstract Fragment getFragment();
}
