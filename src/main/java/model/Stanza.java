package model;
import java.util.*;
public class Stanza {

    private String nome;
    private String descrizione;
    private Map<String, Stanza> uscite;
    private Map<String, Boolean> usciteSbloccate; // direzione + sbloccata o no
    private Map<String, String> condizioniSblocco;
    private List<Oggetto> oggettiPresenti;

    // costruttore semplificato (per semplificare la creazione di stanze nel main)
    public Stanza(String nome, String descrizione) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.uscite = new HashMap<>();
        this.oggettiPresenti = new ArrayList<>();
        this.usciteSbloccate = new HashMap<>();
        this.condizioniSblocco = new HashMap<>();
    }

    public void aggiungiUscita(String direzione, Stanza stanzaDestinazione, boolean bloccata, String oggettoPerSbloccare) {
        uscite.put(direzione, stanzaDestinazione);
        usciteSbloccate.put(direzione, !bloccata);
        if (bloccata && oggettoPerSbloccare != null) {
            condizioniSblocco.put(direzione, oggettoPerSbloccare.toLowerCase());
        }
    }
    public Stanza getUscita(String direzioneUscita){
        if(usciteSbloccate.getOrDefault(direzioneUscita,true)){
            return uscite.get(direzioneUscita);
        }else{
            return null; // se l'uscita è presente, ma ancora bloccata
        }
    }
    public boolean sbloccaUscita(String direzione, String nomeOggetto) {
        if (condizioniSblocco.containsKey(direzione) && condizioniSblocco.get(direzione).equalsIgnoreCase(nomeOggetto)) {
            usciteSbloccate.put(direzione, true);
            return true;
        }
        return false;
    }
    public boolean esisteUscita(String direzione){
        return uscite.containsKey(direzione);
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

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
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

    public Map<String, Stanza> getUscite() {
        return uscite;
    }

    public Map<String, Boolean> getUsciteSbloccate() {
        return usciteSbloccate;
    }

    public Map<String, String> getCondizioniSblocco() {
        return condizioniSblocco;
    }


    // TODO: parametro nemiciStanza, eventiAutomatici, condizioniAccesso
}
