package com.example.trabajofct.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.trabajo1.R;
import com.example.trabajofct.Activities.AnadirGrupo;
import com.example.trabajofct.Activities.MainActivity;
import com.example.trabajofct.Activities.RegisterActivity;
import com.example.trabajofct.Adapters.AdapterGrupos;
import com.example.trabajofct.Modules.Grupos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GruposFragment extends Fragment{

    private FloatingActionButton anadirGrupos;
    private View view;
    private ListView listaGrupos;
    private AdapterGrupos adapterGrupos;
    private ArrayList<Grupos> grupos = new ArrayList<>();
    private DatabaseReference BBDD = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String idEliminar;
    private String textoFiltrar;

    public GruposFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (inflater.inflate(R.layout.fragment_grupos, container, false));

        anadirGrupos = (FloatingActionButton) view.findViewById(R.id.btnAnadirGrupo);
        listaGrupos = (ListView) view.findViewById(R.id.listaGrupos);

        registerForContextMenu(listaGrupos);
        final String id = firebaseAuth.getUid();

        setHasOptionsMenu(true);

        BBDD.child("Grupos").orderByChild("idUsuario").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Grupos grupo = dataSnapshot1.getValue(Grupos.class);
                        grupos.add(grupo);
                        adapterGrupos = new AdapterGrupos(R.layout.list_item_grupos, getContext(), grupos);
                        listaGrupos.setAdapter(adapterGrupos);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        anadirGrupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AnadirGrupo.class));
            }
        });

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(grupos.get(info.position).getNombre());
        MenuInflater inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.menu_grupo, menu);

    }


    @Override
    public boolean onContextItemSelected(MenuItem menu){

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu.getMenuInfo();

        switch(menu.getItemId()){
            case R.id.eliminarGrupo:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder
                        .setTitle("Eliminar grupo")
                        .setMessage("¿Seguro que quiere eliminar" + grupos.get(info.position).getNombre())
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BBDD.child("Grupos").child(grupos.get(info.position).getIdGrupo()).removeValue();
                                grupos.remove(info.position);
                                grupos.clear();
                                adapterGrupos.notifyDataSetChanged();
                            }
                        });
                builder.setCancelable(false);
                builder.show();
                return true;

            case R.id.modificarGrupo :
                Intent intent = new Intent(getActivity(), AnadirGrupo.class);
                intent.putExtra("idGrupo", grupos.get(info.position).getIdGrupo());
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(menu);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscador, menu);
        MenuItem searchItem = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(view, 0);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //ArrayList <Grupos> grupoFiltrado = filtrarGrupos(grupos, textoFiltrar);
                adapterGrupos.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

   /* @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList <Grupos> grupoFiltrado = filtrarGrupos(grupos, textoFiltrar);
                //adapterGrupos.setFilter(grupoFiltrado);
                return false;
            }
        });
    }
    private ArrayList<Grupos> filtrarGrupos(ArrayList<Grupos> grupos, String textoFiltrar ){
        ArrayList<Grupos> gruposFiltrados = new ArrayList<>();
        textoFiltrar= textoFiltrar.toLowerCase();
        for ( Grupos grupo1 : grupos) {
            String grupo2 = grupo1.getNombre().toLowerCase();
            if(grupo2.contains(textoFiltrar)){
                gruposFiltrados.add(grupo1);
            }
        }
        return gruposFiltrados;
    }*/
}
