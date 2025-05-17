import engine.EngineGioco;
import gioco.Mappa;
import model.Giocatore;
import model.Inventario;
import model.Oggetto;
import model.Stanza;

public class Main {
    public static void main(String[] args) {


        // TODO:  vincoli, cioè porte/luoghi che richiedono che il giocatore faccia x per accedere {fatto};
        //  comando "lascia [oggetto] {fatto}"
        //  parametro nemiciStanza {fatto}
        //  eventiAutomatici
        //  condizioniAccesso {fatto}
        //  usare armi, e quindi le loro statistiche durante il combattimento {fatto}
        //  limitare l'inventario del giocatore?
        //  comando per controllare gli hp del giocatore e l'equipaggiamento indossato, armi/armature/anelli {fatto}
        //  aggiungere la crit chance anche ai nemici {fatto}
        //  aggiungere più messaggi di errore, vedi metodo "gestisciInputErrati()" in EngineGioco
        //  aggiungi un modo per cambiare/usare equipaggiamenti durante il combattimento



        stampaIntroduzione();
        Mappa mappa = new Mappa();

        // creo il giocatore e il suo starting gear

        Inventario inventarioGiocatore = new Inventario();
        Giocatore giocatore = new Giocatore("Marco", mappa.getStanzaIniziale(), inventarioGiocatore);

        Oggetto bussola = new Oggetto("Bussola" , "Una vecchia bussola in ottone, sul retro ci sono incise due iniziali, \"FP\"." , true, false, null);
        Oggetto accendino = new Oggetto("Accendino" , "Un accendino a kerosene, provi ad accenderlo e vedi che funziona." , true, true, null);

        inventarioGiocatore.aggiungiOggetto(accendino);
        inventarioGiocatore.aggiungiOggetto(bussola);

        // creo l'engine

        EngineGioco engine = new EngineGioco(giocatore);
        engine.avvia();
    }


    // scriverò altro nell'introduzione, per ora lasciamola così
    public static void stampaIntroduzione(){
        System.out.println("-----Benvenuto in [nomeAvventura], un'avventura fantasy testuale-----\n" +
                "Ti risvegli in una foresta, non hai memoria di chi sei e come ci sei finito. Ti senti smarrito e sta iniziando a fare buio. " +
                "\nControllandoti le tasche, noti che hai una bussola in ottone e un accendino.\n");
    }
}
