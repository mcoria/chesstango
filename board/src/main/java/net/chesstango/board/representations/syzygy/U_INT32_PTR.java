package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class U_INT32_PTR implements Cloneable {
    final MappedFile mappedFile;
    int ptr = 0;

    U_INT32_PTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    void incPtr(int inc) {
        ptr += 4 * inc;
    }

    int read_le_u32(int offset) {
        return mappedFile.read_le_u32(ptr + 4 * offset);
    }

    long read_le_u64(int offset) {
        return mappedFile.read_le_u64(ptr + 4 * offset);
    }

    @Override
    public U_INT32_PTR clone() {
        U_INT32_PTR u_int32_ptr = new U_INT32_PTR(mappedFile);
        u_int32_ptr.ptr = ptr;
        return u_int32_ptr;
    }
}
