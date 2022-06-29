/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package group.rxcloud.capa.rpc;


import group.rxcloud.capa.component.CapaRpcProperties;
import group.rxcloud.capa.component.http.CapaHttpBuilder;
import group.rxcloud.capa.rpc.domain.CapaApiProtocol;

import java.util.function.Supplier;

/**
 * A builder for the {@link CapaRpcClient}, Currently only HTTP Client will be supported.
 */
public class CapaRpcClientBuilder {

    /**
     * Determine if this builder will create Rpc client with HTTP/... clients.
     */
    private final CapaApiProtocol apiProtocol;

    /**
     * Builder for Capa's HTTP Client.
     */
    private final CapaHttpBuilder httpBuilder;

    /**
     * Creates a constructor for {@link CapaRpcClient}.
     */
    public CapaRpcClientBuilder() {
        this(new CapaHttpBuilder());
    }

    /**
     * Creates a constructor for {@link CapaRpcClient} with custom {@link CapaHttpBuilder}.
     */
    public CapaRpcClientBuilder(Supplier<CapaHttpBuilder> capaHttpBuilderSupplier) {
        this(capaHttpBuilderSupplier.get());
    }

    /**
     * Creates a constructor for {@link CapaRpcClient}.
     */
    public CapaRpcClientBuilder(CapaHttpBuilder httpBuilder) {
        this.apiProtocol = CapaApiProtocol.parseProtocol(CapaRpcProperties.Settings.getApiProtocol());
        this.httpBuilder = httpBuilder;
    }

    /**
     * Build an instance of the Client based on the provided setup.
     *
     * @return an instance of the setup Client
     * @throws IllegalStateException if any required field is missing
     */
    public CapaRpcClient build() {
        return buildCapaClient(this.apiProtocol);
    }

    /**
     * Creates an instance of a Capa Client based on the chosen protocol.
     *
     * @param protocol Capa API's protocol.
     * @return the GRPC Client.
     * @throws IllegalStateException if either host is missing or if port is missing or a negative number.
     */
    private CapaRpcClient buildCapaClient(CapaApiProtocol protocol) {
        if (protocol == null) {
            throw new IllegalStateException("[Capa] ApiProtocol is required.");
        }
        if (protocol == CapaApiProtocol.HTTP) {
            return buildCapaClientHttp();
        }
        if (protocol == CapaApiProtocol.GRPC) {
            return buildCapaClientGrpc();
        }
        throw new IllegalStateException("[Capa] Unsupported ApiProtocol: " + protocol.name());
    }

    /**
     * Creates and instance of CapaClient over HTTP.
     *
     * @return CapaClient over HTTP.
     */
    private CapaRpcClient buildCapaClientHttp() {
        return new CapaRpcClientHttp(this.httpBuilder.build());
    }

    /**
     * Creates and instance of CapaClient over GRPC.
     *
     * @return CapaClient over GRPC.
     */
    private CapaRpcClient buildCapaClientGrpc() {
        return new CapaRpcClientGrpc();
    }
}
