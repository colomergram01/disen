package com.example.planillaempresarialeltrabajador;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
public class MainActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener {
    private EditText user, pass, correo,nombre,dni;
    private RequestQueue request;
    private JsonObjectRequest jsonObjectRequest;
    private int turno=1,secuencia=2;
    private ImageButton guardar;
    private String usuario1,correo1;
    public boolean revisar(String dato,int numero){
        String opcion1="[a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion2="[a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion3="[a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[.][a-z,0-9]{3,50}[@][a-z,0-9]{3,50}[.][a-z]{2,10}";
        String opcion4="[a-z,0-9,_,-]{4,20}";
        String opcion5="[a-z,0-9,A-Z,%,&,$,#,@,!,?,¡,¿]{8,50}";
        String opcion6="[A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}";
        String opcion7="[A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}";
        String opcion8="[A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}";
        String opcion9="[A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}";
        String opcion10="[A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}[ ][A-Z][a-z]{2,20}";
        String opcion11="[0-9]{13,20}";
        switch(numero){
            case 1:{
                return dato.matches(opcion4);
            }
            case 2:{
                return dato.matches(opcion5);
            }
            case 3:{
                return dato.matches(opcion1+"|"+opcion2+"|"+opcion3);
            }
            case 4:{
                return dato.matches(opcion6+"|"+opcion7+"|"+opcion8+"|"+opcion9+"|"+opcion10);
            }
            case 5:{
                return dato.matches(opcion11);
            }
            default:{
                return false;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText) findViewById(R.id.usuario);
        pass=(EditText) findViewById(R.id.clave);
        correo=(EditText) findViewById(R.id.correo);
        nombre=(EditText) findViewById(R.id.nombre);
        dni=(EditText) findViewById(R.id.dni);
        request=Volley.newRequestQueue(getApplicationContext());
        guardar=(ImageButton) findViewById(R.id.button1);
    }
    public void insertar2(){
        String url="http://apk.salasar.xyz/correo.php?correo="+correo.getText().toString()+"&user="+user.getText().toString();
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        guardar.setVisibility(View.VISIBLE);
        Toast.makeText(getApplicationContext(), "Error Fatal: " + error.toString(), Toast.LENGTH_SHORT).show();
        secuencia=1;
    }
    @Override
    public void onResponse(JSONObject response) {
        if (secuencia == 2) {
            Usuario usuario = new Usuario();
            JSONArray json = response.optJSONArray("usuario");
            JSONObject jsonObject = null;
            try {
                jsonObject = json.getJSONObject(0);
                usuario.setUser(jsonObject.optString("user"));
                usuario.setCorreo(jsonObject.optString("correo"));
                usuario1 = usuario.getUser();
                correo1 = usuario.getCorreo();
                guardar.setVisibility(View.INVISIBLE);
                switch (correo1){
                    case "s":{
                        guardar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Correo ya registrado",Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case "n":{
                        switch (usuario1){
                            case "s":{
                                guardar.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Usuario ya registrado",Toast.LENGTH_SHORT).show();
                            }
                            break;
                            case "n":{
                                validar();
                            }
                            break;
                            default:{
                                Toast.makeText(getApplicationContext(), "ERROR GRAVE CHIQUITO", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                    default:{
                        Toast.makeText(getApplicationContext(), "ERROR GRAVE GIGANTE", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "ERROR GRAVE", Toast.LENGTH_SHORT).show();
            }
        }
        if(secuencia==1){
            user.setEnabled(false);
            pass.setEnabled(false);
            correo.setEnabled(false);
            nombre.setEnabled(false);
            dni.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Correo de verificacion enviado"/*"Se ha guardado"*/, Toast.LENGTH_LONG).show();
            secuencia=0;
            insertar2();
            guardar.setVisibility(View.VISIBLE);
            user.setText("");
            pass.setText("");
            correo.setText("");
            nombre.setText("");
            dni.setText("");
            user.setEnabled(true);
            pass.setEnabled(true);
            correo.setEnabled(true);
            nombre.setEnabled(true);
            dni.setEnabled(true);
            secuencia=3;
        }
    }
    public void insertar(){
        String url="http://apk.salasar.xyz/insertar.php?usuario="+user.getText().toString()+"&clave="+pass.getText().toString()+"&direccion="+correo.getText().toString()+"&nombre="+nombre.getText().toString()+"&dni="+dni.getText().toString();
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public void validar(){
        if(revisar(user.getText().toString(),turno)) {
            turno = 2;
            if (revisar(pass.getText().toString(), turno)) {
                turno = 3;
                if (revisar(correo.getText().toString(), turno)) {
                    turno = 4;
                    if (revisar(nombre.getText().toString(), turno)) {
                        turno = 5;
                        if (revisar(dni.getText().toString(), turno)) {
                            turno = 1;
                            secuencia=1;
                            insertar();
                        }
                        else{
                            turno=1;
                            guardar.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"DNI no valido",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        turno=1;
                        guardar.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),"Nombre no valido [Nombre completo y respectivas mayusculas]",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    turno=1;
                    guardar.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Correo no valido [Sin mayusculas]",Toast.LENGTH_LONG).show();
                }
            }
            else{
                turno=1;
                guardar.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Contraseña debil [8 caracteres minimo y sin espacios]",Toast.LENGTH_LONG).show();
            }
        }
        else{
            turno=1;
            guardar.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Usuario no valido [Sin caracteres especiales ni mayusculas]",Toast.LENGTH_LONG).show();
        }
    }
    public void atras(View view){
        Intent viajarpantalla=new Intent(this,ActivityIniciar.class);
        startActivity(viajarpantalla);
    }
    public void consultar(View view){
        String url="http://apk.salasar.xyz/consultar.php?user="+user.getText().toString()+"&correo="+correo.getText().toString();
        url=url.replace(" ","%20");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        return false;
    }
}