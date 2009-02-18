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
package com.sun.jini.test.spec.jeri.basicjeriexporter;

import java.util.logging.Level;

/**
 * Purpose: This test verifies that the <code>unexport</code>
 * method behaves as specified.
 * Use Case: Unexporting a remote object that has remote calls in progress
 *     with force flag set to true.
 * <br>
 * For Testing Use Case:
 * <ol>
 * <li>Construct a <code>BasicJeriExporter</code> using an instrumented
 *     <code>ServerEnpoint</code> implementation.</li>
 * <li>Construct an instance of a test remote object and export the instance
 *     using the exporter constructed in step 1.</li>
 * <li>Make a blocking call on the remote object.</li>
 * <li>Call the <code>unexport</code> method on the exporter with the
 *     force flag set to true.</li>
 * <li>Verify that <code>close</code> is called on the instrumented
 *     <code>ServerEnpointListener</code>.</li>
 * <li>Verify that the <code>unexport</code> method returns true.</li>
 * </ol>
 */

//test harness related imports
import com.sun.jini.qa.harness.TestException;

//overture imports
import net.jini.jeri.BasicJeriExporter;
import net.jini.jeri.tcp.TcpServerEndpoint;

//utility classes
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.BJEAbstractTest;
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.BJETestILFactory;
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.BJETestService;
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.TestServerEndpoint;
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.BJETestServiceImpl;
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.BJETransportListener;
import com.sun.jini.test.spec.jeri.basicjeriexporter.util.TransportListener;

//java.lang.reflect
import java.lang.reflect.Method;

//java.rmi
import java.rmi.RemoteException;
import java.rmi.server.ExportException;

public class UnexportTest_InProgressForceTrue extends BJEAbstractTest {

    private static volatile boolean closeCalled = false;
    private static volatile boolean serviceMethodCalled = false;

    /**
     * For Testing Use Case:
     * <ol>
     * <li>Construct a <code>BasicJeriExporter</code> using an instrumented
     *     <code>ServerEnpoint</code> implementation.</li>
     * <li>Construct an instance of a test remote object and export the instance
     *     using the exporter constructed in step 1.</li>
     * <li>Call the <code>unexport</code> method on the exporter with the
     *     force flag set to true.</li>
     * <li>Verify that <code>close</code> is called on the instrumented
     *     <code>ServerEnpointListener</code>.</li>
     * <li>Verify that the <code>unexport</code> method returns true.</li>
     * </ol>
     */
    public void run() throws Exception {
        //register a transport listener
        BJETransportListener.registerListener(new TransportListenerHelper());
        //Construct a BasicJeriExporter using an instrumented server endpoint
        int listenPort = config.getIntConfigVal("com.sun.jini.test.spec.jeri"
            + ".basicjeriexporter.ConstructorTest.listenPort",9090);
        BasicJeriExporter exporter = new BasicJeriExporter(
            new TestServerEndpoint(),
                new BJETestILFactory());
        //export a remote object using the exporter
        BJETestServiceImpl service = new BJETestServiceImpl();
        try {
            final BJETestService stub = (BJETestService)
                exporter.export(service);
            //make a blocking call on the server
            Thread t = new Thread(new Runnable() {
                public void run(){
                    try {
                        stub.doSomethingLong();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
            while (!serviceMethodCalled) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //call the unexport method with the force flag set to true
            boolean result = exporter.unexport(true);
            if (result) {
                //verify that close was called on the server endpoint
                if (closeCalled) {
                    //OK
                } else {
                    throw new TestException("The close"
                        + " method was not called on the server endpoint"
                        + " after a call to unexport");
                }
            } else {
                log.finer("Unexpected false returned after call to unexport");
                throw new TestException("Unexport returns"
                    + " false - expected true");
            }

        } catch (ExportException e) {
            log.finer("Unexpected ExportException: " + e.getMessage());
            throw new TestException("Unexpected ExportException",
                e);
        }
    }

    public static class TransportListenerHelper implements TransportListener {
        public void called (Method m, Object obj, Object[]args){
            if (m.getName().compareTo("close")==0){
                closeCalled = true;
            }
            if (m.getName().compareTo("doSomethingLong")==0){
                serviceMethodCalled = true;
            }
        }
    }

}
