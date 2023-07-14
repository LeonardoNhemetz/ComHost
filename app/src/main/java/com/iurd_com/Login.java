package com.iurd_com;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iurd_com.Amigos.ActivityAmigos;
import com.iurd_com.Amigos.AmigosAtributos;
import com.iurd_com.AtividadeDeUsuarios.Activity_Usuarios;
import com.iurd_com.Menssagens.Menssagens;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    private EditText txtLogin,txtSenha;
    private Button btnEntrar;
    private static final String url = "http://iurdcom.ddns.net:1024/iurdcom/login_getid.php";
    private RequestQueue mRequest;
    private VolleyRP volley;
    private String USER = "";
    private String PASSWORD = "";
    private RadioButton RBSecao;
    private boolean isActivateRadioButton;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        volley = VolleyRP.getInstance( this );
        mRequest = volley.getRequestQueue();

        txtLogin = (EditText) findViewById( R.id.txtLogin );
        txtSenha = (EditText) findViewById( R.id.txtSenha );
        btnEntrar = (Button) findViewById( R.id.btnEntrar );

        RBSecao = (RadioButton) findViewById( R.id.RBSecao );
        isActivateRadioButton = RBSecao.isChecked(); //DESATIVADO

        if (ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(Login.this,
                    new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        SharedPreferences sharedPreferences =  getSharedPreferences("LoginCOM",MODE_PRIVATE);
        String Login = sharedPreferences.getString("Login","");
        String Senha = sharedPreferences.getString("Senha","");

        txtLogin.setText(Login);
        txtSenha.setText(Senha);

        RBSecao.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isActivateRadioButton)
                {
                    RBSecao.setChecked( false );
                }
                isActivateRadioButton = RBSecao.isChecked();
            }
        } );

        btnEntrar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                USER = txtLogin.getText().toString().toLowerCase();
                PASSWORD = txtSenha.getText().toString().toLowerCase();

                SharedPreferences sharedPreferences =  getSharedPreferences("LoginCOM",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Login",USER);
                editor.putString("Senha",PASSWORD);
                editor.commit();

                pd = new ProgressDialog(Login.this);
                pd.setTitle("Verificando Login");
                pd.setMessage("Espere...");

                pd.show();

                Logar();
            }
        } );

    }

    private void Logar()
    {
        HashMap<String,String> hashMapToken = new HashMap<>(  );
        hashMapToken.put( "login",USER);
        hashMapToken.put( "senha",PASSWORD);

        JsonObjectRequest solicitar = new JsonObjectRequest( Request.Method.POST, url, new JSONObject( hashMapToken ), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject dados) {

                VerificarPassword( dados );
                Log.i("COEEEEEEEEEEEEEEE", String.valueOf( dados ) );

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Login.this, "Ocorreu algum erro",Toast.LENGTH_LONG).show();


            }
        });
        VolleyRP.addToQueue(solicitar,mRequest,this,volley);

    }

    public void VerificarPassword(JSONObject dados){
        try {
            String estado = dados.getString("resultado");
            if(estado.equals("CC")){
                JSONObject Jsondados = new JSONObject(dados.getString("dados"));
                String login = Jsondados.getString("login");
                String senha = Jsondados.getString("senha");

                if(login.equals(USER) && senha.equals(PASSWORD)){
                    Intent intent = new Intent(getApplicationContext(), ActivityAmigos.class);
                    AmigosAtributos amigosAtributos = new AmigosAtributos();
                    amigosAtributos.setCaller( USER );
                    pd.dismiss();
                    startActivity(intent);
                    finish();
                }
                else Toast.makeText(Login.this,"a senha está incorreta",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,estado,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {

        }
    }
}
