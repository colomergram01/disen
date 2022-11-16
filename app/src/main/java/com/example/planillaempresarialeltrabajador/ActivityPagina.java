package com.example.planillaempresarialeltrabajador;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.planillaempresarialeltrabajador.entidades.Usuario;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class ActivityPagina extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    private TextView nombre,dinero;
    private String user,nombreC,dineroC;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina);
        nombre=(TextView) findViewById(R.id.nombreP);
        dinero=(TextView)findViewById(R.id.dineroP);
        request= Volley.newRequestQueue(getApplicationContext());
        user=getIntent().getStringExtra("user");
        datos();
    }
    public void datos(){
        String url="http://apk.salasar.xyz/datos.php?user="+user;
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "ERROR FATAL", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {
        Usuario usuario = new Usuario();
        JSONArray json = response.optJSONArray("usuario");
        JSONObject jsonObject = null;
        try {
            jsonObject = json.getJSONObject(0);
            usuario.setUser(jsonObject.optString("nombre"));
            usuario.setCorreo(jsonObject.optString("dinero"));
            nombreC = usuario.getUser();
            dineroC= usuario.getCorreo();
            escribir();
        }
        catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ERROR GRAVE", Toast.LENGTH_SHORT).show();
        }
    }
    public void escribir() {
        nombre.setText(nombreC);
        dinero.setText(dineroC);
    }
    public void atras(View view){
        nombreC="";
        dineroC="";
        Intent retroceder=new Intent(this,ActivityIniciar.class);
        startActivity(retroceder);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}