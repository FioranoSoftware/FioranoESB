/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.crypto;

public interface ICustomEncryptor {

    /**
     * Return encrypted string
     *
     * Use required Encryption scheme and encryption key to generate
     * required cipher
     *
     * @param unencryptedString Unencrypted String
     * @return encrypted cipher
     * @throws StringEncrypter.EncryptionException in case of any excpetion.
     */

    public String encrypt(String unencryptedString) throws StringEncrypter.EncryptionException;

    /**
     * Return Decrypted String
     *
     * Using required Encryption Scheme and encryption key, cipher is decrypted
     * @param encryptedString
     * @return
     * @throws StringEncrypter.EncryptionException
     */
	
    public String decrypt(String encryptedString)   throws StringEncrypter.EncryptionException;

    /**
     * set EncryptionKey that will be used along with EncryptionScheme set.
     * @param encryptionKey
     */
    public void setEncryptionKey(String encryptionKey);

    /**
     * returns Encryption Key,If does not want to reveal encryption key, can return null.
     * @return
     */
    public String getEncryptionKey();

    /**
     * set EncryptionScheme that will be used for encrypting passwords
     * @param encryptionScheme
     */
    public void setEncryptionScheme(String encryptionScheme);

    /**
     * returns EncryptionScheme,If does not want to reveal encryption Scheme, can return null.
     * @return
     */
    public String getEncryptionScheme();

    /**
     * should return password corresponding to supplied key.
     * For example, Vault can be a hashmap with key,password pairs.Upon passing key, it will return password.
     * @param key
     * @return
     */
    public String getPasswdFromVault(String key);

    public IEncryptionLogger getLogger();

    public void setLogger(IEncryptionLogger logger);

}


