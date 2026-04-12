package net.chesstango.search.smart.alphabeta.pv;

import lombok.Setter;
import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;

import java.util.List;

import static net.chesstango.search.Bound.EXACT;

/**
 * TTPVReader will not be considering for statistics purposes.
 *
 * @author Mauricio Coria
 */
public class PVCalculatorTransposition extends PVCalculatorAbstract implements Acceptor{

    @Setter
    private TTable maxMap;

    @Setter
    private TTable minMap;

    private final TranspositionEntry entryWorkspace;

    public PVCalculatorTransposition() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    protected List<PrincipalVariation> walkPrincipalVariation(List<PrincipalVariation> principalVariationList, int eval) {
        // Second PV move
        long currentHash = game.getPosition().getZobristHash();
        Move currentMove = readMoveFromTT(currentHash, eval);

        while (currentMove != null) {

            principalVariationList.add(new PrincipalVariation(currentHash, currentMove));

            currentMove.executeMove();

            // Third PV move and onward
            currentHash = game.getPosition().getZobristHash();
            currentMove = readMoveFromTT(currentHash, eval);
        }

        return principalVariationList;
    }

    final Move readMoveFromTT(long hash, int eval) {
        Move result = null;
        if (maxMap != null && minMap != null) {
            boolean load = Color.WHITE.equals(game.getPosition().getCurrentTurn()) ? maxMap.load(hash, entryWorkspace) : minMap.load(hash, entryWorkspace);
            if (load && EXACT.equals(entryWorkspace.getBound()) && entryWorkspace.getValue() == eval) {
                short bestMoveEncoded = entryWorkspace.getMove();
                result = bestMoveEncoded != 0 ? getMove(bestMoveEncoded) : null;
            }
        }
        return result;
    }
}
