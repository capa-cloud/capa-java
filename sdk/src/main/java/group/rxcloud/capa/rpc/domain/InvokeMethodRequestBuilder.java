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

package group.rxcloud.capa.rpc.domain;


import group.rxcloud.cloudruntimes.domain.core.invocation.HttpExtension;
import group.rxcloud.cloudruntimes.domain.core.invocation.InvokeMethodRequest;

import java.util.Map;

/**
 * Builds a request to invoke a service.
 */
public class InvokeMethodRequestBuilder {

    private final String appId;

    private final String method;

    private String contentType;

    private Object body;

    private HttpExtension httpExtension = HttpExtension.NONE;

    private Map<String, String> metadata;

    public InvokeMethodRequestBuilder(String appId, String method) {
        this.appId = appId;
        this.method = method;
    }

    public InvokeMethodRequestBuilder withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public InvokeMethodRequestBuilder withBody(Object body) {
        this.body = body;
        return this;
    }

    public InvokeMethodRequestBuilder withHttpExtension(HttpExtension httpExtension) {
        this.httpExtension = httpExtension;
        return this;
    }

    public InvokeMethodRequestBuilder withMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Builds a request object.
     *
     * @return Request object.
     */
    public InvokeMethodRequest build() {
        InvokeMethodRequest request = new InvokeMethodRequest(this.appId, this.method);
        request.setContentType(this.contentType);
        request.setBody(this.body);
        request.setHttpExtension(this.httpExtension);
        request.setMetadata(this.metadata);
        return request;
    }
}
