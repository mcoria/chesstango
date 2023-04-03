package net.chesstango.arenaui;

import net.chesstango.mbeans.GameDescriptionInitial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class BotsController {

    @Autowired
    private ArenaJMXClient arenaJMXClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/game_retrieve")
    @SendTo("/topic/game_messages")
    public GameDescriptionInitial retrieveGame(final GetGameById payload) {
        System.out.println(String.format("retrieveGame: %s", payload.getGameId()));
        return arenaJMXClient.getGameDescription(payload.getGameId());
    }

}
