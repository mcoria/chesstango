package chess.uci.protocol;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIResponseChannel {

	void send(UCIResponse response);

}
