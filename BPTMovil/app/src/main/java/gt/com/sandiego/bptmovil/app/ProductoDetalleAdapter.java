package gt.com.sandiego.bptmovil.app;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.Models.ProductoEncabezado;

/**
 * Created by lramirez on 06/09/2017.
 */

public class ProductoDetalleAdapter extends RecyclerView.Adapter<ProductoDetalleAdapter.ProductoDetalleViewHolder> {
    private ArrayList<ProductoEncabezado> productos;
    private int resource;
    private Activity activity;

    public ProductoDetalleAdapter(ArrayList<ProductoEncabezado> producto, int resource, Activity activity) {
        this.productos = producto;
        this.resource = resource;
        this.activity = activity;
    }


    @Override
    public ProductoDetalleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ProductoDetalleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductoDetalleViewHolder holder, int position) {
        ProductoEncabezado producto  = productos.get(position);
        holder.tvBodega.setText(producto.getBodega());
        holder.tvCantidad.setText(String.valueOf(producto.getCantidad()) + " " + producto.getUnidadMedida());
        holder.tvPeso.setText(String.valueOf(producto.getPeso()) + " QQ");
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ProductoDetalleViewHolder extends RecyclerView.ViewHolder{
        private TextView tvBodega ;
        private TextView tvCantidad;
        private TextView tvPeso;

        public ProductoDetalleViewHolder(View itemView){
            super(itemView);
            tvBodega = (TextView) itemView.findViewById(R.id.tv_bodega);
            tvCantidad = (TextView) itemView.findViewById(R.id.tv_cantidad);
            tvPeso =  (TextView) itemView.findViewById(R.id.tv_peso);
        }
    }
}
