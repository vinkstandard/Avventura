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

        int random = ThreadLocalRandom.current().nextInt(0, 101); // calcolo percentuale per il crit
        if (random <= possibilitaCritico) { // se abbiamo crittato
            return ThreadLocalRandom.current().nextInt(1, danno + 1) * 2; // raddoppio in caso di critico
        } else {
            return ThreadLocalRandom.current().nextInt(1, danno + 1); // genera un numero casuale da 1, al danno attuale dell'arma
        }
    }

    public int getDanno() {
        return danno;
    }

    public int getPossibilitaCritico() {
        return possibilitaCritico;
    }
}
