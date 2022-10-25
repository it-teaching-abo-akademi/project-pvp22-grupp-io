package pvp.api.exceptions;

public class ResourceNotFoundException extends Exception {
    /**
     *
     */
    public ResourceNotFoundException(String object, String referenceArg, String arg) {
        super("No " + object + " where " + referenceArg + " == " + arg);
    }
}
