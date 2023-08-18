package net.chesstango.search.smart.statics;

import lombok.Getter;

import java.util.Objects;

@Getter
public class EvaluationEntry {

    private final long key;
    private final int value;

    public EvaluationEntry(long key, int value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EvaluationEntry that = (EvaluationEntry) o;
        return key == that.key && value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

}
