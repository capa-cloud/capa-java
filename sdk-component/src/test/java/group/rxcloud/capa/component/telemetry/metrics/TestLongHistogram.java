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
package group.rxcloud.capa.component.telemetry.metrics;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.BoundLongHistogram;
import io.opentelemetry.context.Context;

/**
 * @author: chenyijiang
 * @date: 2021/12/1 20:08
 */
public class TestLongHistogram extends CapaLongHistogram{

    public TestLongHistogram(String meterName, String schemaUrl, String version, String name, String description,
                             String unit) {
        super(meterName, schemaUrl, version, name, description, unit);
    }

    @Override
    public void record(long value) {

    }

    @Override
    public void record(long value, Attributes attributes) {

    }

    @Override
    public void record(long value, Attributes attributes, Context context) {

    }

    @Override
    public BoundLongHistogram bind(Attributes attributes) {
        return null;
    }
}
