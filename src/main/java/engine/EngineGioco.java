package engine;
import model.Giocatore;
import model.Oggetto;
import model.Stanza;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

            } else {
                System.out.println(">> Comando non valido");
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

}
