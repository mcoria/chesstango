package net.chesstango.arenaui;

import net.chesstango.mbeans.GameDescriptionCurrent;
import net.chesstango.mbeans.GameDescriptionInitial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * @author Mauricio Coria
 */
@Controller
public class WebSocketController {

    @Autowired
    private ArenaJMXClient arenaJMXClient;


    @MessageMapping("/retrieve_game")
    @SendTo("/topic/current_game")
    public JsonResponse retrieveGame() {
        String currentGameId = arenaJMXClient.getCurrentGameId();

        return new JsonResponse(arenaJMXClient.getGameDescriptionInitial(currentGameId), arenaJMXClient.getGameDescriptionCurrent(currentGameId));
    }

    public static class JsonResponse{

        private final GameDescriptionInitial gameDescriptionInitial;

        private final GameDescriptionCurrent gameDescriptionCurrent;

        public JsonResponse(GameDescriptionInitial gameDescriptionInitial, GameDescriptionCurrent gameDescriptionCurrent) {
            this.gameDescriptionInitial = gameDescriptionInitial;
            this.gameDescriptionCurrent = gameDescriptionCurrent;
        }

        public GameDescriptionInitial getGameDescriptionInitial() {
            return gameDescriptionInitial;
        }

        public GameDescriptionCurrent getGameDescriptionCurrent() {
            return gameDescriptionCurrent;
        }
    }

    public void setArenaJMXClient(ArenaJMXClient arenaJMXClient) {
        this.arenaJMXClient = arenaJMXClient;
    }

}
