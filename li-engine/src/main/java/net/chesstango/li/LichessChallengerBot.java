package net.chesstango.li;

import chariot.api.ChallengesAuthCommon;
import chariot.model.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public abstract class LichessChallengerBot {
    public static final int RATING_THRESHOLD = 100;
    private final LichessClient client;
    private final Queue<User> botsOnline = new LinkedList<>();
    protected final List<Consumer<ChallengesAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();

    protected abstract StatsPerfType getRatingType();

    public LichessChallengerBot(LichessClient client) {
        this.client = client;
    }

    public void challengeRandomBot() {
        User aBot = pickRandomBot();
        if (aBot != null) {
            client.challengeUser(aBot, this::consumeChallengeBuilder);
        }
    }

    private synchronized User pickRandomBot() {
        if (botsOnline.isEmpty()) {
            int rating = client.getRating(getRatingType());
            Many<User> bots = client.botsOnline(50);
            bots.stream().filter(bot -> {
                StatsPerf stats = bot.ratings().get(getRatingType());
                if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
                    return statsPerfGame.rating() >= rating - RATING_THRESHOLD && statsPerfGame.rating() <= rating + RATING_THRESHOLD;
                }
                return false;
            }).forEach(botsOnline::add);
        }
        return botsOnline.poll();
    }

    private void consumeChallengeBuilder(ChallengesAuthCommon.ChallengeBuilder challengeBuilder) {
        Random rand = new Random();
        Consumer<ChallengesAuthCommon.ChallengeBuilder> element = builders.get(rand.nextInt(builders.size()));
        element.accept(challengeBuilder);
    }


    public static class BulletChallengerBot extends LichessChallengerBot {
        public BulletChallengerBot(LichessClient client) {
            super(client);
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
        protected StatsPerfType getRatingType() {
            return StatsPerfType.bullet;
        }
    }

    public static class BlitzChallengerBot extends LichessChallengerBot {
        public BlitzChallengerBot(LichessClient client) {
            super(client);
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
        protected StatsPerfType getRatingType() {
            return StatsPerfType.blitz;
        }
    }


    public static class RapidChallengerBot extends LichessChallengerBot {
        public RapidChallengerBot(LichessClient client) {
            super(client);
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
        protected StatsPerfType getRatingType() {
            return StatsPerfType.rapid;
        }

    }

}
