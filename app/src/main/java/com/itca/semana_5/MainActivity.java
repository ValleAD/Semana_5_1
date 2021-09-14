package com.itca.semana_5;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.itca.semana_5.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion.db", null, 1);

    private EditText et_codigo, et_descripcion, et_precio;
    private Button btnAlta, btnConsulta, btnConsulta1, btnElimnar, btnActualizar, btnNuevo, btnSalir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);*/

        et_codigo = findViewById(R.id.et_codigo);
        et_descripcion = findViewById(R.id.et_descripcion);
        et_precio = findViewById(R.id.et_precio);

        btnAlta = findViewById(R.id.btnGuardar);
        btnConsulta = findViewById(R.id.btnConsulta);
        btnConsulta1 = findViewById(R.id.btnConsulta1);
        btnElimnar = findViewById(R.id.btnEliminar);
        btnActualizar = findViewById(R.id.btnEliminar);
        btnNuevo = findViewById(R.id.btnNuevo);
        btnSalir = findViewById(R.id.btnSalir);

        btnAlta.setOnClickListener(this);
        btnConsulta.setOnClickListener(this);
        btnConsulta1.setOnClickListener(this);
        btnElimnar.setOnClickListener(this);
        btnActualizar.setOnClickListener(this);
        btnNuevo.setOnClickListener(this);
        btnSalir.setOnClickListener(this);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        SQLiteDatabase bd = admin.getWritableDatabase();
        switch (view.getId())
        {
            case R.id.btnGuardar:
                //Toast.makeText(this, "Has hecho click en el boton Alta", Toast.LENGTH_SHORT).show();
                String codigo = et_codigo.getText().toString();
                String descripcion = et_descripcion.getText().toString();
                String precio = et_precio.getText().toString();

                ContentValues registro = new ContentValues();
                registro.put("codigo", codigo);
                registro.put("descripcion", descripcion);
                registro.put("precio", precio);

                if(codigo.isEmpty()){
                    et_codigo.setError("campo obligatorio");
                }
                else if (descripcion.isEmpty()){
                    et_descripcion.setError("campo obligatorio");
                }
                else if (precio.isEmpty()){
                    et_precio.setError("campo obligatorio");
                } else {
                    bd.insert("articulos", null, registro);
                    bd.close();
                    et_codigo.setText(null);
                    et_descripcion.setText(null);
                    et_precio.setText(null);
                    Toast.makeText(this,"Registro guardado exactamente" ,Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnConsulta:
                //Toast.makeText(this, "Has hecho click en el boton Consulta", Toast.LENGTH_SHORT).show();

                codigo = et_codigo.getText().toString();
                if(codigo.isEmpty()){
                    et_codigo.setError("Campo obligatorio");
                } else{
                    Cursor file = bd.rawQuery("select descripcion, precio from articulos where codigo=" + codigo, null);
                    if (file.moveToFirst()){
                        et_descripcion.setText(file.getString(0));
                        et_precio.setText(file.getString(1));
                    } else {
                        Toast.makeText(this, "No existe un artículo con código ingresado", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnConsulta1:
                //Toast.makeText(this, "Has hecho click en el boton Consulta 1", Toast.LENGTH_SHORT).show();

                descripcion = et_descripcion.getText().toString();
                if(descripcion.isEmpty()){
                    et_descripcion.setError("Campo obligatorio");
                } else{
                    Cursor file = bd.rawQuery("select codigo, precio from articulos where descripcion=" + descripcion, null);
                    if (file.moveToFirst()){
                        et_codigo.setText(file.getString(0));
                        et_precio.setText(file.getString(1));
                    } else {
                        Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnEliminar:
                //Toast.makeText(this, "Has hecho click en el boton Eliminar", Toast.LENGTH_SHORT).show();

                codigo = et_codigo.getText().toString();
                if (codigo.isEmpty()){
                    et_codigo.setError("Campo obligatorio");
                } else{
                    int cant_2 = bd.delete("articulos", "codigo" + codigo, null);
                    bd.close();
                    et_codigo.setText("");
                    et_descripcion.setText("");
                    et_precio.setText("");
                    if (cant_2 == 1)
                        Toast.makeText(this, "Se eliminó el registro del articulo con dicho código", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "No existe un artículo con código ingresado", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnActualizar:
                //Toast.makeText(this, "Has hecho click en el boton Actualizar", Toast.LENGTH_SHORT).show();

                codigo = et_codigo.getText().toString();
                descripcion = et_descripcion.getText().toString();
                if (codigo.isEmpty()){
                    et_codigo.setError("Campo obligatorio");
                } else {
                    precio = et_precio.getText().toString();
                    ContentValues register = new ContentValues();
                    register.put("codigo", codigo);
                    register.put("descripción", descripcion);
                    register.put("precio", precio);
                    int cant = bd.update("articulos", register, "codigo" + codigo, null);
                    if (cant == 1)
                        Toast.makeText(this, "Se actualizó el registro", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "No existe un artículo con código ingresado", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btnNuevo:
                //Toast.makeText(this, "Has hecho click en el boton Nuevo", Toast.LENGTH_SHORT).show();

                et_codigo.setText(null);
                et_descripcion.setText(null);
                et_precio.setText(null);
                break;
            case R.id.btnSalir:
                //Toast.makeText(this, "Has hecho click en el boton Salir", Toast.LENGTH_SHORT).show();

                finish();
                break;
            default:
                break;
        }
    }
    /*
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}