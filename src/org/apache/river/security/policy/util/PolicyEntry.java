/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
* @author Alexey V. Varlamov
* @author Peter Firmstone.
* @version $Revision$
*/

package org.apache.river.security.policy.util;

import java.net.URL;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.Permission;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class represents an elementary block of a security policy. It associates
 * a CodeSource of an executable code, Principals allowed to execute the code,
 * and a set of granted Permissions.
 * 
 * Immutable
 * 
 * 
 */
public final class PolicyEntry {

    // Store CodeSource
    private final CodeSource cs;

    // Array of principals 
    private final List<Principal> principals;
    private static final List<Principal> noPrincipals = 
            Collections.unmodifiableList(new ArrayList<Principal>(0));

    // Permissions collection
    private final Collection<Permission> permissions;
    private static final Collection<Permission> noPermissions = 
            Collections.unmodifiableCollection(new HashSet<Permission>(0));
    
    private transient final int hashcode;

    /**
     * Constructor with initialization parameters. Passed collections are not
     * referenced directly, but copied.
     */
    public PolicyEntry(CodeSource cs, Collection<? extends Principal> prs,
            Collection<? extends Permission> permissions) {
        this.cs = (cs != null) ? normalizeCodeSource(cs) : null;
        if ( prs == null || prs.isEmpty()) {
            this.principals = noPrincipals;
        }else{
            this.principals = new ArrayList<Principal>(prs.size());
            this.principals.addAll(prs);
        }
        if (permissions == null || permissions.isEmpty()) {
            this.permissions = noPermissions;
        }else{
            Set<Permission> perm = new HashSet<Permission>(permissions.size());
            perm.addAll(permissions);
            this.permissions = Collections.unmodifiableCollection(perm);
        }
        /* Effectively immutable, this will make any hash this is contained in perform.
         * May need to consider Serializable for this class yet, we'll see.
         */ 
        if (this.cs == null){
            hashcode = (principals.hashCode() + this.permissions.hashCode())/2;
        } else {
        hashcode = (this.cs.hashCode() + principals.hashCode() + this.permissions.hashCode())/3;
        }
    }

    /**
     * Checks if passed CodeSource matches this PolicyEntry. Null CodeSource of
     * PolicyEntry implies any CodeSource; non-null CodeSource forwards to its
     * imply() method.
     */
    public boolean impliesCodeSource(CodeSource codeSource) {
        if (cs == null) {
            return true;
        }

        if (codeSource == null) {
            return false;
        }
        return cs.implies(normalizeCodeSource(codeSource));
    }

    private CodeSource normalizeCodeSource(CodeSource codeSource) {
        URL codeSourceURL = PolicyUtils.normalizeURL(codeSource.getLocation());
        CodeSource result = codeSource;

        if (codeSourceURL != codeSource.getLocation()) {
            // URL was normalized - recreate codeSource with new URL
            CodeSigner[] signers = codeSource.getCodeSigners();
            if (signers == null) {
                result = new CodeSource(codeSourceURL, codeSource
                        .getCertificates());
            } else {
                result = new CodeSource(codeSourceURL, signers);
            }
        }
        return result;
    }

    /**
     * Checks if specified Principals match this PolicyEntry. Null or empty set
     * of Principals of PolicyEntry implies any Principals; otherwise specified
     * array must contain all Principals of this PolicyEntry.
     */
    public boolean impliesPrincipals(Principal[] prs) {
       // return PolicyUtils.matchSubset(principals, prs);
        if ( principals.isEmpty()) return true;
        if ( prs == null || prs.length == 0 ) return false;
        List<Principal> princp = Arrays.asList(prs);
        return princp.containsAll(principals);      
    }

    /**
     * Returns unmodifiable collection of permissions defined by this
     * PolicyEntry, may be <code>null</code>.
     */
    public Collection<Permission> getPermissions() {
//        if (permissions.isEmpty()) return null; // not sure if this is good needs further investigation
        return permissions;
    }

    /**
     * Returns true if this PolicyEntry defines no Permissions, false otherwise.
     */
    public boolean isVoid() {
        return permissions.size() == 0;
    }
    
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if ( !(o instanceof PolicyEntry)) return false;
        PolicyEntry pe = (PolicyEntry) o;
        if ( (cs == pe.cs || cs.equals(pe.cs)) && principals.equals(pe.principals) 
                && permissions.equals(pe.permissions) ) return true;
        return false;
    }
    
    @Override
    public int hashCode(){
        return hashcode;        
    }
}
