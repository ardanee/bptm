package gt.com.sandiego.bptmovil.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.Models.ProductoEncabezado;

/**
 * Created by lramirez on 04/09/2017.
 */

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.ProductoViewHolder> {
    private ArrayList<ProductoEncabezado> productos;
    private int resource;
    private Activity activity;

    private static OnItemClickListener mItemClickListener;
    private static int Tipo;

    public interface OnItemClickListener{
        public void onClick(ProductoViewHolder holder,int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }


    public ProductosAdapter(ArrayList<ProductoEncabezado> productos, int resource, Activity activity,OnItemClickListener mItemClickListener) {
        this.productos = productos;
        this.resource = resource;
        this.activity = activity;
        this.mItemClickListener = mItemClickListener;

    }

    @Override
    public ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoViewHolder holder, int position) {
         //Se trabaja con la lista de objetos producto que se declaró en la parte superior
        //por cada elemento de la lista coloca los atributos a los objetos del holder.
        ProductoEncabezado producto = productos.get(position);
        holder.tvProducto.setText(producto.getProducto());
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()) + " " +producto.getUnidadMedida());
        holder.tvPeso.setText(String.valueOf(producto.getPeso()) + " QQ");


        //Listener para onclick disparar intent
        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TextView tvProducto = (TextView) activity.findViewById(R.id.tv_producto);
                Intent iProductoDetalle = new Intent(activity,ProductoDetalleActivity.class);
                //iProductoDetalle.putExtra("idProducto", tvProducto.getText().toString());
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
                    //Coloca transision Explode para la salida
                    Explode exp = new Explode();
                    exp.setDuration(1000);
                    activity.getWindow().setExitTransition(exp);
                    activity.startActivity(iProductoDetalle,
                            //Ejecutar intent con transicion
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity,v,activity.getString (R.string.transitionname_picture)).toBundle());

                }else{
                    //ejecutar intent sin transicion
                    activity.startActivity(iProductoDetalle);
                }
            }
        });
        */
    }

    @Override
    public int getItemCount() {
       return productos.size();
    }


    public static class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Controles en el layout
        public TextView tvProducto;
        private TextView tvCantidad;
        private TextView tvPeso;
        private CardView cardView;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            tvProducto = (TextView) itemView.findViewById(R.id.tv_producto);
            tvCantidad = (TextView) itemView.findViewById(R.id.tv_cantidad);
            tvPeso = (TextView) itemView.findViewById(R.id.tv_peso);
            cardView = (CardView)itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onClick(this,getLayoutPosition());
        }
    }
}
