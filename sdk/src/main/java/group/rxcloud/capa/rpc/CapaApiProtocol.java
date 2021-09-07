/*
 * Copyright (c) Microsoft Corporation and Dapr Contributors.
 * Licensed under the MIT License.
 */

package group.rxcloud.capa.rpc;

/**
 * Transport protocol for Capa's API.
 */
public enum CapaApiProtocol {

    /**
     * HTTP/1.1
     */
    HTTP;

    /**
     * Parse protocol to {@link CapaApiProtocol}.
     *
     * @param protocol the protocol
     * @return the capa api protocol
     */
    public static CapaApiProtocol parseProtocol(String protocol) {
        // FIXME: Fix to HTTP
        return HTTP;
    }
}
