package Domain;

public class Node {
    private Object data;
    private Node next;

    // Constructor
    public Node(Object data) {
        this.data = data;
        this.next = null;
    }

    // Getter and setter methods for data and next node (optional)
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
