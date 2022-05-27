package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.client.Client;

import java.util.List;

public class EndGameCLI {
    public static void displayResults(Client client, List<Island> islands, List<Player> players, Board[] boards){
            int winnerID=-1;
            int winnertowers=0;
            int temptowers=0;
            int tempprof=0;
            for(int i=0;i<players.size();i++){
                for(int j=0;j<islands.size();j++){
                        if(islands.get(j).getPlayer_controller()==i){
                            temptowers+=islands.get(j).getTower();
                        }

                }
                if(temptowers>winnertowers){
                    winnerID=i;
                    winnertowers=temptowers;
                }
                else if(temptowers==winnertowers && winnerID!=-1){
                    int countprof1=0;
                    int countprof2=0;
                    for(int k=0;k<5;k++){
                        if(boards[i].getArrProfessors()[k]==true){
                            countprof1++;
                        }
                        if(boards[winnerID].getArrProfessors()[k]==true){
                            countprof2++;
                        }
                    }
                    if(countprof1>countprof2){
                        winnerID=i;
                    }
                }
                temptowers=0;

            }
            if(players.size()==2 || players.size()==3){
                System.out.println("the winner is: "+players.get(winnerID).getName()+" with a number of "+winnertowers+" towers!\n");

            }
            else if(players.size()==4){
                if(winnerID==0 || winnerID ==1){
                    System.out.println("the winners are: "+players.get(0).getName()+" and "+players.get(1).getName()+" with a number of "+winnertowers+" towers!\n");
                }
                else if(winnerID==2 || winnerID ==3){
                    System.out.println("the winners are: "+players.get(2).getName()+" and "+players.get(3).getName()+" with a number of "+winnertowers+" towers!\n");
                }

            }
            for(int i=0;i<players.size();i++){
                for(int j=0;j<islands.size();j++){
                    if(islands.get(j).getPlayer_controller()==i){
                        temptowers+=islands.get(j).getTower();
                    }

                }
                for(int k=0;k<5;k++) {
                    if (boards[i].getArrProfessors()[k] == true) {
                        tempprof++;
                    }
                }
                System.out.println(players.get(i).getName()+":\n");
                System.out.println("towers:"+temptowers+"\n");
                System.out.println("professors:"+tempprof+"\n");
            }

    }

}
