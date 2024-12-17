package lyc.compiler.model;

public class AlreadyDeclaredIdException extends CompilerException {
    public AlreadyDeclaredIdException(String message) {
        super(message);
    }
}
