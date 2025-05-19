package model;

import java.util.*;

public class Inventario {

    private List<Oggetto> oggetti;

    public Inventario() {
        this.oggetti = new ArrayList<>();
    }

    public void aggiungiOggetto(Oggetto oggetto) {
        oggetti.add(oggetto);
    }

    public boolean rimuoviOggetto(String nomeOggetto) {
        return oggetti.removeIf(i -> i.getNome().equalsIgnoreCase(nomeOggetto));
    }

    public Oggetto getOggetto(String nomeOggetto) {
//        return oggetti.stream()
//                .filter(i -> i.getNome().equalsIgnoreCase(nomeOggetto)).findFirst().orElse(null);
        for(Oggetto oggettoInventario : oggetti){
            if(oggettoInventario.getNome().toLowerCase().contains(nomeOggetto.toLowerCase())) {
                return oggettoInventario;
            }
        }
        return null;
    }

    public List<Oggetto> getOggetti() {
        return oggetti;
    }

    public void visualizzaInventario() {
        if (oggetti.isEmpty()) {
            System.out.println(">> Inventario vuoto");
        } else {
            System.out.println(">> Inventario:");
            for (Oggetto oggetto : oggetti) {
                System.out.println("- " + oggetto.getNome() + ": " + oggetto.getDescrizione());
            }
        }
    }

    public boolean possiedeOggetto(String nomeOggetto) {
        for (Oggetto oggetto : oggetti) {
            if (oggetto.getNome().toLowerCase().contains(nomeOggetto.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
