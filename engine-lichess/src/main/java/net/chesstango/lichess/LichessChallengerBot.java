package net.chesstango.lichess;

import chariot.api.ChallengesApiAuthCommon;
import chariot.model.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
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
            client.challengeUser(aBot, challenger::consumeChallengeBuilder);
        }
    }

    private abstract class Challenger {
        final Queue<User> botsOnline = new LinkedList<>();
        final List<Consumer<ChallengesApiAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();

        abstract StatsPerfType getRatingType();

        synchronized User pickRandomBot() {
            if (botsOnline.isEmpty()) {
                int myRating = client.getRating(getRatingType());
                Many<User> bots = client.botsOnline();
                bots.stream().filter(bot -> {
                    StatsPerf stats = bot.ratings().get(getRatingType());
                    if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
                        return statsPerfGame.rating() >= myRating - RATING_THRESHOLD && statsPerfGame.rating() <= myRating + RATING_THRESHOLD;
                    }
                    return false;
                }).forEach(botsOnline::add);
            }
            return botsOnline.poll();
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
