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
package group.rxcloud.capa.component.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Default impl og log plugin.
 */
public class DefaultLogPlugin implements LogPlugin {

    private static final Logger log = LoggerFactory.getLogger("CapaTagLogger");

    public static String buildLogData(String scenario, Map<String, String> tags) {
        StringBuilder builder = new StringBuilder("[[scenario=").append(scenario);
        if (tags != null) {
            tags.forEach((k, v) -> builder.append(',').append(k).append('=').append(v));
        }
        builder.append("]]");
        return builder.toString();
    }

    @Override
    public void logTags(String scenario, Map<String, String> tags) {
        log.info(buildLogData(scenario, tags));
    }

    @Override
    public boolean logsCanOutput(String logLevel) {
        return true;
    }

    @Override
    public boolean logsCanOutputByTags(Map<String, String> tags) {
        return true;
    }

}
