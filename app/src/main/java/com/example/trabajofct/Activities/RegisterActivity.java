package com.example.trabajofct.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.example.trabajo1.R;
import com.example.trabajofct.Modules.Asignaturas;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextNombre;
    private EditText editTextEmail;
    private EditText editTextApellidos;
    private EditText editTextEdad;
    private EditText editTextContraseña;
    private Spinner spinnerTipo;
    private Button botonAsignarAsignaturas, botonAsignarGrupo;
    private TextView textoAsignarAsignaturas, textoAsignarGrupo;

    private Button buttonRegistrar;
    private Button buttonSubirFoto;
    private Usuarios A;
    private boolean subirfoto = false;
    private String nombre;
    private String email;
    private String contraseña;
    private String apellidos;
    private String rutaFoto;
    private String tipoUsuario;
    private String grupo;
    private int edad;
    private Uri uri;
    private String id;
    private final int GALLERY_INTENT = 1;
    private ImageView imagenUsuario;

    private FirebaseAuth autorizacion;
    private DatabaseReference BBDD;
    private StorageReference storage;
    private Button buttonAsignarAsig;
    private Usuarios usuario;
    private Button buttonAsignarGrupos;
    private boolean crear = true;
    private String idUsuario;
    private Usuarios usuarioMod;
    private boolean modificarFoto = false;
    private List<String> tipos;
    private Collection<String> nombreAsignaturas = new ArrayList<>();
    private boolean[] asignaturasSeleccionadas;
    private ArrayList<Integer> aignaturasItems = new ArrayList<>();
    private CharSequence[] charSequence;
    private ArrayList<String> asignaturas = new ArrayList<String>();
    private List<String> asignaturasRestantes;
    private ListView listaMostrarGrupos;
    private ArrayList<String>  grupos =new ArrayList();
    private AlertDialog.Builder builder;
    private AlertDialog.Builder builder2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        autorizacion = FirebaseAuth.getInstance();
        BBDD = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference("Usuarios");


        buttonSubirFoto = (Button) findViewById(R.id.btnSubirFoto);
        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextContraseña = (EditText) findViewById(R.id.editTextContraseña);
        editTextApellidos = (EditText) findViewById(R.id.editTextApellidos);
        editTextEdad = (EditText) findViewById(R.id.editTextEdad);
        buttonRegistrar = (Button) findViewById(R.id.btnRegistrar);
        imagenUsuario = (ImageView) findViewById(R.id.crearImagenUsuario);
        botonAsignarAsignaturas = (Button) findViewById(R.id.botonAsignarAsignaturas);
        botonAsignarGrupo = (Button) findViewById(R.id.botonAsignarGrupo);
        textoAsignarAsignaturas = (TextView) findViewById(R.id.textoAsignarAsignaturas);
        textoAsignarGrupo = (TextView) findViewById(R.id.textoAsignarGrupo);
        buttonAsignarAsig = (Button) findViewById(R.id.botonAsignarAsignaturas);
        buttonAsignarGrupos = (Button) findViewById(R.id.botonAsignarGrupo);


        buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                modificarFoto = true;
            }

        });
        BBDD.child("Grupos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Grupos grupo = dataSnapshot1.getValue(Grupos.class);
                    grupos.add(grupo.getNombre());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
         builder2 = new AlertDialog.Builder(this);

        buttonAsignarGrupos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutInflater inflaterList2 = getLayoutInflater();
                View dialogView2 = inflaterList2.inflate(R.layout.emergente_asignar_grupo, null);
                listaMostrarGrupos = (ListView) dialogView2.findViewById(R.id.listaMostrarGrupos);

                ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, grupos.toArray());
                listaMostrarGrupos.setAdapter(adapter2);
                builder2.setView(dialogView2);
                builder2.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder2.show();

                listaMostrarGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String nombreGrupo = parent.getItemAtPosition(position).toString();
                        grupo = nombreGrupo;
                        textoAsignarGrupo.setText("grupo: " + nombreGrupo);
                    }
                });
            }
        });

        builder = new AlertDialog.Builder(this);
        buttonAsignarAsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BBDD.child("Asignaturas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Asignaturas a = dataSnapshot1.getValue(Asignaturas.class);

                            nombreAsignaturas.add(a.getNombre());

                        }
                        asignaturasSeleccionadas = new boolean[nombreAsignaturas.size()];

                        builder.setTitle("Escoja las asignaturas");
                        charSequence = nombreAsignaturas.toArray(new CharSequence[nombreAsignaturas.size()]);
                        builder.setMultiChoiceItems(charSequence, asignaturasSeleccionadas, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                                if (isChecked) {
                                    aignaturasItems.add(position);
                                } else {
                                    aignaturasItems.remove((Integer.valueOf(position)));
                                }
                            }
                        });
                        builder.setCancelable(false);
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buttonAsignarAsig.setClickable(true);
                            }
                        });
                        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //guardamos en una lista las asignaturas que han sido seleccionados
                                for (int i = 0; i < aignaturasItems.size(); i++) {
                                    asignaturas.add(charSequence[aignaturasItems.get(i)].toString());
                                }
                                //creamos una lista que hace referencia a las asignaturas disponibles
                                asignaturasRestantes = new ArrayList<>(nombreAsignaturas);
                                //y le borramos los que esten en la lista de titulares
                                asignaturasRestantes.removeAll(asignaturas);
                                String nombreAsignaturas = "";
                                for (int i = 0; i < asignaturas.size(); i++) {

                                    if (i == 0) {
                                        nombreAsignaturas += asignaturas.get(i);

                                    } else {
                                        nombreAsignaturas += ", " + asignaturas.get(i);

                                    }
                                }
                                textoAsignarAsignaturas.setText("Asignaturas: " + nombreAsignaturas);

                            }

                        });
                        builder.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = editTextNombre.getText().toString();
                email = editTextEmail.getText().toString();
                contraseña = editTextContraseña.getText().toString();
                apellidos = editTextApellidos.getText().toString();
                edad = Integer.parseInt(editTextEdad.getText().toString());
                tipoUsuario = "Alumno";

                if (modificarFoto = true) {
                    if (uri != null) {
                        StorageReference filepath = storage.child(uri.getLastPathSegment());

                        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final Task<Uri> rutaUri = storage.child(uri.getLastPathSegment()).getDownloadUrl();
                                rutaUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        rutaFoto = uri.toString();

                                        if (!nombre.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !apellidos.isEmpty() && edad != 0) {
                                            if (contraseña.length() >= 6) {

                                                registerUser();


                                            } else {
                                                Toast.makeText(RegisterActivity.this, "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "Debe completar todos los campos", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            }
                        });


                    } else {
                        Toast.makeText(RegisterActivity.this, "Inserte una imagen", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    //cuando no cambiamos la foto solo actualizamos los valores
                    BBDD.child("Usuarios").child(idUsuario).child("nombre").setValue(editTextNombre.getText().toString());
                    BBDD.child("Usuarios").child(idUsuario).child("apellidos").setValue(editTextApellidos.getText().toString());
                    BBDD.child("Usuarios").child(idUsuario).child("edad").setValue(editTextEdad.getText().toString());
                    BBDD.child("Usuarios").child(idUsuario).child("email").setValue(editTextEmail.getText().toString());
                    BBDD.child("Usuarios").child(idUsuario).child("contraseña").setValue(editTextContraseña.getText().toString());
                    startActivity(new Intent(getApplicationContext(), GestionarActivity.class));
                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.with(this).load(uri).into(imagenUsuario);

            subirfoto = true;
        }
    }

    private void registerUser() {

        autorizacion.createUserWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    id = autorizacion.getCurrentUser().getUid();
                    usuario = new Usuarios(id, rutaFoto, nombre, apellidos, email, edad, contraseña, tipoUsuario, grupo, asignaturas);

                    BBDD.child("Usuarios").child(id).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {

                                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                    Toast.makeText(RegisterActivity.this, "Inicia sesion para continuar", Toast.LENGTH_SHORT).show();
                                    finish();



                            } else {
                                Toast.makeText(RegisterActivity.this, "No se han podido crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}