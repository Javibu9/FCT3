package com.example.trabajofct.Fragments;

import android.app.AlertDialog;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.trabajo1.R;
import com.example.trabajofct.Activities.AnadirAsignatura;
import com.example.trabajofct.Activities.AnadirGrupo;
import com.example.trabajofct.Adapters.AdapterAsignaturas;
import com.example.trabajofct.Adapters.AdapterGrupos;
import com.example.trabajofct.Modules.Asignaturas;
import com.example.trabajofct.Modules.Grupos;
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
public class AsignaturasFragment extends Fragment {

    private FloatingActionButton anadirAsignaturas;
    private View view;
    private ListView listaAsignaturas;
    private AdapterAsignaturas adapterAsignaturas;
    private ArrayList<Asignaturas> asignaturas = new ArrayList<>();
    private DatabaseReference BBDD = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public AsignaturasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_asignaturas, container, false);

        listaAsignaturas = (ListView)view.findViewById(R.id.listaAsignaturas);
        asignaturas.clear();

        registerForContextMenu(listaAsignaturas);

        setHasOptionsMenu(true);

        BBDD.child("Asignaturas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Asignaturas asignatura = dataSnapshot1.getValue(Asignaturas.class);
                    asignaturas.add(asignatura);
                    adapterAsignaturas = new AdapterAsignaturas(R.layout.list_item_asignaturas, getContext(), asignaturas);
                    listaAsignaturas.setAdapter(adapterAsignaturas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        anadirAsignaturas = (FloatingActionButton) view.findViewById(R.id.anadirAsignaturas);
        anadirAsignaturas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AnadirAsignatura.class));

            }
        });



        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(asignaturas.get(info.position).getNombre());
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_asignaturas, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem menu){

        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu.getMenuInfo();

        switch(menu.getItemId()){
            case R.id.eliminarAsignaturas:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder
                        .setTitle("Eliminar grupo")
                        .setMessage("¿Seguro que quiere eliminar" + asignaturas.get(info.position).getNombre())
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BBDD.child("Asignaturas").child(asignaturas.get(info.position).getIdAsignatura()).removeValue();
                                asignaturas.remove(info.position);
                                asignaturas.clear();
                                adapterAsignaturas.notifyDataSetChanged();
                            }
                        });
                builder.setCancelable(false);
                builder.show();
                return true;

            case R.id.modificarAsignaturas :
                Intent intent = new Intent(getActivity(), AnadirAsignatura.class);
                intent.putExtra("idAsignatura", asignaturas.get(info.position).getIdAsignatura());
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
                adapterAsignaturas.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
