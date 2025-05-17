package model;

public class Oggetto {

    private String nome;
    private String descrizione;
    private boolean raccoglibile;
    private boolean usabile;
    private String effetto;  // cura, chiave, arma

    public Oggetto(String nome, String descrizione, boolean raccoglibile, boolean usabile, String effetto) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.raccoglibile = raccoglibile;
        this.usabile = usabile;
        this.effetto = effetto;
    }
    // constructor per l'arma
    public Oggetto(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
    }



    public boolean isRaccoglibile() {
        return raccoglibile;
    }

    public boolean isUsabile() {
        return usabile;
    }

    public String getEffetto() {
        return effetto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getNome() {
        return nome;
    }

}
