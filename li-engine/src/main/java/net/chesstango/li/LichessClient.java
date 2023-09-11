package net.chesstango.li;

import chariot.ClientAuth;
import chariot.model.*;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessClient {

    private final ClientAuth client;
    private int myBlitzRating;
    //private final UserAuth profile;

    public LichessClient(ClientAuth client) {
        this.client = client;
        this.myBlitzRating = getBlitzRating();
    }

    private int getBlitzRating() {
        Map<StatsPerfType, StatsPerf> rating = client.account().profile().get().ratings();
        StatsPerf stats = rating.get(StatsPerfType.blitz);
        if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
            return statsPerfGame.rating();
        }
        throw new RuntimeException("Rating not found");
    }

    public Stream<Event> streamEvents() {
        return client.bot().connect().stream();
    }

    public void challengeAccept(String challengeId) {
        client.bot().acceptChallenge(challengeId);
    }

    public void challengeDecline(String challengeId) {
        client.bot().declineChallenge(challengeId);
    }

    public Stream<GameStateEvent> gameStreamEvents(String gameId) {
        return client.bot().connectToGame(gameId).stream();
    }

    public void gameMove(String gameId, String moveUci) {
        client.bot().move(gameId, moveUci);
    }

    public void gameResign(String gameId) {
        client.bot().resign(gameId);
    }

    public void gameChat(String gameId, String message) {
        client.bot().chat(gameId, message);
    }

    public void challengeRandomBot() {
        User aBot = pickRandomBot();

        if (aBot != null) {
            One<Challenge> challenge = client.challenges().challenge(aBot.id(),
                    challengeBuilder ->
                            challengeBuilder
                                    .clockBlitz5m3s()
                                    .color(Enums.ColorPref.random)
                                    .variant(Enums.VariantName.standard)
                                    .rated(true)
            );
        }

    }

    private Queue<User> botsOnline = new LinkedList<>();

    private synchronized User pickRandomBot() {
        if (botsOnline.isEmpty()) {
            Many<User> bots = client.bot().botsOnline(50);
            bots.stream().filter(bot -> {
                StatsPerf stats = bot.ratings().get(StatsPerfType.blitz);
                if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
                    return statsPerfGame.rating() >= myBlitzRating - 100 && statsPerfGame.rating() <= myBlitzRating + 300;
                }
                return false;
            }).forEach(botsOnline::add);
        }
        return botsOnline.poll();
    }

}
