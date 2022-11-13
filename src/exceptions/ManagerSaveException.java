package exceptions;

public class ManagerSaveException extends Error {

    public ManagerSaveException() {
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
