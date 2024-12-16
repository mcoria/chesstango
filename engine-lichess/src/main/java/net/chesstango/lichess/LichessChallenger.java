package net.chesstango.lichess;

import chariot.api.ChallengesAuthCommon;
import chariot.model.Enums;
import chariot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class LichessChallenger {
    public enum ChallengeType {BULLET, BLITZ, RAPID};

    private static final Logger logger = LoggerFactory.getLogger(LichessChallenger.class);
    private final List<LichessChallengerBot> challengerBotList;
    private final LichessClient client;

    public LichessChallenger(LichessClient client) {
        this.client = client;
        this.challengerBotList = List.of(
                new LichessChallengerBot.BulletChallengerBot(client),
                new LichessChallengerBot.BlitzChallengerBot(client),
                new LichessChallengerBot.RapidChallengerBot(client));
    }


    public void challengeRandomBot() {
        Random rand = new Random();
        LichessChallengerBot lichessChallengerBot = challengerBotList.get(rand.nextInt(challengerBotList.size()));
        lichessChallengerBot.challengeRandomBot();
    }


    public void challengeUser(String username, ChallengeType challengeType) {
        Consumer<ChallengesAuthCommon.ChallengeBuilder> challengeBuilderConsumer = (builder) -> {
            switch (challengeType) {
                case BULLET -> builder.clockBullet2m1s()
                        .color(Enums.ColorPref.random)
                        .variant(Enums.GameVariant.standard)
                        .rated(true);
                case BLITZ -> builder.clockBlitz5m3s()
                        .color(Enums.ColorPref.random)
                        .variant(Enums.GameVariant.standard)
                        .rated(true);
                case RAPID -> builder.clockRapid10m0s()
                        .color(Enums.ColorPref.random)
                        .variant(Enums.GameVariant.standard)
                        .rated(true);
            }
        };

        Optional<User> user = client.findUser(username);

        if (user.isPresent()) {
            client.challengeUser(user.get(), challengeBuilderConsumer);
        } else {
            logger.info("User '{}' not found", username);
        }
    }
}
