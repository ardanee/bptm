package gt.com.sandiego.bptmovil.app;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;
import gt.com.sandiego.bptmovil.app.DataServices.ProductoDataService;
import gt.com.sandiego.bptmovil.app.Models.ProductoEncabezado;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ProductosAdapter.OnItemClickListener {
    Globals vars = Globals.getInstance();
    SharedPreferences sharedPreferences;
    Functions helper = new Functions();
    ArrayList<ProductoEncabezado> lista;
    //controles
    EditText etFecha;
    Button btnBuscar;
    String fecha;

    private int mYear, mMonth, mDay;

    private RecyclerView mRecyclerView;
    private ProductosAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    SwipeRefreshLayout swipeRefreshLayout ;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;

    private void permisosAPI(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        permisosAPI();
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        etFecha = (EditText) findViewById(R.id.et_fecha);
        btnBuscar = (Button) findViewById(R.id.btn_buscar);

        //Ejecuta la búsqueda al cargar la pantalla
        Date hoy = new Date();
        etFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(hoy));
        fecha = etFecha.getText().toString();
        AsyncCallWS task = new AsyncCallWS();
        task.execute();

        /**Swipe Refresh*/
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fecha = etFecha.getText().toString();
                if (helper.isDateValid(fecha)) {
                    helper.showDialog("la fecha es incorrecta", MainActivity.this);
                    AsyncCallWS task = new AsyncCallWS();
                    task.execute();
                }
            }
        });
        /**SwipeRefresh*/

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etFecha.setText(sdf.format(myCalendar.getTime()));
            }
        };


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fecha = etFecha.getText().toString();
                if (!helper.isDateValid(fecha)) {
                    helper.showDialog("la fecha es incorrecta", MainActivity.this);
                    return;
                }
                AsyncCallWS task = new AsyncCallWS();
                task.execute();
            }
        });

        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoFecha();
            }
        });
    }


    void mostrarDialogoFecha() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Display Selected date in textbox

                        if (year < mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (monthOfYear < mMonth && year == mYear)
                            view.updateDate(mYear, mMonth, mDay);

                        if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                            view.updateDate(mYear, mMonth, mDay);

                        etFecha.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);

        String string_date = "01/01/1900";

        Date d;
        long milliseconds = 0;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            d = f.parse(string_date);
            milliseconds = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dpd.getDatePicker().setMinDate(milliseconds);
        dpd.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        TextView tvusuario = (TextView) findViewById(R.id.tv_usuario);
        tvusuario.setText(vars.getNombre().toString());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cerrar_sesion) {
            cerrarSesion();
        } else if (id == R.id.nav_actualizar) {
           Intent i = new Intent(this,AutoUpdateActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_consultaJumbos) {
            Intent i = new Intent(this,ListaJumbosActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void cerrarSesion() {
        try {
            //Limpia variables globales
            vars.setUsuario("");
            vars.setIdentificacion("");
            vars.setIdUsuario("");
            vars.setIniciales("");
            vars.setIdRol("");
            vars.setNombre("");
            vars.setRecordar(false);

            //Borra los datos de las shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("usuario", vars.getUsuario());
            editor.putString("identificacion", vars.getIdentificacion());
            editor.putString("idusuario", vars.getIdUsuario());
            editor.putString("iniciales", vars.getIniciales());
            editor.putString("idrol", vars.getIdRol());
            editor.putString("nombre", vars.getNombre());
            editor.putBoolean("recordar", vars.isRecordar());
            editor.commit();

            //direcciona a pantalla de autenticación
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
            helper.showDialog(e.getMessage(), this).show();
        }
    }

    void mostrarToast(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected Void doInBackground(String... params) {
            Log.i(vars.getTAG(), "doInBackGround");
            try {
                /**/
                ProductoDataService productoDataService = new ProductoDataService();
                JSONObject jsonObject;
                JSONArray jsonArray;
                lista = new ArrayList<>();
                String respuesta = "";
                respuesta = productoDataService.getProductoEncabezado(fecha, helper.GetToken(), "");
                if (!respuesta.equals("")) {
                    jsonArray = new JSONArray(respuesta);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        ProductoEncabezado p = new ProductoEncabezado();
                        p.setProducto(jsonObject.getString("Producto"));
                        p.setCantidad(Double.valueOf(jsonObject.getString("Cantidad")));
                        p.setPeso(Double.valueOf(jsonObject.getString("PesoQQ")));
                        p.setUnidadMedida(jsonObject.getString("UM"));
                        lista.add(p);
                    }
                }

                /**/


            } catch (Exception e) {
                e.printStackTrace();
                Log.d(vars.getTAG(), "doInBackground", e);
            }
            return null;
        }

        protected void onPostExecute(Void resul) {
            /**RECYCLERVIEW**/

            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            //linear layout manager
            mLayoutManager = new LinearLayoutManager(MainActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            //Adapter
            mAdapter = new ProductosAdapter(lista, R.layout.cardview_elementos_bodega, MainActivity.this,MainActivity.this);
            mRecyclerView.setAdapter(mAdapter);

            /**RECYCLERVIEW**/


            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            //quita icono del swipe refresh
            swipeRefreshLayout.setRefreshing(false);

        }

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(MainActivity.this, "Conectando", "Espere...", true, false);
        }
    }


    //OnClick


    @Override
    public void onClick(ProductosAdapter.ProductoViewHolder holder, int position) {
        View v = new View(this);
       Intent iProductoDetalle = new Intent(MainActivity.this,ProductoDetalleActivity.class);
        iProductoDetalle.putExtra("idProducto", holder.tvProducto.getText().toString());
        iProductoDetalle.putExtra("fecha", fecha);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            //Coloca transision Explode para la salida
            Explode exp = new Explode();
            exp.setDuration(1000);
            getWindow().setExitTransition(exp);
            startActivity(iProductoDetalle,
                    //Ejecutar intent con transicion
                    ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,v,getString(R.string.transitionname_picture)).toBundle());

        }else{
            //ejecutar intent sin transicion
            startActivity(iProductoDetalle);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permisosAPI();
                } else {

                }

            }
        }
    }


}
