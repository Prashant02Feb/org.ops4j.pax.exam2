/*
 * Copyright 2011 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.regression.pde.impl;

import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Hashtable;

import org.ops4j.pax.exam.regression.pde.HelloService;
import org.ops4j.pax.exam.regression.pde.Notifier;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator
{

    public void start( BundleContext bc ) throws Exception
    {
        // register two HelloService implementations
        EnglishHelloService service = new EnglishHelloService();
        Hashtable<String, String> props = new Hashtable<String, String>();
        props.put( "language", "en" );
        bc.registerService( HelloService.class.getName(), service, props );

        LatinHelloService latinService = new LatinHelloService();
        Hashtable<String, String> latinProps = new Hashtable<String, String>();
        latinProps.put( "language", "la" );
        bc.registerService( HelloService.class.getName(), latinService, latinProps );

        // Notify a remote listener of bundle startup.
        // This is used for reactor strategy regression tests
        String rmiProperty = System.getProperty( "pax.exam.regression.rmi" );
        if( rmiProperty != null )
        {
            notifyRemoteListener( rmiProperty );
        }
    }

    private void notifyRemoteListener( String rmiProperty ) throws Exception
    {
        int rmiPort = Integer.parseInt( rmiProperty );
        Registry registry = LocateRegistry.getRegistry( rmiPort );
        Remote remote = registry.lookup( "PaxExamNotifier" );
        Notifier notifier = (Notifier) remote;
        notifier.send( "bundle org.ops4j.pax.exam.regression.pde started" );
    }

    /**
     * Optionally blocks framework shutdown for a shutdown timeout regression test.
     */
    public void stop( BundleContext bc ) throws Exception
    {

        String blockOnStop = System.getProperty( "pax.exam.regression.blockOnStop", "false" );
        if( Boolean.valueOf( blockOnStop ) )
        {
            Thread.sleep( Long.MAX_VALUE );
        }
    }
}
