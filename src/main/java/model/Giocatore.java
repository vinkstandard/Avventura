package model;

public class Giocatore {
    private String nome;
    private Stanza stanzaAttuale;
    private Inventario inventario;

    public Giocatore(String nome, Stanza stanzaAttuale, Inventario inventario) {
        this.nome = nome;
        this.stanzaAttuale = stanzaAttuale;
        this.inventario = inventario;
    }

    public String getNome() {
        return nome;
    }

    public Stanza getStanzaAttuale() {
        return stanzaAttuale;
    }

    public void setStanzaAttuale(Stanza stanzaAttuale) {
        this.stanzaAttuale = stanzaAttuale;
    }

    public Inventario getInventario() {
        return inventario;
    }
}
