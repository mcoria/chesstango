package chess.uci.protocol.responses;

import java.util.ArrayList;
import java.util.List;

import chess.uci.protocol.UCIResponseBase;
import chess.uci.protocol.UCIResponseChannel;

/**
 * @author Mauricio Coria
 *
 */
public class RspMultiline implements UCIResponseBase {

	private final List<UCIResponseBase> responses = new ArrayList<UCIResponseBase>();
	
	public RspMultiline addResponse(UCIResponseBase response){
		responses.add(response);
		return this;
	}
	
	@Override
	public void respond(UCIResponseChannel responseChannel) {
		for (UCIResponseBase response : responses) {
			response.respond(responseChannel);
		}
	}		

}
