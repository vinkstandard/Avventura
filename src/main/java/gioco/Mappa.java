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
        stanzaIniziale = sterpaglie;

        Stanza cancelloArrugginito = new Stanza("Cancello arrugginito", "Sei davanti a un cancello arrugginito, " +
                "oltre in cancello riesci a scrutare una strada sterrata che conduce ad un grande edificio. Ai tuoi piedi c'è una spranga di metallo.");

        Stanza stradaSterrata = new Stanza("Strada sterrata" , "Sei su una strada sterrata, a nord la strada conduce ad una villa gotica, " +
                "verso est noti delle orme nel fango conducono ad un vecchio capanno");

        Stanza capannoAttrezzi = new Stanza("Capanno per gli attrezzi" , "Sei davanti ad un vecchio capanno per gli attrezzi in disuso." +
                " La porta del capanno è a terra fradicia. Riesci a scrutare una vecchia lanterna a olio sul bancone.");


        sterpaglie.aggiungiUscita("nord", cancelloArrugginito);

        cancelloArrugginito.aggiungiUscita("nord" , stradaSterrata);
        Oggetto spranga = new Oggetto("Spranga arrugginita", "Una Spranga di metallo arrugginita", true, true, null);
        cancelloArrugginito.aggiungiOggetto(spranga);

        stradaSterrata.aggiungiUscita("est" , capannoAttrezzi);
        stradaSterrata.aggiungiUscita("sud", cancelloArrugginito); // per tornare indietro
//        stradaSterrata.aggiungiUscita("nord" , ingressoVilla);

        capannoAttrezzi.aggiungiUscita("ovest" , stradaSterrata);
        Oggetto lanterna = new Oggetto("Lanterna", "Vecchia lanterna a olio, scuotendola senti che il serbatoio è pieno a metà.", true, true, null);
        capannoAttrezzi.aggiungiOggetto(lanterna);

//        Oggetto piedeDiPorco = new Oggetto("Piede di porco", "Un piede di porco, un po' vecchio ma sembra resistente.", true, true, null);
//        capannoAttrezzi.aggiungiOggetto(piedeDiPorco);
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
