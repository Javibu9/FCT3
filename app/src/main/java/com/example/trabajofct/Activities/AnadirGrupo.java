package com.example.trabajofct.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabajo1.R;
import com.example.trabajofct.Fragments.TabsAlumno;
import com.example.trabajofct.Modules.Grupos;
import com.example.trabajofct.Modules.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AnadirGrupo extends AppCompatActivity {

    private EditText EditTextNombre;
    private EditText EditTextNumero;
    private Button anadirGrupo;
    private FirebaseAuth Autorizacion;
    private DatabaseReference BBDD;
    private StorageReference Storage;
    private Grupos grupo;
    private String nombre;
    private String idUsuario;
    private int numero;
    private boolean crear = false;
    private String idGrupo;
    private Grupos grupoMod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_grupo);

        anadirGrupo = (Button) findViewById(R.id.btnAnadir);
        EditTextNombre = (EditText) findViewById(R.id.editTextNombreGrupo);
        EditTextNumero = (EditText) findViewById(R.id.editTextNumeroGrupo);


        Autorizacion = FirebaseAuth.getInstance();
        BBDD = FirebaseDatabase.getInstance().getReference();
        Storage = FirebaseStorage.getInstance().getReference("Grupos");

        //para saber si se va a modificar o crear un grupo
        if (getIntent().getExtras() != null) {
            idGrupo = getIntent().getStringExtra("idGrupo");
            crear = false;
        } else {
            crear = true;
        }

        //si se va a modificar se cargan los datos de ese grupo
        if (crear == false) {

            BBDD.child("Grupos").child(idGrupo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    grupoMod = dataSnapshot.getValue(Grupos.class);
                    EditTextNombre.setText(grupoMod.getNombre());
                    EditTextNumero.setText(""+grupoMod.getNumero());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        anadirGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = EditTextNombre.getText().toString();
                numero = Integer.parseInt(""+EditTextNumero.getText().toString());
                idUsuario = Autorizacion.getUid();


                if (!nombre.isEmpty() && numero != 0) {
                    if (crear == true){
                        //nuevo grupo con su id
                        String id = BBDD.child("Grupos").push().getKey();
                        grupo = new Grupos(id, nombre, numero, idUsuario);
                        BBDD.child("Grupos").child(id).setValue(grupo);
                    }else{
                        //actualizamos con un nuevo objeto de grupos para el que ya estaba
                        grupoMod = new Grupos(idGrupo, nombre, numero, idUsuario);
                        BBDD.child("Grupos").child(idGrupo).setValue(grupoMod);
                    }
                    startActivity(new Intent(getApplicationContext(), GestionarActivity.class));
                } else {
                    Toast.makeText(AnadirGrupo.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


}
