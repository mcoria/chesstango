package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class U_INT16_PTR implements Cloneable{
    final MappedFile mappedFile;
    int ptr = 0;

    U_INT16_PTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    void incPtr(int inc) {
        ptr += inc;
    }

    short read_le_u16(int offset) {
        return mappedFile.read_le_u16(ptr + 2 * offset);
    }

    @Override
    public U_INT16_PTR clone() {
        U_INT16_PTR u_int16_ptr = new U_INT16_PTR(mappedFile);
        u_int16_ptr.ptr = ptr;
        return u_int16_ptr;
    }
}
