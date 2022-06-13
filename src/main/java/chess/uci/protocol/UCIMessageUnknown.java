package chess.uci.protocol;

import static chess.uci.protocol.UCIMessage.MessageType.Unknown;

/**
 * @author Mauricio Coria
 *
 */
public class UCIMessageUnknown implements UCIMessage {

	private final String line;

	public UCIMessageUnknown(String line) {
		this.line = line;
	}

	@Override
	public MessageType getMessageType() {
		return Unknown;
	}

	@Override
	public void execute(UCIMessageExecutor executor) {

	}

	@Override
	public String toString() {
		return line;
	}
}
