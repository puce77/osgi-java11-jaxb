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


import com.github.puce77.osgi.java11.jaxb.startup.main.impl.AdditionalArgumentsProviderImpl;
import com.github.puce77.osgi.java11.jaxb.startup.main.impl.osgi.OSGiStarter;
import org.drombler.commons.client.startup.main.ApplicationInstanceEvent;
import org.drombler.commons.client.startup.main.ApplicationInstanceListener;
import org.drombler.commons.client.startup.main.BootServiceStarter;
import org.drombler.commons.client.startup.main.DromblerClientStarter;
import org.drombler.commons.client.startup.main.MissingPropertyException;
import org.drombler.commons.client.startup.main.cli.CommandLineArgs;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.launch.Framework;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The app starter makes sure all registered {@link BootServiceStarter}s gets started with special support to start the OSGi framework.
 *
 * @author puce
 * @param <T> the configuration type
 */
public class AppStarter<T extends AppConfiguration> extends DromblerClientStarter<T> {

    /**
     * Runs the AppStarter as an application.<br>
     * <br>
     * It supports {@link CommandLineArgs} and its switches.
     *
     * @param args the command line args
     * @throws URISyntaxException
     * @throws IOException
     * @throws MissingPropertyException
     * @throws BundleException
     * @throws InterruptedException
     * @throws Exception
     */
    public static void main(String[] args) throws URISyntaxException, IOException,
            MissingPropertyException, BundleException, InterruptedException, Exception {
        CommandLineArgs commandLineArgs = CommandLineArgs.parseCommandLineArgs(args);
        AppStarter<AppConfiguration> starter = new AppStarter<>(new AppConfiguration(commandLineArgs));
        if (starter.init()) {
            starter.start();
        }
    }

    private final OSGiStarter osgiStarter;

    /**
     * Creates a new instance of this class.
     *
     * @param configuration the configuration
     */
    public AppStarter(T configuration) {
        super(configuration);
        this.osgiStarter = new OSGiStarter(configuration, true);
        addAdditionalStarters(osgiStarter);
    }

    /**
     * {@inheritDoc }
     *
     * Registers the additional command line args as an {@link AdditionalArgumentsProvider} OSGi service.
     */
    @Override
    public void start() {
        super.start();
        getFramework().getBundleContext().
                addFrameworkListener((FrameworkEvent event) -> {
                    if (event.getType() == FrameworkEvent.STARTED) {
                        fireAdditionalArguments(new ApplicationInstanceEvent(this, getConfiguration().getCommandLineArgs().getAdditionalArguments()));
                    }
                });
    }

    private void fireAdditionalArguments(ApplicationInstanceEvent event) {
        osgiStarter.getFramework().getBundleContext().registerService(AdditionalArgumentsProvider.class,
                new AdditionalArgumentsProviderImpl(event.getAdditionalArgs()), null);

    }

    /**
     * Gets the OSGi framework.
     *
     * @return the OSGi framework
     */
    public final Framework getFramework() {
        return osgiStarter.getFramework();
    }

    /**
     * Gets an {@link ApplicationInstanceListener} implementation which registers the additional command line args as an {@link AdditionalArgumentsProvider} OSGi service.
     *
     * @return an application instance listener
     */
    @Override
    protected ApplicationInstanceListener getApplicationInstanceListener() {
        return this::fireAdditionalArguments;
    }

}
