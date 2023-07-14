package com.iurd_com.Amigos;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iurd_com.Login;
import com.iurd_com.Menssagens.FirebaseMenssagem;
import com.iurd_com.Menssagens.Menssagens;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityAmigos extends AppCompatActivity {

    private List<AmigosAtributos> atributosList;
    private AmigosAdapter adapter;
    private RecyclerView rv;
    private RequestQueue mRequest;
    private VolleyRP volley;
    private static final String URL = "http://iurdcom.ddns.net:1024/iurdcom/amigos_getall.php";


    AmigosAtributos amigosAtributos = new AmigosAtributos();
    public static int verificar = 1;

    public static SinchClient sinchClient;
    private static final String APP_KEY = "7cf24359-6476-4b6d-9f69-983ee47bf22d";
    private static final String APP_SECRET = "izUsoe0Rd0WCSdCqVyDNqQ==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_amigos );
        setTitle( "Contatos" );

        atributosList = new ArrayList<>(  );

        volley = VolleyRP.getInstance( this );
        mRequest = volley.getRequestQueue();

        rv = (RecyclerView) findViewById( R.id.amigosReciclerView );
        LinearLayoutManager lm = new LinearLayoutManager( this );
        rv.setLayoutManager( lm );

        adapter = new AmigosAdapter( atributosList, this );
        rv.setAdapter( adapter );

        String callerId = amigosAtributos.getCaller();

        sinchClient = Sinch.getSinchClientBuilder()
                .context( this )
                .userId( callerId )
                .applicationKey( APP_KEY )
                .applicationSecret( APP_SECRET )
                .environmentHost( ENVIRONMENT )
                .build();

        sinchClient.setSupportMessaging( true );
        sinchClient.setSupportActiveConnectionInBackground( true );
        sinchClient.setSupportManagedPush( true );
        sinchClient.startListeningOnActiveConnection();

        final MessageClient messageClient = sinchClient.getMessageClient();
        messageClient.addMessageClientListener( new SinchClientListener() );
        new FirebaseMenssagem();
        sinchClient.start();



        SolicitarJSON();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_activity_amigos,menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id==R.id.EncerrarSecaoMenu)
        {
            Intent i = new Intent( ActivityAmigos.this, Login.class );
            startActivity( i );
            finish();
        }

        return super.onOptionsItemSelected( item );
    }

    public void agregarAmigo(int fotoDeperfil, String nome, String ultimaMensagem, String hora)
    {
        AmigosAtributos amigosAtributos = new AmigosAtributos();
        amigosAtributos.setFotoDePerfil( fotoDeperfil );
        amigosAtributos.setNome( nome );
        amigosAtributos.setHora( hora );
        amigosAtributos.setUltimaMensagem( ultimaMensagem );

        atributosList.add( amigosAtributos );
        adapter.notifyDataSetChanged();
    }

    public void SolicitarJSON()
    {
        JsonObjectRequest solicitar = new JsonObjectRequest( URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    String TodosOsDados = response.getString( "resultado" );
                    JSONArray jsonArray = new JSONArray( TodosOsDados );
                    String caller = amigosAtributos.getCaller();
                    Log.i( "COEEEEEEEEEE",caller );

                    for(int i=0;i<jsonArray.length();i++)
                    {
                         JSONObject js = jsonArray.getJSONObject( i );
                         String login = js.getString( "login" );
                         if(!login.equals( caller ))
                         {
                             agregarAmigo( R.drawable.ic_account_circle,js.getString( "login" ),"Mensagem "+i,"00:00" );

                         }


                    }
                } catch (JSONException e) {
                    Toast.makeText( ActivityAmigos.this, "Erro no JSON", Toast.LENGTH_SHORT ).show();
                }
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText( ActivityAmigos.this, "erro", Toast.LENGTH_SHORT ).show();

            }
        } );
        VolleyRP.addToQueue( solicitar,mRequest,this,volley );
    }

    public class SinchClientListener implements MessageClientListener
    {

        @Override
        public void onIncomingMessage(MessageClient messageClient, Message message) {
            String mensagem = message.getTextBody();
            String sender = message.getSenderId().toString();
            showNotification( mensagem,sender );




        }

        @Override
        public void onMessageSent(MessageClient messageClient, Message message, String s) {

        }

        @Override
        public void onMessageFailed(MessageClient messageClient, Message message, MessageFailureInfo messageFailureInfo) {

        }

        @Override
        public void onMessageDelivered(MessageClient messageClient, MessageDeliveryInfo messageDeliveryInfo) {

        }

        @Override
        public void onShouldSendPushData(MessageClient messageClient, Message message, List<PushPair> list) {

        }
    }

    private void showNotification(final String menssagem, String sender) {
        Intent i = new Intent( ActivityAmigos.this, Login.class );
        PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT );

        Uri soundNotification = RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION );

        final NotificationCompat.Builder builder = new NotificationCompat.Builder( this );
        builder.setAutoCancel( true );
        builder.setContentText( menssagem );
        builder.setSound( soundNotification );
        builder.setContentTitle( sender );
        builder.setTicker( "Ticker" );
        builder.setVibrate( new long[]{0, 500, 120, 500, 120, 500, 120, 500, 120, 500, 120, 500} );
        builder.setSmallIcon( R.drawable.ic_launcher_background );
        builder.setContentIntent( pendingIntent );


        final NotificationManager notificationManager = (NotificationManager) getSystemService( Context.NOTIFICATION_SERVICE );

        final Random random = new Random();



        final long time = 6000; // a cada X ms
        final Timer timer = new Timer();
        TimerTask tarefa = new TimerTask() {
            public void run() {

                if (verificar == 1)
                {
                    try {
                        notificationManager.notify( random.nextInt(), builder.build() );
                        Log.i( "VERFICAROBAGUILOKO","FOI LEK" );
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i( "VERFICAROBAGUILOKO", String.valueOf( e ) );
                    }
                }
                else if (verificar == 2)
                {
                    timer.cancel();
                    verificar = 1;
                }
            }


        };
        timer.scheduleAtFixedRate(tarefa, time, time);

        Log.i( "VERFICAROBAGUILOKO","Nao FOI LEK" );

    }





    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
