package group.rxcloud.capa.spi.demo.config;

import group.rxcloud.capa.infrastructure.env.CapaEnvironment;
import group.rxcloud.capa.spi.config.RpcServiceOptions;

import java.util.Objects;

/**
 * RPC service options. Define for AppId.
 * TODO load by spi class
 */
public class DemoRpcServiceOptions implements RpcServiceOptions {

    /**
     * Unique rpc service ID
     */
    private final String appId;
    private final ServiceRpcInvokeMode rpcInvokeMode;

    /*
     * Optional options
     */
    private CtripToCtripServiceOptions ctripToCtripServiceOptions;
    private CtripToAwsServiceOptions ctripToAwsServiceOptions;
    private AwsToCtripServiceOptions awsToCtripServiceOptions;
    private AwsToAwsServiceOptions awsToAwsServiceOptions;

    /**
     * Instantiates a new Capa rpc service options.
     *
     * @param appId         the app id
     * @param rpcInvokeMode the rpc invoke mode
     */
    public DemoRpcServiceOptions(String appId, ServiceRpcInvokeMode rpcInvokeMode) {
        this.appId = appId;
        this.rpcInvokeMode = rpcInvokeMode;
    }

    /**
     * RPC service invoke mode
     */
    public enum ServiceRpcInvokeMode {
        /**
         * Ctrip -> Ctrip
         */
        CTRIP_TO_CTRIP,
        /**
         * Ctrip -> AWS
         */
        CTRIP_TO_AWS,
        /**
         * AWS -> Ctrip
         */
        AWS_TO_CTRIP,
        /**
         * AWS -> AWS
         */
        AWS_TO_AWS,
        ;
    }

    // -- Properties Defined

    /**
     * Properties required when calling the Ctrip service
     */
    public interface ToCtripServiceOptions {

        /**
         * SOA ServiceCode
         *
         * @return the service code
         */
        String getServiceCode();

        /**
         * SOA ServiceName
         *
         * @return the service name
         */
        String getServiceName();
    }

    /**
     * Properties required when calling the AWS service
     */
    public interface ToAwsServiceOptions {

        /**
         * App Mesh Host name
         *
         * @return the host name
         */
        String getHostName();

        /**
         * App Mesh Host port
         *
         * @return the host port
         */
        String getHostPort();
    }

    /**
     * Properties available when deployed on Ctrip
     */
    public interface CtripServiceOptions {

        /**
         * Ctrip deployment environment
         *
         * @return the service env
         */
        CapaEnvironment.DeployVpcEnvironment getServiceEnv();
    }

    /**
     * Properties available when deployed on AWS
     */
    public interface AwsServiceOptions {

        /**
         * AWS deployment environment
         *
         * @return the service env
         */
        CapaEnvironment.DeployVpcEnvironment getServiceEnv();
    }

    // Specific Properties Impl

    /**
     * The service deployed on Ctrip calls the service of Ctrip
     */
    public static class CtripToCtripServiceOptions implements CtripServiceOptions, ToCtripServiceOptions {

        private final String serviceCode;
        private final String serviceName;
        private final CapaEnvironment.DeployVpcEnvironment serviceEnv;

        /**
         * Instantiates a new Ctrip to ctrip service options.
         *
         * @param serviceCode the service code
         * @param serviceName the service name
         * @param serviceEnv  the service env
         */
        public CtripToCtripServiceOptions(String serviceCode, String serviceName, CapaEnvironment.DeployVpcEnvironment serviceEnv) {
            this.serviceCode = serviceCode;
            this.serviceName = serviceName;
            this.serviceEnv = serviceEnv;
        }

        @Override
        public String getServiceCode() {
            return serviceCode;
        }

        @Override
        public String getServiceName() {
            return serviceName;
        }

        @Override
        public CapaEnvironment.DeployVpcEnvironment getServiceEnv() {
            return serviceEnv;
        }
    }

    /**
     * TODO The service deployed on Ctrip calls the service of AWS
     */
    public static class CtripToAwsServiceOptions implements CtripServiceOptions {

        private final CapaEnvironment.DeployVpcEnvironment serviceEnv;

        /**
         * Instantiates a new Ctrip to aws service options.
         *
         * @param serviceEnv the service env
         */
        public CtripToAwsServiceOptions(CapaEnvironment.DeployVpcEnvironment serviceEnv) {
            this.serviceEnv = serviceEnv;
        }

        @Override
        public CapaEnvironment.DeployVpcEnvironment getServiceEnv() {
            return serviceEnv;
        }
    }

    /**
     * The service deployed on AWS calls the service of Ctrip
     */
    public static class AwsToCtripServiceOptions implements AwsServiceOptions, ToCtripServiceOptions {

        private final String serviceCode;
        private final String serviceName;
        private final CapaEnvironment.DeployVpcEnvironment serviceEnv;

        /**
         * Instantiates a new Aws to ctrip service options.
         *
         * @param serviceCode the service code
         * @param serviceName the service name
         * @param serviceEnv  the service env
         */
        public AwsToCtripServiceOptions(String serviceCode, String serviceName, CapaEnvironment.DeployVpcEnvironment serviceEnv) {
            this.serviceCode = serviceCode;
            this.serviceName = serviceName;
            this.serviceEnv = serviceEnv;
        }

        @Override
        public String getServiceCode() {
            return serviceCode;
        }

        @Override
        public String getServiceName() {
            return serviceName;
        }

        @Override
        public CapaEnvironment.DeployVpcEnvironment getServiceEnv() {
            return serviceEnv;
        }
    }

    /**
     * The service deployed on AWS calls the service of AWS
     */
    public static class AwsToAwsServiceOptions implements AwsServiceOptions, ToAwsServiceOptions {

        private final String hostName;
        private final String hostPort;
        private final CapaEnvironment.DeployVpcEnvironment serviceEnv;

        /**
         * Instantiates a new Aws to aws service options.
         *
         * @param hostName   the host name
         * @param hostPort   the host port
         * @param serviceEnv the service env
         */
        public AwsToAwsServiceOptions(String hostName, String hostPort, CapaEnvironment.DeployVpcEnvironment serviceEnv) {
            this.hostName = hostName;
            this.hostPort = hostPort;
            this.serviceEnv = serviceEnv;
        }

        @Override
        public CapaEnvironment.DeployVpcEnvironment getServiceEnv() {
            return serviceEnv;
        }

        @Override
        public String getHostName() {
            return hostName;
        }

        @Override
        public String getHostPort() {
            return hostPort;
        }
    }

    // -- Getter and Setter

    /**
     * Gets app id.
     *
     * @return the app id
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Gets rpc invoke mode.
     *
     * @return the rpc invoke mode
     */
    public ServiceRpcInvokeMode getRpcInvokeMode() {
        return rpcInvokeMode;
    }

    /**
     * Gets ctrip to ctrip service options.
     *
     * @return the ctrip to ctrip service options
     */
    public CtripToCtripServiceOptions getCtripToCtripServiceOptions() {
        return ctripToCtripServiceOptions;
    }

    /**
     * Sets ctrip to ctrip service options.
     *
     * @param ctripToCtripServiceOptions the ctrip to ctrip service options
     */
    public void setCtripToCtripServiceOptions(CtripToCtripServiceOptions ctripToCtripServiceOptions) {
        this.ctripToCtripServiceOptions = ctripToCtripServiceOptions;
    }

    /**
     * Gets ctrip to aws service options.
     *
     * @return the ctrip to aws service options
     */
    public CtripToAwsServiceOptions getCtripToAwsServiceOptions() {
        return ctripToAwsServiceOptions;
    }

    /**
     * Sets ctrip to aws service options.
     *
     * @param ctripToAwsServiceOptions the ctrip to aws service options
     */
    public void setCtripToAwsServiceOptions(CtripToAwsServiceOptions ctripToAwsServiceOptions) {
        this.ctripToAwsServiceOptions = ctripToAwsServiceOptions;
    }

    /**
     * Gets aws to ctrip service options.
     *
     * @return the aws to ctrip service options
     */
    public AwsToCtripServiceOptions getAwsToCtripServiceOptions() {
        return awsToCtripServiceOptions;
    }

    /**
     * Sets aws to ctrip service options.
     *
     * @param awsToCtripServiceOptions the aws to ctrip service options
     */
    public void setAwsToCtripServiceOptions(AwsToCtripServiceOptions awsToCtripServiceOptions) {
        this.awsToCtripServiceOptions = awsToCtripServiceOptions;
    }

    /**
     * Gets aws to aws service options.
     *
     * @return the aws to aws service options
     */
    public AwsToAwsServiceOptions getAwsToAwsServiceOptions() {
        return awsToAwsServiceOptions;
    }

    /**
     * Sets aws to aws service options.
     *
     * @param awsToAwsServiceOptions the aws to aws service options
     */
    public void setAwsToAwsServiceOptions(AwsToAwsServiceOptions awsToAwsServiceOptions) {
        this.awsToAwsServiceOptions = awsToAwsServiceOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemoRpcServiceOptions)) {
            return false;
        }
        DemoRpcServiceOptions that = (DemoRpcServiceOptions) o;
        return Objects.equals(appId, that.appId)
                && rpcInvokeMode == that.rpcInvokeMode
                && Objects.equals(ctripToCtripServiceOptions, that.ctripToCtripServiceOptions)
                && Objects.equals(ctripToAwsServiceOptions, that.ctripToAwsServiceOptions)
                && Objects.equals(awsToCtripServiceOptions, that.awsToCtripServiceOptions)
                && Objects.equals(awsToAwsServiceOptions, that.awsToAwsServiceOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, rpcInvokeMode, ctripToCtripServiceOptions, ctripToAwsServiceOptions, awsToCtripServiceOptions, awsToAwsServiceOptions);
    }

    @Override
    public String toString() {
        return "DemoRpcServiceOptions{" +
                "appId='" + appId + '\'' +
                ", rpcInvokeMode=" + rpcInvokeMode +
                ", ctripToCtripServiceOptions=" + ctripToCtripServiceOptions +
                ", ctripToAwsServiceOptions=" + ctripToAwsServiceOptions +
                ", awsToCtripServiceOptions=" + awsToCtripServiceOptions +
                ", awsToAwsServiceOptions=" + awsToAwsServiceOptions +
                '}';
    }
}