/*
 * Copyright 2011 Harald Wellmann
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
package org.ops4j.pax.exam.invoker.junit.internal;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.ops4j.pax.exam.util.Injector;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JUnit {@link Runner} which is aware of an {@link Injector} and a {@link BundleContext} for
 * injecting dependencies from the OSGi service registry.
 * 
 * @author Harald Wellmann
 * 
 */
public class ContainerTestRunner extends BlockJUnit4ClassRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ContainerTestRunner.class);

    private Injector injector;

    /**
     * Constructs a runner for the given class which will be injected with dependencies from the
     * given bundle context by the given injector
     * 
     * @param klass
     *            test class to be run
     * @param injector
     *            injector for injecting dependencies
     * @throws InitializationError when test class cannot be initialized           
     */
    public ContainerTestRunner(Class<?> klass, Injector injector) throws InitializationError {
        super(klass);
        this.injector = injector;
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        injector.injectFields(test);
        return test;
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        LOG.info("running {} in reactor", method.getName());
        super.runChild(method, notifier);
    }
}
