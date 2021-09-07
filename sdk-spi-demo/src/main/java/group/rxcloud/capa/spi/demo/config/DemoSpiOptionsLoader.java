package group.rxcloud.capa.spi.demo.config;

import group.rxcloud.capa.infrastructure.env.CapaEnvironment;
import group.rxcloud.capa.spi.config.CapaSpiOptionsLoader;

import java.util.Objects;

public class DemoSpiOptionsLoader implements CapaSpiOptionsLoader<DemoRpcServiceOptions> {

    /**
     * TestService
     */
    private static final String TEST_SERVICE_CODE = "22687";
    private static final String TEST_SERVICE_NAME = "TestSOAService";
    private static final String TEST_HOST_NAME = "hello-world-service.app-namespace.svc.cluster.local";
    private static final String TEST_HOST_PORT = "8888";

    @Override
    public DemoRpcServiceOptions loadRpcServiceOptions(String appId) {
        Objects.requireNonNull(appId, "appId");
        CapaEnvironment.DeployCloudEnvironment deployCloudEnvironment = CapaEnvironment.getDeployCloudEnvironment();
        CapaEnvironment.DeployVpcEnvironment deployVpcEnvironment = CapaEnvironment.getDeployVpcEnvironment();
        DemoRpcServiceOptions rpcServiceOptions = null;
        switch (deployCloudEnvironment) {
            case CTRIP_IDC: {
                // ctirp -> ctrip
                String serviceCode;
                serviceCode = TEST_SERVICE_CODE;
                String serviceName;
                serviceName = TEST_SERVICE_NAME;
                rpcServiceOptions = new DemoRpcServiceOptions(appId, DemoRpcServiceOptions.ServiceRpcInvokeMode.CTRIP_TO_CTRIP);
                DemoRpcServiceOptions.CtripToCtripServiceOptions ctripToCtripServiceOptions =
                        new DemoRpcServiceOptions.CtripToCtripServiceOptions(serviceCode, serviceName, deployVpcEnvironment);
                rpcServiceOptions.setCtripToCtripServiceOptions(ctripToCtripServiceOptions);
                break;
            }
            case AWS_CLOUD: {
                // aws -> aws
                String hostName;
                hostName = TEST_HOST_NAME;
                String hostPort;
                hostPort = TEST_HOST_PORT;
                rpcServiceOptions = new DemoRpcServiceOptions(appId, DemoRpcServiceOptions.ServiceRpcInvokeMode.AWS_TO_AWS);
                DemoRpcServiceOptions.AwsToAwsServiceOptions awsToAwsServiceOptions =
                        new DemoRpcServiceOptions.AwsToAwsServiceOptions(hostName, hostPort, deployVpcEnvironment);
                rpcServiceOptions.setAwsToAwsServiceOptions(awsToAwsServiceOptions);
            }
        }
        return rpcServiceOptions;
    }
}
