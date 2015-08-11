package studios.thinkup.com.apprunning;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import studios.thinkup.com.apprunning.adapter.DetalleCarreraPagerAdapter;
import studios.thinkup.com.apprunning.fragment.IUsuarioCarreraObservable;
import studios.thinkup.com.apprunning.fragment.IUsuarioCarreraObserver;
import studios.thinkup.com.apprunning.model.EstadoCarrera;
import studios.thinkup.com.apprunning.model.RunningApplication;
import studios.thinkup.com.apprunning.model.TutorialesPaginaEnum;
import studios.thinkup.com.apprunning.model.entity.UsuarioCarrera;
import studios.thinkup.com.apprunning.provider.IUsuarioCarreraProvider;
import studios.thinkup.com.apprunning.provider.UsuarioCarreraProvider;
import studios.thinkup.com.apprunning.provider.exceptions.EntidadNoGuardadaException;

/**
 * Created by fcostazini on 21/05/2015.
 * Detalle de Carrera
 */
public class DetalleCarreraActivity extends DrawerPagerActivity implements IUsuarioCarreraObservable {

    private UsuarioCarrera carrera;
    private Menu menu;
    private List<IUsuarioCarreraObserver> observadoresUsuario;

    @Override
    protected PagerAdapter getAdapter() {
        return new DetalleCarreraPagerAdapter(getSupportFragmentManager(), this);
    }

    @Override
    public int getTutorialPage() {
        return TutorialesPaginaEnum.DETALLE.getId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.observadoresUsuario == null) {
            this.observadoresUsuario = new Vector<>();
        }
        if (((RunningApplication) this.getApplication()).getUsuario() == null) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }
        startUp(savedInstanceState);

        findViewById(R.id.viewpager).setBackground(null);
    }

    private void startUp(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("carrera")) {
                this.carrera = (UsuarioCarrera) savedInstanceState.getSerializable("carrera");
            }
        }
        if (this.carrera == null && this.getIntent().getExtras().containsKey("carrera")) {
            this.carrera = (UsuarioCarrera) this.getIntent().getExtras().getSerializable("carrera");

        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.clear();
        this.menu = menu;

        getMenuInflater().inflate(R.menu.menu_detalle_carrera, menu);

        if (this.carrera != null) {
            if (this.carrera.isMeGusta()) {
                menu.getItem(0).setIcon(R.drawable.ic_me_gusta);
            } else {
                menu.getItem(0).setIcon(R.drawable.ic_no_me_gusta);
            }
            if (this.carrera.isAnotado()) {
                menu.getItem(1).setIcon(R.drawable.ic_anotado);
            } else {
                menu.getItem(1).setIcon(R.drawable.ic_no_anotado);
            }
            if (this.carrera.isCorrida()) {
                menu.getItem(2).setIcon(R.drawable.ic_corrida);
            } else {
                menu.getItem(2).setIcon(R.drawable.ic_no_corrida);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (this.carrera == null) {
            return super.onOptionsItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.mnu_me_gusta:


                if (!this.carrera.isMeGusta()) {

                    item.setIcon(R.drawable.ic_me_gusta);
                    this.carrera.setMeGusta(true);
                    this.actualizarUsuarioCarrera(this.carrera, EstadoCarrera.ME_GUSTA);
                } else {


                    item.setIcon(R.drawable.ic_no_me_gusta);
                    this.carrera.setMeGusta(false);
                    this.actualizarUsuarioCarrera(this.carrera, EstadoCarrera.NO_ME_GUSTA);
                }
                updateUsuarioCarrera();
                return true;
            case R.id.mnu_inscripto:
                if (this.carrera.isAnotado()) {
                    desanotarCarrera(item);
                } else {
                    if (this.carrera.getDistancias().contains("/") && this.carrera.getDistancia()== null) {
                        new SelectorCarrera(this.carrera, new ISeleccionHandler() {
                            @Override
                            public void onSelected(Integer distancia) {
                                item.setIcon(R.drawable.ic_anotado);
                                if (distancia >= 0) {
                                    String distSeleccionada = carrera.getDistancias().split("/")[distancia];
                                    marcarAnotada(Integer.valueOf(distSeleccionada.trim()), menu, true);
                                }
                            }
                        }, this).seleccionarCarrera();
                    } else {
                        marcarAnotada(Integer.valueOf(this.carrera.getDistancias().replace("Km", "").trim()), menu, true);
                    }

                }
                updateUsuarioCarrera();
                return true;
            case R.id.mnu_corrida:
                if (this.carrera.isCorrida()) {
                    confirmarNoCorrida(item);
                } else {
                    if (this.carrera.getFechaInicio().compareTo(new Date()) <= 0) {
                        if (this.menu != null) {
                            if (this.carrera.getDistancias().contains("/")) {
                                new SelectorCarrera(this.carrera, new ISeleccionHandler() {
                                    @Override
                                    public void onSelected(Integer distancia) {
                                        if (distancia >= 0) {
                                            String distSeleccionada = carrera.getDistancias().split("/")[distancia];
                                            Integer distanciaElegida = Integer.valueOf(distSeleccionada.replace("Km", "").trim());
                                            marcarCorrida(distanciaElegida, menu);
                                        }
                                    }
                                }, this).seleccionarCarrera();
                            } else {
                                marcarCorrida(Integer.valueOf(this.carrera.getDistancias().replace("Km", "").trim()), menu);
                            }
                        }
                    }
                }
                updateUsuarioCarrera();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void marcarCorrida(Integer distanciaElegida, Menu menu) {
        marcarAnotada(distanciaElegida, menu, false);
        menu.getItem(2).setIcon(R.drawable.ic_corrida);
        carrera.setCorrida(true);

        actualizarUsuarioCarrera(carrera, EstadoCarrera.CORRIDA);
    }

    private void marcarAnotada(Integer distanciaElegida, Menu menu, Boolean actualizar) {
        menu.getItem(1).setIcon(R.drawable.ic_anotado);
        carrera.setAnotado(true);
        carrera.setDistancia(distanciaElegida);
        if (actualizar) {

            actualizarUsuarioCarrera(carrera, EstadoCarrera.ANOTADO);
        }
    }

    @Override
    public void updateUsuarioCarrera() {
        try {
            IUsuarioCarreraProvider up = new UsuarioCarreraProvider(this, this.getUsuario());
            up.actualizarCarrera(this.carrera);
        } catch (EntidadNoGuardadaException e) {
            e.printStackTrace();
        }
    }

    private void confirmarNoCorrida(final MenuItem item) {

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Desmarcar Corrida");
        dialog.setMessage(getString(R.string.confirmar_no_corrida));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                item.setIcon(R.drawable.ic_no_corrida);
                carrera.setCorrida(false);
                carrera.setTiempo(0l);
                carrera.setVelocidad(0);
                actualizarUsuarioCarrera(carrera, EstadoCarrera.NO_CORRIDA);

            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

            }
        });
        dialog.setIcon(R.drawable.ic_corrida);
        dialog.show();


    }

    private void desanotarCarrera(final MenuItem item) {
        if (!this.carrera.isCorrida()) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Borrar Carreras");
            dialog.setMessage(getString(R.string.confirmar_Desanotar));
            dialog.setCancelable(false);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Si", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int buttonId) {
                    item.setIcon(R.drawable.ic_no_anotado);
                    carrera.setAnotado(false);
                    carrera.setTiempo(0l);
                    updateUsuarioCarrera();
                    actualizarUsuarioCarrera(carrera, EstadoCarrera.NO_ANOTADO);
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int buttonId) {

                }
            });
            dialog.setIcon(R.drawable.ic_anotado);
            dialog.show();

        }
    }


    @Override
    public void registrarObservadorUsuario(IUsuarioCarreraObserver ob) {
        if (this.observadoresUsuario == null) {
            this.observadoresUsuario = new Vector<>();
        }
        this.observadoresUsuario.add(ob);
    }

    private void actualizarUsuarioCarrera(UsuarioCarrera usuarioCarrera, EstadoCarrera estado) {

        for (IUsuarioCarreraObserver ob : this.observadoresUsuario) {
            ob.actuliazarUsuarioCarrera(usuarioCarrera, estado);
        }
    }

    @Override
    public UsuarioCarrera getUsuarioCarrera() {
        return this.carrera;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.getUsuarioCarrera() != null) {
            outState.putSerializable("carrera", this.carrera);
        }
    }

    public interface ISeleccionHandler {
        void onSelected(Integer distancia);

    }

    public class SelectorCarrera {

        AlertDialog seleccionDialog;
        private UsuarioCarrera carrera;
        private ISeleccionHandler handler;
        private Context context;

        public SelectorCarrera(UsuarioCarrera carrera, ISeleccionHandler handler, Context context) {
            this.carrera = carrera;
            this.handler = handler;
            this.context = context;
        }

        public void setOnSelectedHandler(ISeleccionHandler handler) {
            this.handler = handler;
        }

        public void seleccionarCarrera() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(this.context.getString(R.string.que_distancia_recorres));
            String[] distancias = this.carrera.getDistancias().replace("Km", "").split("/");
            int i = 0;
            for (String s : distancias) {
                s = s.trim();
                if (Integer.valueOf(s.trim()) < 10) {

                    s = " " + s;
                }
                distancias[i] = s + " Km";
                i++;
            }
            builder.setSingleChoiceItems(distancias, -1, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {

                    if (handler != null) {
                        handler.onSelected(item);
                    }
                    seleccionDialog.dismiss();
                }
            });

            seleccionDialog = builder.create();
            seleccionDialog.setIcon(R.drawable.ic_anotado);
            seleccionDialog.show();
        }
    }


}

