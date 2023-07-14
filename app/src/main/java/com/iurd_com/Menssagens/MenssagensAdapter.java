package com.iurd_com.Menssagens;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iurd_com.R;

import java.util.List;

public class MenssagensAdapter extends RecyclerView.Adapter<MenssagensAdapter.MenssagensViewHolder> {

    private List<MenssagemDeTexto> menssagemDeTextos;
    private Context context;

    public MenssagensAdapter(List<MenssagemDeTexto> menssagemDeTextos, Context context) {
        this.menssagemDeTextos = menssagemDeTextos;
        this.context = context;
    }

    @NonNull
    @Override
    public MenssagensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() ).inflate( R.layout.card_view_menssagens,parent,false );

        return new MenssagensViewHolder( v );
    }

    @Override
    public void onBindViewHolder(@NonNull MenssagensViewHolder holder, int position) {

        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams) holder.menssagemBG.getLayoutParams();
        LinearLayout.LayoutParams llMenssagem = (LinearLayout.LayoutParams) holder.TvMenssagem.getLayoutParams();
        LinearLayout.LayoutParams llHora = (LinearLayout.LayoutParams) holder.TvHora.getLayoutParams();


        if(menssagemDeTextos.get( position ).getTipoMenssagem()==1)
        {
            //EMISSOR
            holder.menssagemBG.setBackgroundResource( R.drawable.in_message_bg );
            rl.addRule( RelativeLayout.ALIGN_PARENT_LEFT,0);
            rl.addRule( RelativeLayout.ALIGN_PARENT_RIGHT );
            llMenssagem.gravity = Gravity.RIGHT;
            llHora.gravity = Gravity.RIGHT;
            fl.gravity = Gravity.RIGHT;
            holder.TvMenssagem.setGravity( Gravity.RIGHT );


        }
        else if (menssagemDeTextos.get( position ).getTipoMenssagem()==2)
        {
            //RECEPTOR
            holder.menssagemBG.setBackgroundResource( R.drawable.out_message_bg );
            rl.addRule( RelativeLayout.ALIGN_PARENT_RIGHT,0);
            rl.addRule( RelativeLayout.ALIGN_PARENT_LEFT );
            llMenssagem.gravity = Gravity.LEFT;
            llHora.gravity = Gravity.LEFT;
            fl.gravity = Gravity.LEFT;
            holder.TvMenssagem.setGravity( Gravity.LEFT);
        }

        holder.cardView.setLayoutParams( rl );
        holder.menssagemBG.setLayoutParams( fl );
        holder.TvHora.setLayoutParams( llHora );
        holder.TvMenssagem.setLayoutParams( llMenssagem );



        holder.TvMenssagem.setText(menssagemDeTextos.get( position ).getMenssagem() );
        holder.TvHora.setText(menssagemDeTextos.get( position ).getHoraDaMenssagem() );

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) holder.cardView.getBackground().setAlpha( 0 );
        else holder.cardView.setBackgroundColor( ContextCompat.getColor(context ,android.R.color.transparent ) );



    }

    @Override
    public int getItemCount() {
        return menssagemDeTextos.size();
    }

    static class MenssagensViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        LinearLayout menssagemBG;
        TextView TvMenssagem,TvHora;

        MenssagensViewHolder(View itemView)
        {
            super(itemView);
            cardView = (CardView) itemView.findViewById( R.id.cvMenssagem );
            menssagemBG = (LinearLayout) itemView.findViewById( R.id.menssagemBG );
            TvMenssagem = (TextView) itemView.findViewById( R.id.msTexto );
            TvHora = (TextView)itemView.findViewById( R.id.msHora );



        }
    }
}
