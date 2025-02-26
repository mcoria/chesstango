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
    private final Queue<User> botsOnline = new LinkedList<>();

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

        User aBot = pickRandomBot(challenger);
        if (aBot != null) {
            client.challengeUser(aBot, challenger::consumeChallengeBuilder);
        }
    }

    private synchronized User pickRandomBot(Challenger challenger) {
        if (botsOnline.isEmpty()) {
            int myRating = client.getRating(challenger.getRatingType());
            Many<User> bots = client.botsOnline();
            bots.stream().filter(bot -> {
                StatsPerf stats = bot.ratings().get(challenger.getRatingType());
                if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
                    return statsPerfGame.rating() >= myRating - RATING_THRESHOLD && statsPerfGame.rating() <= myRating + RATING_THRESHOLD;
                }
                return false;
            }).forEach(botsOnline::add);
        }
        return botsOnline.poll();
    }

    private interface Challenger {
        StatsPerfType getRatingType();

        void consumeChallengeBuilder(ChallengesApiAuthCommon.ChallengeBuilder challengeBuilder);
    }


    private static class BulletChallengerBot implements Challenger {
        private final List<Consumer<ChallengesApiAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();

        public BulletChallengerBot() {
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBullet2m1s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBullet1m0s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
        }

        @Override
        public StatsPerfType getRatingType() {
            return StatsPerfType.bullet;
        }

        @Override
        public void consumeChallengeBuilder(ChallengesApiAuthCommon.ChallengeBuilder challengeBuilder) {
            Consumer<ChallengesApiAuthCommon.ChallengeBuilder> element = builders.get(rand.nextInt(builders.size()));
            element.accept(challengeBuilder);
        }
    }

    private static class BlitzChallengerBot implements Challenger {
        private final List<Consumer<ChallengesApiAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();

        public BlitzChallengerBot() {
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBlitz5m3s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBlitz3m2s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
        }

        @Override
        public StatsPerfType getRatingType() {
            return StatsPerfType.blitz;
        }

        @Override
        public void consumeChallengeBuilder(ChallengesApiAuthCommon.ChallengeBuilder challengeBuilder) {
            Consumer<ChallengesApiAuthCommon.ChallengeBuilder> element = builders.get(rand.nextInt(builders.size()));
            element.accept(challengeBuilder);
        }
    }


    private static class RapidChallengerBot implements Challenger {
        private final List<Consumer<ChallengesApiAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();

        public RapidChallengerBot() {
            builders.add(challengeBuilder -> challengeBuilder
                    .clockRapid10m5s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
            builders.add(challengeBuilder -> challengeBuilder
                    .clockRapid10m0s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.GameVariant.standard)
                    .rated(true));
        }

        @Override
        public StatsPerfType getRatingType() {
            return StatsPerfType.rapid;
        }

        @Override
        public void consumeChallengeBuilder(ChallengesApiAuthCommon.ChallengeBuilder challengeBuilder) {
            Consumer<ChallengesApiAuthCommon.ChallengeBuilder> element = builders.get(rand.nextInt(builders.size()));
            element.accept(challengeBuilder);
        }
    }

}
