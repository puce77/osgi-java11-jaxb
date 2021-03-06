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
package com.github.puce77.osgi.java11.jaxb.extender;


/**
 * The Extension Point interface. Extensions can be registered in the application file.<br>
 * <br>
 * Note: More method might be added in the future!<br>
 * <br>
 * This is a SPI interface. Modules which want to add support for new Extension Points must register an implementation of this interface as an OSGi service per Extension Point.<br>
 * <br>
 * It's a good practice to provide annotations and an annotation processor to generate the extension point configuration. Also consider to provide a type-safe descriptor class.
 *
 * @param <T> the type of the JAXB root class of this Extension Point.
 * @author puce
 */
public interface ExtensionPoint<T> {

    /**
     * Gets the extension JAXB root class.
     *
     * @return the extension JAXB root class
     */
    Class<T> getJAXBRootClass();

//    Collection<T> getExtensions();
}
