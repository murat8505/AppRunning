package studios.thinkup.com.apprunning.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import studios.thinkup.com.apprunning.R;
import studios.thinkup.com.apprunning.model.Carrera;

/**
 * Created by fcostazini on 21/05/2015.
 * Detalle de la carrera
 */
public class DetalleCarreraFragment extends Fragment {
    private Carrera carrera;

    public static DetalleCarreraFragment newInstance(Carrera carrera) {
        DetalleCarreraFragment fragment = new DetalleCarreraFragment();
        Bundle args = new Bundle();
        args.putSerializable(Carrera.class.getSimpleName(), carrera);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detalle_carrera, container, false);
        if (getArguments() != null) {
            this.carrera = (Carrera) getArguments().getSerializable(Carrera.class.getSimpleName());
        }
        TextView txtNombre = (TextView) rootView.findViewById(R.id.txt_nombre_carrera);
        if (this.carrera == null) {
            //Sin RESULTADO
            txtNombre.setText(this.getActivity().getResources().getString(R.string.sin_resultados));
            return rootView;
        }

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyyy", Locale.getDefault());
        if (this.carrera.getUrlImage() != null && !this.carrera.getUrlImage().isEmpty()) {
            ImageView logo = (ImageView) rootView.findViewById(R.id.img_logo);
            Picasso.with(this.getActivity()).load(this.carrera.getUrlImage())
                    .placeholder(R.drawable.common_ic_googleplayservices)
                    .error(R.drawable.common_ic_googleplayservices)
                    .into(logo);
        }

        txtNombre.setText(this.carrera.getNombre());
        TextView fecha = (TextView) rootView.findViewById(R.id.txt_fecha_largada);
        fecha.setText(sf.format(this.carrera.getFechaInicio()));

        TextView descripcion = (TextView) rootView.findViewById(R.id.txt_descripcion);
        descripcion.setText(this.carrera.getDescripcion());

        TextView distancia = (TextView) rootView.findViewById(R.id.txt_distancia);
        distancia.setText(this.carrera.getDistancia() + " Km");


        TextView genero = (TextView) rootView.findViewById(R.id.txt_genero);
        genero.setText(this.carrera.getGenero().getNombre());

        TextView direccion = (TextView) rootView.findViewById(R.id.txt_direccion);
        direccion.setText(this.carrera.getDireccion());
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


}
