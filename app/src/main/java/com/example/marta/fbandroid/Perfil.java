package com.example.marta.fbandroid;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 *
 */
public class Perfil extends AppCompatActivity {
    private String nombre, apellidos, direccion, idUsu;
    private EditText edNombre, edApellidos, edDireccion;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    Button btncambiar;

    private FirebaseAuth mAuth;
    private DatabaseReference dbr;

    private Usuario u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        edNombre = (EditText) findViewById(R.id.edNombre);
        edApellidos = (EditText) findViewById(R.id.edApe);
        edDireccion = (EditText) findViewById(R.id.edDireccion);

        mAuth = FirebaseAuth.getInstance();

        // Coge usuario logeado:
        final FirebaseUser user = mAuth.getCurrentUser();

        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.nodoUsu));

        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    u = datasnapshot.getValue(Usuario.class);

                    String usuario = u.getUsuario();
                    String nombre = u.getNombre();
                    String apellido = u.getApellidos();
                    String correo = u.getCorreo();
                    String pass = u.getContraseña();
                    String dir = u.getDireccion();
                    String userid = u.getId();

                    usuarios.add(new Usuario(usuario, correo, nombre, apellido, pass,
                            dir, userid));
                }

                for (Usuario usu : usuarios) {
                    Usuario eUsu = usu;

                    if (eUsu.getId().compareTo(user.getUid()) == 0) {
                        idUsu = eUsu.getUsuario();

                        edNombre.setText(eUsu.getNombre());
                        edApellidos.setText(eUsu.getDireccion());
                        edDireccion.setText(eUsu.getApellidos());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Perfil.this, "Error al cargar los usuarios.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Button btnMod = (Button) findViewById(R.id.btnCambiar);
        btnMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String eNom = edNombre.getText().toString();
                final String eApe = edApellidos.getText().toString();
                final String eDir = edDireccion.getText().toString();

                // Si los campos no están activos:
                if (!TextUtils.isEmpty(eNom) ||  !TextUtils.isEmpty(eApe) || !TextUtils.isEmpty(eDir) ){
                    // Búsqueda por nombre de usuario:
                    Query q = dbr.orderByChild(getString(R.string.usuario)).equalTo(idUsu);
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                                // Obtiene id de JSON
                                String clave =  datasnapshot.getKey();
                                // Cambiar valores de JSON:
                                dbr.child(clave).child(getString(R.string.nombre)).setValue(eNom);
                                dbr.child(clave).child(getString(R.string.apellidos)).setValue(eApe);
                                dbr.child(clave).child(getString(R.string.direccion)).setValue(eDir);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(Perfil.this, "Hubo un error en la modificación de datos.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            });


    }


    }


