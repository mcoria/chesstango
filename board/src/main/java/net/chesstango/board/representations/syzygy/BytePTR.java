package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class BytePTR {
    final MappedFile mappedFile;
    int ptr = 0;

    BytePTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    public byte read_uint8_t(int offset) {
        return mappedFile.read_uint8_t(ptr + offset);
    }

    public void incPtr(int inc) {
        ptr += inc;
    }
}
