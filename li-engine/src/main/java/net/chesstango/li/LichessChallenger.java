package net.chesstango.li;

import chariot.api.ChallengesAuthCommon;
import chariot.model.*;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Mauricio Coria
 */
public abstract class LichessChallenger {
    private final Queue<User> botsOnline = new LinkedList<>();
    private final LichessClient client;
    protected final List<Consumer<ChallengesAuthCommon.ChallengeBuilder>> builders = new ArrayList<>();
    protected final Random rand = new Random();

    protected abstract StatsPerfType getRatingType();

    public LichessChallenger(LichessClient client) {
        this.client = client;
    }

    public void challengeRandomBot() {
        User aBot = pickRandomBot();
        if (aBot != null) {
            client.challengeBot(aBot, this::getBuilder);
        }
    }

    protected void getBuilder(ChallengesAuthCommon.ChallengeBuilder challengeBuilder) {
        Consumer<ChallengesAuthCommon.ChallengeBuilder> element = builders.get(rand.nextInt(builders.size()));
        element.accept(challengeBuilder);
    }

    private synchronized User pickRandomBot() {
        if (botsOnline.isEmpty()) {
            int rating = client.getRating(getRatingType());
            Many<User> bots = client.botsOnline(50);
            bots.stream().filter(bot -> {
                StatsPerf stats = bot.ratings().get(getRatingType());
                if (stats instanceof StatsPerf.StatsPerfGame statsPerfGame) {
                    return statsPerfGame.rating() >= rating - 100 && statsPerfGame.rating() <= rating + 300;
                }
                return false;
            }).forEach(botsOnline::add);
        }
        return botsOnline.poll();
    }


    public static class BulletChallenger extends LichessChallenger {
        public BulletChallenger(LichessClient client) {
            super(client);
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBullet2m1s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.VariantName.standard)
                    .rated(true));
        }

        @Override
        protected StatsPerfType getRatingType() {
            return StatsPerfType.bullet;
        }
    }

    public static class BlitzChallenger extends LichessChallenger {
        public BlitzChallenger(LichessClient client) {
            super(client);
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBlitz3m2s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.VariantName.standard)
                    .rated(true));
            builders.add(challengeBuilder -> challengeBuilder
                    .clockBlitz5m3s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.VariantName.standard)
                    .rated(true));
        }

        @Override
        protected StatsPerfType getRatingType() {
            return StatsPerfType.blitz;
        }
    }


    public static class RapidChallenger extends LichessChallenger {
        public RapidChallenger(LichessClient client) {
            super(client);
            builders.add(challengeBuilder -> challengeBuilder
                    .clockRapid10m0s()
                    .color(Enums.ColorPref.random)
                    .variant(Enums.VariantName.standard)
                    .rated(true));
        }

        @Override
        protected StatsPerfType getRatingType() {
            return StatsPerfType.rapid;
        }

    }

}
