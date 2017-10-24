package gt.com.sandiego.bptmovil.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import gt.com.sandiego.bptmovil.BuildConfig;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;

/**
 * Created by lramirez on 18/10/2017.
 */

public class AutoUpdate {
    JSONArray jsonArray;
    JSONObject jsonObject;
    Globals vars = Globals.getInstance();
    Functions util = new Functions();

    /**
     * Objeto contexto para ejecutar el instalador.
     * Se puede buscar otra forma mas "limpia".
     */
    Context context;

    /**
     * Listener que se llamara despues de ejecutar algun AsyncTask.
     */
    Runnable listener;

    /**
     * El enlace al archivo público de información de la versión. Puede ser de
     * Dropbox, un hosting propio o cualquier otro servicio similar.
     */

    //private static final String INFO_FILE= "https://dl.dropbox.com/s/4i9jbvq81hb8kqv/version.txt";

//    "https://dl.dropbox.com/s/4i9jbvq81hb8kqv/version.txt"

    //public static final String INFO_FILE = "http://dl.dropbox.com/u/1587994/Androcode/autoupdate_info.txt";

    /**
     * El código de versión establecido en el AndroidManifest.xml de la versión
     * instalada de la aplicación. Es el valor numérico que usa Android para
     * diferenciar las versiones.
     */
    private int currentVersionCode;

    /**
     * El nombre de versión establecido en el AndroidManifest.xml de la versión
     * instalada. Es la cadena de texto que se usa para identificar al versión
     * de cara al usuario.
     */
    private String currentVersionName;

    /**
     * El código de versión establecido en el AndroidManifest.xml de la última
     * versión disponible de la aplicación.
     */
    private int latestVersionCode;

    /**
     * El nombre de versión establecido en el AndroidManifest.xml de la última
     * versión disponible.
     */
    private String latestVersionName;

    /**
     * Enlace de descarga directa de la última versión disponible.
     */
    private String downloadURL;

    /**
     * Constructor unico.
     * @param context Contexto sobre el cual se ejecutara el Instalador.
     */

    public AutoUpdate(Context context) {
        this.context = context;
    }

    /**
     * Método para inicializar el objeto. Se debe llamar antes que a cualquie
     * otro, y en un hilo propio (o un AsyncTask) para no bloquear al interfaz
     * ya que hace uso de Internet.
     *
     *            El contexto de la aplicación, para obtener la información de
     *            la versión actual.
     */

    public void getData(){
        try {
            Log.d("AutoUpdate", "GetData");
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionName = packageInfo.versionName;
            vars.setVersion(currentVersionCode + " " + currentVersionName);

            //Datos
            String data = downloadHttp(new URL(vars.getINFO_FILE()));
            jsonArray = new JSONArray(data);
            jsonObject = jsonArray.getJSONObject(0);
            latestVersionCode = jsonObject.getInt("versionCode");
            latestVersionName = jsonObject.getString("versionName");
            vars.setVersionServidor(latestVersionCode +" " + latestVersionName);

            downloadURL = jsonObject.getString("downloadURL");
            Log.d("AutoUpdate","Datos Obtenidos con exito");

        }catch (JSONException e){
            Log.e("AutoUpdate","Ha habido un error con el JSON ",e);
        }catch (PackageManager.NameNotFoundException e){
            Log.e("AutoUpdate","Ha habido un error con el packete :S",e);
        } catch (IOException e){
            Log.e("AutoUpdate","Ha habido un error al descargar",e);
        }

    }

    /**
     * Método para comparar la versión actual con la última .
     *
     * @return true si hay una versión más nueva disponible que la actual.
     */
    public boolean isNewVersionAvailable() {

        if (getLatestVersionCode() > getCurrentVersionCode()){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Devuelve el código de versión actual.
     *
     * @return integer con la version actual
     */
    public int getCurrentVersionCode() {
        return currentVersionCode;
    }

    /**
     * Devuelve el nombre de versión actual.
     *
     * @return IDEM
     */
    public String getCurrentVersionName() {
        return currentVersionName;
    }

    /**
     * Devuelve el código de la última versión disponible.
     *
     * @return IDEM
     */
    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    /**
     * Devuelve el nombre de la última versión disponible.
     *
     * @return IDEM
     */
    public String getLatestVersionName() {
        return latestVersionName;
    }

    /**
     * Devuelve el enlace de descarga de la última versión disponible
     *
     * @return string con la URL de descarga
     */
    public String getDownloadURL() {
        return downloadURL;
    }

    /**
     * Método auxiliar usado por getData() para leer el archivo de información.
     * Encargado de conectarse a la red, descargar el archivo y convertirlo a
     * String.
     *
     * @param url
     *            La URL del archivo que se quiere descargar.
     * @return Cadena de texto con el contenido del archivo
     * @throws IOException
     *             Si hay algún problema en la conexión
     */

    private static String downloadHttp(URL url) throws IOException{
        //Codigo de conexion, irrelevante al tema.
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setReadTimeout(15 * 1000);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = bufferedReader.readLine())!= null){
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    /**
     * Metodo de Interface.
     * Segundo Metodo a usar.
     * Se encargara, una vez obtenidos los datos de la version mas reciente, y en un hilo separado,
     * de comprobar que haya efectivamente una version mas reciente, descargarla e instalarla.
     * Preparar la aplicacion para ser cerrada y desinstalada despues de este metodo.
     */
    public void InstallNewVersion(){
        if(isNewVersionAvailable()){
            if(getDownloadURL() == "") return;
            String params[] = {getDownloadURL()};
            downloadInstaller.execute(params);

        }
    }

    /**
     * Objeto de AsyncTask encargado de descargar e instalar la ultima version de la aplicacion.
     * No es cancelable.
     */
    private AsyncTask<String,Integer,Intent> downloadInstaller= new AsyncTask<String, Integer, Intent>() {
        private ProgressDialog pDialog;

        @Override
        protected Intent doInBackground(String... params) {
            try{
                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();

                String PATH = Environment.getExternalStorageDirectory() + "/download/";
                File file = new File(PATH);
                file.mkdirs();
                File outputFile = new File(file, vars.getNameApi());
                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream inputStream = httpURLConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1= inputStream.read(buffer))!= -1){
                    fos.write(buffer,0,len1);
                }
                fos.close();
                inputStream.close();

                File toInstall = new File(PATH, vars.getNameApi() );
                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                    Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                } else {
                    Uri apkUri = Uri.fromFile(toInstall);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }catch (IOException e){
                Log.e("Update Error!",e.getMessage());
                util.showDialog( e.getMessage() +"Update Error!!!", (Activity) context).show();
            }
            return null;
        }
        protected  void onPostExecute(Intent intent){
            super.onPostExecute(intent);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(context,"Actualizando","Espere...",true,false);
        }

    };

}

