package model;

public class Nemico {
    private String nome;
    private int vita;
    private int danno;

    public Nemico(String nome, int vita, int danno) {
        this.nome = nome;
        this.vita = vita;
        this.danno = danno;
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
        return danno;
    }
    public boolean isVivo(){
        return vita > 0;
    }
}
