package engine;
import model.Giocatore;
import model.Oggetto;
import model.Stanza;

import java.util.List;
import java.util.Scanner;

public class EngineGioco {

    private Giocatore giocatore;

    public EngineGioco(Giocatore giocatore) {
        this.giocatore = giocatore;
    }

    public void avvia(){
        Scanner scanner = new Scanner(System.in);
        boolean continua = true;

        while(continua){
            System.out.println("\n-" + giocatore.getStanzaAttuale().getNome()); // quesot messaggio apparirà ogni volta che ci muoveremo di stanza
            giocatore.getStanzaAttuale().stampaDescrizioneStanza();
            giocatore.getStanzaAttuale().stampaUscite();
            System.out.println("\nCosa vuoi fare? ");
            String comando = scanner.nextLine().toLowerCase();

            if(comando.startsWith("vai ")){
                String direzione = comando.substring(4);
                vai(direzione);
            }
            else if(comando.startsWith("prendi ")){
                String nomeOggetto = comando.substring(7); // rimuove dal comando prendi, "prendi ", es. "prendi pala", substring(7) == "pala"; stessa cosa con il "vai "
                prendi(nomeOggetto);

            }
            else if(comando.equals("guarda")){
                guarda();
            }
            else if(comando.startsWith("esci")){
                continua = false;
                System.out.println("Uscita dal gioco...");
            }else{
                System.out.println("Comando non valido");
            }

        }
    }
    private void vai(String direzione){
        Stanza stanza = giocatore.getStanzaAttuale().getUscita(direzione);
        if(stanza != null){
            giocatore.setStanzaAttuale(stanza); // se esiste una stanza in quella direzione, allora ci spostiamo
        }
        else{
            System.out.println("Non puoi andare a " + direzione);
        }
    }

    private void prendi(String nomeOggetto) {
//        se non dovesse funzionare proviamo con le stream
//        Oggetto oggetto = giocatore.getStanzaAttuale().getOggettiPresenti().stream()
//                .filter(i -> i.getNome().equalsIgnoreCase(nomeOggetto))
//                .findFirst().orElse(null);

        List<Oggetto> oggettiPerTerra = giocatore.getStanzaAttuale().getOggettiPresenti();
        Oggetto oggetto = oggettiPerTerra.get(oggettiPerTerra.indexOf(nomeOggetto));

        if(oggetto != null){ // se l'oggetto esiste
            giocatore.getInventario().aggiungiOggetto(oggetto); // lo aggiungiamo all'inventario
            giocatore.getStanzaAttuale().getOggettiPresenti().remove(oggetto); // e lo rimuoviamo dalla stanza

            // test per rimovere l'oggetto a terra dalla descrizione della stanza
            giocatore.getStanzaAttuale().setDescrizione(rimuoviOggettoDaTerra(giocatore.getStanzaAttuale().getDescrizione(), oggetto.getNome())); // da testare

            System.out.println("Hai raccolto: " + oggetto.getNome());
        }
        else{
            System.out.println("Oggetto non trovato");
        }
    }
    private void guarda(){
        giocatore.getStanzaAttuale().stampaDescrizioneStanza();

        List<Oggetto> oggetti = giocatore.getStanzaAttuale().getOggettiPresenti();
        if(oggetti != null && !oggetti.isEmpty()){
            System.out.println("A terra:");
            for(Oggetto oggetto : oggetti){
                System.out.println("- " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            }
        }
    }
    private String rimuoviOggettoDaTerra(String descrizioneStanza, String nomeOggetto){
        StringBuilder risultato = new StringBuilder();

        // regex per dividere la descrizioni in blocchi in base ai punti
        String[] blocchi = descrizioneStanza.split("(?<=\\.)\\s*");

        for (String blocco : blocchi) {
            // trovo gli indici di inizio e di fine, basandomi sui punti
            int inizio = blocco.indexOf('.');
            int fine = blocco.indexOf('.', inizio + 1);

            if (inizio != -1 && fine != -1) {
                String contenuto = blocco.substring(inizio + 1, fine).trim();

                // se NON contiene la parola da rimuovere lo aggiungiamo al risultato
                if (!contenuto.toLowerCase().contains(nomeOggetto.toLowerCase())) {
                    risultato.append(blocco.substring(0, fine + 1));
                }
            }
        }

        return risultato.toString().trim();



    }

}
