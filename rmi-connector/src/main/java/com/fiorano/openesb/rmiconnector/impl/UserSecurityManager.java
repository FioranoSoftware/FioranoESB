/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.impl;

import com.fiorano.openesb.rmiconnector.api.IUserSecurityManager;
import com.fiorano.openesb.rmiconnector.api.IUserSecurityManagerListener;
import com.fiorano.openesb.rmiconnector.api.ServiceException;
import com.fiorano.openesb.security.SecurityManager;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.ExceptionUtil;
import com.fiorano.openesb.utils.exception.FioranoException;

import javax.security.auth.login.LoginException;
import java.rmi.RemoteException;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class UserSecurityManager extends AbstractRmiManager implements IUserSecurityManager {

    protected SecurityManager userSecurityManager;
    private InstanceHandler handler;


    public UserSecurityManager(RmiManager rmiManager, InstanceHandler handler, String context) throws RemoteException {
        super(rmiManager);
        this.userSecurityManager = rmiManager.getSecurityManager();
        this.handler = handler;
        setHandleID(handler.getHandleID());
    }

    public Group getGroup(String grpName) throws RemoteException, ServiceException {
        return null;
    }

    public void addMembertoGroup(String groupName, String memberName, String handleID) throws RemoteException, ServiceException {
        try {
            userSecurityManager.addGroup(memberName, groupName);
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public void removeMemberfromGroup(String groupName, String memberName, String handleID) throws RemoteException, ServiceException {
        try {
            userSecurityManager.deleteGroup(memberName, groupName);
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public Map<String, String> getGroupNames() throws RemoteException, ServiceException {
        try {
           return userSecurityManager.getGroups();
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public Vector getMemberNames(String groupName) throws RemoteException, ServiceException {
       return null;
    }

    public Principal getUser(String userName) throws RemoteException, ServiceException {
        return null;
    }

    public List getUserNames() throws RemoteException, ServiceException {
        try {
            return userSecurityManager.getUsers();
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public Vector getGroupsOfMember(String userName) throws RemoteException, ServiceException {
        return null;
    }

    public Acl getAcl(String aclName) throws RemoteException, ServiceException {
        return null;
    }

    public boolean isPermissionAvailable(String handleID, String action) throws RemoteException, ServiceException {
        return false;
    }

    public Vector getAclNames() throws RemoteException, ServiceException {
       return null;
    }

    public Principal getDefaultAclOwner() throws RemoteException, ServiceException {
        return null;
    }

    public Permission getPermission(int type) throws RemoteException, ServiceException {
        return null;
    }

    public String getName() throws RemoteException, ServiceException {
      return null;
    }

    public Group getNodeGroup(String grpName) throws RemoteException, ServiceException {
       return null;
    }

    public Vector getNodeGroupNames() throws RemoteException, ServiceException {
        return null;
    }

    public Principal getNode(String nodeName) throws RemoteException, ServiceException {
        return null;
    }

    public Vector getNodeNames() throws RemoteException, ServiceException {
       return null;
    }

    public Acl getServiceAcl(String aclName) throws RemoteException, ServiceException {
        return null;
    }

    public Vector getServiceAclNames() throws RemoteException, ServiceException {
       return null;
    }

    public Principal getDefaultServiceAclOwner() throws RemoteException, ServiceException {
        return null;
    }

    public boolean setAcl(String context, Acl acl) throws RemoteException, ServiceException {
       return false;
    }

    public boolean setServiceAcl(String aclName, Acl acl) throws RemoteException, ServiceException {

        return false;
    }

    public void authenticateUser(String userName, String password) throws RemoteException, ServiceException {
        try {
            userSecurityManager.login(userName, password);
        } catch (LoginException e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public boolean canClearUserEvents(String userName) throws RemoteException, ServiceException {
        return false;
    }

    public void changePassword(String userName, String oldPassword, String newPassword) throws RemoteException, ServiceException {
        validateHandleID(handleId, "Change Password :" + userName);
        try {
            userSecurityManager.changePassword(userName, newPassword);


        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public Group createGroup(String name) throws RemoteException, ServiceException {
        return null;
    }

    public boolean deleteGroup(String name) throws RemoteException, ServiceException {
        return false;
    }

    public void createUser(String userName, String passwd) throws RemoteException, ServiceException {
        validateHandleID(handleId, "Create User :" + userName);
        try {
            userSecurityManager.addUser(userName, passwd);
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public void deleteUser(String name) throws RemoteException, ServiceException {
        try {
            userSecurityManager.deleteUser(name);
        } catch (Exception e) {
            throw new ServiceException(ExceptionUtil.getMessage(e));
        }
    }

    public Acl createAcl(String name, Principal owner) throws RemoteException, ServiceException {
        return null;
    }

    public AclEntry createAclEntry() throws RemoteException, ServiceException {
        return null;
    }

    public boolean removeAcl(String aclName) throws RemoteException, ServiceException {
        return false;
    }

    public Group createNodeGroup(String name) throws RemoteException, ServiceException {
       return null;
    }

    public boolean deleteNodeGroup(String name) throws RemoteException, ServiceException {
       return false;
    }

    public boolean removeServiceAcl(String aclName) throws RemoteException, ServiceException {
        return false;
    }

    public boolean isPermissionAvailable(String action) throws ServiceException {
       return false;

    }

    public boolean isConnected(String userName) throws RemoteException, ServiceException {
        return false;
    }

    protected String getHighestVersion(String id, String handleID) throws ServiceException {
        throw new UnsupportedOperationException("This operation is not supported for this instance");
    }

    public void unreferenced() {
        handler.onUnReferenced(this.toString());

    }

    public String toString() {
        return Constants.USER_SECURITY_MANAGER + ":" + getPartnerName(handleId);
    }

    private IUserSecurityManager clientProxyInstance;

    public void setClientProxyInstance(IUserSecurityManager clientProxyInstance) {
        this.clientProxyInstance = clientProxyInstance;
    }

    public IUserSecurityManager getClientProxyInstance() {
        return clientProxyInstance;
    }

    public boolean addModulePermissions(String principalName, boolean isNegative, String... permissions) throws RemoteException, ServiceException {
        return false;    }

    public boolean checkModulePermission(String permissionName) throws RemoteException, ServiceException {
        return false;
    }

    public Map<String, Boolean> getPermissions(String userName) throws RemoteException, ServiceException {
        return null;
    }

    public String getUserNameForHandleID(String handleID) {
        // get the username from handleID
        if (handleID != null) {
            userSecurityManager.getUserName(handleID);
        }
        return null;
    }

    public String getPartnerName(String handleID) {
        return null;
    }

    public void addUserSecurityListener(IUserSecurityManagerListener listener, String userName) throws RemoteException, ServiceException {
        validateHandleID(handleId, "add User Security Listener");
        dapiEventManager.registerSecurityEventListener(listener, userName, handleId);
    }

    public void removeUserSecurityListener(IUserSecurityManagerListener listener, String userName) throws RemoteException, ServiceException {
        validateHandleID(handleId, "Remove User Security Listener");
        dapiEventManager.unRegisterSecurityEventListener(listener, userName, handleId);
    }
}

