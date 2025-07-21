package net.chesstango.lichess;

import chariot.api.ChallengesApiAuthCommon;
import chariot.model.Enums;
import chariot.model.User;
import chariot.model.UserAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public class LichessChallengerUser {
    private static final Logger logger = LoggerFactory.getLogger(LichessChallengerUser.class);

    private final LichessClient client;


    public LichessChallengerUser(LichessClient client) {
        this.client = client;
    }


    public void challengeUser(String username, ChallengeType challengeType) {
        Consumer<ChallengesApiAuthCommon.ChallengeBuilder> challengeBuilderConsumer = (builder) -> {
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

        Optional<UserAuth> user = client.findUser(username);

        if (user.isPresent()) {
            client.challengeUser(user.get(), challengeBuilderConsumer);
        } else {
            logger.info("User '{}' not found", username);
        }
    }
}
