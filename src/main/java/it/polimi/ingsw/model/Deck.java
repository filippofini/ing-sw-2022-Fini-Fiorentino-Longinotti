package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
       // REMEMBER TO IMPLEMENT

   }
   public void remove_used_card(Assistance_card chosen_remove){
       cards.remove(chosen_remove);
   }


}
