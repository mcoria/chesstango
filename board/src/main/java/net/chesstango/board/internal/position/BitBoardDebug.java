package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.iterators.bysquare.PositionsSquareIterator;
import net.chesstango.board.position.SquareBoardReader;


/**
 * @author Mauricio Coria
 *
 */
public class BitBoardDebug extends BitBoardImp {

	@Override
	public void init(SquareBoardReader board) {
		super.init(board);
		validar(board);
	}

	@Override
	public void swapPositions(Piece piece, Square remove, Square add) {
		super.swapPositions(piece, remove, add);
		validar();
	}	
	
	@Override
	public void addPosition(PiecePositioned position) {
		super.addPosition(position);
		validar();
	}
	
	@Override
	public void removePosition(PiecePositioned position) {
		super.removePosition(position);
		validar();
	}

	public void validar() {
		long solapados = this.squareWhites & this.squareBlacks;
		if( solapados != 0 ){
			String solapadosStr = "";
			for (SquareIterator iterator = new PositionsSquareIterator(solapados); iterator.hasNext();) {
				solapadosStr = solapadosStr + iterator.next().toString() + " ";
			}

			throw new RuntimeException("El mismo casillero esta tomado por blanca y negro: " + solapadosStr);
		}
	}
	
	public void validar(SquareBoardReader squareBoard) {
		validar();
		
		for (PiecePositioned piecePositioned : squareBoard) {
			if(piecePositioned.piece() == null){
				if(! this.isEmpty(piecePositioned.square()) ){
					throw new RuntimeException("BitBoard contiene una pieza " + this.getColor(piecePositioned.square()) + " en " + piecePositioned.square() + " pero en PosicionPieza esta vacia");
				}
			} else {
				Color colorBoard = piecePositioned.piece().getColor();
				Color color = this.getColor(piecePositioned.square());
				
				if(! colorBoard.equals(color) ){
					throw new RuntimeException("SquareBoard contiene una pieza de color distinto a BitBoard en " + piecePositioned.square());
				}

				long bitPosition = piecePositioned.square().bitPosition();

				long piecesPositions = switch (piecePositioned.piece()) {
					case PAWN_WHITE, PAWN_BLACK ->  pawns;
					case ROOK_WHITE, ROOK_BLACK -> rooks;
					case BISHOP_WHITE, BISHOP_BLACK -> bishops;
					case KNIGHT_WHITE, KNIGHT_BLACK -> knights;
					case QUEEN_WHITE, QUEEN_BLACK -> queens;
					case KING_WHITE, KING_BLACK -> kings;
				};

				if( (piecesPositions & bitPosition) == 0){
					throw new RuntimeException("SquareBoard contiena una pieza que no está presente en  BitBoard " + piecePositioned.square());
				}
			}
		}
	}
}
