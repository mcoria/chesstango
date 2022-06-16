package chess.uci.engine;

import chess.uci.protocol.stream.UCIActivePipe;
import chess.uci.protocol.stream.UCIInputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStreamAdapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mauricio Coria
 *
 */
public class Main {
	private final Engine engine;

	private final UCIActivePipe pipe;

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		Main main = new Main(new EngineZonda(executorService), System.in, System.out);
		//Main main = new Main(new EngineProxy(), System.in, System.out);


		main.main(executorService);
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
		executorService.execute(pipe);

		executorService.shutdown();
		try {
			while(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				//System.out.println("Engine still executing");
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}

}
