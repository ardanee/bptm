package gt.com.sandiego.bptmovil.app;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.ClasesUtilidades.Functions;
import gt.com.sandiego.bptmovil.app.DataServices.ListaJumbosDataService;
import gt.com.sandiego.bptmovil.app.DataServices.ProductoDataService;
import gt.com.sandiego.bptmovil.app.Models.Jumbo;


public class ListaJumbosActivity extends AppCompatActivity
        implements JumboAdapter.OnItemClickListenerJ {
    private RecyclerView mRecyclerView;
    private JumboAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Jumbo> lista;
    private Functions helper = new Functions();
    private String textoBusqueda;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_jumbos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_jumbos_options_menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setInputType(InputType.TYPE_CLASS_NUMBER);

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
                    try{
                        textoBusqueda = s;
                        AsyncCallWS task = new AsyncCallWS();
                        task.execute();
                    }
                    catch(Exception x){
                        Log.d("ListaJmb-onQueryTextS", x.toString());
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    Log.d("ListaJumbosActivity", "onQueryTextChange ");
                   /* cursor=studentRepo.getStudentListByKeyword(s);
                    if (cursor!=null){
                        customAdapter.swapCursor(cursor);
                    }
                    */
                    return false;
                }

            });

        }

        return true;

    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected Void doInBackground(String... params) {
            Log.i("ListaJumbosActivity", "doInBackGround");
            try {
                /**/
                ListaJumbosDataService jumboDataService = new ListaJumbosDataService();
                JSONObject jsonObject;
                JSONArray jsonArray;
                lista = new ArrayList<>();
                String respuesta = "";
                respuesta = jumboDataService.GetJumboAFecha(textoBusqueda,helper.GetToken());
                if (!respuesta.equals("")) {
                    jsonArray = new JSONArray(respuesta);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        Jumbo j = new Jumbo();
                        j.setCorrelativo(Integer.valueOf(jsonObject.getString("Correlativo")));
                        j.setColor(Integer.valueOf(jsonObject.getString("Color")));
                        j.setBodega(Integer.valueOf(jsonObject.getString("Bodega")));
                        j.setColorFecha(Integer.valueOf(jsonObject.getString("ColorFecha")));
                        j.setColumna(jsonObject.getString("Columna"));
                        j.setEstiba(Integer.valueOf(jsonObject.getString("estiba")));
                        j.setFila(Integer.valueOf(jsonObject.getString("Fila")));
                        j.setPlana(Integer.valueOf(jsonObject.getString("plana")));
                        j.setHumedad(Double.valueOf(jsonObject.getString("Humedad")));
                        j.setNumeroRegistro(Integer.valueOf(jsonObject.getString("NumeroRegistro")));
                        lista.add(j);
                    }
                }

                /**/


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ListaJumbosActivity", "doInBackground", e);
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
            return null;
        }

        protected void onPostExecute(Void resul) {
            /**RECYCLERVIEW**/
            mRecyclerView = (RecyclerView) findViewById(R.id.jumbo_reciclerview);
            mRecyclerView.setHasFixedSize(true);
            //linear layout manager
            mLayoutManager = new LinearLayoutManager(ListaJumbosActivity.this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            //Adapter
            mAdapter = new JumboAdapter(lista, R.layout.cardview_detalle_jumbos, ListaJumbosActivity.this, ListaJumbosActivity.this);
            mRecyclerView.setAdapter(mAdapter);

            Toast t = Toast.makeText(getApplicationContext(), "Busqueda realizada: " + textoBusqueda, Toast.LENGTH_SHORT);
            t.show();

            /**RECYCLERVIEW**/


            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }


        }

        @Override
        protected void onPreExecute() {
            pDialog = ProgressDialog.show(ListaJumbosActivity.this, "Conectando", "Espere...", true, false);
        }
    }


    @Override
    public void onClick(JumboAdapter.JumboViewHolder holder, int position) {
//Click en el item del cardview en el recycler
    }


} //end of class
