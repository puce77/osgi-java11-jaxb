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
package com.github.puce77.osgi.java11.jaxb.startup.main.impl;

import com.github.puce77.osgi.java11.jaxb.startup.main.AdditionalArgumentsProvider;

import java.util.List;

/**
 *
 * @author puce
 */
public class AdditionalArgumentsProviderImpl implements AdditionalArgumentsProvider {

    private final List<String> additionalArguments;

    public AdditionalArgumentsProviderImpl(List<String> additionalArguments) {
        this.additionalArguments = additionalArguments;
    }

    @Override
    public List<String> getAdditionalArguments() {
        return additionalArguments;
    }

}
