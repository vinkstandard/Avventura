import engine.EngineGioco;
import model.Giocatore;
import model.Inventario;
import model.Oggetto;
import model.Stanza;

public class Main {
    public static void main(String[] args) {


        stampaIntroduzione();
        Stanza sterpaglie = new Stanza("Sterpaglie", "Sei in una foresta e vedi delle luci verso nord.");

        Stanza cancelloArrugginito = new Stanza("Cancello arrugginito", "Sei davanti a un cancello arrugginito, " +
                "oltre in cancello riesci a scrutare una strada sterrata che conduce ad un grande edificio. Ai tuoi piedi c'è una spranga di metallo");

        Stanza stradaSterrata = new Stanza("Strada sterrata" , "Sei su una strada sterrata, a nord la strada conduce ad una villa gotica, " +
                "verso est noti delle orme nel fango conducono ad un vecchio capanno");

        Stanza capannoAttrezzi = new Stanza("Capanno per gli attrezzi" , "Sei davanti ad un vecchio capanno per gli attrezzi in disuso." +
                " La porta del capanno è a terra fradicia. Riesci a scrutare una vecchia lanterna a olio e un vecchio piede di porco sul bancone.");


        sterpaglie.aggiungiUscita("nord", cancelloArrugginito);

        cancelloArrugginito.aggiungiUscita("nord" , stradaSterrata);
        Oggetto spranga = new Oggetto("Spranga arrugginita", "Una Spranga di metallo arrugginita", true, true, null);
        cancelloArrugginito.aggiungiOggetto(spranga);

        stradaSterrata.aggiungiUscita("est" , capannoAttrezzi);
        stradaSterrata.aggiungiUscita("sud", cancelloArrugginito); // per tornare indietro
//        stradaSterrata.aggiungiUscita("nord" , ingressoVilla);

        capannoAttrezzi.aggiungiUscita("ovest" , stradaSterrata);
        Oggetto lanterna = new Oggetto("Lanterna", "Vecchia lanterna a olio, scuotendola senti che il serbatoio è pieno a metà", true, true, null);
        Oggetto piedeDiPorco = new Oggetto("Piede di porco", "Vecchia lanterna a olio, scuotendola senti che il serbatoio è pieno a metà", true, true, null);
        capannoAttrezzi.aggiungiOggetto(lanterna);
        capannoAttrezzi.aggiungiOggetto(piedeDiPorco);

        Oggetto bussola = new Oggetto("Bussola" , "Una vecchia bussola in ottone, sul retro ci sono incise due iniziali, FP" , true, false, null);
        Oggetto accendino = new Oggetto("Accendino" , "Un accendino a kerosene, provi ad accenderlo e vedi che funziona" , true, true, null);

        // creo il player
        Inventario inventarioGiocatore = new Inventario();
        inventarioGiocatore.aggiungiOggetto(accendino);
        inventarioGiocatore.aggiungiOggetto(bussola);
        Giocatore giocatore = new Giocatore("Marco", sterpaglie, inventarioGiocatore);

        // creo l'engine
        EngineGioco engine = new EngineGioco(giocatore);
        engine.avvia();
    }


    // scriverò altro nell'introduzione, per ora lasciamola così
    public static void stampaIntroduzione(){
        System.out.println("-----Benvenuto in [nomeAvventura], un'avventura fantasy testuale-----\n" +
                "Ti risvegli in una foresta, non hai memoria di chi sei e come ci sei finito. Ti senti smarrito e sta iniziando a fare buio. " +
                "Controllandoti le tasche, noti che hai solo una bussola in ottone, e un accendino");
    }
}
