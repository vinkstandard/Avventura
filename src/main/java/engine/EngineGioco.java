package engine;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class EngineGioco {

    private Giocatore giocatore;

    public EngineGioco(Giocatore giocatore) {
        this.giocatore = giocatore;
    }

    public void avvia() {
        Scanner scanner = new Scanner(System.in);
        boolean continua = true;
        List<Stanza> stanzeVisitate = new ArrayList<>();
        stanzeVisitate.add(giocatore.getStanzaAttuale());
        guarda();
        while (continua) {
            if (!stanzeVisitate.contains(giocatore.getStanzaAttuale())) { // serve per non far apparire nome-desc a ogni ciclo, ma solo quando cambiamo stanza
                stanzeVisitate.clear();
                stanzeVisitate.add(giocatore.getStanzaAttuale());
                System.out.println("\n-" + giocatore.getStanzaAttuale().getNome());
                giocatore.getStanzaAttuale().stampaDescrizioneStanza();
                giocatore.getStanzaAttuale().stampaUscite();
            }
            System.out.println("\n>> Cosa vuoi fare? ");
            String comando = scanner.nextLine().toLowerCase();

            if (comando.startsWith("vai ") || comando.equals("nord") || comando.equals("sud") || comando.equals("ovest") || comando.equals("est")) {
                String direzione = comando;
                if (comando.charAt(0) == 'v') { // se è vai
                    direzione = comando.substring(4);
                    vai(direzione);
                } else { // se è direttamente una direzione [nord, est, sud, ovest]
                    vai(direzione);
                }
            } else if (comando.startsWith("prendi ") || comando.startsWith("raccogli ")) {
                String nomeOggetto;
                // rimuove dal comando prendi, "prendi ", es. "prendi pala", substring(7) == "pala"; stessa cosa con il "vai "
                switch (Character.toLowerCase(comando.charAt(0))) {
                    case 'p': // prendi
                        nomeOggetto = comando.substring(7);
                        prendi(nomeOggetto);
                        break;
                    case 'r': // raccogli
                        nomeOggetto = comando.substring(9);
                        prendi(nomeOggetto);
                        break;
                }

            } else if (comando.equals("guarda")) {
                guarda();

            } else if (comando.startsWith("esci")) {
                continua = false;
                System.out.println(">> Uscita dal gioco...");

            } else if (comando.equals("inventario") || comando.equals("inv")) {
                giocatore.getInventario().visualizzaInventario();

            } else if (comando.startsWith("usa ")) {
                String[] boxati = comando.replaceFirst("usa ", "").split(" ");
                usa(boxati[0].toLowerCase(), boxati[1].toLowerCase());

            } else if (comando.equals("debug")) {
                debug();
            }
            else if (comando.equals("combatti") || comando.startsWith("combatti ")) {
                combatti();
            }
            else if (comando.startsWith("lascia ") || comando.startsWith("droppa ")) {
                lascia(comando);
            }
            else if (comando.startsWith("equipaggia ")) {
                equipaggia(comando);
            }
            else if (comando.equals("info")) {
                info();
            }
            else if (comando.equals("help")) {
                help();
            }
            else {
                gestisciInputErrati(comando);
            }
        }

    }

    private void vai(String direzione) {
        Stanza stanzaAttuale = giocatore.getStanzaAttuale();

        if (!stanzaAttuale.esisteUscita(direzione)) {
            System.out.println(">> Non c'è nessuna uscita in quella direzione");
            return;
        }
        Stanza prossimaStanza = stanzaAttuale.getUscita(direzione);
        if (prossimaStanza == null) {
            System.out.println(">> La strada verso " + direzione + " è bloccata.");
            return;
        }

        giocatore.setStanzaAttuale(prossimaStanza);
    }

    private void prendi(String nomeOggetto) {

        List<Oggetto> oggettiPerTerra = giocatore.getStanzaAttuale().getOggettiPresenti();
        Oggetto oggettoTrovato = null;

        for (Oggetto oggettoPerTerra : oggettiPerTerra) {
            if (oggettoPerTerra.getNome().toLowerCase().contains(nomeOggetto.toLowerCase())) {
                oggettoTrovato = oggettoPerTerra;
//                System.out.println("OGGETTO PER TERRA -"+oggettoTrovato.getNome()+"-"); // debug
                break;
            }
        }

        if (oggettoTrovato != null) { // se l'oggetto esiste
            giocatore.getInventario().aggiungiOggetto(oggettoTrovato); // lo aggiungiamo all'inventario
            giocatore.getStanzaAttuale().getOggettiPresenti().remove(oggettoTrovato); // e lo rimuoviamo dalla stanza

            // test per rimovere l'oggetto a terra dalla descrizione della stanza
            giocatore.getStanzaAttuale().setDescrizione(rimuoviOggettoDaTerra(giocatore.getStanzaAttuale().getDescrizione(), nomeOggetto)); // da testare, meglio passare nomeOggetto che oggettoTrovato.getNome() poiché il nome dell'oggetto potrebbe non coincidere al 100%

            System.out.println(">> Hai raccolto: " + oggettoTrovato.getNome());
        } else {
            System.out.println(">> Oggetto non trovato");
        }

    }

    private void guarda() {
        giocatore.getStanzaAttuale().stampaDescrizioneStanza();

        List<Oggetto> oggetti = giocatore.getStanzaAttuale().getOggettiPresenti();
        if (oggetti != null && !oggetti.isEmpty()) {
            System.out.println(">> A terra:");
            for (Oggetto oggetto : oggetti) {
                System.out.println("- " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            }
        }

    }

    private String rimuoviOggettoDaTerra(String descrizioneStanza, String nomeOggetto) {
        StringBuilder risultato = new StringBuilder();

        // regex per dividere la descrizioni in blocchi in base ai punti
        String[] frasi = descrizioneStanza.split("(?<=\\.)\\s*");

        for (String frase : frasi) {
            // se la frase non ha il nome dell'oggetto nel nome, allora la manteniamo
            if (!frase.toLowerCase().contains(nomeOggetto.toLowerCase())) {
                risultato.append(frase).append(" ");
            }
        }

        return risultato.toString().trim();
    }

    private void usa(String nomeOggetto, String direzione) {

        if (giocatore.getInventario().possiedeOggetto(nomeOggetto)) {
            boolean sbloccato = giocatore.getStanzaAttuale().sbloccaUscita(direzione, nomeOggetto);
            if (sbloccato) {
                System.out.println(">> Hai usato l'oggetto {" + nomeOggetto + "} il passaggio si è aperto.");
            } else {
                System.out.println(">> Non puoi usare questo oggetto qui.");
            }
        } else {
            System.out.println(">> Non possiedi questo oggetto.");
        }

    }

    private void combatti(){
        Scanner scanner = new Scanner(System.in);
        Nemico nemico = giocatore.getStanzaAttuale().getNemico();

        if(nemico == null){
            System.out.println(">> Non c'è nessuno da combattere qui.");
            return;
        }

        System.out.println(">> Hai ingaggiato un combattimento con " + nemico.getNome() + "!");
        System.out.println(">> Mosse disponibili: \n-Attacca\n-Scappa");

        while(nemico.isVivo()){

            String mossa;
            do{
                mossa = scanner.nextLine().toLowerCase();
                if (!mossa.equals("attacca") && !mossa.equals("scappa")) {
                    System.out.println(">> Comando non valido.");
                }
            }while (!mossa.equals("attacca") && !mossa.equals("scappa"));

            // turno del giocatore, calcolando anche la possibilità di critico

            if (mossa.equals("attacca")) {
                int dannoInflitto = giocatore.getArmaEquipaggiata().infliggiDanno();
                if(dannoInflitto > giocatore.getArmaEquipaggiata().getDanno()){
                    System.out.println(">> Colpo critico! Hai colpito " + nemico.getNome() + " per " + dannoInflitto + " danni.");
                    nemico.subisciDanno(dannoInflitto);
                }else{
                    System.out.println(">> Hai colpito " + nemico.getNome() + " per " + dannoInflitto + " danni.");
                    nemico.subisciDanno(dannoInflitto);

                }
            }
            if (!nemico.isVivo()) {
                System.out.println(">> Hai sconfitto " + nemico.getNome() + "!");
                giocatore.getStanzaAttuale().setNemico(null); // rimuoviamo il nemico dalla stanza
                break;
            }
            if(mossa.equals("scappa")){
                int numeroRandom = ThreadLocalRandom.current().nextInt(1,4);
                int numeroRandom2 = ThreadLocalRandom.current().nextInt(1,4);
                if(numeroRandom2 == numeroRandom){
                    System.out.println(">> Sei riuscito a scappare dal combattimento.");
                    break;
                }else{
                    System.out.println(">> Tenti di scappare, ma fallisci, il nemico colpisce ti colpisce due volte.");
                    giocatore.subisciDanno(nemico.attacca());
                    System.out.println(">> " + nemico.getNome() +  " ti ha colpito per " + nemico.attacca() + " danni.");
                }
            }

            // turno del nemico, anche lui può crittare

            int dannoInflittoNemico = nemico.attacca();
            if(dannoInflittoNemico > nemico.getDanno()){
                giocatore.subisciDanno(dannoInflittoNemico);
                System.out.println(">> Colpo critico! "+ nemico.getNome() +  " ti ha colpito per " + (dannoInflittoNemico) + " danni.");

            }else{
                giocatore.subisciDanno(nemico.attacca());
                System.out.println(">> "+ nemico.getNome() +  " ti ha colpito per " + dannoInflittoNemico + " danni.");
            }


            if(giocatore.getVita() <= 0){
                System.out.println(">> Sei stato sconfitto.\nGame Over.");
                System.exit(11);
            }
        }
    }

    private void debug() {
        Stanza stanzaAttuale = giocatore.getStanzaAttuale();
        System.out.println("Stanza attuale: " + stanzaAttuale.getNome());
        System.out.println("Descrizione: " + stanzaAttuale.getDescrizione());
        System.out.println("Uscite:");
        for (String direzione : stanzaAttuale.getUscite().keySet()) {
            Stanza collegata = stanzaAttuale.getUscite().get(direzione);
            boolean sbloccata = stanzaAttuale.getUsciteSbloccate().getOrDefault(direzione, true);
            String stato = sbloccata ? "Sbloccata" : "Bloccata";
            System.out.println(direzione + " --> " + collegata.getNome() + " [" + stato + "]");
        }
    }

    public void lascia(String comando){
        String nomeOggetto = switch (comando.charAt(0)) {
            case 'l' -> comando.replaceFirst("lascia ", "");
            case 'd' -> comando.replaceFirst("droppa ", "");
            default -> "";
        };
        for(Oggetto oggetto: giocatore.getInventario().getOggetti()){
            if(oggetto.getNome().toLowerCase().contains(nomeOggetto)){
                giocatore.getStanzaAttuale().aggiungiOggetto(oggetto);
                System.out.println(">> Hai lasciato: " + oggetto.getNome());
                return;
            }
        }
        System.out.println(">> Non possiedi quell'oggetto.");
    }
    public void equipaggia(String comando){
        String nomeArma = comando.replaceFirst("equipaggia ", "");

        Oggetto oggetto = giocatore.getInventario().getOggetto(nomeArma);
        if(oggetto instanceof Arma arma){
            giocatore.equipaggiaArma(arma);
            System.out.println(">> Hai equipaggiato " + arma.getNome());
        }else{
            System.out.println(">> Non puoi usarlo come arma.");
        }
    }
    public void info(){

        System.out.println("HP: " + giocatore.getVita());
        System.out.println("Arma equipaggiata: " + giocatore.getArmaEquipaggiata().getNome() + "\tDMG: " + giocatore.getArmaEquipaggiata().getDanno() + "\t CRT: " + giocatore.getArmaEquipaggiata().getPossibilitaCritico() + "%");
    }
    public void help(){
        System.out.println("Comandi validi:");
        System.out.println("-Movimento--------------------------------------------------------------------\n" +
                "\"vai\" + direzione (nord, sud, est, ovest): Ti sposti in una direzione.\n" +
                "\"nord\", \"sud\", \"est\", \"ovest\" (abbreviazione del comando vai).");
        System.out.println("-Combattimento----------------------------------------------------------------\n" +
                "\"combatti\"/\"combatti [nome]\": Ingaggi un nemico in combattimento.\n" +
                "\"equipaggia [nomeArma]\": Equipaggi un arma.\n" +
                "\"attacca\": Sferri un colpo con l'arma attualmente equipaggiata durante un combattimento.\n" +
                "\"fuggi\": Tenti di scappare da un combattimento.");
        System.out.println("-Utilità----------------------------------------------------------------------\n" +
                "\"info\": Mostra hp giocatore ed equipaggiamento.\n" +
                "\"guarda\": Ti guardi attorno e controlli se ci sono oggetti.\n" +
                "\"inventario\": Visualizzi il tuo inventario.\n" +
                "\"usa [nomeOggetto] + direzione\": Usi un oggetto in una direzione. Es: usa spranga nord\n" +
                "\"esci\": Esci dal gioco");

    }
    public void gestisciInputErrati(String comando){

        // qui scriverò tutti i messaggi di errore personalizzati

        if(comando.equals("attacca") || comando.startsWith("attacca ")){ // da modificare poi con un check per vedere se ci sono effettivamente nemici da attaccare, per ora il comando per attaccare è solo "combatti"
            Nemico nemico = giocatore.getStanzaAttuale().getNemico();
            if(nemico == null){
                System.out.println(">> Non c'è nulla da attaccare qui.");
            }
        }
        else{
            System.out.println(">> Comando non valido.");
        }
    }
}
