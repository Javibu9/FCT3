package com.example.trabajofct.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trabajo1.R;
import com.example.trabajofct.Modules.Asignaturas;
import com.example.trabajofct.Modules.Grupos;
import com.example.trabajofct.Modules.Usuarios;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AnadirAsignatura extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextCurso;
    private EditText editTextDescripcion;


    private Button añadirAsignatura;
    private Button añadirImagen;
    private Usuarios A;
    private boolean subirfoto= false;
    private String nombre;
    private String curso;
    private String descripcion;
    private String urlImagenAsig;
    private String idUsuario;
    private Uri uri;
    private String id;
    private final int GALLERY_INTENT = 1;
    private ImageView crearImagenAsignatura;
    private boolean crear = false;
    private FirebaseAuth autorizacion;
    private DatabaseReference BBDD;
    private StorageReference storage;
    private String idAsignatura;
    private boolean modificarFoto = false;
    private Asignaturas asignatura;
    private Asignaturas asignaturaMod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_asignatura);

        autorizacion = FirebaseAuth.getInstance();
        BBDD = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference("Asignaturas");

        añadirImagen = (Button) findViewById(R.id.btnSubirFotoAsignatura);
        añadirAsignatura = (Button) findViewById(R.id.btnRegistrarAsignatura);
        editTextNombre = (EditText) findViewById(R.id.editTextNombreAsignatura);
        editTextCurso = (EditText) findViewById(R.id.editTextCursoAsigatura);
        editTextDescripcion = (EditText) findViewById(R.id.editTextDescripcionAsignatura);
        crearImagenAsignatura = (ImageView) findViewById(R.id.crearImagenAsignatura);

        idUsuario = autorizacion.getUid();

        //para saber si se va a modificar o crear una asignatura
        if (getIntent().getExtras() != null) {
            idAsignatura = getIntent().getStringExtra("idAsignatura");
            crear = false;
        } else {
            crear = true;
        }

        //si se va a modificar se cargan los datos de esa asignatura
        if (crear == false) {

            BBDD.child("Asignaturas").child(idAsignatura).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    asignaturaMod = dataSnapshot.getValue(Asignaturas.class);
                    editTextNombre.setText(asignaturaMod.getNombre());
                    editTextCurso.setText(asignaturaMod.getCurso());
                    editTextDescripcion.setText(asignaturaMod.getDescripcion());

                    Picasso.with(getApplicationContext()).load(asignaturaMod.getUrlImagenAsig()).into(crearImagenAsignatura);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        //al añadir una imagen se va a cambiar la foto asi que la vamos a modificar
        añadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                modificarFoto = true;
            }

        });

        añadirAsignatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = editTextNombre.getText().toString();
                curso = editTextCurso.getText().toString();
                descripcion = editTextDescripcion.getText().toString();

                if (modificarFoto == true){
                    if (uri != null){
                        StorageReference filepath = storage.child(uri.getLastPathSegment());

                        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> rutaUri = storage.child(uri.getLastPathSegment()).getDownloadUrl();
                                rutaUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        urlImagenAsig = uri.toString();

                                        if (!nombre.isEmpty() && !curso.isEmpty() && !descripcion.isEmpty()) {

                                            if (crear == true){
                                                //creamos una nueva asignatura con su id
                                                String id = BBDD.child("Asignaturas").push().getKey();
                                                asignatura = new Asignaturas( id, nombre, curso, descripcion , urlImagenAsig, idUsuario);
                                                BBDD.child("Asignaturas").child(id).setValue(asignatura);
                                            }else{
                                                //actualizamos con un nuevo objeto de asignaturas para la que ya estaba
                                                asignaturaMod = new Asignaturas(idAsignatura, nombre, curso, descripcion, urlImagenAsig, idUsuario);
                                                BBDD.child("Asignaturas").child(idAsignatura).setValue(asignaturaMod);
                                            }


                                            startActivity(new Intent(getApplicationContext(), GestionarActivity.class));

                                        } else {
                                            Toast.makeText( AnadirAsignatura.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                        });


                    }else{
                        Toast.makeText( AnadirAsignatura.this, "Inserte una imagen", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    //cuando no cambiamos la foto solo actualizamos los valores
                    BBDD.child("Asignaturas").child(idAsignatura).child("nombre").setValue(editTextNombre.getText().toString());
                    BBDD.child("Asignaturas").child(idAsignatura).child("curso").setValue(editTextCurso.getText().toString());
                    BBDD.child("Asignaturas").child(idAsignatura).child("descripcion").setValue(editTextDescripcion.getText().toString());

                    startActivity(new Intent(getApplicationContext(), GestionarActivity.class));
                }

            }
        });

    }

    //para captar la imagen en directo
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            Picasso.with(this).load(uri).into(crearImagenAsignatura);

            subirfoto= true;
        }
    }


}