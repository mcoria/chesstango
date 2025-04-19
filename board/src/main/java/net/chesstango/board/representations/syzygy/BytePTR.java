package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class BytePTR implements Cloneable {
    final MappedFile mappedFile;
    int ptr = 0;

    BytePTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    BytePTR incPtr(int inc) {
        ptr += inc;
        return this;
    }

    byte read_uint8_t(int offset) {
        return mappedFile.read_uint8_t(ptr + offset);
    }

    CharPTR createCharPTR(int offset) {
        CharPTR charPTR = new CharPTR(mappedFile);
        charPTR.ptr = ptr + offset;
        return charPTR;
    }

    @Override
    public BytePTR clone() {
        BytePTR bytePTR = new BytePTR(mappedFile);
        bytePTR.ptr = ptr;
        return bytePTR;
    }
}
