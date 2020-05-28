package com.example.trabajofct.Activities;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.trabajofct.Adapters.AdapterGrupos;
import com.example.trabajofct.Adapters.PagerAdapter;

import com.example.trabajo1.R;
import com.example.trabajofct.Fragments.ModificarFragment;
import com.example.trabajofct.Fragments.ReunionesFragment;
import com.example.trabajofct.Fragments.TabsAlumno;
import com.example.trabajofct.Modules.Usuarios;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GestionarActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    public static final int ALUMNOS_FRAGMENT = 0;
    public static final int  GRUPOS_FRAGMENT= 1;
    public static final int  ASIGNATURAS_FRAGMENT = 2;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView nombreUsuario;
    private PagerAdapter adapter;
    private ImageView imagenPerfil;
    private AdapterGrupos adapterGrupos;

    private DatabaseReference firebase;
    private String id;
    private FirebaseAuth autorizacion = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbar();
        //setTabLayout();
      //  setViewPager();
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        drawerLayout = (DrawerLayout) findViewById(R.id.dlayout);
        navigationView= (NavigationView) findViewById(R.id.navview);
        View headerView = navigationView.getHeaderView(0);
        nombreUsuario = (TextView) headerView.findViewById(R.id.nombreusuario);
        firebase = FirebaseDatabase.getInstance().getReference();
        imagenPerfil = (ImageView) headerView.findViewById(R.id.imagenPerfil);

        //id = autorizacion.getCurrentUser().getUid();

        firebase.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Usuarios usuarios = dataSnapshot1.getValue(Usuarios.class);
                    if (usuarios.getEmail().equals(autorizacion.getCurrentUser().getEmail())) {
                        nombreUsuario.setText("" + usuarios.getNombre());
                        Picasso.with(getApplicationContext()).load(usuarios.getUrlImagen()).into(imagenPerfil);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

     /*   drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                boolean fragmentTransaction = false;
                Fragment fragment = null;

                switch (menuItem.getItemId()){
                    case R.id.menu_gestionar:
                        Intent intent = new Intent(getApplicationContext(), GestionarActivity.class);
                        drawerLayout.closeDrawers();
                        startActivity(intent);

                        break;
                    case R.id.menu_modificar:
                        fragment = new ModificarFragment();
                        fragmentTransaction = true;
                        break;
                    case R.id.menu_reuniones:
                        fragment = new ReunionesFragment();
                        fragmentTransaction = true;
                        break;

                }

                if (fragmentTransaction) {
                    changeFragment(fragment, menuItem);
                    drawerLayout.closeDrawers();
                }
                return false;

            }
        });
            setFragment( new TabsAlumno());
    }

    private void changeFragment(Fragment fragment, MenuItem menuItem) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
        menuItem.setChecked(true);
        getSupportActionBar().setTitle(menuItem.getTitle());
    }
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    public void setSupportActionBar(Toolbar toolbar) {
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buscador, menu);
        MenuItem item = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //ArrayList<Grupos> grupoFiltrado = filtrarGrupos(grupos, textoFiltrar);
                //adapterGrupos.setFilter(grupoFiltrado);
                adapterGrupos.setFilter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private void setTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Alumnos"));
        tabLayout.addTab(tabLayout.newTab().setText("Asignaturas"));
        tabLayout.addTab(tabLayout.newTab().setText("Grupos"));
    }*/

    /*private void setViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_logout:
                logOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void setFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dlayout);
        drawer.closeDrawer(GravityCompat.START);
    }
    private void logOut() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences() {
        preferences.edit().clear().apply();
    }
}
