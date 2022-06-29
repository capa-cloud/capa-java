package group.rxcloud.capa;

import java.util.List;

public abstract class AbstractCapaClient implements CapaClient {

    protected List<String> registryNames;

    @Override
    public List<String> registryNames() {
        return registryNames;
    }
}
