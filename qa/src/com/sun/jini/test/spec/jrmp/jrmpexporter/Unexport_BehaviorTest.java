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
package com.sun.jini.test.spec.jrmp.jrmpexporter;

import java.util.logging.Level;

// java.util
import java.util.logging.Level;

// net.jini
import net.jini.jrmp.JrmpExporter;

// com.sun.jini
import com.sun.jini.qa.harness.TestException;
import com.sun.jini.test.spec.jrmp.util.AbstractTestBase;
import com.sun.jini.test.spec.jrmp.util.TestRemoteObject;
import com.sun.jini.test.spec.jrmp.util.TestRemoteInterface;
import java.rmi.server.ExportException;


/**
 * <pre>
 * Purpose
 *  This test verifies the following:
 *    If force if true, unexport method unexports the object even if there are
 *    pending or in-progress calls; if false, only unexports the object if there
 *    are no pending or in-progress calls.
 *    The return value is true if the object is (or was previously) unexported,
 *    and false if the object is still exported.
 *
 * Test Cases
 *   This test uses different constructors to construct JrmpExporter.
 *
 * Infrastructure
 *   This test requires the following infrastructure:
 *     TestRemoteObject - object that implements java.rmi.Remote interface.
 *
 * Action
 *   For each test case the test performs the following steps:
 *     1) construct a JrmpExporter1
 *     2) construct a TestRemoteObject
 *     3) invoke export method of constructed JrmpExporter1 with constructed
 *        TestRemoteObject as a parameter
 *     4) invoke unexport method from constructed JrmpExporter1 with true value
 *        for 'force' parameter
 *     5) assert that unexport method invocation will return true as a result
 *     6) invoke unexport method of constructed JrmpExporter1 with true value
 *        for 'force' parameter again
 *     7) assert that unexport method invocation will return true as a result
 *     8) invoke unexport method of constructed JrmpExporter1 with false value
 *        for 'force' parameter
 *     9) assert that unexport method invocation will return true as a result
 *     10) construct a JrmpExporter2
 *     11) invoke export method of constructed JrmpExporter2 with constructed
 *         TestRemoteObject as a parameter
 *     12) invoke unexport method of constructed JrmpExporter2 with false
 *         value for 'force' parameter
 *     13) assert that unexport method invocation will return true as a result
 *     14) invoke unexport method of constructed JrmpExporter2 with false
 *         value for 'force' parameter again
 *     15) assert that unexport method invocation will return true as a result
 *     16) invoke unexport method of constructed JrmpExporter2 with true
 *         value for 'force' parameter
 *     17) assert that unexport method invocation will return true as a result
 *     18) construct a JrmpExporter3
 *     19) invoke export method of constructed JrmpExporter3 with constructed
 *         TestRemoteObject as a parameter
 *     20) start thread which will invoke unexport method of constructed
 *         JrmpExporter3
 *     21) invoke remote method of constructed TestRemoteObject
 *     22) invoke unexport method of constructed JrmpExporter3 with false
 *         value for 'force' parameter (in started separate thread)
 *     23) assert that unexport method invocation will return false as a result
 *     24) invoke unexport method of constructed JrmpExporter3 with true
 *         value for 'force' parameter (in started separate thread)
 *     25) assert that unexport method invocation will return true as a result
 * </pre>
 */
public class Unexport_BehaviorTest extends AbstractTestBase {

    /**
     * This method performs all actions mentioned in class description.
     *
     */
    public void run() throws Exception {
        
        TestRemoteObject tro = new TestRemoteObject();
        ExporterAndStub es1=tryExportPossiblyMoreThanOnce(tro);
        logger.log(Level.FINE,
                "Invoke unexport method of constructed"
                + " JrmpExporter1 with 'true' value...");
        boolean uRes = es1.exporter.unexport(true);

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter1 with 'true' value has returned false "
                    + "while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
        logger.log(Level.FINE,
                "Invoke unexport method of constructed"
                + " JrmpExporter1 with 'true' value again...");
        uRes = es1.exporter.unexport(true);

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter1 with 'true' value again has returned "
                    + "false while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
        logger.log(Level.FINE,
                "Invoke unexport method of constructed"
                + " JrmpExporter1 with 'false' value...");
        uRes = es1.exporter.unexport(false);

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter1 with 'false' value has returned false "
                    + "while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
        
        ExporterAndStub es2=tryExportPossiblyMoreThanOnce(tro);
        logger.log(Level.FINE,
                "Invoke unexport method of constructed"
                + " JrmpExporter2 with 'false' value...");
        uRes = es2.exporter.unexport(false);

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter2 with 'false' value has returned false "
                    + "while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
        logger.log(Level.FINE,
                "Invoke unexport method of constructed"
                + " JrmpExporter2 with 'false' value again...");
        uRes = es2.exporter.unexport(false);

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter2 with 'false' value again has returned "
                    + "false while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
        logger.log(Level.FINE,
                "Invoke unexport method of constructed"
                + " JrmpExporter2 with 'true' value...");
        uRes = es2.exporter.unexport(true);

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter2 with 'true' value has returned false "
                    + "while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
        
        /* 
        There seems to be a little stickiness here in unexporting - We're using
        a fixed port (which maybe isn't the best idea), and it sometimes
        takes a few seconds for the last port to close.  Hence, we get BindException
        on the export operation.  We'll try and just repeat for up to ten seconds.
        */
        
        ExporterAndStub es3=tryExportPossiblyMoreThanOnce(tro);
        
        Unexporter u = new Unexporter(es3.exporter, false);
        logger.log(Level.FINE,
                "Start thread which will invoke unexport method"
                + " of constructed JrmpExporter3 with 'false' value...");
        u.start();
        es3.stub.wait(new Integer(5000));
        uRes = u.getResult();

        if (uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter3 with 'false' value while remote "
                    + "call is in progress has returned true "
                    + "while false is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned false as expected.");
        }
        u = new Unexporter(es3.exporter, true);
        logger.log(Level.FINE,
                "Start thread which will invoke unexport method"
                + " of constructed JrmpExporter3 with 'true' value...");
        u.start();
        es3.stub.wait(new Integer(5000));
        uRes = u.getResult();

        if (!uRes) {
            // FAIL
            throw new TestException(
                    "performed unexport method invocation of constructed "
                    + "JrmpExporter3 with 'true' value while remote "
                    + "call is in progress has returned false "
                    + "while true is expected.");
        } else {
            // PASS
            logger.log(Level.FINE, "Method returned true as expected.");
        }
    }

    private ExporterAndStub tryExportPossiblyMoreThanOnce( TestRemoteObject tro) throws InterruptedException, ExportException {
        ExporterAndStub ret=new ExporterAndStub();
        
        java.rmi.server.ExportException exception=null;
        for (int i=0; ret.stub==null && i<10; i++) {
            exception=null;
            try {
                ret.exporter=createJrmpExporter();
                ret.stub=(TestRemoteInterface) ret.exporter.export(tro);
            } catch(java.rmi.server.ExportException ex) {
                exception = ex;
                Thread.sleep(1000);
            }
        }
        if (exception !=null) {
            throw exception;
        }
        return ret;
    }

    private class ExporterAndStub {
        JrmpExporter exporter=null;
        TestRemoteInterface stub=null;
    }

    /**
     * Auxiliary class which will invoke unexport() method of specified
     * JrmpExporter.
     */
    class Unexporter extends Thread {

        /** JrmpExporter which unexport method will be invoked */
        JrmpExporter jExp;

        /** Value of parameter for unexport method invocation */
        boolean val;

        /** Result of unexport method invocation */
        boolean res;

        /**
         * Constructor which initialize fields of the class.
         *
         * @param exp JrmpExporter instance.
         * @param val value fo unexport method invocation
         */
        public Unexporter(JrmpExporter exp, boolean val) {
            this.jExp = exp;
            this.val = val;
            res = false;
        }

        /**
         * Main method which will invoke unexport() method of JrmpExporter.
         */
        public void run() {
            try {
                // wait for a while to let main thread start remote invocation
                sleep(1000);

                // invoke unexport method
                res = jExp.unexport(val);
            } catch (Exception ex) {}
        }

        /**
         * Returns result of unexport method invocation.
         *
         * @return result of unexport method invocation
         */
        public boolean getResult() {
            return res;
        }
    }
}
