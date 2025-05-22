package engine;

import model.*;

import java.util.ArrayList;
import java.util.Arrays;
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
                usa(comando.replaceFirst("usa " , ""));

            } else if (comando.equals("debug")) {
                debug();
            } else if (comando.equals("combatti") || comando.startsWith("combatti ") || comando.equals("attacca") || comando.startsWith("attacca ")) {
                combatti();
            } else if (comando.startsWith("lascia ") || comando.startsWith("droppa ")) {
                lascia(comando);
            } else if (comando.startsWith("bevi ") || comando.equals("bevi")) {
                bevi();
            } else if (comando.startsWith("equipaggia ")) {
                equipaggia(comando);
            } else if (comando.equals("info")) {
                info();
            } else if (comando.equals("help")) {
                help();
            } else if (comando.startsWith("depreda ") || comando.equals("loot")) {

                Nemico nemico = giocatore.getStanzaAttuale().getNemico();
                if (!giocatore.getStanzaAttuale().contieneNemico() || nemico.getLootNemico().isEmpty()) {
                    System.out.println(">> Non c'è nessun nemico da depredare qui.");
                } else if (comando.startsWith("l")) {
                    if (giocatore.getStanzaAttuale().getNemico().getLootNemico().isEmpty()) {
                        System.out.println(">> Il nemico non possedeva niente di utile con se.");
                    } else {
                        depredaCadavere();
                    }
                } else if (comando.startsWith("d")) {
                    String bersaglio = comando.replaceFirst("depreda ", "");
                    if (!nemico.getNome().toLowerCase().equals(bersaglio)) {
                        System.out.println(">> Non c'è il nemico {" + bersaglio + "} quì.");
                    } else {
                        depredaCadavere();
                    }
                }
            } else {
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

        if (oggettoTrovato != null && oggettoTrovato.isRaccoglibile()) { // se l'oggetto esiste, ed è raccoglibile
            giocatore.getInventario().aggiungiOggetto(oggettoTrovato); // lo aggiungiamo all'inventario
            giocatore.getStanzaAttuale().getOggettiPresenti().remove(oggettoTrovato); // e lo rimuoviamo dalla stanza

            // test per rimovere l'oggetto a terra dalla descrizione della stanza
            giocatore.getStanzaAttuale().setDescrizione(rimuoviOggettoDaTerra(giocatore.getStanzaAttuale().getDescrizione(), nomeOggetto)); // da testare, meglio passare nomeOggetto che oggettoTrovato.getNome() poiché il nome dell'oggetto potrebbe non coincidere al 100%

            System.out.println(">> Hai raccolto: " + oggettoTrovato.getNome() + ".");
        } else {
            if (oggettoTrovato == null) {
                System.out.println(">> Oggetto non trovato.");
            } else if (!oggettoTrovato.isRaccoglibile()) {
                System.out.println(">> Non puoi raccogliere questo oggetto.");

            }
        }

    }

    private void guarda() {

        giocatore.getStanzaAttuale().stampaDescrizioneStanza();
        List<Oggetto> oggettiPerTerra = giocatore.getStanzaAttuale().getOggettiPresenti();
        if (oggettiPerTerra != null && !oggettiPerTerra.isEmpty()) {
            System.out.println(">> A terra:");
            for (Oggetto oggetto : oggettiPerTerra) {
                System.out.println("- " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            }
        }
        if(giocatore.getStanzaAttuale().getNemico() != null){
            Nemico nemico = giocatore.getStanzaAttuale().getNemico();
            if(!nemico.isVivo() && oggettiPerTerra != null && oggettiPerTerra.isEmpty()){
                System.out.println(">> A terra:\n- Cadavere di " + nemico.getNome());
            }else{
                System.out.println("- Cadavere di " + nemico.getNome());
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

    private void usa(String comando) {
        String[] comandi = comando.split(" ", 2); // limite 2,

        if(comandi.length < 2 || comandi[1].isEmpty()){
            System.out.println(">> Specifica la direzione.");
            return;
        }
        String nomeOggetto = comandi[0];
        String direzione = comandi[1];

        Oggetto oggetto = giocatore.getInventario().getOggetto(nomeOggetto);
        if (giocatore.getInventario().possiedeOggetto(nomeOggetto)) {
            // check per vedere se è un arma
            if (oggetto instanceof Arma) {
                System.out.println(">> Non puoi usare un'arma come oggetto. Le armi si usano solo in combattimento.");
                return;
            }
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

    private void combatti() {
        // testing, per ora non stiamo rimuovendo i nemici dalla stanza dopo averli sconfitti, per dare il prompt al giocatore di lootarli, se dovesse
        // servire, il comando per rimuoverli è questo: giocatore.getStanzaAttuale().setNemico(null);

        Scanner scanner = new Scanner(System.in);
        Nemico nemico = giocatore.getStanzaAttuale().getNemico();

        if (nemico == null || nemico.getVita() <= 0) {
            System.out.println(">> Non c'è nessuno da combattere qui.");
            return;
        }

        System.out.println(">> Hai ingaggiato un combattimento con " + nemico.getNome() + "!");
        System.out.println(">> Mosse disponibili: \n-Attacca\n-Equipaggia {arma}\n-Curati\n-Scappa");

        while (nemico.isVivo()) {
            List<String> mosseDisponibili = new ArrayList<>(
                    Arrays.asList("attacca", "equipaggia", "curati", "info", "scappa", "oneshot"));
            String mossa;
            do {
                mossa = scanner.nextLine().toLowerCase();
                if (!mosseDisponibili.contains(mossa)) {
                    System.out.println(">> Comando non valido.");
                }
            } while (!mosseDisponibili.contains(mossa));

            // turno del giocatore, calcolando anche la possibilità di critico

            if (mossa.equals("curati")) {
                bevi();
            } else if (mossa.startsWith("equipaggia ")) {
                equipaggia(mossa);
            } else if (mossa.startsWith("info")) {
                info();
            } else if (mossa.equals("oneshot")){
                nemico.subisciDanno(nemico.getVita());
                if (!nemico.isVivo()) {
                    System.out.println(">> Hai sconfitto " + nemico.getNome() + "!");
                    break;
                }
            }


            if (mossa.equals("attacca")) {
                if (giocatore.getArmaEquipaggiata() == null) {
                    System.out.println(">> Ti serve un arma per attaccare!");
                    continue;
                }
                int dannoInflitto = giocatore.getArmaEquipaggiata().infliggiDanno();
                if (dannoInflitto > giocatore.getArmaEquipaggiata().getDanno()) {
                    System.out.println(">> Colpo critico! Hai colpito " + nemico.getNome() + " per " + dannoInflitto + " danni.");
                    nemico.subisciDanno(dannoInflitto);
                } else {
                    System.out.println(">> Hai colpito " + nemico.getNome() + " per " + dannoInflitto + " danni.");
                    nemico.subisciDanno(dannoInflitto);

                }
            }
            if (!nemico.isVivo()) {
                System.out.println(">> Hai sconfitto " + nemico.getNome() + "!");
                break;
            }
            if (mossa.equals("scappa")) {
                int numeroRandom = ThreadLocalRandom.current().nextInt(1, 4);
                int numeroRandom2 = ThreadLocalRandom.current().nextInt(1, 4);
                if (numeroRandom2 == numeroRandom) {
                    System.out.println(">> Sei riuscito a scappare dal combattimento.");
                    break;
                } else {
                    System.out.println(">> Tenti di scappare, ma fallisci, il nemico colpisce ti colpisce due volte.");
                    giocatore.subisciDanno(nemico.attacca());
                    System.out.println(">> " + nemico.getNome() + " ti ha colpito per " + nemico.attacca() + " danni.\n>> Ti restano: " + giocatore.getVita() + " hp.");
                }
            }

            // turno del nemico, anche lui può crittare

            int dannoInflittoNemico = nemico.attacca();
            if (dannoInflittoNemico > nemico.getDanno()) {
                giocatore.subisciDanno(dannoInflittoNemico);
                System.out.println(">> Colpo critico! " + nemico.getNome() + " ti ha colpito per " + (dannoInflittoNemico) + " danni.\n>> Ti restano: " + giocatore.getVita() + " hp.");

            } else {
                giocatore.subisciDanno(nemico.attacca());
                System.out.println(">> " + nemico.getNome() + " ti ha colpito per " + dannoInflittoNemico + " danni.\n>> Ti restano: " + giocatore.getVita() + " hp.");
            }


            if (giocatore.getVita() <= 0) {
                System.out.println(">> Sei stato sconfitto.\nGame Over.");
                System.exit(11);
            }
        }
    }

    public void bevi() {

        Scanner scanner = new Scanner(System.in);
        List<Oggetto> oggettiCurativi = giocatore.getInventario().getOggettiCurativi();

        if (oggettiCurativi.isEmpty()) {
            System.out.println(">> Non possiedi oggetti da bere.");
            return;
        }
        int numOggetto = 0;
        System.out.println(">> Consumabili in tuo possesso:");
        for (Oggetto oggettoCurativo : oggettiCurativi) {
            System.out.println((numOggetto + 1) + ". " + oggettoCurativo.getNome() + ": " + oggettoCurativo.getDescrizione());
            numOggetto++;
        }
        System.out.println(">> Inserisci il numero dell'oggetto che vuoi utilizzare.");
        String numero = scanner.nextLine();
        if (numero.length() > 1 || !Character.isDigit(numero.charAt(0)) || Integer.parseInt(numero) - 1 > oggettiCurativi.size()) {
            System.out.println(">> Numero non valido.");
        } else {
            curaGiocatore(oggettiCurativi.get(Integer.parseInt(numero) - 1).getNome());
            giocatore.getInventario().rimuoviOggetto(oggettiCurativi.get(Integer.parseInt(numero) - 1).getNome());
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

    public void lascia(String comando) {

        String nomeOggetto = switch (comando.charAt(0)) {
            case 'l' -> comando.replaceFirst("lascia ", "");
            case 'd' -> comando.replaceFirst("droppa ", "");
            default -> "";
        };
        for (Oggetto oggetto : giocatore.getInventario().getOggetti()) {
            if (oggetto.getNome().toLowerCase().contains(nomeOggetto)) {
                giocatore.getStanzaAttuale().aggiungiOggetto(oggetto);
                System.out.println(">> Hai lasciato: " + oggetto.getNome());
                return;
            }
        }
        System.out.println(">> Non possiedi quell'oggetto.");
    }

    public void equipaggia(String comando) {

        String nomeArma = comando.replaceFirst("equipaggia ", "");
        Oggetto oggetto = giocatore.getInventario().getOggetto(nomeArma);
        if (oggetto instanceof Arma arma) {
            giocatore.equipaggiaArma(arma);
            System.out.println(">> Hai equipaggiato " + arma.getNome());
        } else {
            System.out.println(">> Non puoi usarlo come arma.");
        }
    }
    public void depredaCadavere(){

        Scanner scanner = new Scanner(System.in);
        Nemico nemico = giocatore.getStanzaAttuale().getNemico();
        int indiceOggetto = 1;
        List<Oggetto> lootNemico = nemico.getLootNemico();
        List<Oggetto> daRimuovere = new ArrayList<>();
        System.out.println(">> Frugando tra i resti del nemico hai trovato:");
        for(Oggetto oggetto : lootNemico){
            System.out.println(indiceOggetto + ". " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            giocatore.getInventario().aggiungiOggetto(oggetto);
            daRimuovere.add(oggetto);
        }
        for(Oggetto oggetto : daRimuovere){
            nemico.rimuoviLootNemico(oggetto);
        }
    }

    public void curaGiocatore(String nome) { // nome fa riferimento a un oggetto curativo, o a una possible abilità

        if (nome.equalsIgnoreCase("Pozione Rossa")) {
            giocatore.guadagnaHpGiocatore(40);
            System.out.println(">> Hai usato " + nome + "!");
        }
    }

    public void info() {

        System.out.println("HP: " + giocatore.getVita());
        if(giocatore.getArmaEquipaggiata() != null){
            System.out.println("Arma equipaggiata: " + giocatore.getArmaEquipaggiata().getNome() + "\tDMG: " + giocatore.getArmaEquipaggiata().getDanno() + "\t CRT: " + giocatore.getArmaEquipaggiata().getPossibilitaCritico() + "%");
        }else{
            System.out.println("Arma equipaggiata: Nessuna.");
        }
    }

    public void help() {

        System.out.println("Comandi validi:");
        System.out.println("""
                -Movimento--------------------------------------------------------------------
                "vai" + direzione (nord, sud, est, ovest): Ti sposti in una direzione.
                "nord", "sud", "est", "ovest" (abbreviazione del comando vai).""");
        System.out.println("""
                -Combattimento----------------------------------------------------------------
                "combatti"/"combatti [nome]": Ingaggi un nemico in combattimento.
                "equipaggia [nomeArma]": Equipaggi un arma.
                "attacca": Sferri un colpo con l'arma attualmente equipaggiata durante un combattimento.
                "fuggi": Tenti di scappare da un combattimento.""");
        System.out.println("""
                -Utilità----------------------------------------------------------------------
                "info": Mostra hp giocatore ed equipaggiamento.
                "guarda": Ti guardi attorno e controlli se ci sono oggetti.
                "inventario": Visualizzi il tuo inventario.
                "usa [nomeOggetto] + direzione": Usi un oggetto in una direzione. Es: usa mazza sud
                "bevi": Bevi un oggetto curativo.
                "depreda [nome]" / "loot": Depredi un cadavere di un nemico sconfitto.
                "esci": Esci dal gioco""");

    }

    public void gestisciInputErrati(String comando) {

        // qui scriverò tutti i messaggi di errore personalizzati

        if (comando.equals("per ora non so cosa mettere")) {
            // per ora niente
        } else {
            System.out.println(">> Comando non valido.");
        }
    }
}
