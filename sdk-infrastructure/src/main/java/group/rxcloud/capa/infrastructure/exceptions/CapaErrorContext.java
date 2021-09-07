package group.rxcloud.capa.infrastructure.exceptions;


import com.kevinten.vrml.error.code.ErrorCodeContext;

/**
 * Application error info context:
 * [CAPA-200000] SUCCESS
 * [CAPA-10Xxxx] {@code parameter} error. Capa parameter check exception
 * [CAPA-20Xxxx] {@code business} error. Capa business logic exception
 * [CAPA-30Xxxx] {@code repository service}. Repository operation exception
 * [CAPA-40Xxxx] {@code dependent service}. Dependency service exception
 * [CAPA-50Xxxx] {@code system} error. Application system exception
 */
public enum CapaErrorContext implements ErrorCodeContext {

    /**
     * The successful error code.
     */
    SUCCESS("200000", "Success"),

    /*-------------------------------------------Parameter error as below---------------------------------------**/

    /**
     * Invalid basic parameter error, the code starts with 0.
     */
    PARAMETER_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createParameterErrorCode("0000"), "Invalid parameter error!"),

    /**
     * RPC parameter error, the code starts with 1.
     */
    PARAMETER_RPC_REQUEST_SERIALIZE_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createParameterErrorCode("1001"), "RPC request serialize error!"),
    PARAMETER_RPC_RESPONSE_DESERIALIZE_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createParameterErrorCode("1002"), "RPC response deserialize error!"),

    /*-------------------------------------------Business error as below---------------------------------------**/

    /**
     * Basic business error, the code starts with 0.
     */
    BUSINESS_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createBusinessProcessErrorCode("0001"), "Business error!"),

    /*-------------------------------------------Repository error as below---------------------------------------**/

    /**
     * Basic repository error, the code starts with 0.
     */
    REPOSITORY_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createRepositoryErrorCode("0000"), "Repository error!"),

    /*-------------------------------------------Dependent service error as below---------------------------------------**/

    /**
     * Basic dependent service error, the code starts with 0.
     */
    DEPENDENT_SERVICE_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createDependentServiceErrorCode("0000"), "Failed to call the dependent service!"),

    /*-------------------------------------------System error as below---------------------------------------**/

    /**
     * Basic system error, the code starts with 0.
     */
    SYSTEM_ERROR(
            CapaErrorCodeGenerator.INSTANCE.createSystemErrorCode("0000"), "System error!"),
    ;

    // -- Encapsulation

    private final String code;
    private final String message;

    CapaErrorContext(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    static {
        ErrorCodeManager.assertErrorCodesNoDuplicate(CapaErrorContext.values());
    }

    /**
     * The Capa error code generator.
     */
    private static class CapaErrorCodeGenerator implements ErrorCodeGenerator {

        private static final CapaErrorCodeGenerator INSTANCE = new CapaErrorCodeGenerator();

        /**
         * Capa system code.
         */
        private static final String APPLICATION_CODE = "CAP";

        @Override
        public String applicationErrorCode() {
            return APPLICATION_CODE;
        }
    }
}
