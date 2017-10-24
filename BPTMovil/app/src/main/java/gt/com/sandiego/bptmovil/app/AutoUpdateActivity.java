package gt.com.sandiego.bptmovil.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;

public class AutoUpdateActivity extends AppCompatActivity {

    Globals vars = Globals.getInstance();
    Functions util = new Functions();
    private AutoUpdate autoUpdate;
    Intent intent;
    AsyncCallActualizar taskAtuclizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_update);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Actualizacion de Aplicacion");

        autoUpdate = new AutoUpdate(AutoUpdateActivity.this);
        ((TextView) findViewById(R.id.lblVersion)).setText(util.getVersion(AutoUpdateActivity.this));
        ((ProgressBar) findViewById(R.id.progressBar)).setIndeterminate(true);
        taskAtuclizar = new AsyncCallActualizar();
        taskAtuclizar.execute();
    }

    @Override
    protected void onDestroy() {
        if(taskAtuclizar != null){
            taskAtuclizar.cancel(true);
        }
        super.onDestroy();
    }

    private class AsyncCallActualizar extends AsyncTask<String, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected Void doInBackground(String... params) {
            Log.i(vars.getTAG(), "doInBackGround");
            try {
                autoUpdate.getData();
            }catch (Exception e){
                e.printStackTrace();
                Log.d(vars.getTAG(),"doInBackground",e);
            }
            return null;
        }

        protected  void onPostExecute(Void resul){
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            isNewApi();
        }

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(AutoUpdateActivity.this, "Obteniendo la Ultima Version", "Espere...", true, false);
        }
    }

    private void isNewApi(){
        if(autoUpdate.isNewVersionAvailable()){
            String msj = "Version Actual: " + autoUpdate.getCurrentVersionCode() ;
            msj += "\nVersion Nueva: " + autoUpdate.getLatestVersionCode();
            msj += "\nDesea Actualizar?";
            AlertDialog.Builder dialog = new AlertDialog.Builder(AutoUpdateActivity.this);
            dialog.setTitle("Existe una Nueva Version");
            dialog.setMessage(msj);
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    autoUpdate.InstallNewVersion();
                }
            });
            dialog.show();
        }else{
            util.showDialog("Aplicacion con la ultima actualizacion","Mensaje del Sistema",AutoUpdateActivity.this).show();
            ((TextView) findViewById(R.id.lblMensaje)).setText("Aplicacion Actualizada");
            ((TextView) findViewById(R.id.lblVersion)).setText(util.getVersion(AutoUpdateActivity.this));
            ((ProgressBar) findViewById(R.id.progressBar)).setIndeterminate(false);
        }
    }

    @Override
    public void onBackPressed() {
        goHome();
        super.onBackPressed();
    }

    private void goHome(){
        if (vars.getIdRol().equals("1") || vars.getIdRol().equals("2")){
            intent = new Intent().setClass(AutoUpdateActivity.this,MainActivity.class);
        }else{
            intent = new Intent().setClass(AutoUpdateActivity.this,MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                goHome();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
