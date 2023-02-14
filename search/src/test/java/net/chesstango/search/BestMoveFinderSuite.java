package net.chesstango.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.chesstango.search.smart.MinMaxPruning;
import net.chesstango.search.smart.Quiescence;
import net.chesstango.evaluation.imp.GameEvaluatorImp01;
import net.chesstango.board.Game;
import net.chesstango.board.builder.imp.GameBuilder;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 *
 */
public class BestMoveFinderSuite {

	public static void main(String[] args) {
		execute("main/ferdy_perft_double_checks.epd");
	}
	
	private static void execute(String filename){
		try {
			System.out.println("Starting suite " + filename);
			
			List<String> failedSuites = new ArrayList<String>();
			
			BestMoveFinderSuite suite = new BestMoveFinderSuite();
			
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
	
	public BestMoveFinderSuite() {
		this(new ChessFactory());
	}
	
	public BestMoveFinderSuite(ChessFactory chessFactory) {
		this.chessFactory =  chessFactory;
	}	

	protected boolean run(String epd) {
		EdpParsed edpParsed = parseTests(epd);
		
		BestMoveFinder moveFinder = new MinMaxPruning( new Quiescence( new GameEvaluatorImp01() ) );
		
		Game game = getGame(edpParsed.fen);

		Move resulBestMove = moveFinder.searchBestMove(game, 1);
		
		return encodeMove(resulBestMove).equals(edpParsed.bestMove);
	}

	protected EdpParsed parseTests(String tests) {
		EdpParsed edpParsed = new EdpParsed();
		String[] splitStrings = tests.split("bm");
		
		edpParsed.fen =  splitStrings[0].trim();
		edpParsed.bestMove = splitStrings[1].trim();
		
		return edpParsed;
	}
	
	private static class EdpParsed {
		String fen;
		String bestMove;
	}
	
	private Game getGame(String fen) {		
		GameBuilder builder = new GameBuilder(chessFactory);

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parseFEN(fen);
		
		return builder.getResult();
	}		

	private String encodeMove(Move move) {
		String promotionStr = "";
		if(move instanceof MovePromotion){
			MovePromotion movePromotion = (MovePromotion) move;
			switch (movePromotion.getPromotion()) {
				case ROOK_WHITE:
				case ROOK_BLACK:
					promotionStr = "r";
					break;
				case KNIGHT_WHITE:
				case KNIGHT_BLACK:
					promotionStr = "k";
					break;
				case BISHOP_WHITE:
				case BISHOP_BLACK:
					promotionStr = "b";
					break;
				case QUEEN_WHITE:
				case QUEEN_BLACK:
					promotionStr = "q";
					break;
			default:
				throw new RuntimeException("Invalid promotion " + move);
			} 
		}
		
		return move.getFrom().getKey().toString() + move.getTo().getKey().toString() + promotionStr;
	}	
}