package chess.uci.engine;

import java.io.*;
import java.util.Scanner;

import chess.uci.protocol.*;

/**
 * @author Mauricio Coria
 *
 */
public class Main {
	private final UCIDecoder uciDecoder = new UCIDecoder();

	private final Engine engine;

	private final PrintStream out;
	private final BufferedReader reader;

	public static void main(String[] args) {
		Main main = new Main(new EngineZonda(), System.out, System.in);
		main.start();
	}

	public Main(Engine engine, PrintStream out, InputStream in) {
		this.engine = engine;
		this.out = out;
		this.reader =  new BufferedReader(new InputStreamReader(in));
		this.engine.setInputStream(new UCIInputStream() {
			@Override
			public UCIMessage read() {
				return readMessage();
			}
		});
		this.engine.setOutputStream(new UCIOutputStream() {
			@Override
			public void write(UCIMessage message) {
				writeMessage(message);
			}
		});
	}

	protected void start() {
		engine.main();
	}

	protected UCIMessage readMessage() {
		try {
			return uciDecoder.parseMessage(reader.readLine());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeMessage(UCIMessage message) {
		out.println(message);
	}

}
