/**
 * 
 */
package chess.uci.engine;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.SmartLoop;
import chess.board.Game;
import chess.board.representations.MoveEncoder;
import chess.board.representations.fen.FENDecoder;
import chess.board.moves.Move;
import chess.uci.protocol.requests.CmdGo;
import chess.uci.protocol.requests.CmdIsReady;
import chess.uci.protocol.requests.CmdUci;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.uci.RspIdEngineAuthor;
import chess.uci.protocol.responses.uci.RspIdEngineName;
import chess.uci.protocol.responses.uci.RspUciOk;

/**
 * @author Mauricio Coria
 *
 */
public class EngineZonda extends EngineAbstract  {

	private final BestMoveFinder bestMoveFinder;

	private final MoveEncoder moveEncoder;

	private Game game;

	private Executor executor = Executors.newSingleThreadExecutor();

	public EngineZonda() {
		this.keepProcessing = true;
		this.bestMoveFinder = new SmartLoop();
		this.moveEncoder = new MoveEncoder();
	}

	@Override
	public void do_uci(CmdUci cmdUci) {
		output.write(new RspIdEngineName());
		output.write(new RspIdEngineAuthor());
		output.write(new RspUciOk());
	}

	@Override
	public void do_setOptions() {
	}

	@Override
	public void do_newGame() {
		this.game = null;
	}


	@Override
	public void do_position_startpos(List<String> moves) {
		game = loadGame(FENDecoder.INITIAL_FEN);
		executeMoves(moves);
	}


	@Override
	public void do_position_fen(String fen, List<String> moves) {
		game = loadGame(fen);
		executeMoves(moves);
	}

	@Override
	public void do_go(CmdGo cmdGo) {
		executor.execute(() -> {
			Move selectedMove = bestMoveFinder.findBestMove(game);

			output.write(new RspBestMove(moveEncoder.encode(selectedMove)));
		});
	}

	@Override
	public void do_quit() {
		keepProcessing = false;
		bestMoveFinder.stopProcessing();
	}

	@Override
	public void do_stop() {
		keepProcessing = false;
		bestMoveFinder.stopProcessing();
	}

	@Override
	public void do_isReady(CmdIsReady cmdIsReady) {
		output.write(new RspReadyOk());
	}

	public Game getGame(){ return game;}

	private Game loadGame(String fen) {
		return FENDecoder.loadGame(fen);
	}

	private void executeMoves(List<String> moves) {
		for (String moveStr : moves) {
			boolean findMove = false;
			for (Move move : game.getPossibleMoves()) {
				String encodedMoveStr = moveEncoder.encode(move);
				if (encodedMoveStr.equals(moveStr)) {
					game.executeMove(move);
					findMove = true;
					break;
				}
			}
			if (!findMove) {
				throw new RuntimeException("No move found " + moveStr);
			}
		}
	}
}
