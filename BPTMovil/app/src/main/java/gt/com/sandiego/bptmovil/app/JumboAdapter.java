package gt.com.sandiego.bptmovil.app;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gt.com.sandiego.bptmovil.R;
import gt.com.sandiego.bptmovil.app.Models.Jumbo;


/**
 * Created by lramirez on 17/10/2017.
 */

public class JumboAdapter  extends RecyclerView.Adapter<JumboAdapter.JumboViewHolder>  {

    private ArrayList<Jumbo> jumbos;
    private int resource;
    private Activity activity;

    private static OnItemClickListenerJ mItemClickListener;
    private static int Tipo;

    public interface OnItemClickListenerJ{
        public void onClick(JumboViewHolder holder, int position);
    }

    public JumboAdapter(ArrayList<Jumbo> jumbo, int resource, Activity activity,OnItemClickListenerJ mItemClickListener) {
        this.jumbos = jumbo;
        this.resource = resource;
        this.activity = activity;
        this.mItemClickListener = mItemClickListener;

    }

    @Override
    public JumboAdapter.JumboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new JumboAdapter.JumboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JumboAdapter.JumboViewHolder holder, int position) {
        //Se trabaja con la lista de objetos jumbos que se declaró en la parte superior
        //por cada elemento de la lista coloca los atributos a los objetos del holder.
        int color = 0;
        Jumbo jumbo = jumbos.get(position);
        holder.tvCorrelativo.setText("Correlativo: " + String.valueOf(jumbo.getCorrelativo()) );
        holder.tvColor.setText("Color: " + String.valueOf(jumbo.getColor()) );
        holder.tvColoralafecha.setText("Color a la fecha: " + String.valueOf(jumbo.getColorFecha()));
        holder.tvBodega.setText("Bodega: " + String.valueOf(jumbo.getBodega()));
        holder.tvEstiba.setText("Estiba: " + String.valueOf(jumbo.getEstiba()));
        holder.tvPlana.setText("Plana: " + String.valueOf(jumbo.getPlana()));
        holder.tvFila.setText("Fila: " + String.valueOf(jumbo.getFila()));
        holder.tvColumna.setText("Columna: " + jumbo.getColumna());
       if(jumbo.getColor()<225){
           color = 0xFF00FF00;
       }else if(jumbo.getColor()>=225 && color <= 300){
           color = 0xFFFFEB3B;
       }else if(jumbo.getColor()>=301 && color <= 375){
           color = 0xFFFF5722;
       }else if(jumbo.getColor()>375){
           color = 0xFFD32F2F;
       }
        holder.lytColor.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return jumbos.size();
    }



    public static class JumboViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            //Controles en el layout
            public TextView tvCorrelativo;
            public TextView tvColoralafecha;
            public TextView tvBodega;
            public TextView tvEstiba;
            public TextView tvColor;
            public TextView tvPlana;
            public TextView tvFila;
            public TextView tvColumna;
            public LinearLayout lytColor;


            private CardView cardView;

            public JumboViewHolder(View itemView) {
                super(itemView);
                tvCorrelativo = (TextView) itemView.findViewById(R.id.tv_correlativo);
                tvColor = (TextView) itemView.findViewById(R.id.tv_color);
                tvColoralafecha = (TextView) itemView.findViewById(R.id.tv_coloralafecha);
                tvBodega = (TextView) itemView.findViewById(R.id.tv_bodega);
                tvEstiba = (TextView) itemView.findViewById(R.id.tv_estiba);
                tvPlana = (TextView) itemView.findViewById(R.id.tv_plana);
                tvFila = (TextView) itemView.findViewById(R.id.tv_fila);
                tvColumna = (TextView) itemView.findViewById(R.id.tv_columna);
                lytColor = (LinearLayout) itemView.findViewById(R.id.cuadro_color);

                cardView = (CardView)itemView.findViewById(R.id.card_view);
                cardView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mItemClickListener.onClick(this,getLayoutPosition());
            }
        }
}
