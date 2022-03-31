/**
 * 
 */
package uci.protocol.imp.responses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import uci.protocol.UCIResponseMultiple;
import uci.protocol.UCIResponseSingle;

/**
 * @author Mauricio Coria
 *
 */
public class MultilineImp implements UCIResponseMultiple {

	private List<UCIResponseSingle> responses = new ArrayList<UCIResponseSingle>();
	
	public UCIResponseMultiple addResponse(UCIResponseSingle response){
		responses.add(response);
		return this;
	}
	
	@Override
	public Iterator<UCIResponseSingle> iterator() {
		return responses.iterator();
	}

}
