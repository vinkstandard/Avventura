package model;

import java.util.*;
public class Inventario {

    private List<Oggetto> oggetti;

    public Inventario(){
        this.oggetti = new ArrayList<>();
    }

    public void aggiungiOggetto(Oggetto oggetto){
        oggetti.add(oggetto);
    }
    public boolean rimuoviOggetto(String nomeOggetto){
        return oggetti.removeIf(i -> i.getNome().equalsIgnoreCase(nomeOggetto));
    }

    public Oggetto getOggetto(String nomeOggetto){
        return oggetti.stream()
                .filter(i -> i.getNome().equalsIgnoreCase(nomeOggetto)).findFirst().orElse(null);
    }
    public List<Oggetto> getOggetti(){
        return oggetti;
    }

    public void visualizzaInventario(){
        if(oggetti.isEmpty() || oggetti == null){
            System.out.println(">> Inventario vuoto");
        }else{
            System.out.println(">> Inventario:");
            for(Oggetto oggetto : oggetti){
                System.out.println("- " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            }
        }
    }

}
