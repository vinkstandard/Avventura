package gioco;

import model.Nemico;
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

        // queste sono tutte le mappe, per ora le scrivo qui, ma ho letto che si possono importare tramite un .xml

        Stanza sterpaglie = new Stanza("Sterpaglie", "Sei in una fitta foresta che si estende in tutte le direzioni. Vedi delle luci verso nord.");

        Stanza cancelloArrugginito = new Stanza("Cancello arrugginito", "Sei davanti a un cancello arrugginito, " +
                "oltre in cancello riesci a scrutare una strada sterrata che conduce ad un grande edificio. Ai tuoi piedi c'è una spranga di metallo.");

        Stanza stradaSterrata = new Stanza("Strada sterrata" , "Sei su una strada sterrata, a nord la strada conduce ad una villa gotica, " +
                "verso est noti delle orme nel fango conducono ad un vecchio capanno");

        Stanza capannoAttrezzi = new Stanza("Capanno per gli attrezzi" , "Sei davanti ad un vecchio capanno per gli attrezzi in disuso." +
                " La porta del capanno è a terra fradicia. Riesci a scrutare una vecchia lanterna a olio sul bancone.");

        Stanza fronteVilla = new Stanza("Ingresso nord-Villa", "Sei davanti all'ingresso principale della villa, la porta è in legno robusto e non ha una serratura. " +
                "Noti una cavità circolare al centro della porta. Senti dei passi pesanti provenire dall'interno.");

        Stanza ovestVilla = new Stanza("Viottolo a ovest", "Stai percorrendo un viottolo di pietra che sembra circumnavigare tutta la villa. " +
                "Noti una finestra aperta verso nord, con delle enormi tende che rendono impossibile capire cosa c'è dietro.");

        Stanza cucinaVilla = new Stanza("Cucina", "Ti trovi nella cucina della villa, spezie e condimenti sono appesi lungo le pareti." +
                " Senti il cigolare del legno verso est, e senti un'odore nauseabondo provenire da nord. Sul tagliere c'è un coltello.");

        Stanza atrioVilla = new Stanza("Atrio villa", "Sei nell'atrio della villa, davanti a te, di spalle, c'è una creatura che ansima guardando la porta di ingresso." +
                " Non sembra avere buone intenzioni.");

        Stanza salaDaPranzoVilla = new Stanza("Sala da pranzo", "Una sala da pranzo in stile coloniale. La tavola è apparecchiata, e i piatti sono pieni vermi." +
                " Emana un pessimo odore.");


//         sterpaglie, questa sarà la stanza iniziale, la cambierò di tanto in tanto per debuggare, facendo spawnare il player dove voglio
        sterpaglie.aggiungiUscita("nord", cancelloArrugginito,false,null);

//        cancello arrugginito
        cancelloArrugginito.aggiungiUscita("sud" , sterpaglie, false, null);
        cancelloArrugginito.aggiungiUscita("nord" , stradaSterrata, true, "spranga");
        Oggetto spranga = new Oggetto("Spranga arrugginita", "Una Spranga di metallo arrugginita", true, true, null);
        cancelloArrugginito.aggiungiOggetto(spranga);

//        strada sterrata
        stradaSterrata.aggiungiUscita("est" , capannoAttrezzi,false,null);
        stradaSterrata.aggiungiUscita("sud", cancelloArrugginito,false,null);
        stradaSterrata.aggiungiUscita("nord", fronteVilla, false,null);
        // da fare == stradaSterrata.aggiungiUscita("nord" , ingressoVilla);

//        capanno attrezzi
        capannoAttrezzi.aggiungiUscita("ovest" , stradaSterrata,false,null);
        Oggetto lanterna = new Oggetto("Lanterna", "Vecchia lanterna a olio, scuotendola senti che il serbatoio è pieno a metà.", true, true, null);
        capannoAttrezzi.aggiungiOggetto(lanterna);

//        fronte villa
        Oggetto sfera = new Oggetto("Sfera", "Una sfera in vetro decorato.", true, true, null);
        fronteVilla.aggiungiUscita("sud", stradaSterrata, false, null);
        fronteVilla.aggiungiUscita("ovest" , ovestVilla, false, null);
        fronteVilla.aggiungiUscita("nord" , atrioVilla, true, "sfera");


//        ovest villa
        ovestVilla.aggiungiUscita("est", fronteVilla, false, null);
        ovestVilla.aggiungiUscita("nord", cucinaVilla, false, null);

//        cucina villa
        Oggetto coltello = new Oggetto("Coltello", "Un coltello da cucina ben affilato", true, true, null);
        cucinaVilla.aggiungiOggetto(coltello);
        cucinaVilla.aggiungiUscita("sud", ovestVilla, false, null);
        cucinaVilla.aggiungiUscita("est", atrioVilla, false,null);
        cucinaVilla.aggiungiUscita("nord" , salaDaPranzoVilla, false,null);

//        sala da pranzo
        salaDaPranzoVilla.aggiungiUscita("sud", cucinaVilla, false, null);

//        atrio villa
        Nemico goblin = new Nemico("Goblin", 40, 10);
        atrioVilla.setNemico(goblin);
        atrioVilla.aggiungiUscita("ovest", cucinaVilla, false, null);

        stanzaIniziale = cucinaVilla;
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
