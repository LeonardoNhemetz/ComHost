package com.iurd_com.Amigos;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iurd_com.Menssagens.Menssagens;
import com.iurd_com.R;

import org.w3c.dom.Text;

import java.util.List;

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.BuilderAmigos> {

    private List<AmigosAtributos> atributosList;
    private Context context;

    public AmigosAdapter(List<AmigosAtributos> atributosList, Context context)
    {
        this.atributosList = atributosList;
        this.context = context;

    }

    @Override
    public BuilderAmigos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos,parent,false);
        return new AmigosAdapter.BuilderAmigos( v );
    }

    @Override
    public void onBindViewHolder(BuilderAmigos holder, final int position) {
        holder.imageView.setImageResource( atributosList.get( position ).getFotoDePerfil() );
        holder.nome.setText( atributosList.get( position ).getNome() );
        holder.mensagem.setText( atributosList.get( position ).getUltimaMensagem() );
        holder.hora.setText( atributosList.get( position ).getHora() );

        holder.cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmigosAtributos amigosAtributos = new AmigosAtributos();
                amigosAtributos.setRecipientId( atributosList.get(position).getNome() );
                String nomeDaVez = amigosAtributos.getRecipientId();
                Log.i("COEEEEEEEEEEEEEE",nomeDaVez);


                Intent intent = new Intent( context, Menssagens.class );
                context.startActivity(intent);
            }
        } );




    }

    @Override
    public int getItemCount() {
        return atributosList.size();
    }

    static class BuilderAmigos extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imageView;
        TextView nome,mensagem,hora;

        public BuilderAmigos(View itemView) {
            super( itemView );
            cardView = (CardView) itemView.findViewById( R.id.cardViewAmigos );
            imageView = (ImageView) itemView.findViewById( R.id.FotoDePerfilAmigos );
            nome = (TextView) itemView.findViewById( R.id.NomeDeUsuarioAmigos );
            mensagem = (TextView) itemView.findViewById( R.id.MenssagemAmigos );
            hora = (TextView) itemView.findViewById( R.id.horaAmigos );
        }
    }


}
