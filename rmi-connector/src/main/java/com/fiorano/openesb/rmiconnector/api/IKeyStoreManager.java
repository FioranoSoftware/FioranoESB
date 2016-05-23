/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.rmiconnector.api;

import java.rmi.RemoteException;
import java.util.ArrayList;

import java.rmi.Remote;
import java.security.Key;
import java.security.cert.Certificate;
import java.util.HashMap;

public interface IKeyStoreManager extends Remote {

    /**
     * This method initializes KeyStore with the password
     * @param password Password with which KeyStore is to be initialized
     * @throws RemoteException
     * @throws ServiceException
     */

    public boolean initialize(String password) throws RemoteException, ServiceException;

    /**
     * This method adds certificate entry to the KeyStore.
     * @param alias Alias entry used in the KeyStore
     * @param cert Certificate details
     * @throws RemoteException
     * @throws ServiceException
     */

    public void addCertificateEntry(String alias, Certificate cert) throws RemoteException, ServiceException;

    /**
     * This method adds key entry to the KeyStore.
     * @param alias Alias entry used in the KeyStore
     * @param key value of the key being added
     * @param chain Array of the certificates to be used
     * @throws RemoteException
     * @throws ServiceException
     */

    public void addKeyEntry(String alias, byte[] key, Certificate[] chain) throws RemoteException, ServiceException;

    /**
     * This method adds key entry to the KeyStore.
     * @param alias Alias entry used in the KeyStore
     * @param key value of the key being added
     * @param password password of the KeyStore
     * @param chain Array of the certificates to be used
     * @throws RemoteException
     * @throws ServiceException
     */

    public void addKeyEntry(String alias, Key key, char[] password, Certificate[] chain) throws RemoteException, ServiceException;

    /**
     * This method returns the size of the KeyStore.
     * @return int - Size of the KeyStore
     * @throws RemoteException
     * @throws ServiceException
     */

    public int size() throws RemoteException, ServiceException;

    /**
     * This method returns ArrayList of KeyStore aliases.
     * @return ArrayList - Array List of KeyStore Aliases
     * @throws RemoteException
     * @throws ServiceException
     */

    public ArrayList<String> aliases() throws RemoteException, ServiceException;

    /**
     * This method checks whether specified alias is present in KeyStore or not.
     * @param alias Alias to be checked in the KeyStore
     * @return boolean - true if alias is present,returns false otherwise
     * @throws RemoteException
     * @throws ServiceException
     */

    public boolean containsAlias(String alias) throws RemoteException, ServiceException;

    /**
     * This method deletes the entry from KeyStore.
     * @param alias Alias of the entry to be deleted
     * @throws RemoteException
     * @throws ServiceException
     */

    public void deleteEntry(String alias) throws RemoteException, ServiceException;

    /**
     * This method returns the certificate values from KeyStore.
     * @param alias Alias of the entry whose certificate is to be fetched
     * @return Certificate - Object of Certificate containing details of the certificate entry
     * @throws RemoteException
     * @throws ServiceException
     */

    public Certificate getCertificate(String alias) throws RemoteException, ServiceException;

    /**
     * This method returns certificate chain from KeyStore.
     * @param alias Alias of the entry whose certificate chain is to be fetched
     * @return Certificate Array - An array of Certificate Object containing the certificate chain
     * @throws RemoteException
     * @throws ServiceException
     */

    public Certificate[] getCertificateChain(String alias) throws RemoteException, ServiceException;

    /**
     * This method returns the Key from KeyStore.
     * @param alias Alias of the entry to be fetched
     * @param password password of the KeyStore
     * @return Key - An object of Key containing Key details of the KeyStore for the specified alias and password
     * @throws RemoteException
     * @throws ServiceException
     */

    public Key getKey(String alias, char[] password) throws RemoteException, ServiceException;

    /**
     * This method returns whether the specified alias is certificate entry or not.
     * @param alias Alias to be checked for certificate entry
     * @return boolean - returns true if its a certificate entry , false otherwise
     * @throws RemoteException
     * @throws ServiceException
     */

    public boolean isCertificateEntry(String alias) throws RemoteException, ServiceException;

    /**
     * This method returns whether the specified alias is key entry or not
     * @param alias Alias to be checked for Key entry
     * @return boolean - returns true if its a key entry , false otherwise
     * @throws RemoteException
     * @throws ServiceException
     */

    public boolean isKeyEntry(String alias) throws RemoteException, ServiceException;

    /**
     * This method merges KeyStore certificates with Server KeyStore
     * @param keyStoreContent Content of the KeyStore to be merged with Server KeyStore
     * @param keyStoreType Type of KeyStore to be merged
     * @param password password for the KeyStore
     * @throws RemoteException
     * @throws ServiceException
     */

    public void mergeKeyStoreCerts(byte[] keyStoreContent, String keyStoreType, String password) throws RemoteException, ServiceException;

    /**
     * This method merges KeySore keys with Server KeyStore
     * @param keyStoreContent Content of the KeyStore
     * @param keyStoreType Type of the KeyStore
     * @param keyStorePassword Password of the KeyStore
     * @param alias Alias of the entry
     * @param keyPassword Key password of the Key
     * @throws RemoteException
     * @throws ServiceException
     */

    public void mergeKeyStoreKey(byte[] keyStoreContent, String keyStoreType, String keyStorePassword, String alias, String keyPassword) throws RemoteException, ServiceException;

    /**
     * This method merges a KeyStore public key with server KeyStore
     * @param keyStoreContent Content of the KeyStore
     * @param keyStoreType Type of the KeyStore
     * @param keyStorePassword Password of the KeyStore
     * @param alias Alias of the entry
     * @throws RemoteException
     * @throws ServiceException
     */

    public void mergeKeyStorePublicKey(byte[] keyStoreContent, String keyStoreType, String keyStorePassword, String alias) throws RemoteException, ServiceException;

    /**
     * This method adds User KeyStore/TrustStore files to server store location
     * @param fileContent Content of the User KeyStore/TrustStore file
     * @param fileName fileName of the User KeyStore/TrustStore file that needs to be added
     * @return boolean - returns true if the user KeyStore is successfully added, false otherwise
     * @throws RemoteException
     * @throws ServiceException
     */

    public boolean addUserKeyStore(byte[] fileContent, String fileName) throws RemoteException, ServiceException;

    /**
     * This methos removes User KeyStore/TrustStore files from server store location
     * @param fileName fileName of the User KeyStore/TrustStore file to be removed
     * @return boolean - returns true if the user KeyStore is successfully removed, false otherwise
     * @throws RemoteException
     * @throws ServiceException
     */

    public boolean removeUserKeyStore(String fileName) throws RemoteException, ServiceException;

    /**
     * This method returns all key aliases from server KeyStore
     * @return ArrayList - List of all Key aliases from the KeyStore
     * @throws RemoteException
     * @throws ServiceException
     */

    public ArrayList<String> getKeyAliases() throws RemoteException, ServiceException;

    /**
     * This method returns all certificate aliases from server keyStore
     * @return ArrayList - List of all certificate aliases from the KeyStore
     * @throws RemoteException
     * @throws ServiceException
     */

    public ArrayList<String> getCertificateAliases() throws RemoteException, ServiceException;

    /**
     * This method returns all keys information from server Key Store
     * @return HashMap - Map of all Key information from the KeyStore
     * @throws RemoteException
     * @throws ServiceException
     */

    public HashMap<String, HashMap<String, String>> getAllKeysInfo() throws RemoteException, ServiceException;

    /**
     * This method will synchronize the Key Store with all connected peer servers.
     * @throws RemoteException
     * @throws ServiceException
     */
    public void synchronizeKeyStore() throws RemoteException, ServiceException;


}
