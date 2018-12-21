package com.example.marta.fbandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QuickTrade extends AppCompatActivity {
    private Button btnAcceder, btnAnyadir, btnModProd, btnBorrar, btnBuscarProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_trade);

        btnAcceder = (Button) findViewById(R.id.btnAcceder);
        btnAnyadir = (Button) findViewById(R.id.btnAnyadir);
        btnModProd = (Button) findViewById(R.id.btnMod);
        btnBorrar = (Button) findViewById(R.id.btnBorrar);
        btnBuscarProd = (Button) findViewById(R.id.btnBuscar);

        // Modificación de perfil:
        btnAcceder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Perfil.class);
                startActivity(i);
            }
        });
    }
}
