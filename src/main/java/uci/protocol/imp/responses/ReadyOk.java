/**
 * 
 */
package uci.protocol.imp.responses;

import uci.protocol.UCIResponseSingle;
import uci.protocol.UCIResponseType;

/**
 * @author Mauricio Coria
 *
 */
public class ReadyOk implements UCIResponseSingle {


	@Override
	public UCIResponseType getType() {
		return UCIResponseType.READYOK;
	}


	@Override
	public String encode() {
		return "readyok";
	}

}
