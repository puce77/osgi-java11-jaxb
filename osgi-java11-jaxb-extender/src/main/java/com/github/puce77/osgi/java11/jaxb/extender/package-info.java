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
@Requirement(namespace = ExtenderNamespace.EXTENDER_NAMESPACE,
        filter = "(osgi.extender=osgi.serviceloader.processor)")
@Requirement(namespace = "osgi.serviceloader",
        filter = "(osgi.serviceloader=javax.xml.bind.JAXBContextFactory)",
        cardinality = Requirement.Cardinality.MULTIPLE)
package com.github.puce77.osgi.java11.jaxb.extender;

import org.osgi.namespace.extender.ExtenderNamespace;
import org.osgi.annotation.bundle.Requirement;