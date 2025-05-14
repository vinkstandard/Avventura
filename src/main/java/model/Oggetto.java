package model;

public class Oggetto {

    private String nome;
    private String descrizione;
    private boolean raccoglibile;
    private boolean usabile;
    private String effetto;  // cura, chiave, arma

    public String getDescrizione() {
        return descrizione;
    }

    public String getNome() {
        return nome;
    }
}
