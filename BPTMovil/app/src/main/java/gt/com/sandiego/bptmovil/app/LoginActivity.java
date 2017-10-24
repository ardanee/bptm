package gt.com.sandiego.bptmovil.app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;
import gt.com.sandiego.bptmovil.app.DataServices.LoginDataService;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    Functions helper = new Functions();
    EditText etUsuario;
    EditText etPassword;
    CheckBox chkRecordar;
    Button btnLogin;
    JSONArray jsonArray;
    JSONObject jsonObject;
    Globals vars = Globals.getInstance();
    SharedPreferences sharedPreferences;
    LoginDataService ws = new LoginDataService();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();


        setContentView(R.layout.activity_login);
        etUsuario = (EditText) findViewById(R.id.et_usuario);
        etPassword = (EditText) findViewById(R.id.et_password);
        chkRecordar = (CheckBox) findViewById(R.id.chk_recordar);
        btnLogin = (Button) findViewById(R.id.btn_login);

        //Se establece en modo privado para que otras apps no tengan acceso
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        //Carga los datos de login en caso que se hayan almacenado en las shared preferences
        loadLoginData();

        //Si estaba marcado como recordar llena los controles de usuario
        if (vars.isRecordar()) {
            etUsuario.setText(vars.getUsuario());
            etPassword.setText(vars.getIdentificacion());
            chkRecordar.setChecked(vars.isRecordar());

            //!!!!QUITAR
            Intent x = new Intent(this,MainActivity.class); startActivity(x);return;

            //Ejecuta método de autenticación
            //autenticar();


        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticar();
            }
        });


    }

    private void autenticar() {
        String usuario, password, token;

        try {
            //ValidaCamposObligatorios
            usuario = etUsuario.getText().toString().trim();
            password = etPassword.getText().toString().trim();

            if (usuario.equals("")) {
                helper.showDialog(getString(R.string.usuario_requerido), this).show();
                return;
            }
            if (password.equals("")) {
                helper.showDialog(getString(R.string.password_requerido), this).show();
                return;
            }
            token = helper.GetToken();

            //coloca datos en vars
            vars.setUsuario(usuario);
            vars.setIdentificacion(password);
            vars.setRecordar(chkRecordar.isChecked());
            vars.setToken(token);


            vars.setMETHOD_NAME("Login");

            //Ejecuta AsyncTask que conecta al web service
            AsyncCallWS task = new AsyncCallWS();
            task.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveLoginData() {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usuario", vars.getUsuario());
            editor.putString("identificacion", vars.getIdentificacion());
            editor.putString("idusuario", vars.getIdUsuario());
            editor.putString("iniciales", vars.getIniciales());
            editor.putString("idrol", vars.getIdRol());
            editor.putString("nombre", vars.getNombre());
            editor.putBoolean("recordar", vars.isRecordar());
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
            helper.showDialog(e.getMessage(), LoginActivity.this).show();
        }
    }

    private void loadLoginData() {
        try {
            vars.setUsuario(sharedPreferences.getString("usuario", ""));
            vars.setIdentificacion(sharedPreferences.getString("identificacion", ""));
            vars.setIdUsuario(sharedPreferences.getString("idusuario", ""));
            vars.setIniciales(sharedPreferences.getString("iniciales", ""));
            vars.setIdRol(sharedPreferences.getString("idrol", ""));
            vars.setNombre(sharedPreferences.getString("nombre", ""));
            vars.setRecordar(sharedPreferences.getBoolean("recordar", false));
        } catch (Exception e) {
            e.printStackTrace();
            helper.showDialog(e.getMessage(), this).show();
        }
    }

    //Metod for Call Asyncrono the Question user
    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected Void doInBackground(String... params) {
            Log.i(vars.getTAG(), "doInBackGround");
            try {
                ws.getLogin(vars.getUsuario(), vars.getIdentificacion(), vars.getToken());
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(vars.getTAG(), "doInBackground", e);
            }
            return null;
        }

        protected void onPostExecute(Void resul) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (vars.getRespuesta() != "") {
                obtenerUsuario();
                if (vars.isRecordar()){saveLoginData();}
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mainIntent);

            } else {
                helper.showDialog("Usuario/Contraseña Incorrectos", LoginActivity.this).show();
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(LoginActivity.this, "Ingresando", "Espere...", true, false);
        }
    }

    private void obtenerUsuario() {
        try {
            jsonArray = new JSONArray(vars.getRespuesta());
            jsonObject = jsonArray.getJSONObject(0);
            vars.setIdUsuario(jsonObject.getString("idUsuario"));
            vars.setIniciales(jsonObject.getString("Iniciales"));
            vars.setIdRol(jsonObject.getString("idRol"));
            vars.setNombre(jsonObject.getString("Nombre"));
        } catch (Exception ex) {
            ex.printStackTrace();
            helper.showDialog(ex.getMessage(), this).show();

        }
    }



    }




