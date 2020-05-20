package com.example.trabajofct.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;


import com.example.trabajo1.R;
import com.example.trabajofct.Modules.Usuarios;
import com.example.trabajofct.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private EditText EditTextEmail;
    private EditText EditTextContraseña;
    private Button ButtonLogin;
    private Button ButtonRegistrer;
    private Button ButtonRecuperar;
    private Switch remember;
    private String email="";
    private String contraseña="";
    private FirebaseAuth Autorizacion;
    private Usuarios usuarios;
    private DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindUI();
        //guardar ultima sesion
        preferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        setCredentialsIfExist();

        Autorizacion= FirebaseAuth.getInstance();

        ButtonRegistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        ButtonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                email= EditTextEmail.getText().toString();
                contraseña= EditTextContraseña.getText().toString();
                //si los campos email y contraseña no estan vacios el usuario se logea
                if (!email.isEmpty() && !contraseña.isEmpty()){
                    if (loginUser()){
                        //AdvanceActivity();
                        saveOnPreferences(email, contraseña);

                    }
                }//si los campos email y contraseña estan vacios escribe el siguiente mensaje
                else {
                    Toast.makeText(MainActivity.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }

        });
        ButtonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecuperarContrasena.class);
                startActivity(intent);
            }
        });

    }
    private void saveOnPreferences(String email, String contraseña) {
        //si en el switch elige guardar lo guarda
        if (remember.isChecked()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("email", email);
            editor.putString("pass", contraseña);
            editor.apply();
        }
    }

    private void bindUI() {

        remember= (Switch) findViewById(R.id.remember);
        EditTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditTextContraseña = (EditText) findViewById(R.id.editTextPassword);
        ButtonLogin = (Button) findViewById(R.id.btnLogin);
        ButtonRegistrer = (Button) findViewById(R.id.btnRegistrar);
        ButtonRecuperar = (Button) findViewById(R.id.btnRecuperar);
    }
    private void setCredentialsIfExist(){
        String email = Util.getUserMailPrefs(preferences);
        String password = Util.getUserPassPrefs(preferences);
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            EditTextEmail.setText(email);
            EditTextContraseña.setText(password);
        }
    }


    private boolean loginUser(){
        Autorizacion.signInWithEmailAndPassword(email, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //metodo para logearse
                if (task.isSuccessful()){

                    Intent intent = new Intent(MainActivity.this, GestionarActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                //si al logearse los datos son incorrectos se escribira el siguiente mensaje
                else{
                    Toast.makeText(MainActivity.this, "ERROR. comprueba los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return false;
    }
}
