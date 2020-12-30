package com.fourteenosix;

import com.fourteenosix.Factories.GameFactory;
import com.fourteenosix.games.Game;
import com.fourteenosix.games.RPS;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.ReactionAddEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Bot {
    private String token;
    private String channelToRespondIn;
    private DiscordClient client;
    private GatewayDiscordClient gateway;
    private Map<String, Map<String,Game>> games;
    private  Map<String,String> names;
    private Map<String, GameFactory> gameClasses;

    public Bot(String token, String channelToRespondIn){
        this.token = token;
        this.channelToRespondIn = channelToRespondIn;
        games = new HashMap<>();
        names = new HashMap<>();
        gameClasses = new HashMap<>();
        try{
            this.client = DiscordClient.create(token);
        } catch (NumberFormatException e){
            System.err.println("Could not login with the token.");
        }

    }

    public Bot(String token){
        this(token, "");
    }
    public boolean isReady(){
        return this.client != null;
    }

    public void addGame(GameFactory f){
        gameClasses.put(f.getPrefix(),f);

    }

    public void start(){
        gateway = client.login().block();
        gateway.on(MessageCreateEvent.class).subscribe(this::onMessage);
        gateway.on(ReactionAddEvent.class).subscribe(this::onReaction);
    }

    public GatewayDiscordClient getGateway() {
        return gateway;
    }

    public void waitForDisconnect(){
        if (gateway != null)
            gateway.onDisconnect().block();
    }

    private Game getGameToPlay(String message, Set <String> players, MessageCreateEvent event){
        if (message.contains(" ")){
            message = message.substring(message.indexOf(" "));
        }
        if (!gameClasses.containsKey(message)) {
            return null;
        }
        GameFactory found = gameClasses.get(message);
        if (!found.isValidNumberOfPlayers(players.size())){
            event.getMessage().getChannel().block().createMessage("Invalid number of players").block();
        }
        return found.getGame(this.getGateway(), players, event.getMessage().getChannel().block());
    }

    private void onMessage(MessageCreateEvent event){
        String userId = event.getMessage().getAuthor().get().getId().asString();
        String channelId = event.getMessage().getChannel().block().getId().asString();
        String message = event.getMessage().getContent().toLowerCase();
        names.put(userId, event.getMessage().getAuthor().get().getUsername());
        if (!channelId.equals(channelToRespondIn)
                && !channelToRespondIn.equals(""))
            return;
        else if (userId.equals(gateway.getSelf().block().getId().asString())){
            return;
        }
        //{"chan_id":{"user_id":game}}
        if (!games.containsKey(channelId)){
            games.put(channelId, new HashMap<>());
        }
        if (!games.get(channelId).containsKey(userId)){
            List<String> players = new ArrayList<>();
            players.add(userId);
            Set<String> gamePlayers = new HashSet<>();
            gamePlayers.add(userId);

            for (Snowflake mention: event.getMessage().getUserMentionIds()){
                gamePlayers.add(mention.asString());
            }

            Game gameToPlay = getGameToPlay(message, gamePlayers, event);
            if (gameToPlay == null) {
                event.getMessage().getChannel().block().createMessage("Could not find the game, or the number of players was incorrect").block();
                return;
            }
            event.getMessage().getChannel().block().createMessage("Started New Game").block();
            for (String userIdPlaying : gamePlayers){
                    games.get(channelId).put(userIdPlaying, gameToPlay);
                }

            return;
        }

        System.out.println(event.getMessage().getAuthor().get().getUsername() +
                " has said: " +
                event.getMessage().getContent());

        Game beingPlayed = games.get(channelId).get(userId);

        beingPlayed.message(event);
        if (beingPlayed.isGameFinished()){
            System.out.println("Game has been marked as finished");
            String scores = "";
            event.getMessage().getChannel().block().createMessage("The game has finished").block();
            for (String user:beingPlayed.getScores().keySet()){
                event.getMessage().getChannel().block().createMessage(names.get(user)+ " has a score of " + beingPlayed.getScores().get(user)).block();
            }
        }


    }
    private void onReaction(ReactionAddEvent event){
        if (event.getChannel().block().getId().asString().equals(channelToRespondIn)
                && !channelToRespondIn.equals(""))
            return;
        System.out.println("Reaction added: " + event.getEmoji());

    }
}
