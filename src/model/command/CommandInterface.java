package model.command;

/**
 * The Undo/Redo Command interface
 * which can be implemented by all commands
 * that are supposed to have this functionality.
 */
public interface CommandInterface {

    /**
     * Interface method redo
     */
    void redo();

    /**
     * Interface method undo
     */
    void undo();
}
