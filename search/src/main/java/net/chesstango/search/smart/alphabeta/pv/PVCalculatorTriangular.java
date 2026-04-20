package net.chesstango.search.smart.alphabeta.pv;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PVCalculatorTriangular extends PVCalculatorAbstract implements Acceptor {

    @Setter
    private TriangularPVTable trianglePV;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    protected List<PrincipalVariation> walkPrincipalVariation(List<PrincipalVariation> principalVariationList, int eval) {
        int pvMoveCounter = 0;
        short[] pvMoves = trianglePV.getRootPV();

        // First PV move

        while (pvMoveCounter < pvMoves.length) {

            long currentHash = game.getPosition().getZobristHash();
            Move currentMove = getMove(pvMoves[pvMoveCounter++]);

            principalVariationList.add(new PrincipalVariation(currentHash, currentMove));

            currentMove.executeMove();
        }

        return principalVariationList;
    }
}
