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
package group.rxcloud.capa.examples.log;

import lombok.extern.slf4j.Slf4j;

/**
 * An application cannot use log4j and logback configuration to print logs at the same time.
 * So if you want to test the log4j2 configuration to print logs, then you need to copy the resources/xml/log4j2.xml file to the resources directory, and then add log4j-slf4j-impl dependency to the pom file.
 * Else if you want to use logback configuration to print logs, then the resources/xml/logback.xml file needs to be copied to the resources path, and the logback-classic dependency needs to be added to the pom file.
 * Notice:
 * 1. Resources cannot contain log4j2.xml and logback.xml files at the same time,
 * 2. log4j-slf4j-impl and logback-classic cannot exist at the same time.
 */
@Slf4j
public class DemoLog {

    public static void main(String[] args) {
        try {
            log.info("test");
        } catch (Exception e) {
            System.out.println();
        }
    }
}
