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
package com.github.puce77.osgi.java11.jaxb.startup.main;

import java.util.List;

/**
 * Provides the list of additional arguments passed to the application.<br>
 * <br>
 * Note: If this is a single instance application a new instance of this service is registered for every application start notification.<br>
 * <br>
 * With the OSGi annotations (Declarative Services) you can listen for multiple instances with the following method annotation:<br>
 * {@code  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)}
 *
 * @author puce
 */
public interface AdditionalArgumentsProvider {

    /**
     * Gets the list of additional arguments passed to the application.
     *
     * @return the list of additional arguments passed to the application
     */
    List<String> getAdditionalArguments();
}
