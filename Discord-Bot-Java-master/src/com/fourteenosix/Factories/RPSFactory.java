package com.fourteenosix.Factories;

import com.fourteenosix.games.Game;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;

import java.util.Set;

public class RPSFactory implements  GameFactory{
    public Game getGame(GatewayDiscordClient gateway, Set<String> players, MessageChannel initial){
        return new com.fourteenosix.games.RPS(gateway, players, initial);
    }
    public int getMinPlayers(){
        return 1;
    };
    public int getMaxPlayers(){
        return 1;
    };
    public String getPrefix(){
        return "rps";
    };
    public boolean isValidNumberOfPlayers(int number){
        return number==1;
    }
}
