package studios.thinkup.com.apprunning.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import studios.thinkup.com.apprunning.IconTextView;
import studios.thinkup.com.apprunning.R;
import studios.thinkup.com.apprunning.TemporizadorActivity;
import studios.thinkup.com.apprunning.components.CustomNumberPickerView;
import studios.thinkup.com.apprunning.model.EstadoCarrera;
import studios.thinkup.com.apprunning.model.RunningApplication;
import studios.thinkup.com.apprunning.model.entity.UsuarioCarrera;
import studios.thinkup.com.apprunning.provider.IUsuarioCarreraProvider;
import studios.thinkup.com.apprunning.provider.TypefaceProvider;
import studios.thinkup.com.apprunning.provider.UsuarioCarreraProvider;
import studios.thinkup.com.apprunning.provider.UsuarioProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstadisticaCarreraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadisticaCarreraFragment extends Fragment implements View.OnClickListener, IUsuarioCarreraObserver {
    private LinearLayout aCorrer;
    private UsuarioProvider usuarioProvider;
    private Dialog dialog;
    private TextView tiempo;
    private IconTextView editar;
    private IUsuarioCarreraObservable usuarioObservable;
    public EstadisticaCarreraFragment() {
        // Required empty public constructor
    }
    public void setObservable(IUsuarioCarreraObservable usuarioObservable){
        this.usuarioObservable = usuarioObservable;
    }
    public static EstadisticaCarreraFragment newInstance(int idCarrera) {
        EstadisticaCarreraFragment fragment = new EstadisticaCarreraFragment();
        Bundle args = new Bundle();
        args.putInt(UsuarioCarrera.class.getSimpleName(), idCarrera);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_estadistica_carrera, container, false);


        if(this.usuarioObservable == null){
            this.usuarioObservable = (IUsuarioCarreraObservable) this.getActivity();
        }
        initView(rootView);
        //aCorrer.setOnClickListener(this);
        aCorrer.setOnTouchListener(new View.OnTouchListener() {
            private static final int MAX_CLICK_DURATION = 200;
            private long startClickTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN: {
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                        EstadisticaCarreraFragment.this.aCorrer.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        EstadisticaCarreraFragment.this.aCorrer.getBackground().clearColorFilter();
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if (clickDuration < MAX_CLICK_DURATION) {
                            EstadisticaCarreraFragment.this.onClickACorrer();
                        }
                        return true;
                    }
                    case MotionEvent.ACTION_CANCEL: {
                        EstadisticaCarreraFragment.this.aCorrer.getBackground().clearColorFilter();
                        return true;
                    }

                }
                return true;
            }

        });

        return rootView;
    }

    private void initView(View rootView) {
        this.editar = (IconTextView) rootView.findViewById(R.id.icon_edit_time);

        this.aCorrer = (LinearLayout) rootView.findViewById(R.id.lb_a_correr);
        editar.setOnClickListener(this);
        updateEstadoEdicionTiempo();

        Typeface type = TypefaceProvider.getInstance(this.getActivity()).getTypeface(TypefaceProvider.DIGIT);
        this.tiempo = (TextView) rootView.findViewById(R.id.txt_tiempo);

        actualizarValores();
        tiempo.setTypeface(type);
    }

    private void updateEstadoEdicionTiempo() {
        editar.setVisibility(View.INVISIBLE);
        aCorrer.setVisibility(View.GONE);
        if (this.usuarioObservable.getUsuarioCarrera().isAnotado()){
            if(!this.usuarioObservable.getUsuarioCarrera().isCorrida() &&
                    this.usuarioObservable.getUsuarioCarrera().getTiempo() <= 0){
                aCorrer.setVisibility(View.VISIBLE);
            }
            editar.setVisibility(View.VISIBLE);
        }
    }


    protected void onClickACorrer() {
        Intent i = new Intent(this.getActivity().getApplicationContext(), TemporizadorActivity.class);
        i.putExtra(UsuarioCarrera.class.getSimpleName(), this.usuarioObservable.getUsuarioCarrera().getId());
        this.getActivity().startActivity(i);
        this.getActivity().finish();

    }


    @Override
    public void onClick(View v) {
// custom dialog

        this.dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.time_picker_fragment);
        dialog.setTitle("Ingrese su tiempo");
        dialog.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // set the custom dialog components - text, image and button
        CustomNumberPickerView hr = (CustomNumberPickerView) dialog.findViewById(R.id.np_hr);

        CustomNumberPickerView min = (CustomNumberPickerView) dialog.findViewById(R.id.np_min);
        CustomNumberPickerView sec = (CustomNumberPickerView) dialog.findViewById(R.id.np_sec);

        hr.setNumeroVal((int) (this.usuarioObservable.getUsuarioCarrera().getTiempo() / 3600000));
        min.setNumeroVal((int) (this.usuarioObservable.getUsuarioCarrera().getTiempo() - hr.getNumeroVal() * 3600000) / 60000);
        sec.setNumeroVal((int) (this.usuarioObservable.getUsuarioCarrera().getTiempo() - hr.getNumeroVal() * 3600000 - min.getNumeroVal() * 60000) / 1000);



        IconTextView dialogOk = (IconTextView) dialog.findViewById(R.id.ic_save);
        IconTextView dialogCancel = (IconTextView) dialog.findViewById(R.id.ic_cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int h = ((CustomNumberPickerView) dialog.findViewById(R.id.np_hr)).getNumeroVal();
                int m = ((CustomNumberPickerView) dialog.findViewById(R.id.np_min)).getNumeroVal();
                int s = ((CustomNumberPickerView) dialog.findViewById(R.id.np_sec)).getNumeroVal();

                long tiempo = (s * 1000) + (m * 60000) + (h * 3600000);
                EstadisticaCarreraFragment.this.usuarioObservable.getUsuarioCarrera().setTiempo(tiempo);
                actualizarValores();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void actualizarValores() {
        if (this.usuarioObservable.getUsuarioCarrera().getTiempo() >= 0) {
            int h = (int) (this.usuarioObservable.getUsuarioCarrera().getTiempo() / 3600000);
            int m = (int) (this.usuarioObservable.getUsuarioCarrera().getTiempo() - h * 3600000) / 60000;
            int s = (int) (this.usuarioObservable.getUsuarioCarrera().getTiempo() - h * 3600000 - m * 60000) / 1000;

            String tiempoStr = (h < 10 ? "0" + h : h + "");
            tiempoStr += ":" + (m < 10 ? "0" + m : m + "");
            tiempoStr += ":" + (s < 10 ? "0" + s : s + "");

            tiempo.setText(tiempoStr);
            updateEstadoEdicionTiempo();
        }
    }


    @Override
    public void actuliazarUsuarioCarrera(UsuarioCarrera usuario, EstadoCarrera estado) {
        updateEstadoEdicionTiempo();
        actualizarValores();

    }
}
