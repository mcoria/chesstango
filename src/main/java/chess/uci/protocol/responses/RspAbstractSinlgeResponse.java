/**
 * 
 */
package chess.uci.protocol.responses;

import chess.uci.protocol.UCIResponse;
import chess.uci.protocol.UCIResponseChannel;

/**
 * @author Mauricio Coria
 *
 */
public abstract class RspAbstractSinlgeResponse implements UCIResponse {

	@Override
	public String toString() {
		return encode();
	}
	
	@Override
	public void respond(UCIResponseChannel responseChannel) {
		responseChannel.send(this);
	}	

}
