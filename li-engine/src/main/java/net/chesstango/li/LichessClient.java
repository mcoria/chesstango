package net.chesstango.li;

import chariot.ClientAuth;
import chariot.model.Event;
import chariot.model.GameStateEvent;

import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessClient {

	private final ClientAuth client;
	//private final UserAuth profile;

	public LichessClient(ClientAuth client) {
		this.client = client;
		//this.profile = client.account().profile().get();
	}

	public Stream<Event> streamEvents() {
		return client.bot().connect().stream();
	}

	public void challengeAccept(String challengeId) {
		client.bot().acceptChallenge(challengeId);
	}

	public void challengeDecline(String challengeId) {
		client.bot().declineChallenge(challengeId);
	}

	public Stream<GameStateEvent> gameStreamEvents(String gameId) {
		return client.bot().connectToGame(gameId).stream();
	}

	public void gameMove(String gameId, String moveUci) {
		client.bot().move(gameId, moveUci);
	}

	public void gameResign(String gameId) {
		client.bot().resign(gameId);
	}

	public void gameChat(String gameId, String message) {
		client.bot().chat(gameId, message);
	}
}
