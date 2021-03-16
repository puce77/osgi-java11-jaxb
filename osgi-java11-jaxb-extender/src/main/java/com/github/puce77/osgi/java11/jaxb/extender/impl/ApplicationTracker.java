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
package com.github.puce77.osgi.java11.jaxb.extender.impl;


import com.github.puce77.osgi.java11.jaxb.extender.ExtensionPoint;
import com.github.puce77.osgi.java11.jaxb.extender.jaxb.ApplicationType;
import com.github.puce77.osgi.java11.jaxb.extender.jaxb.ExtensionsType;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContextFactory;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.puce77.osgi.java11.jaxb.extender.impl.JAXBUtils.createJAXBContext;

/**
 *
 * @author puce
 */
@Component(immediate = true)
public class ApplicationTracker {

    public static final String APPLICATION_XML_RELATIVE_NAME = "META-INF/osgiapp/application.xml";

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationTracker.class);

    private BundleTracker<ApplicationType> bundleTracker;
    private final Set<Class<?>> jaxbRootClassesSet = new HashSet<>(Arrays.asList(ApplicationType.class));
    private final Map<Long, List<ServiceRegistration<?>>> serviceRegistrations = new HashMap<>();
    private final Set<Bundle> unresolvedExtensions = new LinkedHashSet<>();

    @Reference
    private JAXBContextFactory jaxbContextFactory;

    @Activate
    public void activate(BundleContext context) {
        LOG.debug("JAXBContextFactory implementations class: " + jaxbContextFactory.getClass());
        bundleTracker = new BundleTracker<>(context, Bundle.ACTIVE,
                new BundleTrackerCustomizer<>() {

                    @Override
                    public ApplicationType addingBundle(Bundle bundle, BundleEvent event) {
                        return registerExtensions(bundle);
                    }

                    @Override
                    public void modifiedBundle(Bundle bundle, BundleEvent event, ApplicationType application) {
                        // TODO: ???
                    }

                    @Override
                    public void removedBundle(Bundle bundle, BundleEvent event, ApplicationType application) {
                        unregisterExtensions(bundle);
                    }
                });
        bundleTracker.open();
    }

    @Deactivate
    public void deactivate() {
        bundleTracker.close();
    }

    private ApplicationType registerExtensions(Bundle bundle) {
        URL actionsURL = bundle.getEntry(APPLICATION_XML_RELATIVE_NAME);
        if (actionsURL != null) {
            try {
                JAXBContext jaxbContext = createJAXBContext(jaxbContextFactory, getJAXBRootPackageNames(), bundle);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                ApplicationType application = (ApplicationType) unmarshaller.unmarshal(actionsURL);
                if (loadedExtensionsSuccessfully(application.getExtensions())) {
                    if (unresolvedExtensions.contains(bundle)) {
                        unresolvedExtensions.remove(bundle);
                    }
                    registerExtensions(bundle, application.getExtensions());
                    return application;
                } else {
                    if (!unresolvedExtensions.contains(bundle)) {
                        unresolvedExtensions.add(bundle);
                    }
                }
            } catch (JAXBException ex) {
                // TODO: ???
                LOG.error(ex.getMessage(), ex);
            }
        }
        return null;
    }

    private Set<String> getJAXBRootPackageNames() {
        return jaxbRootClassesSet.stream().map(Class::getPackageName).collect(Collectors.toSet());
    }

    private boolean loadedExtensionsSuccessfully(ExtensionsType extensions) {
        boolean successfullyLoaded = true;
        for (Object extension : extensions.getAny()) {
            if (!jaxbRootClassesSet.contains(extension.getClass())) {
                successfullyLoaded = false;
                break;
            }
        }
        return successfullyLoaded;
    }

    private void registerExtensions(Bundle bundle, ExtensionsType extensions) {
        final List<ServiceRegistration<?>> registrations = extensions.getAny().stream().
                map(extension
                        -> bundle.getBundleContext().registerService(extension.getClass().getName(), extension, null)).
                collect(Collectors.toList());
        serviceRegistrations.put(bundle.getBundleId(), registrations);
    }

    private void unregisterExtensions(Bundle bundle) {
        if (serviceRegistrations.containsKey(bundle.getBundleId())) {
            serviceRegistrations.remove(bundle.getBundleId()).forEach(ServiceRegistration::unregister);
        }
        if (unresolvedExtensions.contains(bundle)) {
            unresolvedExtensions.remove(bundle);
        }
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void bindExtensionPoint(ExtensionPoint<?> extensionPoint) {
        jaxbRootClassesSet.add(extensionPoint.getJAXBRootClass());

        if (!unresolvedExtensions.isEmpty()) {
            // avoid concurrent modification // TODO: needed here?
            List<Bundle> extensionBundles = new ArrayList<>(unresolvedExtensions);
            extensionBundles.forEach(this::registerExtensions);
        }
    }

    public void unbindExtensionPoint(ExtensionPoint<?> extensionPoint) {
        jaxbRootClassesSet.remove(extensionPoint.getJAXBRootClass());
//        unregisterExtensions
    }

}
