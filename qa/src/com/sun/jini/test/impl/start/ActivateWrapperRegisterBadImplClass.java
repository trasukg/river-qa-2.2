/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sun.jini.test.impl.start;

import java.util.logging.Level;

import java.rmi.*;
import java.rmi.activation.*;

import com.sun.jini.start.*;
import com.sun.jini.start.ActivateWrapper.*;
import com.sun.jini.start.ServiceStarter.*;
import com.sun.jini.qa.harness.TestException;

/**
 * This test verifies that ActivateWrapper.register() throws the 
 * appropriate exception when passed an invalid implementation
 * class name. That is, a class that cannot be obtained from the
 * import location.
 */

public class ActivateWrapperRegisterBadImplClass extends AbstractStartBaseTest {

    public void run() throws Exception {
        logger.log(Level.INFO, "run()");
	try { 
	    net.jini.event.EventMailbox mailbox =
		    (net.jini.event.EventMailbox)
		    manager.startService("net.jini.event.EventMailbox");
            logger.log(Level.INFO, "Created bad impl object");
            throw new TestException( "ActivateWrapper.register()"
		    + " did not throw expected exception" );
	} catch (Exception ae) {
	    if (verifyClassNotFoundException(ae)) {
                logger.log(Level.INFO, "Expected Exception was thrown: " + ae);
            } else {
                throw new TestException("Unexpected Exception", ae);
            }
	}
    }
}
