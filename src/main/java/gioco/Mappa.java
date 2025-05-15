package gioco;

import model.Stanza;
import model.Oggetto;

import java.util.HashMap;
import java.util.Map;
public class Mappa {
    private Map<String, Stanza> stanze;
    private Stanza stanzaIniziale;

    public Mappa() {
        this.stanze = new HashMap<>();
        costruisciMappa();
    }
    private void costruisciMappa() {

        Stanza sterpaglie = new Stanza("Sterpaglie", "Sei in una fitta foresta che si estende in tutte le direzioni. Vedi delle luci verso nord.");

        Stanza cancelloArrugginito = new Stanza("Cancello arrugginito", "Sei davanti a un cancello arrugginito, " +
                "oltre in cancello riesci a scrutare una strada sterrata che conduce ad un grande edificio. Ai tuoi piedi c'è una spranga di metallo.");

        Stanza stradaSterrata = new Stanza("Strada sterrata" , "Sei su una strada sterrata, a nord la strada conduce ad una villa gotica, " +
                "verso est noti delle orme nel fango conducono ad un vecchio capanno");

        Stanza capannoAttrezzi = new Stanza("Capanno per gli attrezzi" , "Sei davanti ad un vecchio capanno per gli attrezzi in disuso." +
                " La porta del capanno è a terra fradicia. Riesci a scrutare una vecchia lanterna a olio sul bancone.");


//         sterpaglie
        sterpaglie.aggiungiUscita("nord", cancelloArrugginito,false,null);

//        cancello arrugginito
        cancelloArrugginito.aggiungiUscita("sud" , sterpaglie, false, null);
        cancelloArrugginito.aggiungiUscita("nord" , stradaSterrata, true, "spranga");
        Oggetto spranga = new Oggetto("Spranga arrugginita", "Una Spranga di metallo arrugginita", true, true, null);
        cancelloArrugginito.aggiungiOggetto(spranga);

//        strada sterrata
        stradaSterrata.aggiungiUscita("est" , capannoAttrezzi,false,null);
        stradaSterrata.aggiungiUscita("sud", cancelloArrugginito,false,null); // per tornare indietro
        // da fare == stradaSterrata.aggiungiUscita("nord" , ingressoVilla);

//        capanno attrezzi
        capannoAttrezzi.aggiungiUscita("ovest" , stradaSterrata,false,null);
        Oggetto lanterna = new Oggetto("Lanterna", "Vecchia lanterna a olio, scuotendola senti che il serbatoio è pieno a metà.", true, true, null);
        capannoAttrezzi.aggiungiOggetto(lanterna);

        stanzaIniziale = sterpaglie;
    }

    public Stanza getStanzaIniziale() {
        return stanzaIniziale;
    }

    public Stanza getStanza(String nome) {
        return stanze.get(nome);
    }

    public Map<String, Stanza> getTutteLeStanze() {
        return stanze;
    }
}
