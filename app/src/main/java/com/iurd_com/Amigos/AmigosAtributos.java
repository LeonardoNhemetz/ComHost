package com.iurd_com.Amigos;

public class AmigosAtributos {
    private int fotoDePerfil;
    private String nome, ultimaMensagem, hora;
    private static String caller,recipientId;

    public AmigosAtributos() {

    }

    public int getFotoDePerfil() {
        return fotoDePerfil;
    }

    public void setFotoDePerfil(int fotoDePerfil) {
        this.fotoDePerfil = fotoDePerfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public static String getCaller() {
        return caller;
    }

    public static void setCaller(String caller) {
        AmigosAtributos.caller = caller;
    }

    public static String getRecipientId() {
        return recipientId;
    }

    public static void setRecipientId(String recipientId) {
        AmigosAtributos.recipientId = recipientId;
    }
}
