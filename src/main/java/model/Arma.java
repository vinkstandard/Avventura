package model;

import java.util.concurrent.ThreadLocalRandom;

public class Arma extends Oggetto {

    private int danno;
    private int possibilitaCritico;

    public Arma(String nome, String descrizione, int danno, int possibilitaCritico) {

        super(nome, descrizione, true); // raccoglibile di default
        this.danno = danno;
        this.possibilitaCritico = possibilitaCritico;
    }

    public int infliggiDanno() {

        int random = ThreadLocalRandom.current().nextInt(0, 101); // calcolo percentuale
        if (random <= possibilitaCritico) { // se abbiamo crittato
            return danno * 2;
        } else {
            return danno;
        }
    }

    public int getDanno() {
        return danno;
    }

    public int getPossibilitaCritico() {
        return possibilitaCritico;
    }
}
