package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class U_INT8_PTR implements Cloneable {
    final MappedFile mappedFile;
    int ptr = 0;

    U_INT8_PTR(MappedFile mappedFile) {
        this.mappedFile = mappedFile;
    }

    U_INT8_PTR incPtr(int inc) {
        ptr += inc;
        return this;
    }

    byte read_uint8_t(int offset) {
        return mappedFile.read_uint8_t(ptr + offset);
    }

    short read_short(int offset) {
        return mappedFile.read_short(ptr + offset);
    }

    int read_le_u32(int offset) {
        return mappedFile.read_le_u32(ptr + offset);
    }

    U_INT16_PTR createU_INT16_PTR(int offset) {
        U_INT16_PTR u_int16_ptr = new U_INT16_PTR(mappedFile);
        u_int16_ptr.ptr = ptr + offset;
        return u_int16_ptr;
    }

    U_INT32_PTR createU_INT32_PTR(int offset) {
        U_INT32_PTR u_int32_ptr = new U_INT32_PTR(mappedFile);
        u_int32_ptr.ptr = ptr + offset;
        return u_int32_ptr;
    }

    @Override
    public U_INT8_PTR clone() {
        U_INT8_PTR u_int8_ptr = new U_INT8_PTR(mappedFile);
        u_int8_ptr.ptr = ptr;
        return u_int8_ptr;
    }

}
