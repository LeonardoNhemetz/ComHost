package com.iurd_com.Menssagens;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iurd_com.Amigos.ActivityAmigos;
import com.iurd_com.Amigos.AmigosAtributos;
import com.iurd_com.Login;
import com.iurd_com.R;
import com.iurd_com.VolleyRP;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Menssagens extends AppCompatActivity {


    private RecyclerView rv;

    private List<MenssagemDeTexto> menssagemDetexto = new ArrayList<>();
    private MenssagensAdapter adapter;

    private Button btnEnviarMenssagem;
    private EditText txtMenssagem;

    Builder builder;

    AmigosAtributos amigosAtributos = new AmigosAtributos();

    private int textLines = 1;


    private RequestQueue mRequest;
    private VolleyRP volley;

    String recipientID = amigosAtributos.getRecipientId();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_menssagens );
        menssagemDetexto = new ArrayList<>();


        volley = VolleyRP.getInstance( this );
        mRequest = volley.getRequestQueue();


        builder = new Builder( this );


        btnEnviarMenssagem = (Button) findViewById( R.id.btnEnviarMenssagem );
        txtMenssagem = (EditText) findViewById( R.id.txtMensagem );
        rv = (RecyclerView) findViewById( R.id.rvMenssagens );
        LinearLayoutManager lm = new LinearLayoutManager( this );
        lm.setStackFromEnd( true );
        rv.setLayoutManager( lm );


        adapter = new MenssagensAdapter( menssagemDetexto, this );
        rv.setAdapter( adapter );

        txtMenssagem.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtMenssagem.getLayout().getLineCount() != textLines) {
                    setScrollBarChat();
                    textLines = txtMenssagem.getLayout().getLineCount();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        } );


        btnEnviarMenssagem.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menssagem = txtMenssagem.getText().toString();

                if (!menssagem.isEmpty()) {

                    CriarMenssagem( menssagem, 1, "00:00" );
                    GuardarMensagem( menssagem );
                    ActivityAmigos activityAmigos = new ActivityAmigos();
                    String recipientID = amigosAtributos.getRecipientId();
                    WritableMessage message = new WritableMessage(
                            recipientID, menssagem);
                    MessageClient messageClient = activityAmigos.sinchClient.getMessageClient();
                    messageClient.send( message );
                }
                else
                {
                    Toast.makeText( Menssagens.this, "Você não digitou nada", Toast.LENGTH_SHORT ).show();
                }

            }
        } );



        setScrollBarChat();

        downloader(recipientID);

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_activity_amigos,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.EncerrarSecaoMenu)
        {
            ActivityAmigos activityAmigos = new ActivityAmigos();
            activityAmigos.verificar = 2;

        }

        return super.onOptionsItemSelected( item );
    }




    public void setScrollBarChat() {
        rv.scrollToPosition( adapter.getItemCount() - 1 );
    }


    public void GuardarMensagem(String mensagem)
    {
        String caller = amigosAtributos.getCaller();
        String recipientId = amigosAtributos.getRecipientId();

        String url = "http://iurdcom.ddns.net:1024/iurdcom/enviar_mensagens.php";

        HashMap<String,String> hash = new HashMap<>(  );
        hash.put( "emissor",caller);
        hash.put( "receptor",recipientId);
        hash.put( "menssagem",mensagem);


        JsonObjectRequest solicitar = new JsonObjectRequest( Request.Method.POST, url, new JSONObject( hash ), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject dados) {



            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Menssagens.this, "Ocorreu algum erro",Toast.LENGTH_LONG).show();
                Log.i( "TOTOTOTOOTO", String.valueOf( error ) );


            }
        });
        VolleyRP.addToQueue(solicitar,mRequest,this,volley);

    }


    public void CriarMenssagem(String corpo, int tipoDeMenssagem, String hora) {
        MenssagemDeTexto menssagemDeTextoAuxiliar = new MenssagemDeTexto();
        menssagemDeTextoAuxiliar.setId( "0" );
        menssagemDeTextoAuxiliar.setMenssagem( corpo );
        menssagemDeTextoAuxiliar.setTipoMenssagem( tipoDeMenssagem );
        menssagemDeTextoAuxiliar.setHoraDaMenssagem( hora );
        menssagemDetexto.add( menssagemDeTextoAuxiliar );
        adapter.notifyDataSetChanged();
        txtMenssagem.setText( "" );
        setScrollBarChat();

    }

    public void downloader(String recipientID)
    {
        String caller = amigosAtributos.getCaller();


        HashMap<String,String> hash = new HashMap<>(  );
        hash.put( "table","Menssagens_"+caller);
        hash.put( "recipientId",recipientID);
        hash.put( "caller",caller);

        String url = "http://iurdcom.ddns.net:1024/iurdcom/DownloadMensagens.php";

        JsonObjectRequest solicitar = new JsonObjectRequest( Request.Method.POST, url, new JSONObject( hash ), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try
                {
                    String TodosOsDados = response.getString( "resultado" );
                    JSONArray jsonArray = new JSONArray( TodosOsDados );
                    Log.i( "AISIMEINMEPARCA", TodosOsDados );

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject js = jsonArray.getJSONObject( i );
                        String mensagem,horaDaMenssagem;
                        int tipo_menssagem;
                        mensagem = js.getString( "menssagem" );
                        tipo_menssagem = Integer.parseInt( js.getString( "tipo_menssagem" ) );
                        horaDaMenssagem = js.getString( "horaDaMenssagem" );

                        CriarMenssagem( mensagem,tipo_menssagem,horaDaMenssagem );

                    }

                }
                catch (JSONException e)
                {

                }
                setScrollBarChat();

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i( "ERRRRRRRROU", String.valueOf( error ) );

            }
        });
        VolleyRP.addToQueue(solicitar,mRequest,this,volley);
    }



}


