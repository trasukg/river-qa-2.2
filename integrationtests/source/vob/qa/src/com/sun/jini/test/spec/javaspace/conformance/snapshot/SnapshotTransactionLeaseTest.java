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
package com.sun.jini.test.spec.javaspace.conformance.snapshot;

import java.util.logging.Level;

// net.jini
import net.jini.core.entry.Entry;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.Transaction;

// com.sun.jini
import com.sun.jini.qa.harness.TestException;
import com.sun.jini.qa.harness.QAConfig;

// com.sun.jini.qa
import com.sun.jini.test.spec.javaspace.conformance.SimpleEntry;


/**
 * SnapshotTransactionLeaseTest asserts, that each write invocation within
 * the non null transaction returns a Lease object that is lease
 * milliseconds long.
 *
 * It tests this statement for snapshots.
 *
 * @author Mikhail A. Markov
 */
public class SnapshotTransactionLeaseTest extends SnapshotAbstractTestBase {

    /**
     * Sets up the testing environment.
     *
     * @param args Arguments from the runner for setup.
     */
    public void setup(QAConfig config) throws Exception {

        // mandatory call to parent
        super.setup(config);

        // get an instance of Transaction Manager
        mgr = getTxnManager();
    }

    /**
     * This method asserts, that each write invocation within the non null
     * transaction returns a Lease object that is lease milliseconds long.
     *
     * It tests this statement for snapshots.
     *
     * <P>Notes:<BR>For more information see the JavaSpaces specification
     * sections 2.3, 2.6.</P>
     */
    public void run() throws Exception {
        SimpleEntry sampleEntry1 = new SimpleEntry("TestEntry #1", 1);
        SimpleEntry sampleEntry2 = new SimpleEntry("TestEntry #2", 2);
        Entry snapshot1;
        Entry snapshot2;
        SimpleEntry result;
        long curTime1;
        long curTime2;
        long expirTime1;
        long expirTime2;
        long leaseTime1 = timeout1;
        long leaseTime2 = timeout2;
        long leaseTime3 = leaseTime1 + checkTime;
        Lease lease1 = null;
        Lease lease2 = null;
        Transaction txn;

        // first check that space is empty
        if (!checkSpace(space)) {
            throw new TestException(
                    "Space is not empty in the beginning.");
        }

        // create snapshots
        snapshot1 = space.snapshot(sampleEntry1);
        snapshot2 = space.snapshot(sampleEntry2);

        // create the non null transaction
        txn = getTransaction();

        /*
         * write an entry with Lease.ANY lease time using it's template
         * within the transaction
         */
	lease1 = space.write(snapshot1, txn, Lease.ANY);
        curTime1 = System.currentTimeMillis();

        /*
         * check that returned lease is not equal to null,
         * expiration time is not less then current time
         */
        if (lease1 == null) {
            throw new TestException(
                    "performed write with Lease.ANY lease time within the"
                    + " non null transaction, expected result is non null"
                    + " lease but null has been returned.");
        }
        expirTime1 = lease1.getExpiration();

        if (expirTime1 < curTime1) {
            throw new TestException(
                    "performed write of " + sampleEntry1
                    + " with Lease.ANY lease time using it's snapshot"
                    + " within the non null transaction, expected"
                    + " expiration time is greater then current time "
                    + curTime1 + " but returned value is " + expirTime1);
        }
        logDebugText("Write operation of " + sampleEntry1
                + " with Lease.ANY value for lease time using it's snapshot"
                + " within the transaction works as expected.");

        // clean the space within the transaction
        cleanSpace(space, txn);

        /*
         * write an entry with Lease.FOREVER lease time using it's snapshot
         * within the transaction
         */
	lease1 = space.write(snapshot1, txn, Lease.FOREVER);
        curTime1 = System.currentTimeMillis();

        /*
         * check that returned lease is not equal to null,
         * expiration time is not less then current time
         */
        if (lease1 == null) {
            throw new TestException(
                    "performed write with Lease.FOREVER lease time within"
                    + " the non null transaction, expected result is non"
                    + " null lease but null has been returned.");
        }
        expirTime1 = lease1.getExpiration();

        if (expirTime1 < curTime1) {
            throw new TestException(
                    "performed write of " + sampleEntry1
                    + " with Lease.FOREVER lease time using it's snapshot"
                    + " within the non null transaction, expected"
                    + " expiration time is greater then current time "
                    + curTime1 + " but returned value is " + expirTime1);
        }
        logDebugText("Write operation of " + sampleEntry1
                + " with Lease.FOREVER value for lease time using it's"
                + " snapshot within the non null transaction works"
                + " as expected.");

        // clean the space
        cleanSpace(space, txn);

        /*
         * write two sample entries with different finite lease times
         * using their snapshots into the space within the transaction.
         */
        lease1 = space.write(snapshot1, txn, leaseTime1);
        curTime1 = System.currentTimeMillis();
        expirTime1 = lease1.getExpiration();
        lease2 = space.write(snapshot2, txn, leaseTime2);
        curTime2 = System.currentTimeMillis();
        expirTime2 = lease2.getExpiration();

        // check that returned leases are not equal to null
        if (lease1 == null) {
            throw new TestException(
                    "performed write of " + sampleEntry1 + " with "
                    + leaseTime1 + " lease time using it's snapshot"
                    + " within the non null transaction returned null"
                    + " lease.");
        }

        if (lease2 == null) {
            throw new TestException(
                    "performed write of " + sampleEntry2 + " with "
                    + leaseTime2 + " lease time using it's snapshot"
                    + " within the non null transaction returned null"
                    + " lease.");
        }

        // check that write operations return required expiration times
        if (((expirTime1 - curTime1) > leaseTime1)
                || (expirTime1 - curTime1) < (leaseTime1 - instantTime)) {
            throw new TestException(
                    "performed write of " + sampleEntry1 + " with "
                    + leaseTime1 + " lease time uting it's snapshot"
                    + " within the non null transaction."
                    + " Expected conditions are not satisfied: "
                    + leaseTime1 + "(specified lease time) <= ("
                    + expirTime1 + "(returned expiration time) - "
                    + curTime1 + "(current time)) >= (" + leaseTime1
                    + "(specified lease time) + " + instantTime + ").");
        }

        if (((expirTime2 - curTime2) > leaseTime2)
                || (expirTime2 - curTime2) < (leaseTime2 - instantTime)) {
            throw new TestException(
                    "performed write of " + sampleEntry2 + " with "
                    + leaseTime2 + " lease time uting it's snapshot"
                    + " within the non null transaction."
                    + " Expected conditions are not satisfied: "
                    + leaseTime2 + "(specified lease time) <= ("
                    + expirTime2 + "(returned expiration time) - "
                    + curTime2 + "(current time)) >= (" + leaseTime2
                    + "(specified lease time) + " + instantTime + ").");
        }
        logDebugText("Write operations within the non null tranasction of "
                + sampleEntry1 + " with " + leaseTime1 + " lease time and\n"
                + sampleEntry2 + " with " + leaseTime2
                + " lease time using their snapshots work as expected.");

        /*
         * write 1-st sample entry with another finite lease time
         * using it's snapshot to the space within the transaction again
         */
        lease1 = space.write(snapshot1, txn, leaseTime3);
        curTime1 = System.currentTimeMillis();
        expirTime1 = lease1.getExpiration();

        // check that returned leases are not equal to null
        if (lease1 == null) {
            throw new TestException(
                    "performed 2-nd write of " + sampleEntry1 + " with "
                    + leaseTime3 + " lease time using it's snapshot"
                    + " within the non null tranasaction returned null"
                    + " result.");
        }

        // check that write operations return required expiration times
        if (((expirTime1 - curTime1) > leaseTime3)
                || (expirTime1 - curTime1) < (leaseTime3 - instantTime)) {
            throw new TestException(
                    "performed 2-nd write of " + sampleEntry1 + " with "
                    + leaseTime3 + " lease time using it's snapshot"
                    + " within the non null transaction."
                    + " Expected conditions are not satisfied: "
                    + leaseTime3 + "(specified lease time) <= ("
                    + expirTime1 + "(returned expiration time) - "
                    + curTime1 + "(current time)) >= (" + leaseTime3
                    + "(specified lease time) + " + instantTime + ").");
        }
        logDebugText("2-nd write operation of " + sampleEntry1 + " with "
                + leaseTime3 + " lease time within the non null transaction"
                + " works as expected.");

        /*
         * write 2-nd sample entry with Lease.ANY value for lease time
         * to the space using it's snapshot within the transaction again
         */
        lease2 = space.write(snapshot2, txn, Lease.ANY);
        curTime2 = System.currentTimeMillis();
        expirTime2 = lease2.getExpiration();

        // check that returned leases are not equal to null
        if (lease2 == null) {
            throw new TestException(
                    "performed 2-nd write of " + sampleEntry2
                    + " with Lease.ANY value for lease time using it's"
                    + " snapshot within the non"
                    + " null transaction returned null lease.");
        }

        // check that expiration time is not less then current time
        if (expirTime2 < curTime2) {
            throw new TestException(
                    "performed 2-nd write of " + sampleEntry2
                    + " with Lease.ANY lease time using it's snapshot"
                    + " within the non null transaction, expected"
                    + " expiration time is greater then current time "
                    + curTime2 + " but returned value is " + expirTime2);
        }
        logDebugText("2-nd write operation of " + sampleEntry2
                + " with Lease.ANY value for lease time using it's snapshot"
                + " within the non null transaction works as expected.");

        /*
         * write snapshot of 1-st sample entry with Lease.FOREVER value
         * for lease time to the space within the transaction again
         */
        lease1 = space.write(snapshot1, txn, Lease.FOREVER);
        curTime1 = System.currentTimeMillis();
        expirTime1 = lease1.getExpiration();

        // check that returned leases are not equal to null
        if (lease1 == null) {
            throw new TestException(
                    "performed 3-rd write of " + sampleEntry1
                    + " with Lease.FOREVER value for lease time using it's"
                    + " snapshot within the"
                    + " non null transaction returned null lease.");
        }

        // check that expiration time is not less then current time
        if (expirTime1 < curTime1) {
            throw new TestException(
                    "performed 3-rd write of " + sampleEntry1
                    + " with Lease.FOREVER lease time using it's snapshot"
                    + " within the non null transaction, expected"
                    + " expiration time is greater then current time "
                    + curTime1 + " but returned value is " + expirTime1);
        }
        logDebugText("3-rd write operation of " + sampleEntry1
                + " with Lease.FOREVER value for lease time using it's"
                + " snapshot within the non"
                + " null transaction works as expected.");

        // commit the transaction
        txnCommit(txn);
    }
}
