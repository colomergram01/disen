package com.example.planillaempresarialeltrabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.planillaempresarialeltrabajador.entidades.Datos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class ActivityIniciar extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    private EditText user,clave;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue request;
    private String usuario1,clave1,estado1;
    private ImageButton sesion1;
    private CheckBox ChekClave;
    private Spinner spinner1;
    private int nesecidad=1,entrada=1;
    private Intent viajarpagina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar);
        user=(EditText) findViewById(R.id.usersesion);
        clave=(EditText) findViewById(R.id.clavesesion);
        request= Volley.newRequestQueue(getApplicationContext());
        sesion1=(ImageButton) findViewById(R.id.entrar);
        ChekClave=(CheckBox) findViewById(R.id.checkBoxC);
        SharedPreferences preferencias1=getSharedPreferences("usuario", Context.MODE_PRIVATE);
        user.setText(preferencias1.getString("user",""));
        SharedPreferences preferencias2=getSharedPreferences("clave", Context.MODE_PRIVATE);
        clave.setText(preferencias2.getString("pass",""));
        spinner1 = (Spinner) findViewById(R.id.spinner);
        String[] opciones = {"Seleccione","Recuperación de usuario", "Restablecer contraseña"};
        ArrayAdapter<String> adactador = new ArrayAdapter<String>(this, R.layout.spinner_estilo, opciones);
        spinner1.setAdapter(adactador);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selegir = spinner1.getSelectedItem().toString();
                switch (selegir) {
                    case "Recuperación de usuario": {
                        Intent paginarecuperar=new Intent(getApplicationContext(),ActivityRecuperar.class);
                        startActivity(paginarecuperar);
                    }
                    break;
                    case "Restablecer contraseña":{
                        Intent paginarestablecer=new Intent(getApplicationContext(),ActivityRestablecer.class);
                        startActivity(paginarestablecer);
                    }
                    break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void correo(){
        Intent enviar=new Intent(this,ActivityReenviar.class);
        enviar.putExtra("user",user.getText().toString().toLowerCase());
        user.setText("");
        clave.setText("");
        user.setEnabled(true);
        clave.setEnabled(true);
        sesion1.setEnabled(true);
        startActivity(enviar);
    }
    public void revisar(View view){
        String url="http://apk.salasar.xyz/sesion.php?user="+user.getText().toString()+"&clave="+clave.getText().toString();
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void registrar(View view){
        Intent viajarpantalla=new Intent(this,MainActivity.class);
        startActivity(viajarpantalla);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "ERROR FATAL: "+error.toString(), Toast.LENGTH_SHORT).show();
    }
    public void pagina(){
        viajarpagina=new Intent(getApplicationContext(),ActivityPagina.class);
        viajarpagina.putExtra("user",user.getText().toString().toLowerCase());
        SharedPreferences prefe=getSharedPreferences("usuario",Context.MODE_PRIVATE);
        SharedPreferences.Editor O_editor=prefe.edit();
        O_editor.putString("user",user.getText().toString());
        O_editor.commit();
        if(ChekClave.isChecked()==true){
            SharedPreferences prefe1=getSharedPreferences("clave",Context.MODE_PRIVATE);
            SharedPreferences.Editor O_editor1=prefe1.edit();
            O_editor1.putString("pass",clave.getText().toString());
            O_editor1.commit();
        }
        user.setText("");
        clave.setText("");
        user.setEnabled(true);
        clave.setEnabled(true);
        sesion1.setEnabled(true);
        startActivity(viajarpagina);
    }
    @Override
    public void onResponse(JSONObject response) {
        Datos usuario2 = new Datos();
        JSONArray json = response.optJSONArray("usuario");
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject(0);
            usuario2.setUser(jsonObject.optString("user"));
            usuario2.setClave(jsonObject.optString("clave"));
            usuario2.setEstado(jsonObject.optString("estado"));
            usuario1 = usuario2.getUser();
            clave1 = usuario2.getClave();
            estado1 = usuario2.getEstado();
            //Toast.makeText(getApplicationContext(), "|" + clave1 + "|" + usuario1 + "|" + estado1 + "|", Toast.LENGTH_LONG).show();
            user.setEnabled(false);
            clave.setEnabled(false);
            sesion1.setEnabled(false);
            switch (usuario1) {
                case "s": {
                    switch (clave1) {
                        case "s": {
                            switch (estado1) {
                                case "s": {
                                    pagina();
                                }
                                break;
                                case "n": {
                                    correo();
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
                case "n": {
                    user.setEnabled(true);
                    clave.setEnabled(true);
                    sesion1.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Usuario y Contraseña ERRONEOS", Toast.LENGTH_LONG).show();
                }
                break;
                default: {
                    Toast.makeText(getApplicationContext(), "ERROR GRAVE GIGANTE", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR GRAVE", Toast.LENGTH_SHORT).show();
        }
    }
    public void recuperar(View view){
        switch (nesecidad){
            case 1:{
                spinner1.setVisibility(View.VISIBLE);
                nesecidad=2;
            }
            break;
            case 2:{
                spinner1.setVisibility(View.INVISIBLE);
                nesecidad=1;
            }
            break;
            default:
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==event.KEYCODE_BACK) {
            finishAffinity();
        }
        return super.onKeyDown(keyCode,event);
    }
}