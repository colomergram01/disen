package com.example.planillaempresarialeltrabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ActivityReenviar extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{
    private String user;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reenviar);
        request= Volley.newRequestQueue(getApplicationContext());
        user=getIntent().getStringExtra("user");
        datos();
    }
    public void datos(){
        String url="http://apk.salasar.xyz/reenviarcorreo.php?user="+user;
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
        Toast.makeText(getApplicationContext(), "Correo de verificacion enviado", Toast.LENGTH_LONG).show();
    }
    public void atras(View view){
        Intent volver=new Intent(this, ActivityIniciar.class);
        startActivity(volver);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}