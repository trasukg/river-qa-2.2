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

package com.sun.jini.test.impl.end2end.e2etest;

/**
 * A runtime exception generated by this test.
 * Used to convert an exception to a RuntimeException, which is
 * then thrown to prematurely terminate the test
 */
class TestException extends RuntimeException {
    Throwable t;

    /**
     * Construct an exception with the given message.
     *
     * @param message A String describing the exception.
     */
    TestException(String message) {
	this(message,null);
    }

    /**
     * Construct an exception with the given message and Throwable.
     *
     * @param message A String describing the exception.
     *
     * @param t The exception which was thrown, causing the creation of
     *          this exception. <code>t</code> may be null.
     */
    TestException(String message,Throwable t) {
	super(message);
    }

    /**
     * Print a stack trace. The trace of the nested exception is printed also,
     * if it is non-null.
     */
    public void printStackTrace() {
	System.err.println("Stack trace for this exception");
	super.printStackTrace();
	if (t != null) {
	    System.err.println("Stack trace for the nested exception");
	    t.printStackTrace();
	}
    }
}