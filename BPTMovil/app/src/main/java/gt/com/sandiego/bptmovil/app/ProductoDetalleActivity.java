package gt.com.sandiego.bptmovil.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Globals;
import gt.com.sandiego.bptmovil.app.DataServices.ProductoDataService;
import gt.com.sandiego.bptmovil.app.Models.ProductoEncabezado;

public class ProductoDetalleActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProductoDetalleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String idProducto = "";
    String fecha ="";
    Globals vars = Globals.getInstance();
    SharedPreferences sharedPreferences;
    Functions helper = new Functions();
    ArrayList<ProductoEncabezado> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        idProducto= getIntent().getExtras().getString("idProducto","defaultKey");
        fecha = getIntent().getExtras().getString("fecha","defaultKey");
        setTitle(idProducto);
           }

    @Override
    protected void onStart() {
        super.onStart();
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
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
                respuesta = productoDataService.getProductoEncabezado(fecha, helper.GetToken(), idProducto);
                if (!respuesta.equals("")) {
                    jsonArray = new JSONArray(respuesta);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        ProductoEncabezado p = new ProductoEncabezado();
                        p.setProducto(jsonObject.getString("Producto"));
                        p.setCantidad(Double.valueOf(jsonObject.getString("Cantidad")));
                        p.setPeso(Double.valueOf(jsonObject.getString("PesoQQ")));
                        p.setUnidadMedida(jsonObject.getString("UM"));
                        p.setBodega("Bodega " + jsonObject.getString("Bodega"));
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

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.detalle_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            //linear layout manager
            mLayoutManager = new LinearLayoutManager(ProductoDetalleActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            //Adapter
            mAdapter = new ProductoDetalleAdapter(lista, R.layout.cardview_producto_detalle, ProductoDetalleActivity.this);
            mRecyclerView.setAdapter(mAdapter);

            /**RECYCLERVIEW**/


            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(ProductoDetalleActivity.this, "Conectando", "Espere...", true, false);
        }
    }
}
