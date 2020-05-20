package com.example.trabajofct.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trabajo1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasena extends AppCompatActivity {
    private EditText EmailVerif;
    private Button verificar;
    private String email;
    private FirebaseAuth Autorizacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasena);

        Autorizacion= FirebaseAuth.getInstance();

        EmailVerif = (EditText) findViewById(R.id.VerificarEmail);
        verificar = (Button) findViewById(R.id.btnVerificar);

        verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = EmailVerif.getText().toString();
                if(!email.isEmpty()){
                    ResetContrasena();
                }else{
                    Toast.makeText(RecuperarContrasena.this, "Ingresa el email", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void ResetContrasena(){
        Autorizacion.setLanguageCode("es");
        Autorizacion.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(RecuperarContrasena.this, "Mensaje enviado con exito", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(RecuperarContrasena.this, "No se pudo enviar el mensaje", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}
