package model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Nemico {
    private String nome;
    private int vita;
    private int danno;
    private int possibilitaCritico;
    private List<Oggetto> lootNemico = new ArrayList<>();

    public Nemico(String nome, int vita, int danno, int possibilitaCritico) {
        this.nome = nome;
        this.vita = vita;
        this.danno = danno;
        this.possibilitaCritico = possibilitaCritico;
    }
    public Nemico(List<Oggetto> lootNemico){
        this.lootNemico = lootNemico;
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
            return ThreadLocalRandom.current().nextInt(1, danno + 1) * 2;
        }else{
            return ThreadLocalRandom.current().nextInt(1, danno + 1);
        }
    }
    public boolean isVivo(){
        return vita > 0;
    }

    public int getDanno() {
        return danno;
    }

    public List<Oggetto> getLootNemico() {
        return lootNemico;
    }

    public void setLootNemico(List<Oggetto> lootNemico) {
        this.lootNemico = lootNemico;
    }
    public void rimuoviLootNemico(Oggetto oggettoDaRimuovere){
        lootNemico.remove(oggettoDaRimuovere);
    }

}
