package capstone;

import java.util.Objects;

public class GraphEdge {

    private int from;
    private int to;

    public GraphEdge(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphEdge graphEdge = (GraphEdge) o;
        return getFrom() == graphEdge.getFrom() &&
                getTo() == graphEdge.getTo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFrom(), getTo());
    }
}
