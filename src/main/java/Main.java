import engine.EngineGioco;
import gioco.Mappa;
import model.*;

public class Main {
    public static void main(String[] args) {


        // TODO:
        //  {fatto} vincoli, cioè porte/luoghi che richiedono che il giocatore faccia x per accedere;
        //  {fatto} comando "lascia [oggetto]"
        //  {fatto} parametro nemiciStanza
        //  eventiAutomatici
        //  {fatto} condizioniAccesso
        //  {fatto} usare armi, e quindi le loro statistiche durante il combattimento
        //  limitare l'inventario del giocatore?
        //  {fatto} comando per controllare gli hp del giocatore e l'equipaggiamento indossato, armi/armature/anelli
        //  {fatto} aggiungere la crit chance anche ai nemici
        //  aggiungere più messaggi di errore, vedi metodo "gestisciInputErrati()" in EngineGioco
        //  {fatto} aggiungi un modo per cambiare/usare equipaggiamenti durante il combattimento
        //  quando il giocatore vince il check per scappare dalla fight, deve poi decidere dove scappare(direzione)
        //  {fatto} aggiungere item curativi e un modo per utilizzarli
        //  {fatto} aggiungi un check per quando il giocatore cerca di usare gli oggetti che hanno la boolean "usabile" su false, o di raccogliere quelli con "raccoglibile" = false




        stampaIntroduzione();
        Mappa mappa = new Mappa();

        // creo il giocatore e il suo starting gear

        Inventario inventarioGiocatore = new Inventario();
        Giocatore giocatore = new Giocatore("Marco", mappa.getStanzaIniziale(), inventarioGiocatore);

        Arma disarmato = new Arma("Disarmato", "Le tue mani nude", 5, 10);
        Oggetto bussola = new Oggetto("Bussola" , "Una vecchia bussola in ottone, sul retro ci sono incise due iniziali, \"FP\"." , true, false, null);
        Oggetto accendino = new Oggetto("Accendino" , "Un accendino a kerosene, provi ad accenderlo e vedi che funziona." , true, true, null);

        inventarioGiocatore.aggiungiOggetto(accendino);
        inventarioGiocatore.aggiungiOggetto(bussola);
        inventarioGiocatore.aggiungiOggetto(disarmato);

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
