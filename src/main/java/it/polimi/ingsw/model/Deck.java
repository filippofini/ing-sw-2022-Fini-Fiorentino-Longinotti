package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Deck {
    private List<Assistance_card> cards;

   public Deck(){
       cards = new ArrayList<Assistance_card>(Arrays.asList(
               Assistance_card.TORTOISE,
               Assistance_card.ELEPHANT,
               Assistance_card.BULLDOG,
               Assistance_card.OCTOPUS,
               Assistance_card.LIZARD,
               Assistance_card.FOX,
               Assistance_card.EAGLE,
               Assistance_card.CAT,
               Assistance_card.OSTRICH,
               Assistance_card.LION));

   }
   public void remove_used_card(Assistance_card chosen_remove){
       cards.remove(chosen_remove);
   }

    public int count_elements(){
       return cards.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Objects.equals(cards, deck.cards);
    }

}
