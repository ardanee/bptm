package gt.com.sandiego.bptmovil.app.ClasesUtilidades;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.EGLExt;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.BuildConfig;

/**
 * Created by angelmendoza on 29/05/17.
 */

public class Functions {

    public  String md5(String string) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);

        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }

        return hex.toString();
    }


    public String GetToken(){
        Calendar calendar = Calendar.getInstance();
        int dia= calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH ) + 1;
        int anio = calendar.get(Calendar.YEAR);
        String token = "$anD!eg00@qq"+ dia + mes + anio;

        return md5(token);
    }

    public String getActualizacion(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getFechaActual(){
        Date date = new Date();
        DateFormat houFormat = new SimpleDateFormat("dd/MM/yyyy");
        String FechaHora = houFormat.format(date);
        return FechaHora;
    }

    public String getHoraActual(){
        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm");
        String Hora = hourFormat.format(date);
        return Hora;
    }

    public  int getIndexSpn(Spinner spinner, String idCodigo){
        int index= 0;
        for(int i=0;i< spinner.getCount();i++){

            if(getCodigo(spinner.getItemAtPosition(i).toString(),"-").equalsIgnoreCase(idCodigo)){
                index =  i;
                break;
            }
        }
        return index;
    }

    public String getCodeFoto(){
        //Date date = new Date();
        //DateFormat houFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
        //String codefoto = houFormat.format(date);
        Long timestamp = System.currentTimeMillis() / 1000;
        String imageName = timestamp.toString() + ".jpg";
        return imageName;
    }

    public  String getCodigo(String xDescripcion,String xSeparado){
        String [] cadena = xDescripcion.split(xSeparado);
        return cadena[0];
    }

    public  String getDescripcion(String xDescripcion,String xSeparado){
        String [] cadena = xDescripcion.split(xSeparado);
        return cadena[1];
    }

    public String getNombreImagen(String path){
        String [] cadena = path.split(File.separator);
        return cadena[cadena.length - 1];
    }

    public String getVersion( Context context) {
        int currentVersionCode= 0;
        String currentVersionName="";
        try {

            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            currentVersionCode = packageInfo.versionCode;
            currentVersionName = packageInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AutoUpdate", "Ha habido un error con el packete :S", e);
        }
        return Integer.toString(currentVersionCode) + " " + currentVersionName;
    }

    public AlertDialog showDialog(String message, String title, Activity activity ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public AlertDialog showDialog(String message, Activity activity ){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        AlertDialog alerta;
        builder.setCancelable(false);

        builder.setTitle(R.string.dialog_title_text);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.positive_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerta = builder.create();
        return alerta;
    }

    public void msgSnackBar(String message, View view){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }

    public Bitmap getBitmap(String path,Activity macty){
        Bitmap bMap = null;
        File imgexts = new File(path);
        Uri uriSaveImage;
        if (imgexts.exists()){
            try {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    uriSaveImage = FileProvider.getUriForFile(macty, BuildConfig.APPLICATION_ID +".provider",imgexts);
                }else{
                    uriSaveImage = Uri.fromFile(imgexts);
                }
                InputStream is = macty.getContentResolver().openInputStream(uriSaveImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                bMap = BitmapFactory.decodeStream(bis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return  bMap;
    }

    public Uri getUri(String Path,Activity macty){
        File directorioImagen = new File(Path);
        Uri uri = null;
       if(directorioImagen.exists()) {
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
               uri = FileProvider.getUriForFile(macty, BuildConfig.APPLICATION_ID + ".provider", directorioImagen);
           } else {
               uri = Uri.fromFile(directorioImagen);
           }
       }
        return  uri;
    }

    public  String getPath(){
        String picturePath="";
        File file = new File(Environment.getExternalStorageDirectory(), "DCIM/SeguridadRondaM");
        boolean isDirectoryCreated = file.exists();
        if(!isDirectoryCreated) {
            isDirectoryCreated = file.mkdirs();
        }
        if(isDirectoryCreated){
            picturePath = Environment.getExternalStorageDirectory() + File.separator + "DCIM/SeguridadRondaM" + File.separator;
        }
        return picturePath;
    }


    public String encodeImage(Bitmap bm) {
        String imgDecodableString;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);

        return imgDecodableString;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public String encodeImages(String path) {
        String imgDecodableString = "";
        try{
            File imagefile = new File(path);
            FileInputStream fis = null;
            fis = new FileInputStream(imagefile);
            Bitmap bm = BitmapFactory.decodeStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] b = baos.toByteArray();
            imgDecodableString = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Base64.de
        return imgDecodableString;

    }

    final static String DATE_FORMAT = "dd/MM/yyyy";

    public static boolean isDateValid(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
