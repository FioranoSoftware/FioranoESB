/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public interface IUserSecurityManager extends Remote {
    /**
     * This method adds particular member to group and update database argument.
     *
     * @param groupName  Name of the group to which member is to be added
     * @param memberName Name of the member
     * @param handleID
     * @return boolean - true if added, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createGroup(String)
     * @see #deleteGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public void addMembertoGroup(String groupName, String memberName, String handleID) throws RemoteException, ServiceException;

    /**
     * This method removes particular member to group and update database argument.
     *
     * @param groupName  Name of the group to which member is to be added
     * @param memberName Name of the member
     * @param handleID
     * @return boolean - true if removed, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createGroup(String)
     * @see #deleteGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public void removeMemberfromGroup(String groupName, String memberName, String handleID) throws RemoteException, ServiceException;

    /**
     * This method returns the group identified by the <code>grpName</code> argument.
     *
     * @param grpName Name of the group which is to be obtained.
     * @return Group - Object representing that group
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createGroup(String)
     * @see #deleteGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public Group getGroup(String grpName) throws RemoteException, ServiceException;

    /**
     * This method returns an List of all the groups names in this particular realm.
     *
     * @return Vector - Vector list of the group names (strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public Map<String, String> getGroupNames() throws RemoteException, ServiceException;

    /**
        * This method returns an List of all the member names of the given group in this particular realm.
        *
        * @return Vector - Vector list of the member names (strings)
        * @throws RemoteException  RemoteException
        * @throws ServiceException ServiceException
        * @see #getGroup(String)
        * @see java.security.acl.Group
        * @since Tifosi2.0
        */
    public Vector getMemberNames(String groupName) throws RemoteException, ServiceException;

    /**
     * This method returns the <code>Principal</code> representing a user
     * identified by the <code>name<code> argument.
     *
     * @param userName Name of the user
     * @return Principal - object representing that user
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createUser(String, String)
     * @see #deleteUser(String)
     * @see java.security.Principal
     * @since Tifosi2.0
     */
    public Principal getUser(String userName) throws RemoteException, ServiceException;

    /**
     * This method returns an List of names of all the users belonging
     * to this particular realm.
     *
     * @return Vector - Vector list of the user names (strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getUser(String)
     * @see java.security.Principal
     * @since Tifosi2.0
     */
    public List getUserNames() throws RemoteException, ServiceException;

    /**
     * This method returns an List of the names of groups
     * of which the user identified by the <code>userName</code> argument is a
     * member.
     *
     * @param userName Name of user for which to find groups.
     * @return Vector - Vector list of the group names (strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroupNames()
     * @since Tifosi2.0
     */
    public Vector getGroupsOfMember(String userName) throws RemoteException, ServiceException;

    /**
     * This method returns the ACL associated with a context(principal) identified
     * by the <code>name</code> argument.
     *
     * @param aclName name of the ACL
     * @return Acl - ACL object associated with the particular context(principal)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createAcl(String, java.security.Principal)
     * @see #setAcl(String, java.security.acl.Acl)
     * @see #removeAcl(String)
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Acl getAcl(String aclName) throws RemoteException, ServiceException;

    /**
     * This method returns a boolean value indicating if permission for performing
     * an action specified by <code>action</code> on this Tifosi service provider
     * is available.
     *
     * @param action   The action for which permission is to be checked or not.
     * @param handleID Connection Handle ID
     * @return boolean - true if action is permitted, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @since Tifosi2.0
     */

    public boolean isPermissionAvailable(String handleID, String action) throws RemoteException, ServiceException;

    /**
     * This method returns the names of all the ACLs in this particular realm.
     *
     * @return Vector - Vector list of the ACL names (strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getAcl(String)
     * @see #createAcl(String, java.security.Principal)
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Vector getAclNames() throws RemoteException, ServiceException;

    /**
     * This method returns the default ACL owner.
     *
     * @return Principal - An Object of Principal which, by default, owns the ACLs in the realm
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see java.security.Principal
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Principal getDefaultAclOwner() throws RemoteException, ServiceException;

    /**
     * This method returns a Permission object representing the permission
     * of the type specified by the <code>type</code> argument.
     *
     * @param type The type of permission
     * @return Permission - An object representing the Permission
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see java.security.acl.Permission
     * @since Tifosi2.0
     */
    public Permission getPermission(int type) throws RemoteException, ServiceException;

    /**
     * This method returns the name of this realm.
     *
     * @return String - The name of this realm
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @since Tifosi2.0
     */
    public String getName() throws RemoteException, ServiceException;

    /**
     * This method returns the node group with the name specified by the
     * <code>grpName</code> argument.
     *
     * @param grpName Name of group
     * @return Group - object representing that group
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createGroup(String)
     * @see #deleteGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public Group getNodeGroup(String grpName) throws RemoteException, ServiceException;

    /**
     * This method returns an List of all the node groups belonging to
     * this particular realm.
     *
     * @return Vector - Vector list of the group names (strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroup(String)
     * @see #getNodeGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public Vector getNodeGroupNames() throws RemoteException, ServiceException;

    /**
     * This method returns the <code>Principal</code> representing the node,
     * with the specified node name.
     *
     * @param nodeName The name of the node
     * @return Principal - An object of Principal representing that node
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getNodeNames()
     * @see java.security.Principal
     * @since Tifosi2.0
     */
    public Principal getNode(String nodeName) throws RemoteException, ServiceException;

    /**
     * This method returns the List of names of all the
     * nodes belonging to this particular realm.
     *
     * @return Vector - Vector list of the node names (strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getNode(String)
     * @see java.security.Principal
     * @since Tifosi2.0
     */
    public Vector getNodeNames() throws RemoteException, ServiceException;

    /**
     * This method returns the service ACL identified by the <code>aclName</code>
     * argument.
     *
     * @param aclName The name of the service ACL
     * @return Acl - an ACL object associated with the particular context(principal)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createAcl(String, java.security.Principal)
     * @see #setServiceAcl(String, java.security.acl.Acl)
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Acl getServiceAcl(String aclName) throws RemoteException, ServiceException;

    /**
     * This method returns an List of names of all the
     * service ACLs in this particular realm.
     *
     * @return Vector - Vector list of the service ACL names(strings)
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getServiceAcl(String)
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Vector getServiceAclNames() throws RemoteException, ServiceException;

    /**
     * This method returns the default service ACL owner of this realm.
     *
     * @return Principal - An object of Principal which, by default, owns the service
     *         ACLs in the realm
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see java.security.Principal
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Principal getDefaultServiceAclOwner() throws RemoteException, ServiceException;

    /**
     * This method sets the ACL specified by the <code>ACL</code> argument
     * for the context specified by the <code>context</code> argument.
     *
     * @param acl     java.security.acl.ACL object that has to be
     *                associated with the topic name, passed as the first argument
     * @param context The new ACL value
     * @return boolean - true if the ACL is set, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see java.security.acl.Acl
     * @see #getAcl(String)
     * @see #removeAcl(String)
     * @since Tifosi2.0
     */
    public boolean setAcl(String context, Acl acl) throws RemoteException, ServiceException;

    /**
     * This method sets an ACL identified by the <code>aclName</code> argument
     * to the specified context.
     *
     * @param aclName The name of the context
     * @param acl     java.security.acl.Acl object that has to be
     *                associated with the context, passed as the first argument
     * @return boolean - true if the service ACL is set, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see java.security.acl.Acl
     * @see #getServiceAcl(String)
     * @see #removeServiceAcl(String)
     * @since Tifosi2.0
     */
    public boolean setServiceAcl(String aclName, Acl acl) throws RemoteException, ServiceException;

    /**
     * This method authenticates the user with the given <code>userName</code>
     * and <code>password</code>, who is trying to connect to the
     * Tifosi Enterprise Server.
     *
     * @param userName The name of the user
     * @param password The password of the user
     * @return boolean - true if the username with given password exists, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @since Tifosi2.0
     */
    public void authenticateUser(String userName, String password) throws RemoteException, ServiceException;

    /**
     * This method checks whether the user with userName has permission to clear user events.
     *
     * @param userName The name of the user
     * @return boolean - true if the user with userName can clear user events exists, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */

    public boolean canClearUserEvents(String userName) throws RemoteException, ServiceException;

    /**
     * This method changes the password of the user identified by the <code>userName</code>
     * argument. The old password specified by the <code>oldPassword<code> argument,
     * is required to authenticate the specified user. On successful authentication,
     * the new password specified by the <code>newPassword</code> is set for that user.
     *
     * @param userName    The name of the user
     * @param oldPassword The current password of the user
     * @param newPassword The new password of the user
     * @return boolean - true if the password is changed successfully, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException.
     * @see #authenticateUser(String,
     *      String)
     * @since Tifosi2.0
     */
    public void changePassword(String userName, String oldPassword, String newPassword) throws RemoteException, ServiceException;

    /**
     * This method creates a new group with the name specified by the <code>name</code>
     * argument in the database.
     *
     * @param name The name of the group
     * @return Group - The newly created empty group
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroup(String)
     * @see #deleteGroup(String)
     * @see #removeAcl(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public Group createGroup(String name) throws RemoteException, ServiceException;

    /**
     * This method deletes a group identified by the <code>name</code> argument,
     * if it is not a member of any group or ACL.
     *
     * @param name The name of the group
     * @return <code>true</code> - if the group is
     *         successfully deleted <code>false</code>- if the group could not be
     *         deleted, because it is used in some aspect of realm security policy.
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroup(String)
     * @see #createGroup(String)
     * @since Tifosi2.0
     */
    public boolean deleteGroup(String name) throws RemoteException, ServiceException;

    /**
     * This method creates a new user with the <code>userName</code> and
     * <code>passwd</code> given as arguments and returns the <code>Principal</code>
     * object associated with the created user.
     *
     * @param userName The name of the user to be created
     * @param passwd   The password of the user
     * @return Principal - An object of Principal representing that user
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getUser(String)
     * @see #deleteUser(String)
     * @see java.security.Principal
     * @since Tifosi2.0
     */
    public void createUser(String userName, String passwd) throws RemoteException, ServiceException;

    /**
     * This method deletes a user identified by the <code>name<code> argument,
     * if it is supported by the underlying realm implementation.
     *
     * @param name The name of the user to be deleted
     * @return <code>true</code> - if the user is
     *         successfully deleted <code>false</code> if the user could not be
     *         deleted because the user is used in some aspect of realm security
     *         policy
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getUser(String)
     * @see #createUser(String,
     *      String)
     * @since Tifosi2.0
     */
    public void deleteUser(String name) throws RemoteException, ServiceException;

    /**
     * This method creates a new ACL with the name specified by the <code>name</code>
     * argument and an owner specified by the <code>owner</code> argument.
     * <p/>
     * An ACL cannot be created without assigning an owner.
     *
     * @param name  The name of the ACL
     * @param owner The principal owner of the ACL
     * @return Acl - The newly created ACL.
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createAclEntry()
     * @see #removeAcl(String)
     * @see java.security.Principal
     * @see java.security.acl.Acl
     * @since Tifosi2.0
     */
    public Acl createAcl(String name, Principal owner) throws RemoteException, ServiceException;

    /**
     * This method creates a new and empty ACL entry.
     *
     * @return AclEntry - An object representing that ACL entry
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createAcl(String, java.security.Principal)
     * @see java.security.Principal
     * @see java.security.acl.Acl
     * @see java.security.acl.AclEntry
     * @since Tifosi2.0
     */
    public AclEntry createAclEntry() throws RemoteException, ServiceException;

    /**
     * This method deletes the ACL specified by the <code>aclName</code> argument.
     *
     * @param aclName The name of the ACL to be removed
     * @return true if the ACL is deleted, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #createAcl(String, java.security.Principal)
     * @see #getAcl(String)
     * @see #setAcl(String, java.security.acl.Acl)
     * @since Tifosi2.0
     */
    public boolean removeAcl(String aclName) throws RemoteException, ServiceException;

    /**
     * This method creates a new node group, in the Tifosi Security database,
     * with the name specified by the <code>name</code> argument.
     *
     * @param name The name of the group
     * @return Group - The newly created empty group
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroup(String)
     * @see #deleteGroup(String)
     * @see java.security.acl.Group
     * @since Tifosi2.0
     */
    public Group createNodeGroup(String name) throws RemoteException, ServiceException;

    /**
     * This method deletes a node group with specified name, if it is
     * empty and is not a member of any group or ACL.
     *
     * @param name The name of the group
     * @return true - if the group is successfully deleted. false - if it couldn't be deleted because the group is used in some aspect of realm security policy.
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getGroup(String)
     * @see #createGroup(String)
     * @since Tifosi2.0
     */
    public boolean deleteNodeGroup(String name) throws RemoteException, ServiceException;

    /**
     * This method deletes the service ACL specified by <code>aclName</code>
     * argument.
     *
     * @param aclName The name of the service ACL to be deleted
     * @return true if the service ACL is deleted successfully, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     * @see #getServiceAcl(String)
     * @see #setServiceAcl(String, java.security.acl.Acl)
     * @since Tifosi2.0
     */
    public boolean removeServiceAcl(String aclName) throws RemoteException, ServiceException;

    /**
     * This method returns a boolean value indicating if permission for
     * performing an action specified by <code>action</code> on this Tifosi
     * service provider is available.
     *
     * @param action The action for which permission is to be checked or not.
     *               The possible values of this variable are constants defined in
     * @return boolean - true if action is permitted, false otherwise
     * @throws ServiceException ServiceException
     */
    public boolean isPermissionAvailable(String action) throws ServiceException;

    /**
     *This method checks if the specified User is connected or not
     *
     * @param userName Username of the particular User (the user name is case insensitive)
     * @return boolean - true if User is connected, false otherwise
     * @throws RemoteException  RemoteException
     * @throws ServiceException ServiceException
     */
    public boolean isConnected(String userName) throws RemoteException, ServiceException;

    /**
     * This method sets the Client Proxy Instance
     *
     * @param returnObject an object of IUserSecurityManager containing client proxy details
     *
     */
    public void setClientProxyInstance(IUserSecurityManager returnObject);

    /**
     * This method returns the Client Proxy Instance
     * @return IUserSecurityManager - An instance of UserSecurityManager containing client proxy details
     *
     */
    public IUserSecurityManager getClientProxyInstance();

    /**
     * This method adds module permissions to the Principal
     *
     * @param principalName Name of the principal to which module permissions will be added
     * @param isNegative true if user doesn't have module permissions, false otherwise
     * @param permissions String array of permissions to be added
     * @return boolean - true if module permissions are successfully added, false otherwise
     * @throws ServiceException
     */
    public boolean addModulePermissions(String principalName, boolean isNegative, String... permissions) throws ServiceException, RemoteException;

    /**
     * This method checks if the specified permissions are present for the module
     *
     * @param permissionName Name of the permission to be checked
     * @return boolean - true if permission is present, false otherwise
     * @throws ServiceException
     */
    public boolean checkModulePermission(String permissionName) throws ServiceException, RemoteException;

    /**
     * This method returns a Map of permissions for the specified user name
     *
     * @param userName Username of the particular User (the user name is case insensitive)
     * @return Map - Map of permissions for the specified user name
     * @throws ServiceException
     */
    public Map<String, Boolean> getPermissions(String userName) throws ServiceException, RemoteException;

    /**
     * This method adds a listener to the changes of IUserSecurityManager's object
     *
     * @param listener The listener to add
     * @param userName User name of the user
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void addUserSecurityListener(IUserSecurityManagerListener listener, String userName) throws RemoteException, ServiceException;

    /**
     * This method removes the listener from the IUserSecurityManager
     *
     * @param listener The listener to remove
     * @param userName  application GUID
     * @throws RemoteException  A communication-related exception that may occur during the execution of a remote method call
     * @throws ServiceException ServiceException
     */
    public void removeUserSecurityListener(IUserSecurityManagerListener listener, String userName) throws RemoteException, ServiceException;
}
