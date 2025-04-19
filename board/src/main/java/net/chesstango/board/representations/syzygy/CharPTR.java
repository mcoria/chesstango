package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class CharPTR {
    final MappedFile mappedFile;
    int ptr = 0;

    CharPTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    public void incPtr(int inc) {
        ptr += inc;
    }
}
