package com.example.trabajofct.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trabajo1.R;
import com.example.trabajofct.Modules.Asignaturas;
import com.example.trabajofct.Modules.Usuarios;
import com.example.trabajofct.Utils.Util;
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

public class AnadirUsuariosActivity extends AppCompatActivity {
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
    private String idUsuarioAnadir;
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
    private List<String>asignaturasRestantes;

    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir_usuarios);

        autorizacion = FirebaseAuth.getInstance();
        BBDD = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference("Usuarios");


        buttonSubirFoto = (Button) findViewById(R.id.btnSubirFotoAnadir);
        editTextNombre = (EditText) findViewById(R.id.editTextNombreAnadir);
        editTextEmail = (EditText) findViewById(R.id.editTextEmailAnadir);
        editTextContraseña = (EditText) findViewById(R.id.editTextContraseñaAnadir);
        editTextApellidos = (EditText) findViewById(R.id.editTextApellidosAnadir);
        editTextEdad = (EditText) findViewById(R.id.editTextEdadAnadir);
        buttonRegistrar = (Button) findViewById(R.id.btnRegistrarAnadir);
        imagenUsuario = (ImageView) findViewById(R.id.crearImagenUsuarioAnadir);
        spinnerTipo = (Spinner) findViewById(R.id.spinnerTipoAnadir);
        botonAsignarAsignaturas = (Button) findViewById(R.id.botonAsignarAsignaturasAnadir);
        botonAsignarGrupo = (Button)findViewById(R.id.botonAsignarGrupoAnadir);
        textoAsignarAsignaturas = (TextView) findViewById(R.id.textoAsignarAsignaturasAnadir);
        textoAsignarGrupo = (TextView) findViewById(R.id.textoAsignarGrupoAnadir);
        buttonAsignarAsig = (Button) findViewById(R.id.botonAsignarAsignaturasAnadir);
        buttonAsignarGrupos = (Button) findViewById(R.id.botonAsignarGrupoAnadir);


        //para saber si se va a modificar o crear una asignatura
        if (getIntent().getExtras() != null) {
            idUsuario = getIntent().getStringExtra("idUsuario");
            crear = false;
        } else {
            crear = true;
        }

        //si se va a modificar se cargan los datos de esa asignatura
        if (crear == false) {

            BBDD.child("Usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    usuarioMod = dataSnapshot.getValue(Usuarios.class);
                    editTextNombre.setText(usuarioMod.getNombre());
                    editTextApellidos.setText(usuarioMod.getApellidos());
                    editTextEmail.setText(usuarioMod.getEmail());
                    editTextEdad.setText(""+usuarioMod.getEdad());
                    spinnerTipo.setSelection(getIndex(spinnerTipo, usuarioMod.getTipoUsuario()));

                    Picasso.with(getApplicationContext()).load(usuarioMod.getUrlImagen()).into(imagenUsuario);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        tipos = Util.tiposUsuario();
        final ArrayAdapter<String> posicionAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(posicionAdapter);


        buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                modificarFoto = true;
            }

        });
        builder = new AlertDialog.Builder(this);
        buttonAsignarAsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BBDD.child("Asignaturas").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
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
                                for (int i = 0 ; i<asignaturas.size(); i++){

                                    if (i==0){
                                        nombreAsignaturas += asignaturas.get(i);

                                    }else{
                                        nombreAsignaturas += ", "+asignaturas.get(i);

                                    }
                                }
                                textoAsignarAsignaturas.setText("Asignaturas: "+nombreAsignaturas);

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
                tipoUsuario = spinnerTipo.getSelectedItem().toString();

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
                                        id = autorizacion.getCurrentUser().getUid();

                                        if (!nombre.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !apellidos.isEmpty() && edad != 0) {
                                            if (contraseña.length() >= 6) {
                                                if (crear == true) {
                                                    idUsuarioAnadir = BBDD.child("Usuarios").push().getKey();
                                                    usuario = new Usuarios(idUsuarioAnadir, rutaFoto, nombre, apellidos, email, edad, contraseña, tipoUsuario, grupo, asignaturas);
                                                    registerUser();

                                                } else {
                                                    //actualizamos con un nuevo objeto de asignaturas para la que ya estaba
                                                    usuarioMod = new Usuarios(id, rutaFoto, nombre, apellidos, email, edad, contraseña, tipoUsuario, grupo, asignaturas);
                                                    BBDD.child("Usuarios").child(idUsuario).setValue(usuarioMod);
                                                    startActivity(new Intent(getApplicationContext(), GestionarActivity.class));
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(), "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "Inserte una imagen", Toast.LENGTH_SHORT).show();

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

                    BBDD.child("Usuarios").child(idUsuarioAnadir).setValue(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {

                            if (task2.isSuccessful()) {
                                    startActivity(new Intent(getApplicationContext(), GestionarActivity.class));
                                    finish();



                            } else {
                                Toast.makeText(getApplicationContext(), "No se han podido crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }
}