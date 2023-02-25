package net.chesstango.uci.engine;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.protocol.UCIEncoder;
import net.chesstango.uci.protocol.requests.CmdGo;
import net.chesstango.uci.protocol.requests.CmdIsReady;
import net.chesstango.uci.protocol.requests.CmdPosition;
import net.chesstango.uci.protocol.requests.CmdUci;
import net.chesstango.uci.protocol.responses.RspReadyOk;

import java.util.List;

class Ready implements ZondaState {

    private final EngineTango engineTango;

    Ready(EngineTango engineTango) {
        this.engineTango = engineTango;
    }

    @Override
    public void do_uci(CmdUci cmdUci) {
    }

    @Override
    public void do_isReady(CmdIsReady cmdIsReady) {
        engineTango.responseOutputStream.accept(new RspReadyOk());
    }

    @Override
    public void do_go(CmdGo cmdGo) {
    }

    @Override
    public void do_stop() {
    }

    @Override
    public void do_position(CmdPosition cmdPosition) {
        engineTango.game = CmdPosition.CmdType.STARTPOS == cmdPosition.getType() ? FENDecoder.loadGame(FENDecoder.INITIAL_FEN) : FENDecoder.loadGame(cmdPosition.getFen());
        executeMoves(engineTango.game, cmdPosition.getMoves());
        engineTango.currentState = new WaitCmdGo(engineTango);
    }

    void executeMoves(Game game, List<String> moves) {
        if (moves != null && !moves.isEmpty()) {
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
    }
}
