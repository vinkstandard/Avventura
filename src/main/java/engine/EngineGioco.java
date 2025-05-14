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
            System.out.println("\n-" + giocatore.getStanzaAttuale().getNome()); // questo messaggio apparirà ogni volta che ci muoveremo di stanza
            giocatore.getStanzaAttuale().stampaDescrizioneStanza();
            giocatore.getStanzaAttuale().stampaUscite();
            System.out.println("\n>> Cosa vuoi fare? ");
            String comando = scanner.nextLine().toLowerCase();

            if(comando.startsWith("vai ") || comando.equals("nord") || comando.equals("sud") || comando.equals("ovest") || comando.equals("est")){
                String direzione = comando;
                if(comando.charAt(0) == 'v') {
                    direzione = comando.substring(4);
                    vai(direzione);
                }else{
                    vai(direzione);
                }
            }

            else if(comando.startsWith("prendi ") || comando.startsWith("raccogli ") ){
                String nomeOggetto;
                // rimuove dal comando prendi, "prendi ", es. "prendi pala", substring(7) == "pala"; stessa cosa con il "vai "
                switch (Character.toLowerCase(comando.charAt(0))) {
                    case 'p':
                        nomeOggetto = comando.substring(7);
                        prendi(nomeOggetto);
                        break;
                    case 'r':
                        nomeOggetto = comando.substring(9);
                        prendi(nomeOggetto);
                        break;
                }

            }
            else if(comando.equals("guarda")){
                guarda();
            }
            else if(comando.startsWith("esci")){
                continua = false;
                System.out.println(">> Uscita dal gioco...");
            }
            else if(comando.equals("inventario")){
                giocatore.getInventario().visualizzaInventario();
            }else{
                System.out.println(">> Comando non valido");
            }

        }
    }
    private void vai(String direzione){
        Stanza stanza = giocatore.getStanzaAttuale().getUscita(direzione);
        if(stanza != null){
            giocatore.setStanzaAttuale(stanza); // se esiste una stanza in quella direzione, allora ci spostiamo
        }
        else{
            System.out.println(">> Non puoi andare a " + direzione);
        }
    }

    private void prendi(String nomeOggetto) {
//        se non dovesse funzionare proviamo con le stream
//        Oggetto oggetto = giocatore.getStanzaAttuale().getOggettiPresenti().stream()
//                .filter(i -> i.getNome().equalsIgnoreCase(nomeOggetto))
//                .findFirst().orElse(null);

        List<Oggetto> oggettiPerTerra = giocatore.getStanzaAttuale().getOggettiPresenti();
        Oggetto oggettoTrovato = null;

        for(Oggetto oggettoPerTerra : oggettiPerTerra){
            if(oggettoPerTerra.getNome().equalsIgnoreCase(nomeOggetto)){
                oggettoTrovato = oggettoPerTerra;
                break;
            }
        }

        if(oggettoTrovato != null){ // se l'oggetto esiste
            giocatore.getInventario().aggiungiOggetto(oggettoTrovato); // lo aggiungiamo all'inventario
            giocatore.getStanzaAttuale().getOggettiPresenti().remove(oggettoTrovato); // e lo rimuoviamo dalla stanza

            // test per rimovere l'oggetto a terra dalla descrizione della stanza
            giocatore.getStanzaAttuale().setDescrizione(rimuoviOggettoDaTerra(giocatore.getStanzaAttuale().getDescrizione(), oggettoTrovato.getNome())); // da testare

            System.out.println(">> Hai raccolto: " + oggettoTrovato.getNome());
        }
        else{
            System.out.println(">> Oggetto non trovato");
        }
    }
    private void guarda(){
        giocatore.getStanzaAttuale().stampaDescrizioneStanza();

        List<Oggetto> oggetti = giocatore.getStanzaAttuale().getOggettiPresenti();
        if(oggetti != null && !oggetti.isEmpty()){
            System.out.println(">> A terra:");
            for(Oggetto oggetto : oggetti){
                System.out.println("- " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            }
        }
    }
    private String rimuoviOggettoDaTerra(String descrizioneStanza, String nomeOggetto){
        StringBuilder risultato = new StringBuilder();

        // regex per dividere la descrizioni in blocchi in base ai punti
        String[] frasi = descrizioneStanza.split("(?<=\\.)\\s*");

        for (String frase : frasi) {
            // se la frase non ha il nome dell'oggetto nel nome, allora la manteniamo
            if(!frase.toLowerCase().contains(nomeOggetto.toLowerCase())){
                risultato.append(frase).append(" ");
            }
        }

        return risultato.toString().trim();



    }

}
