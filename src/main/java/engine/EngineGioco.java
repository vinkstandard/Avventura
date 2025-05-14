package engine;
import model.Giocatore;

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
            System.out.println("\nTi trovi nella stanza " + giocatore.getStanzaAttuale().getNome());
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
    private void vai(){

    }
    private void prendi(){

    }
    private void guarda(){

    }

}
