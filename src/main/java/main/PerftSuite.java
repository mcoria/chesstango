package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import builder.ChessBuilderGame;
import builder.ChessFactory;
import chess.Game;
import parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class PerftSuite {

	public static void main(String[] args) {
		//execute("main/perftsuite1.txt");
		//execute("main/perftsuite2.txt");
		//Todavia no podemos procesar bien este archivo
		execute("main/perftsuite3.txt");
	}
	
	private static void execute(String filename){
		try {
			PerftSuite suite = new PerftSuite();
			
			InputStream instr = suite.getClass().getClassLoader().getResourceAsStream(filename);

			// reading the files with buffered reader
			InputStreamReader strrd = new InputStreamReader(instr);

			BufferedReader rr = new BufferedReader(strrd);

			String line;

			// outputting each line of the file.
			while ((line = rr.readLine()) != null) {
				if(!line.startsWith("#")){
					suite.run(line);
				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}		
	}
	
	private ChessFactory chessFactory;
	
	public PerftSuite() {
		this(new ChessFactory());
	}
	
	public PerftSuite(ChessFactory chessFactory) {
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
			
			ChessMain main = new ChessMain();
			
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
			String[] perftResultStr = splitStrings[i].split(" ");
			if (this.startNivel == 0) {
				this.startNivel = Integer.parseInt(perftResultStr[0].substring(1));
			}
			perftResults[i - 1] = Integer.parseInt(perftResultStr[1]);
		}
		
	}
	
	private Game getGame() {		
		ChessBuilderGame builder = new ChessBuilderGame(chessFactory);

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(this.fen);
		
		return builder.getGame();
	}		

}
