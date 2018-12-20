package com.example.marta.fbandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Clase de registro:
 */
public class Registro extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etCorreo, etPass, etNom, etApe, etDir, etUsu;
    private Button btnRegistro;
    DatabaseReference dbr;
    Usuario u;

    private String email, password, nombre, apellidos, direccion, usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etCorreo = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        etNom = (EditText) findViewById(R.id.etNombre);
        etApe = (EditText) findViewById(R.id.etApe);
        etDir = (EditText) findViewById(R.id.etDir);
        etUsu = (EditText) findViewById(R.id.etUsuario);

        btnRegistro = (Button) findViewById(R.id.btnRegistro);


        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etCorreo.getText().toString();
                password = etPass.getText().toString();
                nombre = etNom.getText().toString();
                apellidos = etApe.getText().toString();
                usuario = etUsu.getText().toString();
                direccion = etDir.getText().toString();

                registro(nombre, apellidos, password, email, usuario, direccion);

            }
        });
    }

    private void registro(final String mNombre, final String mApellidos, final String mPassword, final String mEmail,
                          final String mUsuario, final String mDireccion) {
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Registro.this, "Datos registrados.",
                                    Toast.LENGTH_SHORT).show();

                            // Pillada referencia + creado el nuevo usuario:
                            dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));
                            u = new Usuario(user.getUid(), mUsuario, mPassword, mNombre, mApellidos,
                                    mDireccion, mEmail);

                            //Genera clave:
                            String clave = dbr.push().getKey();
                            // Añadida clave a usuario:
                            dbr.child(clave).setValue(u);

                        } else {
                            Toast.makeText(Registro.this, "Registro fallido.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

}
