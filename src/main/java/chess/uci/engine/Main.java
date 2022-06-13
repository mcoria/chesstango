package chess.uci.engine;

import java.io.*;

import chess.uci.protocol.stream.UCIInputStreamReader;
import chess.uci.protocol.stream.UCIOutputStreamWriter;

/**
 * @author Mauricio Coria
 *
 */
public class Main {
	private final Engine engine;

	public static void main(String[] args) {
		Main main = new Main(new EngineZonda(), System.in, System.out);
		//Main main = new Main(new EngineProxy(), System.out, System.in);
		main.main();
	}

	public Main(Engine engine, InputStream in, PrintStream out) {
		this.engine = engine;
		this.engine.setOutputStream(new UCIOutputStreamWriter(new OutputStreamWriter(out)));
		this.engine.setInputStream(new UCIInputStreamReader(new InputStreamReader(in)));
	}

	protected void main() {
		engine.main();
	}

}
