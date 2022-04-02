/**
 * 
 */
package uci.protocol;

/**
 * @author Mauricio Coria
 *
 */
public interface UCIResponse extends UCIResponseBase {
	
	UCIResponseType getType();
	
	String encode();

}
