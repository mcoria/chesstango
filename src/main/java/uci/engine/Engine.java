/**
 * 
 */
package uci.engine;

/**
 * @author Mauricio Coria
 *
 */
public class Engine {
	private boolean keepProcessing = true;

	public void do_quit() {
		keepProcessing = false;
	}

	public void do_ping() {
	}


	public void do_stop() {
	}

	public void do_go() {		
	}
	
	public boolean keepProcessing() {
		return keepProcessing;
	}	
}
