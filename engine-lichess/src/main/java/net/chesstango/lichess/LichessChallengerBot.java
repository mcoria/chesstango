package net.chesstango.lichess;

import chariot.api.ChallengesApiAuthCommon;
import chariot.model.Enums;
import chariot.model.StatsPerf;
import chariot.model.StatsPerfType;
import chariot.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class LichessChallengerBot {
    public static final int RATING_THRESHOLD = 150;

    private static final Random rand = new Random();

    private final LichessClient client;

    private final List<Challenger> challengerBotList;

    public LichessChallengerBot(LichessClient client) {
        this.client = client;
        this.challengerBotList = List.of(
                new BulletChallengerBot(),
                new BlitzChallengerBot(),
                new RapidChallengerBot()
        );
    }

    public void challengeRandomBot() {
        Challenger challenger = challengerBotList.get(rand.nextInt(challengerBotList.size()));
        User aBot = challenger.pickRandomBot();
        if (aBot != null) {
            client.challenge(aBot, challenger::consumeChallengeBuilder);
        } else {
            log.warn("No bots online :S");
        }
    }

    void loadOnlineBots() {
        Map<StatsPerfType, StatsPerf> myRatings = client.getRatings();
        challengerBotList.forEach(challenger -> challenger.setRating(myRatings));

        client.botsOnline()
                .stream()
                .forEach(bot -> challengerBotList.forEach(challenger -> challenger.filer(bot)));
    }

    private abstract class Challenger {
        final Queue<User> botsOnline = new LinkedList<>();

        final List<Consumer<ChallengesApiAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();

        int myRating;

        abstract StatsPerfType getRatingType();

        synchronized User pickRandomBot() {
            if (botsOnline.isEmpty()) {
                loadOnlineBots();
                log.info("Bots {} online: {}", getRatingType(), botsOnline.size());
            }
            return botsOnline.poll();
        }

        void setRating(Map<StatsPerfType, StatsPerf> myRatings) {
            myRating = getRating(myRatings);
        }

        void filer(User bot) {
            int botRating = getRating(bot.ratings());
            if (botRating >= myRating - RATING_THRESHOLD && botRating <= myRating + RATING_THRESHOLD) {
                botsOnline.add(bot);
            }
        }

        int getRating(Map<StatsPerfType, StatsPerf> ratings) {
            StatsPerf stats = ratings.get(getRatingType());
            if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
                return statsPerfGame.rating();
            }
            return 0;
        }


        void consumeChallengeBuilder(ChallengesApiAuthCommon.ChallengeBuilder challengeBuilder) {
            Consumer<ChallengesApiAuthCommon.ChallengeBuilder> element = builders.get(rand.nextInt(builders.size()));
            element.accept(challengeBuilder);
        }

    }


    private class BulletChallengerBot extends Challenger {
        public BulletChallengerBot() {

            // Genera demasiado trafico y nos desconecta del server
//            builders.add(challengeBuilder -> challengeBuilder
//                    .clockBullet1m0s()
//                    .color(Enums.ColorPref.random)
//                    .variant(Enums.GameVariant.standard)
//                    .rated(true));

            builders.add(challengeBuilder -> challengeBuilder
                    .clockBullet2m1s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
        }

        @Override
        public StatsPerfType getRatingType() {
            return StatsPerfType.bullet;
        }
    }

    private class BlitzChallengerBot extends Challenger {
        public BlitzChallengerBot() {
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBlitz3m2s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBlitz5m3s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
        }

        @Override
        public StatsPerfType getRatingType() {
            return StatsPerfType.blitz;
        }
    }


    private class RapidChallengerBot extends Challenger {
        public RapidChallengerBot() {
            builders.add(challengeBuilder -> challengeBuilder
                    .clockRapid10m0s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
            builders.add(challengeBuilder -> challengeBuilder
                    .clockRapid10m5s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
        }

        @Override
        public StatsPerfType getRatingType() {
            return StatsPerfType.rapid;
        }
    }

}
