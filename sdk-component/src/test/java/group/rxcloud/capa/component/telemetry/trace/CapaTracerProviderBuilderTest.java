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
package group.rxcloud.capa.component.telemetry.trace;

import group.rxcloud.capa.component.telemetry.SamplerConfig;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.sdk.trace.IdGenerator;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.data.SpanData;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author: chenyijiang
 * @date: 2021/11/25 21:45
 */
public class CapaTracerProviderBuilderTest {

    @Test
    public void buildFromTraceConfg() {
        TracerConfig config = new TracerConfig();
        config.setEnableIdValidate(false);
        config.setSpanLimits(new SpanLimitsConfig());
        config.getSpanLimits().setMaxAttributeValueLength(1);
        config.getSpanLimits().setMaxNumAttributes(5);
        config.getSpanLimits().setMaxNumAttributesPerEvent(2);
        config.getSpanLimits().setMaxNumAttributesPerLink(2);
        config.getSpanLimits().setMaxNumEvents(2);
        config.getSpanLimits().setMaxNumLinks(2);

        SpanLimitsConfig outter = new SpanLimitsConfig();
        outter.setMaxAttributeValueLength(5);

        SpanProcessor processor = mock(SpanProcessor.class);
        CapaTracerProvider provider = new CapaTracerProviderBuilder()
                .setTracerConfig(config)
                .addProcessor(processor)
                .setSpanLimits(outter)
                .setIdGenerator(IdGenerator.random())
                .setSamplerConfig(() -> SamplerConfig.DEFAULT_CONFIG)
                .buildTracerProvider();
        Span spanAnother = provider.tracerBuilder("otherTracer").build().spanBuilder("otherSpan").startSpan();
        spanAnother.end(System.currentTimeMillis(), TimeUnit.MILLISECONDS);


        Span span = provider.tracerBuilder("test")
                            .build()
                            .spanBuilder("???")
                            .setSpanKind(SpanKind.SERVER)
                            .setNoParent()
                            .setAttribute("len", "aaaaaaaa")
                            .setAttribute("aaa", 1L)
                            .setAttribute("bbb", 2.0)
                            .setAttribute("ccc", true)
                            .setAttribute(AttributeKey.stringKey("ssss"), "str")
                            .addLink(spanAnother.getSpanContext())
                            .addLink(spanAnother.getSpanContext(), Attributes.builder().put(StatusAttributeKey.MESSAGE, "testing").build())
                            .startSpan();

        span.updateName("span").recordException(new RuntimeException(), Attributes.builder().put(StatusAttributeKey.MESSAGE, "fail").build());

        span.setStatus(StatusCode.OK);
        span.setAttribute("lalala", "hahaha");
        span.addEvent("countEvent", Attributes.builder().put(EventAttributeKey.COUNT, 1L).build());

        span.end();

        verify(processor).onEnd(argThat(new ArgumentMatcher<ReadableSpan>() {
            @Override
            public boolean matches(ReadableSpan span) {
                if ("span".equals(span.getName())) {
                    assertEquals("test", span.getInstrumentationLibraryInfo().getName());
                    assertEquals("span", span.getName());
                    assertEquals(SpanKind.SERVER, span.getKind());
                    assertEquals(SpanContext.getInvalid(), span.getParentSpanContext());
                    SpanData spanData = span.toSpanData();
                    assertEquals(2, spanData.getEvents().size());
                    assertEquals(2, spanData.getLinks().size());
                    assertEquals(5, spanData.getAttributes().size());
                    assertEquals(StatusCode.OK, spanData.getStatus().getStatusCode());
                    return span.getAttribute(AttributeKey.stringKey("len")).length() == 5;
                }
                return false;
            }
        }));
    }

    @Test
    public void buildFromTraceConfig() {
        CapaTracerProvider provider = new CapaTracerProviderBuilder()
                .setSamplerConfig(() -> SamplerConfig.DEFAULT_CONFIG)
                .buildTracerProvider();

        Span span = provider.tracerBuilder("test")
                            .setSchemaUrl("url")
                            .setInstrumentationVersion("1.1.1")
                            .build()
                            .spanBuilder("span2")
                            .setSpanKind(SpanKind.SERVER)
                            .setAttribute("len", "aaaaaaaa")
                            .startSpan();

        span.recordException(new RuntimeException(), Attributes.builder().put(StatusAttributeKey.MESSAGE, "fail").build());

        span.end();

        assertTrue(TestSpanProcessor.called("span2"));
    }
}