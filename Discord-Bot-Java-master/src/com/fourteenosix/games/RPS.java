package com.fourteenosix.games;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;

import java.util.*;

public class RPS extends Game{
    int gamesPlayed;
    int gamesWon;
    int gamesTied;
    Random rand;
     public RPS (GatewayDiscordClient gateway, Set<String> players, MessageChannel channel){
        super(gateway, players);
        gamesPlayed = 0;
        gamesWon = 0;
        gamesTied = 0;
        rand = new Random();
    }


    public Map<String, Integer> getScores(){
        Map<String, Integer> scores = new HashMap();
        scores.put((String) getPlayers().toArray()[0], this.gamesWon);
        return  scores;
    }
    public void message(MessageCreateEvent message){
        System.out.println("Running on message for RPS");
        String messageBody = message.getMessage().getContent().toLowerCase();

        String[] moves = {"rock", "paper", "scissors"};
        if (messageBody.equals("quit")){
            setFinished(true);
            return;
        }

        boolean isMoveValid = false;
        for (String move: moves){
            if (move.equals(messageBody))
                isMoveValid = true;
        }
        if (!isMoveValid){
            message.getMessage().getChannel().block().createMessage("Sorry, the only valid moves are rock, paper scissors").block();
            return;
        }
        String compMove = moves[rand.nextInt(3)];
        if (compMove.equals(messageBody)){
            message.getMessage().getChannel().block().createMessage("You have tied").block();
            gamesTied++;
            System.out.println("You have tied");
        } else if (messageBody.equals("scissors") && compMove.equals("paper")
                    || messageBody.equals("rock") && compMove.equals("scissors")
                    || messageBody.equals("paper") && compMove.equals("rock")){
            message.getMessage().getChannel().block().createMessage("You have Won with " + messageBody + " vs " + compMove).block();
            System.out.println("You have Won with " + messageBody + " vs " + compMove);
            gamesWon++;
        } else {
            System.out.println("You have lost with " + messageBody + " vs " + compMove);
            message.getMessage().getChannel().block().createMessage("You have lost with " + messageBody + " vs " + compMove).block();
        }

        gamesPlayed++;

    }
    public void reaction(String emoji, String playerId){

    }

}
