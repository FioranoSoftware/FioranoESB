/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.security;

import org.apache.karaf.jaas.config.JaasRealm;
import org.apache.karaf.jaas.modules.BackingEngine;
import org.apache.karaf.jaas.modules.BackingEngineFactory;
import org.apache.karaf.jaas.modules.properties.PropertiesBackingEngineFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.apache.karaf.jaas.boot.principal.UserPrincipal;
import org.apache.karaf.jaas.boot.principal.GroupPrincipal;
import org.apache.karaf.jaas.boot.principal.RolePrincipal;

import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

public class SecurityManager {

    private BackingEngine backingEngine;
    private Map<String, ConnectionHandle> connectionHandleMap = new HashMap();
    public SecurityManager(BundleContext context){
        ServiceReference<JaasRealm> realmReference = context.getServiceReference(JaasRealm.class);
        ServiceReference<BackingEngineFactory> references3 = context.getServiceReference(BackingEngineFactory.class);
        JaasRealm jaasRealm = context.getService(realmReference);
        PropertiesBackingEngineFactory propertiesBackingEngineFactory = (PropertiesBackingEngineFactory) context.getService(references3);
        this.backingEngine = propertiesBackingEngineFactory.build(jaasRealm.getEntries()[0].getOptions());
    }

    public String login(final String userName, final String password) throws LoginException {
        List usersList = backingEngine.listUsers();
        LoginContext loginContext = new LoginContext("karaf", new CallbackHandler() {
            public void handle(Callback[] callbacks) {
                NameCallback nameCallback = (NameCallback) callbacks[0];
                PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
                nameCallback.setName(userName);
                passwordCallback.setPassword(password.toCharArray());
            }
        });
        loginContext.login();
        String handleId = String.valueOf(new Random().nextInt());
        return handleId;
    }

    public void addConnectionHandle(String handleID, ConnectionHandle connectionHandle){
        connectionHandleMap.put(handleID, connectionHandle);
    }

    public ConnectionHandle removeConnectionHandle(String handleID){
       return connectionHandleMap.remove(handleID);
    }


    public ConnectionHandle getConnectionHandle(String handleId) {
        if(connectionHandleMap.containsKey(handleId)){
            return connectionHandleMap.get(handleId);
        }
        return null;
    }

    public String getUserName(String handleID){
        if(connectionHandleMap.containsKey(handleID)){
           return connectionHandleMap.get(handleID).getUserName();
        }
        return null;
    }

    public String getPassword(String handleID) {
        if(connectionHandleMap.containsKey(handleID)){
            return connectionHandleMap.get(handleID).getPassword();
        }
        return null;
    }

    public List getUsers(){
       List<UserPrincipal> list =  backingEngine.listUsers();
        List<String> usersList = new ArrayList<String>();
        for(UserPrincipal userPrincipal: list){
            usersList.add(userPrincipal.getName());
        }
        return usersList;
    }

    public Map getGroups(){
        Map<GroupPrincipal, String> map =  backingEngine.listGroups();
        Map<String, String> groupMap = new HashMap<String, String>();
        for(GroupPrincipal groupPrincipal: map.keySet()){
            groupMap.put(groupPrincipal.getName(), map.get(groupPrincipal));
        }
        return groupMap;
    }

    public List getRoles(Principal p){
        List<RolePrincipal> list =  backingEngine.listRoles(p);
        List<String> rolesList = new ArrayList<String>();
        for(RolePrincipal userPrincipal: list){
            rolesList.add(userPrincipal.getName());
        }
        return rolesList;
    }

    public List getGroups(String userName){
        UserPrincipal p = new UserPrincipal(userName);
        List<GroupPrincipal> list =  backingEngine.listGroups(p);
        List<String> groupsList = new ArrayList<String>();
        for(GroupPrincipal groupPrincipal: list){
            groupsList.add(groupPrincipal.getName());
        }
        return groupsList;
    }

    public void addUser(String userName, String password){
        backingEngine.addUser(userName, password);
    }

    public void addGroup(String userName, String group){
        backingEngine.addGroup(userName, group);
    }

    public void deleteGroup(String username, String group) {
        backingEngine.deleteGroup(username, group);
    }

    public void deleteRole(String username, String role) {
        backingEngine.deleteRole(username, role);
    }

    public void deleteUser(String username) {
        backingEngine.deleteUser(username);
    }

    public void deleteGroupRole(String group, String role) {
        backingEngine.deleteGroupRole(group, role);
    }

    public void changePassword(String username, String password){
        backingEngine.deleteUser(username);
        backingEngine.addUser(username, password);
    }
}
