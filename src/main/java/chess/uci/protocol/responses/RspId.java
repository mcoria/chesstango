package chess.uci.protocol.responses;

import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.UCIResponse;
import chess.uci.ui.EngineClientResponseListener;

/**
 * @author Mauricio Coria
 *
 */
public class RspId implements UCIResponse {

	public enum RspIdType{NAME, AUTHOR};

	private final RspIdType type;

	private final String text;

	public RspId(RspIdType type, String text) {
		this.type = type;
		this.text = text;
	}

	public RspIdType getIdType() {
		return type;
	}

	public String getText() {
		return text;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.Response;
	}

	@Override
	public void execute(UCIMessageExecutor executor) {
		executor.receive_id(this);
	}

	@Override
	public UCIResponseType getResponseType() {
		return UCIResponseType.ID;
	}


	@Override
	public String toString() {
		return "id " +  (RspIdType.AUTHOR.equals(type) ? "author " : "name ") + getText();
	}

}
