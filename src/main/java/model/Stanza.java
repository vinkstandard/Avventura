package model;
import java.util.*;
public class Stanza {

    private String nome;
    private String descrizione;
    private Map<String, Stanza> uscite;
    private List<Oggetto> oggettiPresenti;

    public Stanza(String nome, String descrizione, Map<String, Stanza> uscite, List<Oggetto> oggettiPresenti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.uscite = uscite;
        this.oggettiPresenti = oggettiPresenti;
    }

    public void aggiungiUscita(String direzioneUscita, Stanza stanza){
        uscite.put(direzioneUscita, stanza);
    }
    public Stanza getUscita(String direzioneUscita){
        return uscite.get(direzioneUscita);
    }
    public void aggiungiOggetto(Oggetto oggetto){
        oggettiPresenti.add(oggetto);
    }

    public List<Oggetto> getOggettiPresenti(){
        return oggettiPresenti;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }
    public void stampaDescrizioneStanza(){
        System.out.println(descrizione);
    }

    public void stampaUscite(){
        System.out.println("Uscite disponibili:");
        for(String direzione : uscite.keySet()){
            System.out.println("- " + direzione);
        }
    }
    // TODO: parametro nemiciStanza, eventiAutomatici, condizioniAccesso
}
