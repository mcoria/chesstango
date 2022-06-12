package chess.uci.engine;

import java.io.*;

import chess.uci.protocol.*;

/**
 * @author Mauricio Coria
 *
 */
public class Main {
	private final Engine engine;

	public static void main(String[] args) {
		Main main = new Main(new EngineZonda(), System.out, System.in);
		//Main main = new Main(new EngineProxy(), System.out, System.in);
		main.start();
	}

	public Main(Engine engine, PrintStream out, InputStream in) {
		this.engine = engine;

		this.engine.setInputStream(new UCIInputStreamReader(in)::read);
		this.engine.setOutputStream(new UCIOutputStreamWriter(out)::write);
	}

	protected void start() {
		engine.mainReadRequestLoop();
	}

}
