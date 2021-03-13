/*
 *         COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Notice
 *
 * The contents of this file are subject to the COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL)
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.opensource.org/licenses/cddl1.txt
 *
 * The Original Code is osgi-java11-jaxb. The Initial Developer of the
 * Original Code is Florian Brunner (GitHub user: puce77).
 * Copyright 2021 Florian Brunner. All Rights Reserved.
 *
 * Contributor(s): .
 */
package com.github.puce77.osgi.java11.jaxb.sampleextension.impl;

import com.github.puce77.osgi.java11.jaxb.sampleextensionpoint.jaxb.SampleExtensionType;
import com.github.puce77.osgi.java11.jaxb.sampleextensionpoint.jaxb.SampleExtensionsType;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author puce
 */
@Component(immediate = true)
public class SampleExtensionHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(SampleExtensionHandler.class);

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindStatusBarElementsType(ServiceReference<SampleExtensionsType> serviceReference) {
        BundleContext context = serviceReference.getBundle().getBundleContext();
        SampleExtensionsType sampleExtensionsType = context.getService(serviceReference);
        registerSampleExtensions(sampleExtensionsType);
    }

    public void unbindStatusBarElementsType(SampleExtensionsType sampleExtensionsType) {
        // TODO
    }

    private void registerSampleExtensions(SampleExtensionsType sampleExtensionsType) {
        sampleExtensionsType.getSampleExtension().forEach(this::registerSampleExtension);
    }

    private void registerSampleExtension(SampleExtensionType sampleExtensionType) {
        LOG.info("Sample Extension message found: {}", sampleExtensionType.getMessage());
    }

}
