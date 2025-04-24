package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class U_INT32_PTR {
    final MappedFile mappedFile;
    int ptr = 0;

    U_INT32_PTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    void incPtr(int inc) {
        ptr += 4 * inc;
    }

    short read_short(int offset) {
        return mappedFile.read_short(ptr + 4 * offset);
    }
}
