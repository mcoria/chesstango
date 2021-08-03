package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import builder.ChessBuilderGame;
import builder.ChessFactory;
import chess.Game;
import parsers.FENParser;

public class PerftSuite {

	public static void main(String[] args) {
		execute("main/perftsuite1.txt");
		execute("main/perftsuite2.txt");
	}
	
	private ChessFactory chessFactory;
	
	public PerftSuite() {
		this(new ChessFactory());
	}
	
	public PerftSuite(ChessFactory chessFactory) {
		this.chessFactory =  chessFactory;
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

	protected String fen;
	protected int[] perftResults;
	

	protected void run(String tests) {
		parseTests(tests);
		
		System.out.println("Testing FEN: " + this.fen);
		for(int i = 0; i < perftResults.length; i++){
			
			ChessMain main = new ChessMain();
			
			Node rootNode = main.start(getGame(), i + 1);
			
			if(rootNode.getChildNodesCounter() == perftResults[i]){
				System.out.println("depth " + (i + 1) + " OK" );
			} else {
				System.out.println("depth " + (i + 1) + " FAIL, expected = " + perftResults[i] + ", actual = " + rootNode.getChildNodesCounter());
				break;
			}
			
		}
		System.out.println("=============");
		
	}

	protected void parseTests(String tests) {
		String[] splitStrings = tests.split(";");
		
		this.fen = splitStrings[0].trim();
		this.perftResults = new int[splitStrings.length - 1];
		
		for(int i = 1; i < splitStrings.length; i++){
			String[] perftResultStr = splitStrings[i].split(" ");
			perftResults[i - 1] =  Integer.parseInt(perftResultStr[1]);
		}
		
	}
	
	private Game getGame() {		
		ChessBuilderGame builder = new ChessBuilderGame(chessFactory);

		FENParser parser = new FENParser(builder);
		
		parser.parseFEN(this.fen);
		
		return builder.getGame();
	}		

}
