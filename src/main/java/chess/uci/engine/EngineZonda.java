/**
 * 
 */
package chess.uci.engine;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.SmartLoop;
import chess.board.Game;
import chess.uci.protocol.UCIEncoder;
import chess.board.representations.fen.FENDecoder;
import chess.board.moves.Move;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspUciOk;

/**
 * @author Mauricio Coria
 *
 */
public class EngineZonda extends EngineAbstract {

	private final ExecutorService executorService;

	private final BestMoveFinder bestMoveFinder;

	private final UCIEncoder uciEncoder;

	private Game game;

	private ZondaState currentState;

	public EngineZonda() {
		this.keepProcessing = true;
		this.bestMoveFinder = new SmartLoop();
		this.uciEncoder = new UCIEncoder();
		this.currentState = new WaitCmdUci();
		this.executorService = Executors.newSingleThreadExecutor();
	}

	@Override
	public void do_uci(CmdUci cmdUci) {
		currentState.do_uci();
	}

	@Override
	public void do_setOption(CmdSetOption cmdSetOption) {
	}

	@Override
	public void do_newGame(CmdUciNewGame cmdUciNewGame) {
		currentState.do_newGame();
	}

	@Override
	public void do_position(CmdPosition cmdPosition) {
		currentState.do_position(cmdPosition);
	}

	@Override
	public void do_go(CmdGo cmdGo) {
		currentState.do_go(cmdGo);
	}

	@Override
	public void do_stop(CmdStop cmdStop) {
		currentState.do_stop();
	}

	@Override
	public void do_quit(CmdQuit cmdQuit) {
		keepProcessing = false;
		currentState.do_stop();
	}

	@Override
	public void do_isReady(CmdIsReady cmdIsReady) {
		output.write(new RspReadyOk());
	}

	public Game getGame(){ return game;}

	private void executeMoves(List<String> moves) {
		for (String moveStr : moves) {
			boolean findMove = false;
			for (Move move : game.getPossibleMoves()) {
				String encodedMoveStr = uciEncoder.encode(move);
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


	private interface ZondaState {

		void do_uci();

		void do_newGame();

		void do_position(CmdPosition cmdPosition);

		void do_go(CmdGo cmdGo);

		void do_stop();
	}

	private class WaitCmdUci implements ZondaState {

		@Override
		public void do_uci() {
			output.write(new RspId(RspId.RspIdType.NAME, "Zonda"));
			output.write(new RspId(RspId.RspIdType.AUTHOR, "Mauricio Coria"));
			output.write(new RspUciOk());
			currentState = new WaitCmdUciNewGame();
		}

		@Override
		public void do_newGame() {

		}

		@Override
		public void do_position(CmdPosition cmdPosition) {

		}

		@Override
		public void do_go(CmdGo cmdGo) {

		}

		@Override
		public void do_stop() {

		}
	}

	private class WaitCmdUciNewGame implements ZondaState{

		@Override
		public void do_uci() {
		}

		@Override
		public void do_newGame() {
			game = null;
			currentState =  new WaitCmdPosition();
		}

		@Override
		public void do_position(CmdPosition cmdPosition) {
		}

		@Override
		public void do_go(CmdGo cmdGo) {

		}

		@Override
		public void do_stop() {

		}
	}

	private class WaitCmdPosition implements ZondaState {

		@Override
		public void do_uci() {
		}

		@Override
		public void do_newGame() {
		}

		@Override
		public void do_position(CmdPosition cmdPosition) {
			game = FENDecoder.loadGame(cmdPosition.getFen());
			executeMoves(cmdPosition.getMoves());
			currentState =  new WaitCmdGo();
		}

		@Override
		public void do_go(CmdGo cmdGo) {
		}

		@Override
		public void do_stop() {

		}
	}

	private class WaitCmdGo implements ZondaState {

		@Override
		public void do_uci() {
		}

		@Override
		public void do_newGame() {
		}

		@Override
		public void do_position(CmdPosition cmdPosition) {
		}

		@Override
		public void do_go(CmdGo cmdGo) {
			FindingBestMove findingBestMove = new FindingBestMove();
			currentState = findingBestMove;
			executorService.execute(findingBestMove::findBestMove);
		}

		@Override
		public void do_stop() {
		}
	}

	private class FindingBestMove implements ZondaState {


		@Override
		public void do_uci() {
		}

		@Override
		public void do_newGame() {
		}

		@Override
		public void do_position(CmdPosition cmdPosition) {
		}

		@Override
		public void do_go(CmdGo cmdGo) {
		}

		@Override
		public void do_stop() {
			bestMoveFinder.stopProcessing();
		}

		public void findBestMove() {
			Move selectedMove = bestMoveFinder.findBestMove(game);

			output.write(new RspBestMove(uciEncoder.encode(selectedMove)));

			currentState = new WaitCmdPosition();
		}
	}
}
