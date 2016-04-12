/*
 * Copyright 2012 Harald Wellmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.testng.test;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.testng.Assert.assertNotNull;

import javax.inject.Inject;

import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Info;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.testng.inject.InjectConfigurationFactory;
import org.ops4j.pax.exam.testng.listener.PaxExam;
import org.osgi.framework.BundleContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({ PaxExam.class })
public class TestNGExamTest {

    @Inject
    private BundleContext bc;

    @Configuration
    public Option[] config() {
        return options(
            InjectConfigurationFactory.regressionDefaults(),
            mavenBundle("org.testng", "testng", "6.9.10"),
            mavenBundle("com.beust", "jcommander", "1.48"),
            mavenBundle("org.ops4j.pax.exam", "pax-exam-invoker-testng", Info.getPaxExamVersion()),
            systemProperty("osgi.console").value("6666"));
    }

    @Test
    public void helloTestNG() {
        assertNotNull(bc);
        System.out.println("Hello TestNG!");
    }

    @Test
    public void helloPaxExam() {
        System.out.println("Hello Pax Exam!");
    }
}
