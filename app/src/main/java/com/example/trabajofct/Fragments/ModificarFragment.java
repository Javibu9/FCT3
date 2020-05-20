
package com.example.trabajofct.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.trabajo1.R;
import com.example.trabajofct.Activities.GestionarActivity;
import com.example.trabajofct.Activities.RegisterActivity;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModificarFragment extends Fragment {

    private EditText editTextNombreModificar;
    private EditText editTextEmailModificar;
    private EditText editTextApellidosModificar;
    private EditText editTextEdadModificar;
    private EditText editTextContraseñaModificar;

    private Button buttonModificar;
    private Button buttonSubirFoto;
    private boolean modificarFoto= false;
    private Uri uri;
    private final int GALLERY_INTENT = 1;
    private ImageView imagenUsuario;
    private FirebaseAuth autorizacion;
    private DatabaseReference BBDD;
    private StorageReference storage;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    private String id;
    private String rutaFoto;
    private String nombre, email, contraseña, apellidos;
    private int edad;

    public ModificarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_modificar, container, false);

        autorizacion = FirebaseAuth.getInstance();
        BBDD = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();


        buttonSubirFoto= (Button) vista.findViewById(R.id.btnSubirFotoModificar);
        editTextNombreModificar = (EditText) vista.findViewById(R.id.editTextNombreModificar);
        editTextEmailModificar = (EditText) vista.findViewById(R.id.editTextEmailModificar);
        editTextContraseñaModificar = (EditText) vista.findViewById(R.id.editTextContraseñaModificar);
        editTextApellidosModificar = (EditText) vista.findViewById(R.id.editTextApellidosModificar);
        editTextEdadModificar = (EditText) vista.findViewById(R.id.editTextEdadModificar);
        buttonModificar = (Button) vista.findViewById(R.id.btnModificar);
        imagenUsuario = (ImageView) vista.findViewById(R.id.modificarImagenUsuario) ;
        id = autorizacion.getCurrentUser().getUid();

        firebase.child("Usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuarios u = dataSnapshot.getValue(Usuarios.class);
                editTextNombreModificar.setText(""+u.getNombre());
                editTextApellidosModificar.setText(""+u.getApellidos());
                editTextEdadModificar.setText(""+u.getEdad());
                editTextEmailModificar.setText(""+u.getEmail());
                editTextContraseñaModificar.setText(""+u.getContraseña());

                Picasso.with(getContext()).load(u.getUrlImagen()).into(imagenUsuario);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        buttonSubirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
                modificarFoto= true;
            }

        });

        buttonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombre = editTextNombreModificar.getText().toString();
                email = editTextEmailModificar.getText().toString();
                contraseña = editTextContraseñaModificar.getText().toString();
                apellidos =  editTextApellidosModificar.getText().toString();
                edad = Integer.parseInt(editTextEdadModificar.getText().toString());

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
                                        rutaFoto = uri.toString();

                                        if (!nombre.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !apellidos.isEmpty() && edad!=0) {
                                            if (contraseña.length() >= 6) {
                                                firebase.child("Usuarios").child(id).child("nombre").setValue(editTextNombreModificar.getText().toString());
                                                firebase.child("Usuarios").child(id).child("apellidos").setValue(editTextApellidosModificar.getText().toString());
                                                firebase.child("Usuarios").child(id).child("edad").setValue(Integer.parseInt(editTextEdadModificar.getText().toString()));
                                                firebase.child("Usuarios").child(id).child("email").setValue(editTextEmailModificar.getText().toString());
                                                firebase.child("Usuarios").child(id).child("contraseña").setValue(editTextContraseñaModificar.getText().toString());
                                                firebase.child("Usuarios").child(id).child("urlImagen").setValue(rutaFoto);

                                                Intent intent = new Intent(getContext(), GestionarActivity.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getContext(), "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();

                                            }
                                        } else {
                                            Toast.makeText( getContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                            }
                        });


                    }else{
                        Toast.makeText( getContext(), "Inserte una imagen", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    if (!nombre.isEmpty() && !email.isEmpty() && !contraseña.isEmpty() && !apellidos.isEmpty() && edad!=0) {
                        if (contraseña.length() >= 6) {
                            firebase.child("Usuarios").child(id).child("nombre").setValue(editTextNombreModificar.getText().toString());
                            firebase.child("Usuarios").child(id).child("apellidos").setValue(editTextApellidosModificar.getText().toString());
                            firebase.child("Usuarios").child(id).child("edad").setValue(Integer.parseInt(editTextEdadModificar.getText().toString()));
                            firebase.child("Usuarios").child(id).child("email").setValue(editTextEmailModificar.getText().toString());
                            firebase.child("Usuarios").child(id).child("contraseña").setValue(editTextContraseñaModificar.getText().toString());

                            Intent intent = new Intent(getContext(), GestionarActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "La contraseña debe de tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText( getContext(), "Debe completar todos los campos", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

        // Inflate the layout for this fragment
        return vista;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            Picasso.with(getContext()).load(uri).into(imagenUsuario);

        }
    }
}
