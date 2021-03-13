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
package com.github.puce77.osgi.java11.jaxb.startup.main.impl.osgi;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author puce
 */
enum BundleAction {

    INSTALL("install"),
    START("start"),
    UPDATE("update"),
    UNINSTALL("uninstall");
    private static final Map<String, BundleAction> bundleActions = new HashMap<>(BundleAction.values().length);

    static {
        for (BundleAction bundleAction : values()) {
            bundleActions.put(bundleAction.id, bundleAction);
        }
    }
    private final String id;

    private BundleAction(String id) {
        this.id = id;
    }

    public static BundleAction getBundleAction(String bundleActionId) {
        return bundleActions.get(bundleActionId);
    }
}
