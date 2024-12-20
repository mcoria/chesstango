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

    //private final UserAuth profile;

    public LichessClient(ClientAuth client) {
        this.client = client;
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

    public void gameAbort(String gameId) {
        client.bot().abort(gameId);
    }


    public int getRating(StatsPerfType type) {
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

    public void challengeUser(User user, Consumer<ChallengesApiAuthCommon.ChallengeBuilder> challengeBuilderConsumer) {
        client.challenges().challenge(user.id(), challengeBuilderConsumer);
    }

    public Many<User> botsOnline(int i) {
        return client.bot().botsOnline(i);
    }

    public boolean isMe(UserInfo theUser) {
        return client.account().profile().get().id().equals(theUser.id());
    }

    public Optional<User> findUser(String username) {
        Many<User> users = client.users().byIds(List.of(username));
        return users.stream().findFirst();
    }
}
