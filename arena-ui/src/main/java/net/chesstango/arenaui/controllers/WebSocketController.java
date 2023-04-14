package net.chesstango.arenaui.controllers;

import net.chesstango.arenaui.services.ArenaMBeanClient;
import net.chesstango.mbeans.GameDescriptionCurrent;
import net.chesstango.mbeans.GameDescriptionInitial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
@Controller
public class WebSocketController {

    @Autowired
    private ArenaMBeanClient arenaMBeanClient;

    @MessageMapping("/select_game")
    @SendTo("/topic/current_game")
    public JsonResponse selectGame(Map<String, Object> request) {
        JsonResponse response = null;
        if(request.containsKey("game")) {
            arenaMBeanClient.selectProxy( (String) request.get("game"));

            String currentGameId = arenaMBeanClient.getCurrentGameId();

            response = new JsonResponse(arenaMBeanClient.getGameDescriptionInitial(currentGameId), arenaMBeanClient.getGameDescriptionCurrent(currentGameId));
        }

        return response;
    }

    @MessageMapping("/retrieve_games")
    @SendTo("/topic/list_games")
    public List<String> retrieveGames() {
        List<String> games = arenaMBeanClient.getMBeanNames();
        return games;
    }


    @MessageMapping("/retrieve_game")
    @SendTo("/topic/current_game")
    public JsonResponse retrieveGame() {
        String currentGameId = arenaMBeanClient.getCurrentGameId();

        return new JsonResponse(arenaMBeanClient.getGameDescriptionInitial(currentGameId), arenaMBeanClient.getGameDescriptionCurrent(currentGameId));
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

    public void setArenaJMXClient(ArenaMBeanClient arenaMBeanClient) {
        this.arenaMBeanClient = arenaMBeanClient;
    }

}
