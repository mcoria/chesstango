package net.chesstango.lichess;

/**
 * @author Mauricio Coria
 */
public class LichessChallenger {

    private final LichessChallengerBot lichessChallengerBot;
    private final LichessChallengerUser lichessChallengerUser;

    public LichessChallenger(LichessClient client) {
        lichessChallengerBot = new LichessChallengerBot(client);
        lichessChallengerUser = new LichessChallengerUser(client);
    }


    public void challengeRandomBot() {
        lichessChallengerBot.challengeRandomBot();
    }


    public void challengeUser(String username, ChallengeType challengeType) {
        lichessChallengerUser.challengeUser(username, challengeType);
    }
}
