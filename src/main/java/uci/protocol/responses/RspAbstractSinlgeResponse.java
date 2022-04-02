/**
 * 
 */
package uci.protocol.responses;

import uci.protocol.UCIResponseChannel;
import uci.protocol.UCIResponse;

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
