package net.chesstango.lichess;

import chariot.ClientAuth;
import chariot.api.ChallengesApiAuthCommon;
import chariot.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessClient {

    private final ClientAuth client;

    public LichessClient(ClientAuth client) {
        this.client = client;
    }
    
    public Stream<Event> streamEvents() {
        return client.bot().connect().stream();
    }

    public Stream<GameStateEvent> streamGameStateEvent(String gameId) {
        return client.bot().connectToGame(gameId).stream();
    }

    public synchronized void challengeUser(User user, Consumer<ChallengesApiAuthCommon.ChallengeBuilder> challengeBuilderConsumer) {
        client.challenges().challenge(user.id(), challengeBuilderConsumer);
    }

    public synchronized void challengeAccept(String challengeId) {
        client.bot().acceptChallenge(challengeId);
    }

    public synchronized void challengeDecline(String challengeId) {
        client.bot().declineChallenge(challengeId);
    }

    public synchronized void gameMove(String gameId, String moveUci) {
        client.bot().move(gameId, moveUci);
    }

    public synchronized void gameResign(String gameId) {
        client.bot().resign(gameId);
    }

    public synchronized void gameChat(String gameId, String message) {
        client.bot().chat(gameId, message);
    }

    public synchronized void gameAbort(String gameId) {
        client.bot().abort(gameId);
    }

    public synchronized int getRating(StatsPerfType type) {
        Map<StatsPerfType, StatsPerf> rating = client.account()
                .profile()
                .get()
                .ratings();
        StatsPerf stats = rating.get(type);
        if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
            return statsPerfGame.rating();
        }
        throw new RuntimeException("Rating not found");
    }

    public synchronized boolean isMe(UserInfo theUser) {
        return client.account().profile().get().id().equals(theUser.id());
    }

    public synchronized Many<User> botsOnline() {
        return client.bot().botsOnline();
    }

    public synchronized Optional<User> findUser(String username) {
        Many<User> users = client.users().byIds(List.of(username));
        return users.stream().findFirst();
    }
}
