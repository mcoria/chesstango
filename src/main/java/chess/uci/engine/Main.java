package chess.uci.engine;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
		//Main main = new Main(new EngineZonda(), System.in, System.out);
		Main main = new Main(new EngineProxy(), System.in, System.out);

		ExecutorService executorService = Executors.newFixedThreadPool(2);

		main.main(executorService);

		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				//System.out.println("Engine still executing");
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}

	public Main(Engine engine, InputStream in, PrintStream out) {
		this.engine = engine;
		this.engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(out)));

		this.pipe = new UCIActivePipe();
		this.pipe.setInputStream(new UCIInputStreamAdapter(new InputStreamReader(in)));
		this.pipe.setOutputStream(this.engine);
	}


	protected void main(ExecutorService executorService) {
		if(engine instanceof  Runnable){
			executorService.execute((Runnable) engine);
		}
		executorService.execute(this.pipe);
	}

}
