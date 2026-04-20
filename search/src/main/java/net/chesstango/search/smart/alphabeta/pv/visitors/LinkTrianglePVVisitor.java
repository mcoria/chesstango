package net.chesstango.search.smart.alphabeta.pv.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangular;
import net.chesstango.search.smart.alphabeta.pv.filters.CalculatePV;
import net.chesstango.search.smart.alphabeta.pv.filters.ClearPV;
import net.chesstango.search.smart.alphabeta.pv.filters.UpdatePV;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;

/**
 *
 * @author Mauricio Coria
 */
public class LinkTrianglePVVisitor implements Visitor {

    /**
     * Se utiliza para el calculo de PV
     */
    private final TriangularPVTable trianglePV;

    public LinkTrianglePVVisitor(TriangularPVTable trianglePV) {
        this.trianglePV = trianglePV;
    }

    @Override
    public void visit(PVCalculatorTriangular setTrianglePV) {
        setTrianglePV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(CalculatePV calculatePV) {
        calculatePV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(ClearPV clearPV) {
        clearPV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(UpdatePV updatePV) {
        updatePV.setTrianglePV(trianglePV);
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setTrianglePV(trianglePV);
    }
}
