package studios.thinkup.com.apprunning.fragment;


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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;

import studios.thinkup.com.apprunning.IconTextView;
import studios.thinkup.com.apprunning.R;
import studios.thinkup.com.apprunning.TemporizadorActivity;
import studios.thinkup.com.apprunning.components.CustomNumberPickerView;
import studios.thinkup.com.apprunning.model.Carrera;
import studios.thinkup.com.apprunning.provider.TypefaceProvider;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EstadisticaCarreraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EstadisticaCarreraFragment extends Fragment implements View.OnClickListener {
private LinearLayout aCorrer;
private Carrera carrera;
    public static EstadisticaCarreraFragment newInstance(Carrera carrera) {
        EstadisticaCarreraFragment fragment = new EstadisticaCarreraFragment();
        Bundle args = new Bundle();
        args.putSerializable(Carrera.class.getSimpleName(), carrera);
        fragment.setArguments(args);
        return fragment;
    }

    public EstadisticaCarreraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.carrera = (Carrera)getArguments().getSerializable(Carrera.class.getSimpleName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_estadistica_carrera, container, false);
        IconTextView editar = (IconTextView)rootView.findViewById(R.id.icon_edit_time);

        this.aCorrer = (LinearLayout) rootView.findViewById(R.id.lb_a_correr);
        editar.setOnClickListener(this);
        if(this.carrera.isEstoyInscripto() && !this.carrera.isFueCorrida()){
            aCorrer.setVisibility(View.VISIBLE);

        }else{
            aCorrer.setVisibility(View.GONE);
        }
        Typeface type = TypefaceProvider.getInstance(this.getActivity()).getTypeface(TypefaceProvider.DIGIT);
        TextView tiempo = (TextView) rootView.findViewById(R.id.txt_tiempo);
        tiempo.setTypeface(type);
        if(this.carrera.getTiempo()>0) {
            int h = (int) (this.carrera.getTiempo() / 3600000);
            int m = (int) (this.carrera.getTiempo() - h * 3600000) / 60000;
            int s = (int) (this.carrera.getTiempo() - h * 3600000 - m * 60000) / 1000;
            int ms = (int) (this.carrera.getTiempo() - h * 3600000 - m * 60000 - s * 1000) / 10;
            String tiempoStr = (h < 10 ? "0" + h : h + "");
            tiempoStr += ":" + (m < 10 ? "0" + m : m + "");
            tiempoStr += ":" + (s < 10 ? "0" + s : s + "");
            tiempoStr += ":" + (ms < 10 ? "0" + ms : ms + "");
            tiempo.setText(tiempoStr);
            aCorrer.setVisibility(View.GONE);
            editar.setVisibility(View.VISIBLE);
        }


        //aCorrer.setOnClickListener(this);
        aCorrer.setOnTouchListener(new View.OnTouchListener() {
            private static final int MAX_CLICK_DURATION = 200;
            private long startClickTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:{
                        startClickTime = Calendar.getInstance().getTimeInMillis();
                       EstadisticaCarreraFragment.this.aCorrer.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        return true;
                    }
                    case MotionEvent.ACTION_UP: {
                        EstadisticaCarreraFragment.this.aCorrer.getBackground().clearColorFilter();
                        long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                        if(clickDuration < MAX_CLICK_DURATION) {
                            EstadisticaCarreraFragment.this.onClickACorrer();
                        }
                        return true;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        EstadisticaCarreraFragment.this.aCorrer.getBackground().clearColorFilter();
                        return true;
                    }

                }
                return true;
            }

        });

        return rootView;
    }



    protected void onClickACorrer() {
       Intent i = new Intent(this.getActivity(), TemporizadorActivity.class);
        i.putExtra(Carrera.class.getSimpleName(), this.carrera);
        this.getActivity().startActivity(i);

    }


    @Override
    public void onClick(View v) {
// custom dialog
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.setContentView(R.layout.time_picker_fragment);
        dialog.setTitle("Ingrese su tiempo");

        // set the custom dialog components - text, image and button
        CustomNumberPickerView hr = (CustomNumberPickerView) dialog.findViewById(R.id.np_hr);

        CustomNumberPickerView min = (CustomNumberPickerView) dialog.findViewById(R.id.np_min);
        CustomNumberPickerView sec = (CustomNumberPickerView) dialog.findViewById(R.id.np_sec);
        CustomNumberPickerView ms = (CustomNumberPickerView) dialog.findViewById(R.id.np_ms);

        if(this.carrera.getTiempo()>0){
            hr.setNumeroVal((int)(this.carrera.getTiempo() / 3600000));
            min.setNumeroVal((int) (this.carrera.getTiempo() - h * 3600000) / 60000);
            sec.setNumeroVal((int) (this.carrera.getTiempo() - h * 3600000 - m * 60000) / 1000);
            ms.setNumeroVal((int)  (this.carrera.getTiempo() - h * 3600000 - m * 60000 - s * 1000) / 10);

        }



        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
