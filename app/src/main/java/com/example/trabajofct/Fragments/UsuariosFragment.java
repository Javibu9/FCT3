package com.example.trabajofct.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.trabajo1.R;
import com.example.trabajofct.Activities.AnadirAsignatura;
import com.example.trabajofct.Activities.RegisterActivity;
import com.example.trabajofct.Adapters.AdapterAsignaturas;
import com.example.trabajofct.Adapters.AdapterUsuarios;
import com.example.trabajofct.Modules.Asignaturas;
import com.example.trabajofct.Modules.Usuarios;
import com.example.trabajofct.Utils.Global;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsuariosFragment extends Fragment {

    private FloatingActionButton anadirUsuarios;
    private View view;
    private ListView listaUsuarios;
    private AdapterUsuarios adapterUsuarios;
    private ArrayList<Usuarios> usuarios = new ArrayList<>();
    private DatabaseReference BBDD = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public UsuariosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        listaUsuarios = (ListView)view.findViewById(R.id.listaUsuarios);
        usuarios.clear();

        //para que coja el menu de eliminar y editar
        registerForContextMenu(listaUsuarios);

        //para que coja el menu de buscar en el toolbar
        setHasOptionsMenu(true);

        BBDD.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Usuarios usuario = dataSnapshot1.getValue(Usuarios.class);
                    usuarios.add(usuario);
                    adapterUsuarios = new AdapterUsuarios(R.layout.list_item_usuarios, getContext(), usuarios);
                    listaUsuarios.setAdapter(adapterUsuarios);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        anadirUsuarios = (FloatingActionButton) view.findViewById(R.id.btnAnadirUsuario);
        anadirUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.desdeDentro = true;
                startActivity(new Intent(getContext(), RegisterActivity.class));

            }
        });



        return view;
    }

    //la información que saldrá a acada item al seleccionarlo
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(usuarios.get(info.position).getNombre());
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_usuarios, menu);
    }

    //para eliminar y editar cada item seleccionado
    @Override
    public boolean onContextItemSelected(MenuItem menu){

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu.getMenuInfo();

        switch(menu.getItemId()){
            case R.id.eliminarUsuario:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder
                        .setTitle("Eliminar usuario")
                        .setMessage("¿Seguro que quiere eliminar" + usuarios.get(info.position).getNombre())
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BBDD.child("Usuario").child(usuarios.get(info.position).getIdUsuario()).removeValue();
                                usuarios.remove(info.position);
                                usuarios.clear();
                                adapterUsuarios.notifyDataSetChanged();
                            }
                        });
                builder.setCancelable(false);
                builder.show();
                return true;

            case R.id.modificarUsuario :
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                intent.putExtra("idUsuario", usuarios.get(info.position).getIdUsuario());
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(menu);
        }
    }

    //para buscar en la lista
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscador, menu);
        MenuItem searchItem = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterUsuarios.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
