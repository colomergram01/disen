package com.example.planillaempresarialeltrabajador;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
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
public class ActivityRecuperar extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    private EditText correo,codigo;
    private TextView usuario;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue request;
    private String usuarioC;
    private int entrada=0;
    private ImageButton ojo;
    public boolean validar(String dato,int numero){
        String opcion1="[a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion2="[a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion3="[a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion4="[0-9]{6}";
        switch(numero){
            case 1:{
                return dato.matches(opcion1+"|"+opcion2+"|"+opcion3);
            }
            case 2:{
                return dato.matches(opcion4);
            }
            default:{
                return false;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        correo=(EditText) findViewById(R.id.txtcorreo);
        codigo=(EditText) findViewById(R.id.txtcodigo);
        usuario=(TextView) findViewById(R.id.txtusuario);
        request= Volley.newRequestQueue(getApplicationContext());
        ojo=(ImageButton) findViewById(R.id.ojoloco);
    }
    public void generarcodigo(View view){
        if(validar(correo.getText().toString(),1)){
            entrada=1;
            generarc();
        }
        else{
            Toast.makeText(this,"Correo no valido", Toast.LENGTH_SHORT).show();
        }
    }
    public void generarc(){
        String url="http://apk.salasar.xyz/gcodigou.php?correo="+correo.getText().toString();
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void validarcodigo(View view){
        if(validar(correo.getText().toString(),1)){
            if(validar(codigo.getText().toString(),2)){
                entrada=2;
                validarc();
            }
            else {
                Toast.makeText(this, "Codigo no valido", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this,"Correo no valido", Toast.LENGTH_SHORT).show();
        }
    }
    public void validarc(){
        String url="http://apk.salasar.xyz/vcodigou.php?correo="+correo.getText().toString()+"&codigo="+codigo.getText().toString();
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,"Error FATAL", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onResponse(JSONObject response) {
        switch (entrada){
            case 1:{
                Toast.makeText(this,"Codigo enviado al correo", Toast.LENGTH_LONG).show();
                ojo.setVisibility(View.VISIBLE);
            }
            break;
            case 2:{
                Usuario usuario1 = new Usuario();
                JSONArray json = response.optJSONArray("usuario");
                JSONObject jsonObject = null;
                try {
                    jsonObject = json.getJSONObject(0);
                    usuario1.setUser(jsonObject.optString("user"));
                    usuarioC = usuario1.getUser();
                    switch (usuarioC){
                        case "XXXXXXXXXX":{
                            Toast.makeText(this,"Codigo feo", Toast.LENGTH_LONG).show();
                        }
                        break;
                        default:{
                            escribir();
                        }
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "ERROR GRAVE", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                Toast.makeText(this,"Error FATAL", Toast.LENGTH_SHORT).show();
        }
    }
    public void escribir(){
        usuario.setText(usuarioC);
        correo.setText("");
        codigo.setText("");
        ojo.setVisibility(View.INVISIBLE);
    }
    public void retroceder(View view){
        usuario.setText("XXXXXXXXXX");
        correo.setText("");
        codigo.setText("");
        ojo.setVisibility(View.INVISIBLE);
        Intent paginainicial=new Intent(this,ActivityIniciar.class);
        startActivity(paginainicial);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}