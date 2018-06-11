package model.command;

import java.util.Stack;

/**
 * The command invoker class.
 */
public class CommandInvoker {

    private Stack<CommandInterface> undoStack;
    private Stack<CommandInterface> redoStack;

    public CommandInvoker() {
        this.undoStack = new Stack<>();
        this.redoStack = new Stack<>();
    }

    /**
     * Add a command to the Invoker
     * @param command the command to add
     */
    public void setCommand(CommandInterface command) {
        if(command instanceof DeleteCommand){
            redoStack.push(command);
        }else {
            undoStack.push(command);
        }
    }

    /**
     * Make the Invoker undo the last added command on the undostack
     */
    public void undo() {
        if (!undoStack.empty()) {
            CommandInterface cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    /**
     * Make the Invoker redo the last added command on the redostack
     */
    public void redo() {
        if (!redoStack.empty()) {
            CommandInterface cmd = redoStack.pop();
            cmd.redo();
            undoStack.push(cmd);
        }
    }
}
