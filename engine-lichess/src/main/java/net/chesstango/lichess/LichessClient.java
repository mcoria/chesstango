package net.chesstango.lichess;

import chariot.ClientAuth;
import chariot.api.ChallengesApiAuthCommon;
import chariot.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
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

    public synchronized void challenge(User user, Consumer<ChallengesApiAuthCommon.ChallengeBuilder> challengeBuilderConsumer) {
        client.bot()
                .challenge(user.id(), challengeBuilderConsumer)
                .ifPresentOrElse(challenge -> {
                    log.info("Challenge sent successfully to {}", challenge);
                }, () -> {
                    throw new RuntimeException("Error sending challenge");
                });
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

    public synchronized Map<StatsPerfType, StatsPerf> getRatings() {
        return client.account()
                .profile()
                .get()
                .ratings();
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

    public synchronized Optional<UserAuth> findUser(String username) {
        Many<UserAuth> users = client.users().byIds(List.of(username));
        return users.stream().findFirst();
    }
}
