package studios.thinkup.com.apprunning;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import studios.thinkup.com.apprunning.model.RunningApplication;
import studios.thinkup.com.apprunning.model.entity.GrupoRunning;
import studios.thinkup.com.apprunning.model.entity.UsuarioApp;
import studios.thinkup.com.apprunning.provider.GrupoRunningProvider;
import studios.thinkup.com.apprunning.provider.IGrupoRunningProvider;
import studios.thinkup.com.apprunning.provider.IUsuarioProvider;
import studios.thinkup.com.apprunning.provider.UsuarioProvider;


public class NuevoUsuario extends Activity implements View.OnClickListener {
private UsuarioApp ua;
    private boolean esNuevoUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_usuario);

        initActivity(savedInstanceState);
        initView();
    }

    private void initActivity(Bundle savedInstanceState) {
        if(savedInstanceState!= null){
            if(savedInstanceState.containsKey("usuario")) {
                this.ua = (UsuarioApp) savedInstanceState.getSerializable("usuario");
            }
            if(savedInstanceState.containsKey("nuevoUsuario")){
                this.esNuevoUsuario = true;
            }else{
                this.esNuevoUsuario = false;
            }
        }else {
            if (this.getIntent().getExtras() != null) {
                if (this.getIntent().getExtras().containsKey("usuario")) {
                    this.ua = (UsuarioApp) this.getIntent().getExtras().getSerializable("usuario");
                }

                if (this.getIntent().getExtras().containsKey("nuevoUsuario")) {
                    this.esNuevoUsuario = true;
                } else {
                    this.esNuevoUsuario = false;
                }

            }
        }
        if(this.ua == null){
            if(( (RunningApplication) this.getApplication()).getUsuario()!= null){
                this.ua = ( (RunningApplication) this.getApplication()).getUsuario();
            }else {
                this.ua = new UsuarioApp();
            }
        }
    }

    private void initView() {
        TextView txtNickname = (TextView)findViewById(R.id.txt_nick);
        txtNickname.setText(this.ua.getNick());
        TextView txtNombre = (TextView)findViewById(R.id.txt_nombre);
        txtNombre.setText(this.ua.getNombre());
        TextView txtApellido = (TextView)findViewById(R.id.txt_apellido);
        txtApellido.setText(this.ua.getApellido());
        TextView txtEmail = (TextView)findViewById(R.id.txt_email);
        txtEmail.setText(this.ua.getEmail());
        TextView txtFechaNac = (TextView)findViewById(R.id.txt_fecha_nac);

        txtFechaNac.setText(this.ua.getFechaNacimiento());
        txtFechaNac.setInputType(InputType.TYPE_NULL);
        ImageView perfil = (ImageView)findViewById(R.id.img_profile);
        if (this.ua.getFotoPerfilUrl() != null) {
            Picasso.with(this).load(this.ua.getFotoPerfilUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(perfil);

        }
        perfil.requestFocus();
        ImageView calendario = (ImageView)findViewById(R.id.img_calendario);
        DatePickerListener dl = new DatePickerListener(txtFechaNac);
        calendario.setOnClickListener(dl);
        txtFechaNac.setOnClickListener(dl);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount(); // you dont display last item. It is used as hint.
            }

        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IGrupoRunningProvider gp = new GrupoRunningProvider(this);
        List<GrupoRunning> grupos = gp.findAll(GrupoRunning.class);
        int selection = -1;
        int count = 0;
        for(GrupoRunning g : grupos) {
            adapter.add(g.getNombre());
            if (this.ua.getGrupoId() != null && !this.ua.getGrupoId().isEmpty()){
                if (g.getNombre().equals(this.ua.getGrupoId())) {
                    selection =count;
                }
            }
        count++;
        }

        Spinner spinner = (Spinner)findViewById(R.id.sp_grupo);
        spinner.setAdapter(adapter);
        if(selection >=0){
            spinner.setSelection(selection); //display hint
        }else{
            spinner.setSelection(0); //display hint
        }




        Button guardar = (Button)findViewById(R.id.btn_guardar);
        guardar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nuevo_usuario, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(this.ua != null){
            outState.putSerializable("usuario",this.ua);
        }
        if(this.esNuevoUsuario){
            outState.putBoolean("nuevoUsuario",true);
        }else {
            if (outState.containsKey("nuevoUsuario")) {
                outState.remove("nuevoUsuario");

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    if(this.ua == null){
        this.ua = new UsuarioApp();
    }
        TextView txtNickname = (TextView)findViewById(R.id.txt_nick);
        this.ua.setNick(txtNickname.getText().toString());
        TextView txtNombre = (TextView)findViewById(R.id.txt_nombre);
        this.ua.setNombre(txtNombre.getText().toString());
        TextView txtApellido = (TextView)findViewById(R.id.txt_apellido);
        this.ua.setApellido(txtApellido.getText().toString());
        TextView txtEmail = (TextView)findViewById(R.id.txt_email);
        this.ua.setEmail(txtEmail.getText().toString());
        TextView txtFechaNac = (TextView)findViewById(R.id.txt_fecha_nac);

        this.ua.setFechaNacimiento(txtFechaNac.getText().toString());
        Spinner grupo = (Spinner)findViewById(R.id.sp_grupo);
        if(!grupo.getSelectedItem().equals(getString(R.string.corres_grupo))){
            this.ua.setGrupoId((String) grupo.getSelectedItem());
        }
        IUsuarioProvider up = new UsuarioProvider(this);
        try {
            if (this.ua.getId() == null) {
                up.grabar(this.ua);
            } else {
                up.update(this.ua);
            }

            ((RunningApplication) this.getApplication()).setUsuario(this.ua);
            if(this.esNuevoUsuario){
                Intent intent = new Intent(this,RecomendadosActivity.class);
                this.startActivity(intent);
            }else{
                Toast.makeText(this, "Datos de Usuario Guardados", Toast.LENGTH_LONG).show();

            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "No se puede guardar el usuario", Toast.LENGTH_LONG).show();
        }

    }
    private class DatePickerListener implements View.OnClickListener {
        private TextView fechaToUpdate;

        DatePickerListener(TextView fecha) {
            this.fechaToUpdate = fecha;
        }

        @Override
        public void onClick(View v) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
            if(fechaToUpdate.getText()!= null && !fechaToUpdate.getText().toString().isEmpty()){
                try {
                    c.setTime(sf.parse(ua.getFechaNacimiento()));
                } catch (ParseException e) {
                    c.set(c.get(Calendar.YEAR) - 20, 1, 1);
                }
            }else{
                c.set(c.get(Calendar.YEAR)-20,1,1);
            }

            DatePickerDialog newFragment = new DatePickerDialog(NuevoUsuario.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar c = Calendar.getInstance();
                    c.set(year,monthOfYear,dayOfMonth);
                    SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaToUpdate.setText(sf.format(c.getTime()));
                }
            },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
            newFragment.show();

        }
    }
    @Override
    public void onBackPressed() {
        if(this.esNuevoUsuario){
            Intent setIntent = new Intent(this,MainActivity.class);
            startActivity(setIntent);
            this.finish();
        }else{
            super.onBackPressed();
        }
    }

}
