package com.fourteenosix.games;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public  abstract class Game {
    private boolean finished;
    private Set<String> players;
    private GatewayDiscordClient gateway;

    public  Game(GatewayDiscordClient gateway, Set<String> players){
        finished = false;
        this.players = players;
    }


    public Set<String> getPlayers() {
        return players;
    }

    public GatewayDiscordClient getGateway() {
        return gateway;
    }

    protected void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isGameFinished(){
        return  finished;
    }

    abstract public void message(MessageCreateEvent message);
    abstract public void reaction(String emoji, String playerId);
    abstract public Map<String, Integer> getScores();

}
