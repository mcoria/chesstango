package chess.uci.engine;

import java.io.*;

import chess.uci.protocol.stream.UCIActivePipe;
import chess.uci.protocol.stream.UCIInputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStreamAdapter;

/**
 * @author Mauricio Coria
 *
 */
public class Main {
	private final Engine engine;

	private final UCIActivePipe pipe;

	public static void main(String[] args) {
		Main main = new Main(new EngineZonda(), System.in, System.out);
		//Main main = new Main(new EngineProxy(), System.out, System.in);
		main.main();
	}

	public Main(Engine engine, InputStream in, PrintStream out) {
		this.engine = engine;
		this.engine.setOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(out)));

		pipe = new UCIActivePipe();
		this.pipe.setInputStream(new UCIInputStreamAdapter(new InputStreamReader(in)));
		this.pipe.setOutputStream(this.engine);
	}


	protected void main() {
		pipe.activate();
	}

}
