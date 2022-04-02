/**
 * 
 */
package uci.protocol.responses;

import java.util.ArrayList;
import java.util.List;

import uci.protocol.UCIResponseBase;
import uci.protocol.UCIResponseChannel;

/**
 * @author Mauricio Coria
 *
 */
public class RspMultiline implements UCIResponseBase {

	private List<UCIResponseBase> responses = new ArrayList<UCIResponseBase>();
	
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
