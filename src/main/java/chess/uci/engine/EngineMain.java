package chess.uci.engine;

import chess.uci.engine.imp.EngineZonda;
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
public class EngineMain {
	private final Engine engine;

	private final UCIActivePipe pipe;

	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(2);

		EngineMain engineMain = new EngineMain(new EngineZonda(), System.in, System.out);
		//Main main = new Main(new EngineProxy(), System.in, System.out);


		engineMain.main(executorService);

		//TODO: no podemos llamar a shutdown() aca, de lo contrario impedimos que Go se ejecute en Zonda
		//      de momento terminamos en EngineZonda.do_quit()
		try {
			while(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				//System.out.println("Engine still executing");
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}
	}


	public EngineMain(Engine engine, InputStream in, PrintStream out) {
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

		//TODO: no podemos esperar que los threads terminen, de lo contrario impedimos la ejecucion de test unitarios
	}

}
