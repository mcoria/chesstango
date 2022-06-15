/**
 * 
 */
package chess.uci.engine;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chess.ai.BestMoveFinder;
import chess.ai.imp.smart.SmartLoop;
import chess.board.Game;
import chess.uci.protocol.UCIEncoder;
import chess.board.representations.fen.FENDecoder;
import chess.board.moves.Move;
import chess.uci.protocol.UCIMessage;
import chess.uci.protocol.UCIMessageExecutor;
import chess.uci.protocol.requests.*;
import chess.uci.protocol.responses.RspBestMove;
import chess.uci.protocol.responses.RspReadyOk;
import chess.uci.protocol.responses.RspId;
import chess.uci.protocol.responses.RspUciOk;
import chess.uci.protocol.stream.*;

/**
 * @author Mauricio Coria
 *
 */
public class EngineZonda implements Engine, UCIMessageExecutor {
	private final ExecutorService executorService;
	private final BestMoveFinder bestMoveFinder;
	private Game game;
	private ZondaState currentState;
	private UCIOutputStream output;

	public EngineZonda() {
		this.bestMoveFinder = new SmartLoop();
		this.currentState =  new Ready();
		this.executorService = Executors.newSingleThreadExecutor();
	}


	public void setOutputStream(UCIOutputStream output){
		this.output = output;
	}


	@Override
	public void do_uci(CmdUci cmdUci) {
		output.write(new RspId(RspId.RspIdType.NAME, "Zonda"));
		output.write(new RspId(RspId.RspIdType.AUTHOR, "Mauricio Coria"));
		output.write(new RspUciOk());
	}

	@Override
	public void do_setOption(CmdSetOption cmdSetOption) {
	}

	@Override
	public void do_newGame(CmdUciNewGame cmdUciNewGame) {
	}

	@Override
	public void do_position(CmdPosition cmdPosition) {
		game = CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ?  FENDecoder.loadGame(FENDecoder.INITIAL_FEN) : FENDecoder.loadGame(cmdPosition.getFen());
		executeMoves(cmdPosition.getMoves());
		currentState =  new WaitCmdGo();
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
		currentState.do_stop();
		try {
			output.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void receive_uciOk(RspUciOk rspUciOk) {

	}

	@Override
	public void receive_id(RspId rspId) {

	}

	@Override
	public void receive_readyOk(RspReadyOk rspReadyOk) {

	}

	@Override
	public void receive_bestMove(RspBestMove rspBestMove) {

	}

	@Override
	public void do_isReady(CmdIsReady cmdIsReady) {
		output.write(new RspReadyOk());
	}

	public Game getGame(){ return game;}

	protected ZondaState getCurrentState() {
		return currentState;
	}

	private void executeMoves(List<String> moves) {
		UCIEncoder uciEncoder = new UCIEncoder();
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

	@Override
	public void write(UCIMessage message) {
		message.execute(this);
	}

	@Override
	public void close() throws IOException {
		output.close();
	}

	interface ZondaState {

		void do_go(CmdGo cmdGo);

		void do_stop();
	}


	class Ready implements ZondaState {
		@Override
		public void do_go(CmdGo cmdGo) {
		}

		@Override
		public void do_stop() {
		}
	}


	class WaitCmdGo implements ZondaState {
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

	class FindingBestMove implements ZondaState {

		private UCIEncoder uciEncoder = new UCIEncoder();


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

			currentState = new Ready();
		}
	}
}
