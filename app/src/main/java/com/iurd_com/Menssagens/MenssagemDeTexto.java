package com.iurd_com.Menssagens;

public class MenssagemDeTexto {
    private String id,menssagem,HoraDaMenssagem;
    private int tipoMenssagem;
    private String mensagemDownload;

    public MenssagemDeTexto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenssagem() {
        return menssagem;
    }

    public void setMenssagem(String menssagem) {
        this.menssagem = menssagem;
    }

    public String getHoraDaMenssagem() {
        return HoraDaMenssagem;
    }

    public void setHoraDaMenssagem(String horaDaMenssagem) {
        HoraDaMenssagem = horaDaMenssagem;
    }

    public int getTipoMenssagem() {
        return tipoMenssagem;
    }

    public void setTipoMenssagem(int tipoMenssagem) {
        this.tipoMenssagem = tipoMenssagem;
    }

}

