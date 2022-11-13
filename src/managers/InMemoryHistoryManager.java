package managers;

import structures.Node;
import tasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> customTaskViewHistory = new CustomLinkedList<>();

    @Override
    public void add(Task task) {
        int id = task.getId();
        customTaskViewHistory.removeNode(customTaskViewHistory.nodesMap.get(id));
        customTaskViewHistory.linkLast(task);
        customTaskViewHistory.nodesMap.put(id, customTaskViewHistory.tail);
    }

    @Override
    public void remove(Task task) {
        int id = task.getId();
        customTaskViewHistory.removeNode(customTaskViewHistory.nodesMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return customTaskViewHistory.getTasks();
    }

    private static class CustomLinkedList<T extends Task> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;
        private final Map<Integer, Node<T>> nodesMap = new HashMap<>();

        private void linkLast(T task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            size++;
        }

        private List<T> getTasks() {
            List<T> tasks = new ArrayList<>();
            if (head == null) {
                return tasks;
            }
            Node<T> node = head;
            while (node != null) {
                tasks.add(0, node.data);
                if (node.next == null) {
                    break;
                } else {
                    node = node.next;
                }
            }
            return tasks;
        }

        private void removeNode(Node<T> node) {
            if (node == null) return;

            final Task task = node.data;
            final Node<T> next = node.next;
            final Node<T> prev = node.prev;

            if (prev == null) {
                head = next;
            } else {
                prev.next = next;
                node.prev = null;
            }

            if (next == null) {
                tail = prev;
            } else {
                next.prev = prev;
                node.next = null;
            }

            nodesMap.remove(task.getId());
            node.data = null;
            size--;
        }
    }
}
