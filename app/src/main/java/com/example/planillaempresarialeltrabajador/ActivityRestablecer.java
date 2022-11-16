package com.example.planillaempresarialeltrabajador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ActivityRestablecer extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    private EditText correo, codigo;
    private EditText clave;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue request;
    private String usuarioC;
    private int entrada = 0;
    private ImageButton disco;

    public boolean validar(String dato, int numero) {
        String opcion1 = "[a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion2 = "[a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion3 = "[a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion4 = "[0-9]{10}";
        String opcion5 = "[a-z,0-9,A-Z,%,&,$,#,@,!,?,¡,¿]{8,50}";
        switch (numero) {
            case 1: {
                return dato.matches(opcion1 + "|" + opcion2 + "|" + opcion3);
            }
            case 2: {
                return dato.matches(opcion4);
            }
            case 3: {
                return dato.matches(opcion5);
            }
            default: {
                return false;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer);
        correo = (EditText) findViewById(R.id.txtcorreo1);
        codigo = (EditText) findViewById(R.id.txtcodigo1);
        clave = (EditText) findViewById(R.id.txtclave1);
        request = Volley.newRequestQueue(getApplicationContext());
        disco = (ImageButton) findViewById(R.id.discoloco);
    }

    public void generarcodigo1(View view) {
        if (validar(correo.getText().toString(), 1)) {
            entrada = 1;
            generarc();
        } else {
            Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
        }
    }

    public void generarc() {
        String url = "http://apk.salasar.xyz/gcodigoc.php?correo=" + correo.getText().toString();
        url = url.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    public void validarcodigo1(View view) {
        if (validar(correo.getText().toString(), 1)) {
            if (validar(codigo.getText().toString(), 2)) {
                if (validar(clave.getText().toString(), 3)) {
                    entrada = 2;
                    validarc();
                } else {
                    Toast.makeText(this, "Contraseña no valida", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Codigo no valido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
        }
    }

    public void validarc() {
        String url = "http://apk.salasar.xyz/vcodigoc.php?correo=" + correo.getText().toString() + "&codigo=" + codigo.getText().toString() + "&clave=" + clave.getText().toString();
        url = url.replace(" ", "%20");
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this, "Error FATAL", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        switch (entrada) {
            case 1: {
                Toast.makeText(this, "Codigo enviado al correo", Toast.LENGTH_LONG).show();
                disco.setVisibility(View.VISIBLE);
            }
            break;
            case 2: {
                Usuario usuario1 = new Usuario();
                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject(0);
                    usuario1.setUser(jsonObject.optString("user"));
                    usuarioC = usuario1.getUser();
                    switch (usuarioC) {
                        case "guardado": {
                            Toast.makeText(this, "Contraseña modificada", Toast.LENGTH_LONG).show();
                            clave.setText("");
                            correo.setText("");
                            codigo.setText("");
                            disco.setVisibility(View.INVISIBLE);
                        }
                        break;
                        case "denegado": {
                            Toast.makeText(this, "Codigo incorrecto", Toast.LENGTH_SHORT).show();
                        }
                        default: {
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "ERROR GRAVE", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                Toast.makeText(this, "Error FATAL", Toast.LENGTH_SHORT).show();
        }
    }

    public void izquierda(View view) {
        correo.setText("");
        codigo.setText("");
        disco.setVisibility(View.INVISIBLE);
        Intent paginainicial = new Intent(this, ActivityIniciar.class);
        startActivity(paginainicial);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}