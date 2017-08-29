package exception;

/**
 * Created by roshanalwis on 8/26/17.
 */
public class RequiredAttributeMissingException extends Exception {
    public RequiredAttributeMissingException(){

    }

    public RequiredAttributeMissingException(String message){
        super(message);
    }
}
