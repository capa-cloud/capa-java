package group.rxcloud.capa.infrastructure.exceptions;


import com.kevinten.vrml.error.code.ErrorCodeContext;
import com.kevinten.vrml.error.exception.ErrorCodeException;

/**
 * A Capa's specific exception.
 */
public class CapaException extends ErrorCodeException {

    /**
     * Instantiates a new Capa exception.
     *
     * @param errorCodeContext the error code context
     */
    public CapaException(ErrorCodeContext errorCodeContext) {
        super(errorCodeContext);
    }

    /**
     * Instantiates a new Capa exception.
     *
     * @param errorCodeContext the error code context
     * @param cause            the cause
     */
    public CapaException(ErrorCodeContext errorCodeContext, Throwable cause) {
        super(errorCodeContext, cause);
    }

    /**
     * Instantiates a new Capa exception.
     *
     * @param errorCodeContext the error code context
     * @param errorMessage     the error message
     */
    public CapaException(ErrorCodeContext errorCodeContext, String errorMessage) {
        super(errorCodeContext, errorMessage);
    }

    /**
     * Instantiates a new Capa exception.
     *
     * @param errorCodeContext the error code context
     * @param errorMessage     the error message
     * @param cause            the cause
     */
    public CapaException(ErrorCodeContext errorCodeContext, String errorMessage, Throwable cause) {
        super(errorCodeContext, errorMessage, cause);
    }

    /**
     * Instantiates a new Capa exception.
     *
     * @param exception the exception
     */
    public CapaException(Throwable exception) {
        super(CapaErrorContext.SYSTEM_ERROR, exception);
    }
}
