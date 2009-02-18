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
/* @test 
 *  
 * @summary Functional test to verify configurability of the preferred
 * class provider using the RequireDlPermProvider
 *
 * @author Laird Dornin
 *
 * @library ../../../../../../testlibrary
 * @build ExerciseRequireDlPermProvider Foo Bar ConnectBack TestLoaderProvider
 * @build TestLibrary TestParams
 * @run main/othervm/policy=requiredlpermprovider.policy
 *     -Djava.rmi.server.RMIClassLoaderSpi=net.jini.loader.pref.RequireDlPermProvider
 *     ExerciseRequireDlPermProvider
 */

/**
 * Wrapper to check behavior of RequireDlPermProvider
 */
public class ExerciseRequireDlPermProvider {
    public static void main(String[] args) {
	ExerciseProviderConfig.main(args);
    }
}
