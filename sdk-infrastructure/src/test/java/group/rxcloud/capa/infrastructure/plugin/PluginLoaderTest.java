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
package group.rxcloud.capa.infrastructure.plugin;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class PluginLoaderTest {

    @Test
    public void loadPluginImpl_spi() {
        Optional<Plugin> pluginOp = PluginLoader.loadPluginImpl(Plugin.class);
        Plugin plugin = pluginOp.get();
        assertNotEquals(plugin.sayHello(), "hello");
    }

    @Test
    public void loadPluginImpl_default() {
        Plugin plugin = PluginLoader.loadPluginImpl(Plugin.class, () -> new Plugin() {
            @Override
            public String sayHello() {
                return "hhh";
            }
        });
        assertNotEquals(plugin.sayHello(), "hello");
    }
}