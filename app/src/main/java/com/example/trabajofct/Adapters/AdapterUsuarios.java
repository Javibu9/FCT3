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
import com.example.trabajofct.Modules.Usuarios;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterUsuarios extends BaseAdapter implements Filterable {

    private int layout;
    private Context context;
    private List<Usuarios> usuarios;
    private List<Usuarios> listaUsuariosFiltrar;
    private ViewHolder viewHolder;
    private Usuarios usuarioSeleccionado;

    public AdapterUsuarios(int layout, Context context, List<Usuarios> usuarios) {
        this.layout = layout;
        this.context = context;
        this.usuarios = usuarios;
        this.listaUsuariosFiltrar = new ArrayList<>(usuarios);
    }

    @Override
    public int getCount() {
        return usuarios.size();
    }

    @Override
    public Usuarios getItem(int position) {
        return usuarios.get(position);
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
            viewHolder.imagenUsuario = (ImageView) convertView.findViewById(R.id.imagenUsuario);
            viewHolder.nombreUsuario = (TextView)convertView.findViewById(R.id.nombreUsuario);
            viewHolder.tipoUsuario = (TextView)convertView.findViewById(R.id.tipoUsuario);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }



        usuarioSeleccionado = getItem(position);
        viewHolder.nombreUsuario.setText(usuarioSeleccionado.getNombre());
        viewHolder.tipoUsuario.setText(usuarioSeleccionado.getTipoUsuario());

        Picasso.with(context).load(usuarioSeleccionado.getUrlImagen()).into(viewHolder.imagenUsuario);



        return convertView;
    }

    @Override
    public Filter getFilter() {
        return filtro;
    }

    private Filter filtro = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Usuarios> filtrada = new ArrayList<>();

            if (constraint==null || constraint.length() == 0){
                filtrada.addAll(listaUsuariosFiltrar);
            }else{
                String letrasFiltrar = constraint.toString().toLowerCase().trim();

                for (Usuarios usuarios : listaUsuariosFiltrar){
                    if (usuarios.getNombre().toLowerCase().contains(letrasFiltrar)){
                        filtrada.add(usuarios);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filtrada;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            usuarios.clear();
            usuarios.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder{
        private ImageView imagenUsuario;
        private TextView nombreUsuario;
        private TextView tipoUsuario;
    }
}
