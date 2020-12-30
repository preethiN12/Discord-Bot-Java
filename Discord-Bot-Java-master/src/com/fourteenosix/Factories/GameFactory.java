package com.fourteenosix.Factories;

import com.fourteenosix.games.Game;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.TextChannel;

import java.util.List;
import java.util.Set;

public interface GameFactory {
    public Game getGame(GatewayDiscordClient gateway, Set<String> players, MessageChannel channel);
    public int getMinPlayers();
    public int getMaxPlayers();
    public String getPrefix();
    public  boolean isValidNumberOfPlayers(int number);
}
