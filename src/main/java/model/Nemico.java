package model;

import java.util.concurrent.ThreadLocalRandom;

public class Nemico {
    private String nome;
    private int vita;
    private int danno;
    private int possibilitaCritico;

    public Nemico(String nome, int vita, int danno, int possibilitaCritico) {
        this.nome = nome;
        this.vita = vita;
        this.danno = danno;
        this.possibilitaCritico = possibilitaCritico;
    }

    public String getNome() {
        return nome;
    }

    public int getVita() {
        return vita;
    }
    public void subisciDanno(int danno){
        this.vita -= danno;
        if(this.vita < 0) this.vita = 0;
    }
    public int attacca(){
        int random = ThreadLocalRandom.current().nextInt(0, 101); // calcolo percentuale
        if(random <= possibilitaCritico){ // se abbiamo crittato
            return danno * 2;
        }else{
            return danno;
        }
    }
    public boolean isVivo(){
        return vita > 0;
    }

    public int getDanno() {
        return danno;
    }
}
