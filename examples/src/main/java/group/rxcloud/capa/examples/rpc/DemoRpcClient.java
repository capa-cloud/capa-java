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
package group.rxcloud.capa.examples.rpc;

import group.rxcloud.capa.rpc.CapaRpcClient;
import group.rxcloud.capa.rpc.CapaRpcClientBuilder;
import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.utils.TypeRef;
import reactor.core.publisher.Mono;

public class DemoRpcClient {

    /**
     * Identifier in Capa for the service this client will invoke.
     */
    private static final String SERVICE_APP_ID = "test";

    public static void main(String[] args) {
        CapaRpcClient capaRpcClient = new CapaRpcClientBuilder().build();

        Mono<byte[]> responseMono = capaRpcClient.invokeMethod(SERVICE_APP_ID, "hello", "hello", HttpExtension.POST, null, TypeRef.BYTE_ARRAY);

        byte[] response = responseMono.block();

        System.out.println(new String(response));
    }
}
