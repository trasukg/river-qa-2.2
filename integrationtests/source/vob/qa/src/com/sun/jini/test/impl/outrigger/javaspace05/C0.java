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
package com.sun.jini.test.impl.outrigger.javaspace05;

import net.jini.entry.AbstractEntry;
import net.jini.id.ReferentUuids;
import net.jini.id.Uuid;

/**
 * The tests in this package use 3 hierarchies of entry classes, A, B,
 * and C. They all implement TestEntry, but their depths vary.
 * This class is the root (and only member) of the C hierarchy.
 */
public class C0 extends AbstractEntry implements TestEntry {
    public Uuid id;
    public String color;
    public C0() {};

    public void setUuid(Uuid id) {
	this.id = id;
    }

    public String getColor() {return color;}

    public void setColor(String color) {
	this.color = color;
    }

    public Uuid getReferentUuid() {
	return id;
    }

    public boolean equal(Object other) {
	return ReferentUuids.compare(this, other);
    }

    public int hashCode() {
	return id.hashCode();
    }

    public TestEntry dup() {
	try {
	    return (TestEntry)super.clone();
	} catch (CloneNotSupportedException e) {
	    throw new AssertionError(e);
	}
    }
}

