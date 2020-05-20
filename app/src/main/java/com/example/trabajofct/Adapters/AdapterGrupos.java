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

import com.example.trabajo1.R;
import com.example.trabajofct.Modules.Asignaturas;
import com.example.trabajofct.Modules.Grupos;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterGrupos extends BaseAdapter implements Filterable {
    private int layout;
    private Context context;
    private List<Grupos> grupos;
    private AdapterGrupos.ViewHolder viewHolder;
    private List<Grupos> listaGruposFiltrar;
    private Grupos grupoSeleccionado;

    public AdapterGrupos(int layout, Context context, List<Grupos> grupos) {
        this.layout = layout;
        this.context = context;
        this.grupos = grupos;
        listaGruposFiltrar = new ArrayList<>(grupos);
    }
    @Override
    public int getCount() {
        return grupos.size();
    }

    @Override
    public Grupos getItem(int position) {
        return grupos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, null);

            viewHolder = new AdapterGrupos.ViewHolder();
            viewHolder.nombreGrupo = (TextView)convertView.findViewById(R.id.nombreGrupo);
            viewHolder.numeroGrupo = (TextView)convertView.findViewById(R.id.numeroGrupo);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (AdapterGrupos.ViewHolder)convertView.getTag();
        }

        grupoSeleccionado = getItem(position);
        viewHolder.nombreGrupo.setText(grupoSeleccionado.getNombre());
        viewHolder.numeroGrupo.setText(grupoSeleccionado.getNumero()+"");

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Grupos> filtrada = new ArrayList<>();

            if (constraint==null || constraint.length() == 0){
                filtrada.addAll(listaGruposFiltrar);
            }else{
                String letrasFiltrar = constraint.toString().toLowerCase().trim();

                for (Grupos grupo : listaGruposFiltrar){
                    if (grupo.getNombre().toLowerCase().contains(letrasFiltrar)){
                        filtrada.add(grupo);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            grupos.clear();
            grupos.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder{
        private TextView nombreGrupo;
        private TextView numeroGrupo;
    }

    public void setFilter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        grupos.clear();
        if (charText.length() == 0) {
            this.grupos.addAll(grupos);
        }else{
            for ( Grupos grupo : grupos) {
                if (grupo.getNombre().toLowerCase(Locale.getDefault()).contains(charText)){
                    grupos.add(grupo);
                }
            }
        }
        notifyDataSetChanged();

    }
}
