package com.example.trabajofct.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.trabajo1.R;
import com.example.trabajofct.Modules.Asignaturas;
import com.example.trabajofct.Modules.Grupos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterAsignaturas extends BaseAdapter implements Filterable {

    private int layout;
    private Context context;
    private List<Asignaturas> asignaturas;
    private List<Asignaturas> listaAsignaturasFiltrar;
    private ViewHolder viewHolder;
    private Asignaturas asignaturaSeleccionada;

    public AdapterAsignaturas(int layout, Context context, List<Asignaturas> asignaturas) {
        this.layout = layout;
        this.context = context;
        this.asignaturas = asignaturas;
        this.listaAsignaturasFiltrar = new ArrayList<>(asignaturas);
    }

    @Override
    public int getCount() {
        return asignaturas.size();
    }

    @Override
    public Asignaturas getItem(int position) {
        return asignaturas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);

            viewHolder = new ViewHolder();
            viewHolder.imagenAsignatura = (ImageView) convertView.findViewById(R.id.imagenAsignatura);
            viewHolder.nombreAsignatura = (TextView)convertView.findViewById(R.id.nombreAsignatura);
            viewHolder.cursoAsignatura = (TextView)convertView.findViewById(R.id.CursoAsignatura);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }



        asignaturaSeleccionada = getItem(position);
        viewHolder.nombreAsignatura.setText(asignaturaSeleccionada.getNombre());
        viewHolder.cursoAsignatura.setText(asignaturaSeleccionada.getCurso());

        Picasso.with(context).load(asignaturaSeleccionada.getUrlImagenAsig()).into(viewHolder.imagenAsignatura);



        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Asignaturas> filtrada = new ArrayList<>();

            if (constraint==null || constraint.length() == 0){
                filtrada.addAll(listaAsignaturasFiltrar);
            }else{
                String letrasFiltrar = constraint.toString().toLowerCase().trim();

                for (Asignaturas asignatura : listaAsignaturasFiltrar){
                    if (asignatura.getNombre().toLowerCase().contains(letrasFiltrar)){
                        filtrada.add(asignatura);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            asignaturas.clear();
            asignaturas.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder{
        private ImageView imagenAsignatura;
        private TextView nombreAsignatura;
        private TextView cursoAsignatura;
    }
}
