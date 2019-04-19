package capstone;

import java.util.HashSet;
import java.util.Objects;

public class GraphNode implements Comparable<GraphNode> {

    private int value;
    private HashSet<Integer> followers;
    private boolean covered;

    public GraphNode(int value) {
        this.value = value;
        followers = new HashSet<Integer>();
        covered = false;
    }

    public int getSize() {
        return followers.size();
    }

    public int getValue() {
        return value;
    }

    public void addFollower(int follower) {
        followers.add(follower);
    }

    public HashSet<Integer> getFollowers() {
        return followers;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public boolean isCovered() {
        return covered;
    }

    public void printNode() {
        System.out.println("The vertix - \"" + value + " has next neighbours:");
        for (int m : followers) {
            System.out.println(m);
        }
        System.out.println("*****************************");
    }

    public int compareTo(GraphNode node) {
        if (this.getSize() > node.getSize()) {
            return 1;
        }
        if (this.getSize() < node.getSize()) {
            return -1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode graphNode = (GraphNode) o;
        return getValue() == graphNode.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
