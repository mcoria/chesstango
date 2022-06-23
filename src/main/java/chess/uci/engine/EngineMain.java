package chess.uci.engine;

import chess.uci.engine.imp.EngineZonda;
import chess.uci.protocol.requests.CmdQuit;
import chess.uci.protocol.stream.UCIActivePipe;
import chess.uci.protocol.stream.UCIInputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStreamAdapter;
import chess.uci.protocol.stream.UCIOutputStreamSwitch;

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

	public static void activate(String[] args) {
		EngineMain engineMain = new EngineMain(new EngineZonda(), System.in, System.out);
		//EngineMain engineMain = new EngineMain(new EngineProxy(), System.in, System.out);

		engineMain.open();

		engineMain.waitTermination();
	}


	private ExecutorService executorService = Executors.newFixedThreadPool(2);

	public EngineMain(Engine engine, InputStream in, PrintStream out) {
		this.engine = engine;
		this.engine.setResponseOutputStream(new UCIOutputStreamAdapter(new OutputStreamWriter(out)));

		UCIOutputStreamSwitch actionOutput = new UCIOutputStreamSwitch(uciMessage -> uciMessage instanceof CmdQuit, executorService::shutdown);
		actionOutput.setOutputStream(this.engine);

		this.pipe = new UCIActivePipe();
		this.pipe.setInputStream(new UCIInputStreamAdapter(new InputStreamReader(in)));
		this.pipe.setOutputStream(actionOutput);
	}


	public void open() {
		engine.open();

		executorService.execute(pipe);
		//TODO: no podemos esperar que los threads terminen, de lo contrario impedimos la ejecucion de test unitarios
	}


	public void waitTermination(){
		//TODO: no podemos llamar a shutdown() aca, de lo contrario impedimos que Go se ejecute en Zonda
		//      de momento terminamos en EngineZonda.do_quit()
		try {
			while(!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				//System.out.println("Engine still executing");
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
		}

		engine.close();
	}

}
