package chess.board.perft;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.factory.ChessFactory;
import chess.board.representations.fen.FENDecoder;
import chess.board.perft.imp.PerftBrute;

/**
 * @author Mauricio Coria
 *
 */
public class PerftMainTestSuite {

	public static void main(String[] args) {
		try (PrintStream out = new PrintStream(
				new FileOutputStream("./PerftMainTestSuiteResult.txt", false))) {

//			execute("main/ferdy_perft_double_checks.epd", out);
//			execute("main/ferdy_perft_enpassant_1.epd", out);
//
//			execute("main/ferdy_perft_single_check_1.epd", out);
//			execute("main/ferdy_perft_single_check_2.epd", out);
//			execute("main/ferdy_perft_single_check_3.epd", out);
//			execute("main/ferdy_perft_single_check_4.epd", out);
//			execute("main/ferdy_perft_single_check_5.epd", out);
//			execute("main/ferdy_perft_single_check_6.epd", out);
//			execute("main/ferdy_perft_single_check_7.epd", out);
//			execute("main/ferdy_perft_single_check_8.epd", out);
//			execute("main/ferdy_perft_single_check_9.epd", out);
//			execute("main/ferdy_perft_single_check_10.epd", out);
//			execute("main/ferdy_perft_single_check_11.epd", out);
//			execute("main/ferdy_perft_single_check_12.epd", out);
//			execute("main/ferdy_perft_single_check_13.epd", out);
//			execute("main/ferdy_perft_single_check_14.epd", out);
//			execute("main/ferdy_perft_single_check_15.epd", out);
//			execute("main/ferdy_perft_single_check_16.epd", out);
//			execute("main/ferdy_perft_single_check_17.epd", out);
//			execute("main/ferdy_perft_single_check_18.epd", out);
//			execute("main/ferdy_perft_single_check_19.epd", out);
//
			execute("main/ferdy_perft_double_checks.epd", out);
			execute("main/perft-marcel.epd", out);
			execute("main/perft.epd", out);


//			execute("main/perftsuite1.txt", out);
//			execute("main/perftsuite2.txt", out);
//			execute("main/perftsuite3.txt", out);

		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void execute(String filename, PrintStream out){
		try {
			System.out.println("Starting Test suite " + filename);
			out.println("Starting Test suite " + filename);
			
			List<String> failedSuites = new ArrayList<String>();
			
			PerftMainTestSuite suite = new PerftMainTestSuite();
			
			InputStream instr = suite.getClass().getClassLoader().getResourceAsStream(filename);

			// reading the files with buffered reader
			InputStreamReader inputStreamReader = new InputStreamReader(instr);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;

			// outputting each line of the file.
			while ((line = bufferedReader.readLine()) != null) {
				if(!line.startsWith("#")){
					if(suite.run(line) == false){
						failedSuites.add(line);
					}
				}
			}
			
			System.out.println("Suite summary " + filename);
			if(failedSuites.isEmpty()){
				System.out.println("\t all tests executed successfully");
				out.println("\t all tests executed successfully");
			} else {
				for(String suiteStr: failedSuites){
					System.out.println("\t test failed: " + suiteStr);
					out.println("\t test failed: " + suiteStr);
				}
			}
			System.out.println("=================");

			
		} catch (Exception e) {
			System.out.println(e);
			out.println(e);
		}
		out.flush();
	}
	
	private final ChessFactory chessFactory;
	
	public PerftMainTestSuite() {
		this(new ChessFactory());
	}
	
	public PerftMainTestSuite(ChessFactory chessFactory) {
		this.chessFactory =  chessFactory;
	}

	protected String fen;
	protected long[] expectedPerftResults;
	private int startLevel;

	private PrintStream out = System.out;

	protected boolean run(String perfTest) {
		boolean returnResult = false;
		try {
			parseTests(perfTest);

			out.println("Testing FEN: " + this.fen);
			for (int i = 0; i < expectedPerftResults.length; i++) {

				PerftBrute main = new PerftBrute();

				PerftResult result = main.start(getGame(), this.startLevel + i);

				if (result.getTotalNodes() == expectedPerftResults[i]) {
					out.println("depth " + (this.startLevel + i) + " OK");
				} else {
					out.println("depth " + (this.startLevel + i) + " FAIL, expected = " + expectedPerftResults[i] + ", actual = " + result.getTotalNodes());
					returnResult = false;
					break;
				}

			}
			returnResult = true;
			out.println("=============");
		}catch (Exception e){
			out.println(e);
		}
		return returnResult;
	}

	protected void parseTests(String tests) {
		String[] splitStrings = tests.split(";");
		
		this.fen = splitStrings[0].trim();
		this.expectedPerftResults = new long[splitStrings.length - 1];
		this.startLevel = 0;
		
		for (int i = 1; i < splitStrings.length; i++) {
			String[] perftResultStr = splitStrings[i].trim().split(" ");
			if (this.startLevel == 0) {
				this.startLevel = Integer.parseInt(perftResultStr[0].substring(1));
			}
			expectedPerftResults[i - 1] = Long.parseLong(perftResultStr[1]);
		}
		
	}
	
	private Game getGame() {		
		GameBuilder builder = new GameBuilder(chessFactory);

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(this.fen);
		
		return builder.getResult();
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}
}
