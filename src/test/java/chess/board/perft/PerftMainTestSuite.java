package chess.board.perft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.factory.ChessFactory;
import chess.board.fen.FENDecoder;
import chess.board.perft.imp.PerftBrute;

/**
 * @author Mauricio Coria
 *
 */
public class PerftMainTestSuite {

	public static void main(String[] args) {
		execute("main/ferdy_perft_double_checks.epd");
		execute("main/ferdy_perft_enpassant_1.epd");
		
		execute("main/ferdy_perft_single_check_1.epd");
		execute("main/ferdy_perft_single_check_2.epd");
		execute("main/ferdy_perft_single_check_3.epd");
		execute("main/ferdy_perft_single_check_4.epd");
		execute("main/ferdy_perft_single_check_5.epd");
		execute("main/ferdy_perft_single_check_6.epd");
		execute("main/ferdy_perft_single_check_7.epd");
		execute("main/ferdy_perft_single_check_8.epd");
		execute("main/ferdy_perft_single_check_9.epd");
		execute("main/ferdy_perft_single_check_10.epd");
		execute("main/ferdy_perft_single_check_11.epd");
		execute("main/ferdy_perft_single_check_12.epd");
		execute("main/ferdy_perft_single_check_13.epd");
		execute("main/ferdy_perft_single_check_14.epd");
		execute("main/ferdy_perft_single_check_15.epd");
		execute("main/ferdy_perft_single_check_16.epd");
		execute("main/ferdy_perft_single_check_17.epd");
		execute("main/ferdy_perft_single_check_18.epd");
		execute("main/ferdy_perft_single_check_19.epd");
		
		execute("main/ferdy_perft_double_checks.epd");
		execute("main/perft-marcel.epd");
		execute("main/perft.epd");
		
		
		execute("main/perftsuite1.txt");
		execute("main/perftsuite2.txt");
		execute("main/perftsuite3.txt");
	}
	
	private static void execute(String filename){
		try {
			System.out.println("Starting suite " + filename);
			
			List<String> failedSuites = new ArrayList<String>();
			
			PerftMainTestSuite suite = new PerftMainTestSuite();
			
			InputStream instr = suite.getClass().getClassLoader().getResourceAsStream(filename);

			// reading the files with buffered reader
			InputStreamReader strrd = new InputStreamReader(instr);

			BufferedReader rr = new BufferedReader(strrd);

			String line;

			// outputting each line of the file.
			while ((line = rr.readLine()) != null) {
				if(!line.startsWith("#")){
					if(suite.run(line) == false){
						failedSuites.add(line);
					}
				}
			}
			
			System.out.println("Suite summary " + filename);
			if(failedSuites.isEmpty()){
				System.out.println("\t all tests exceute sucessfully");	
			} else {
				for(String suiteStr: failedSuites){
					System.out.println("\t test failed: " + suiteStr);
				}
			}
			System.out.println("=================");

			
		} catch (IOException e) {
			System.out.println(e);
		}		
	}
	
	private final ChessFactory chessFactory;
	
	public PerftMainTestSuite() {
		this(new ChessFactory());
	}
	
	public PerftMainTestSuite(ChessFactory chessFactory) {
		this.chessFactory =  chessFactory;
	}

	protected String fen;
	protected int[] perftResults;
	private int startNivel;
	

	protected boolean run(String perfTest) {
		boolean retunResult = true;
		parseTests(perfTest);
		
		System.out.println("Testing FEN: " + this.fen);
		for(int i = 0; i < perftResults.length; i++){
			
			PerftBrute main = new PerftBrute();
			
			PerftResult result = main.start(getGame(), this.startNivel + i);
			
			if(result.getTotalNodes() == perftResults[i]){
				System.out.println("depth " + (this.startNivel + i) + " OK" );
			} else {
				System.out.println("depth " + (this.startNivel + i) + " FAIL, expected = " + perftResults[i] + ", actual = " + result.getTotalNodes());
				retunResult = false;
				break;
			}
			
		}
		System.out.println("=============");
		return retunResult;
	}

	protected void parseTests(String tests) {
		String[] splitStrings = tests.split(";");
		
		this.fen = splitStrings[0].trim();
		this.perftResults = new int[splitStrings.length - 1];
		this.startNivel = 0;
		
		for (int i = 1; i < splitStrings.length; i++) {
			String[] perftResultStr = splitStrings[i].trim().split(" ");
			if (this.startNivel == 0) {
				this.startNivel = Integer.parseInt(perftResultStr[0].substring(1));
			}
			perftResults[i - 1] = Integer.parseInt(perftResultStr[1]);
		}
		
	}
	
	private Game getGame() {		
		GameBuilder builder = new GameBuilder(chessFactory);

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(this.fen);
		
		return builder.getResult();
	}		

}
