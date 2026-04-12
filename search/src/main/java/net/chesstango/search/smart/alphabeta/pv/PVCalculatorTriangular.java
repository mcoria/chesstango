package net.chesstango.search.smart.alphabeta.pv;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PVCalculatorTriangular extends PVCalculatorAbstract implements Acceptor {

    @Setter
    private short[][] trianglePV;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    protected List<PrincipalVariation> walkPrincipalVariation(List<PrincipalVariation> principalVariationList, int eval) {
        int pvMoveCounter = 1;
        short[] pvMoves = trianglePV[0];

        // Second PV move
        long currentHash = game.getPosition().getZobristHash();
        Move currentMove = getMove(pvMoves[pvMoveCounter++]);

        while (currentMove != null) {

            principalVariationList.add(new PrincipalVariation(currentHash, currentMove));

            currentMove.executeMove();

            // Third PV move and onward
            currentHash = game.getPosition().getZobristHash();
            currentMove = getMove(pvMoves[pvMoveCounter++]);
        }

        return principalVariationList;
    }
}
