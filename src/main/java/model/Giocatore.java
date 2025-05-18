package model;

public class Giocatore {
    private String nome;
    private Stanza stanzaAttuale;
    private Inventario inventario;
    private int vita;
    private int dannoBase = 5; // a mani nude
    private Arma armaEquipaggiata;

    public Giocatore(String nome, Stanza stanzaAttuale, Inventario inventario) {
        this.nome = nome;
        this.stanzaAttuale = stanzaAttuale;
        this.inventario = inventario;
        this.vita = 100; // hp max
    }

    public void subisciDanno(int danno){
        this.vita -= danno;
        if(this.vita < 0) this.vita = 0;
    }
    public void guadagnaHpGiocatore(int hp){
        this.vita += hp;
    }
    public int getDannoBase(){
        return dannoBase;
    }
    public void equipaggiaArma(Arma arma){
        this.armaEquipaggiata = arma;
    }

    public Arma getArmaEquipaggiata(){
        return armaEquipaggiata;
    }

    public int getVita() {
        return vita;
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
    public String getNome() {
        return nome;
    }
}
