package it.polimi.ingsw.model;

/**
 * This abstract class represents the character cards.
 * There are 12 character cards, all of them with different effect and some with different costs.
 * At the beginning of the game 3 cards are drawn and put in the game table. Every time a card is used,
 * its cost is increased by 1.
 */
public abstract class Character_card {
     int cost;
     int uses = 0;

     //TODO: IMPLEMENT EFFECTS OF ALL THE CARDS

     /**
      * This method is the general method to represent the different effects of all the cards.
      * This method is overwritten by all the cards.
      * @param game_state The game state.
      */
     public void effect(Game_State game_state){}

     /**
      * This method is used when a card has been used.
      * It increases the uses and the cost by 1.
      */
     public void setUses() {
          this.uses++;
          setCost(cost+1);
     }

     /**
      * This method returns the cost.
      * @return The cost.
      */
     public int getCost() {
          return cost;
     }

     /**
      * This method sets the cost.
      * @param cost The new cost of the card.
      */
     public void setCost(int cost) {
          this.cost = cost;
     }

     /**
      * This method returns the uses of the card.
      * @return The uses of the card.
      */
     public int getUses() {
          return uses;
     }
}