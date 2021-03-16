package com.github.puce77.osgi.java11.jaxb.extender.impl;

import com.github.puce77.osgi.java11.jaxb.extender.jaxb.ObjectFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContextFactory;
import javax.xml.bind.JAXBException;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

public final class JAXBUtils {
    private static final Logger LOG = LoggerFactory.getLogger(JAXBUtils.class);

    private JAXBUtils() {
    }

    /**
     * Creates a JAXB context.
     *
     * @return a JAXB context
     * @throws JAXBException
     */
    public static JAXBContext createJAXBContext(JAXBContextFactory jaxbContextFactory, Set<String> jaxbPackages, Bundle bundle) throws JAXBException {
        LOG.debug("Bundle symbolic name: "+bundle.getSymbolicName());
        ServiceLoader<JAXBContextFactory> jaxbContextFactoryLoader = ServiceLoader.load(JAXBContextFactory.class, JAXBUtils.class.getClassLoader());
        for (JAXBContextFactory factory : jaxbContextFactoryLoader) {
            LOG.debug("Factory found!!!!! " + factory.getClass().getName());
        }
//        return JAXBContext.newInstance(calculateContextPath(jaxbPackages), ObjectFactory.class.getClassLoader());
//        return JAXBContext.newInstance(calculateContextPath(jaxbPackages), bundle.adapt(BundleWiring.class).getClassLoader());
//        return jaxbContextFactory.createContext(calculateContextPath(jaxbPackages), ObjectFactory.class.getClassLoader(), Map.of());
        ClassLoader classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
        return jaxbContextFactory.createContext(calculateContextPath(jaxbPackages), classLoader, Map.of());
    }

    private static String calculateContextPath(Set<String> jaxbPackages) {
        return String.join(":", jaxbPackages.toArray(new String[jaxbPackages.size()]));
    }
}
