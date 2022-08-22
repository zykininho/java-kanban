package structures;

public class Node<Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.prev = prev;
        this.data = data;
        this.next = next;
    }
}
