/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.weld.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AfterTypeDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.ObserverMethod;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessBeanAttributes;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessManagedBean;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.ProcessProducerField;
import javax.enterprise.inject.spi.ProcessProducerMethod;
import javax.enterprise.inject.spi.ProcessSessionBean;
import javax.enterprise.inject.spi.ProcessSyntheticAnnotatedType;

import org.jboss.weld.bootstrap.SpecializationAndEnablementRegistry;
import org.jboss.weld.event.ExtensionObserverMethodImpl;
import org.jboss.weld.event.ObserverMethodImpl;
import org.jboss.weld.manager.BeanManagerImpl;
import org.jboss.weld.util.reflection.Reflections;

/**
 * @author pmuir
 */
public class Observers {

    /*
     * Contains all container lifecycle event types
     */
    public static final Set<Class<?>> CONTAINER_LIFECYCLE_EVENT_TYPES;
    /*
     * Contains only top superinterfaces of each chain of container lifecycle event types.
     */
    public static final Set<Class<?>> CONTAINER_LIFECYCLE_EVENT_CANONICAL_SUPERTYPES;

    private Observers() {
    }

    static {
        Set<Class<?>> canonicalSupertypes = new HashSet<Class<?>>();
        canonicalSupertypes.add(BeforeBeanDiscovery.class);
        canonicalSupertypes.add(AfterTypeDiscovery.class);
        canonicalSupertypes.add(AfterBeanDiscovery.class);
        canonicalSupertypes.add(AfterDeploymentValidation.class);
        canonicalSupertypes.add(BeforeShutdown.class);
        canonicalSupertypes.add(ProcessAnnotatedType.class);
        canonicalSupertypes.add(ProcessInjectionPoint.class);
        canonicalSupertypes.add(ProcessInjectionTarget.class);
        canonicalSupertypes.add(ProcessProducer.class);
        canonicalSupertypes.add(ProcessBeanAttributes.class);
        canonicalSupertypes.add(ProcessBean.class);
        canonicalSupertypes.add(ProcessObserverMethod.class);
        CONTAINER_LIFECYCLE_EVENT_CANONICAL_SUPERTYPES = Collections.unmodifiableSet(canonicalSupertypes);

        Set<Class<?>> types = new HashSet<Class<?>>(CONTAINER_LIFECYCLE_EVENT_CANONICAL_SUPERTYPES);
        types.add(ProcessSyntheticAnnotatedType.class);
        types.add(ProcessSessionBean.class);
        types.add(ProcessManagedBean.class);
        types.add(ProcessProducerMethod.class);
        types.add(ProcessProducerField.class);
        CONTAINER_LIFECYCLE_EVENT_TYPES = Collections.unmodifiableSet(types);

    }

    public static boolean isContainerLifecycleObserverMethod(ObserverMethod<?> method) {
        return CONTAINER_LIFECYCLE_EVENT_TYPES.contains(Reflections.getRawType(method.getObservedType()))
                || (Object.class.equals(method.getObservedType()) && method instanceof ExtensionObserverMethodImpl<?, ?>);
    }

    public static boolean isObserverMethodEnabled(ObserverMethod<?> method, BeanManagerImpl manager) {
        if (method instanceof ObserverMethodImpl<?, ?>) {
            Bean<?> declaringBean = Reflections.<ObserverMethodImpl<?, ?>> cast(method).getDeclaringBean();
            return manager.getServices().get(SpecializationAndEnablementRegistry.class).isCandidateForLifecycleEvent(declaringBean);
        }
        return true;
    }
}
