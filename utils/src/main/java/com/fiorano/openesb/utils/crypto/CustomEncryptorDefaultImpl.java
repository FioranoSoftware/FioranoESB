/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.crypto;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CustomEncryptorDefaultImpl implements ICustomEncryptor {

    private SecretKeySpec SkeySpec;
    private Cipher cipher;
    private String encryptionKey = CommonConstants.DEFAULT_ENCRYPTION_KEY;
    private String encryptionScheme = CommonConstants.AES_ENCRYPTION_SCHEME;
    private IEncryptionLogger logger;


    public CustomEncryptorDefaultImpl() {
        this(CommonConstants.AES_ENCRYPTION_SCHEME,CommonConstants.DEFAULT_ENCRYPTION_KEY);
    }

    /**
     * Returns StringEncrypter instance
     *
     * @param encryptionScheme Encryption Scheme
     * @throws StringEncrypter.EncryptionException EncryptionException
     */
    public CustomEncryptorDefaultImpl(String encryptionScheme)  {
        this(encryptionScheme, CommonConstants.DEFAULT_ENCRYPTION_KEY);
    }

    /**
     * Returns StringEncrypter instance
     *
     * @param encryptionScheme Encryption Scheme
     * @param encryptionKey    Encryption Key
     * @throws StringEncrypter.EncryptionException EncryptionException
     */
    public CustomEncryptorDefaultImpl(String encryptionScheme, String encryptionKey)  {
        if (encryptionKey == null)
            throw new IllegalArgumentException("encryption key was null");

        setEncryptionKey(encryptionKey);
        setEncryptionScheme(encryptionScheme);
    }

    private void initialize(String encryptionScheme, String encryptionKey) throws StringEncrypter.EncryptionException {
        try {
            //convert the encryption key to bytes.
            byte[] unhashedkeyAsBytes = encryptionKey.getBytes(CommonConstants.UNICODE_FORMAT);

            //converts the given string EncryptionKey into a message digest hash of length 128 bits
            MessageDigest digest = MessageDigest.getInstance(CommonConstants.MD5);
            digest.update(unhashedkeyAsBytes);
            byte[] keyAsBytes = digest.digest();

            //create Key spec using  corresponding encryption scheme


            if (CommonConstants.AES_ENCRYPTION_SCHEME.equalsIgnoreCase(encryptionScheme)) {
                SkeySpec = new SecretKeySpec(keyAsBytes, CommonConstants.AES_ENCRYPTION_SCHEME);
            } else if (CommonConstants.DES_ENCRYPTION_SCHEME.equalsIgnoreCase(encryptionScheme)) {

                /* DES algorithm uses a 8 byte key (7 +1 parity), so we take the first 8 bytes of the hash code obtained by MD5 */

                byte[] des8bytesKey = new byte[8];
                System.arraycopy(keyAsBytes, 0, des8bytesKey, 0, 7);
                SkeySpec = new SecretKeySpec(des8bytesKey, CommonConstants.DES_ENCRYPTION_SCHEME);
            } else if (CommonConstants.DESEDE_ENCRYPTION_SCHEME.equalsIgnoreCase(encryptionScheme)) {

                /* DESede algorithm uses a 24 byte key (21 + 3 parity), so we take the first 8 bytes of the hash code obtained by MD5 and concatenate
                 * it to the same 16 byte hash code */

                byte[] desede8bytesKey = new byte[24];
                System.arraycopy(keyAsBytes, 0, desede8bytesKey, 0, 15);
                System.arraycopy(keyAsBytes, 0, desede8bytesKey, 16, 7);
                SkeySpec = new SecretKeySpec(desede8bytesKey, CommonConstants.DESEDE_ENCRYPTION_SCHEME);
            } else {
                throw new IllegalArgumentException("Encryption scheme not supported: " + encryptionScheme);
            }

            cipher = Cipher.getInstance(encryptionScheme);
        }
        catch (UnsupportedEncodingException e) {
            throw new StringEncrypter.EncryptionException(e);
        }
        catch (NoSuchAlgorithmException e) {
            throw new StringEncrypter.EncryptionException(e);
        }
        catch (NoSuchPaddingException e) {
            throw new StringEncrypter.EncryptionException(e);
        }
    }

    /**
     * returns encrypted string
     *
     * @param unencryptedString Unencrypted String
     * @return encrypted string
     * @throws StringEncrypter.EncryptionException EncryptionException
     */
    public String encrypt(String unencryptedString) throws StringEncrypter.EncryptionException {

        initialize(getEncryptionScheme(), getEncryptionKey());

        //check if valid String passed
        if (unencryptedString == null)
            throw new IllegalArgumentException("unencrypted string was null or empty");

        try {
            byte[] cleartext;
            byte[] ciphertext;
            synchronized (cipher){
                cipher.init(Cipher.ENCRYPT_MODE, SkeySpec);
                cleartext = unencryptedString.getBytes(CommonConstants.UNICODE_FORMAT);
                //performs an encryption or decryption, based on what mode was initialized
                ciphertext = cipher.doFinal(cleartext);
            }
            //encodes a binary stream as a "Base64" format string
            BASE64Encoder base64encoder = new BASE64Encoder();
            return base64encoder.encode(ciphertext);
        }
        catch (Exception e) {
            throw new StringEncrypter.EncryptionException(e);
        }
    }

    /**
     * Returns decrypted string for an encrypted String
     *
     * @param encryptedString Encrypted String
     * @return Decrypted String
     * @throws StringEncrypter.EncryptionException EncryptionException
     */
    public String decrypt(String encryptedString) throws StringEncrypter.EncryptionException {

        initialize(getEncryptionScheme(), getEncryptionKey());

        if (encryptedString == null)
            throw new IllegalArgumentException("encrypted string was null or empty");

        try {
            byte[] cleartext;
            byte[] ciphertext;
            synchronized (cipher){
                cipher.init(Cipher.DECRYPT_MODE, SkeySpec);
                BASE64Decoder base64decoder = new BASE64Decoder();
                cleartext = base64decoder.decodeBuffer(encryptedString);
                ciphertext = cipher.doFinal(cleartext);
            }
            //converting the bytes to string
            return new String(ciphertext, CommonConstants.UNICODE_FORMAT);
        }
        catch (Exception e) {
            throw new StringEncrypter.EncryptionException(e);
        }
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public String getEncryptionScheme() {
        return encryptionScheme;
    }

    public String getPasswdFromVault(String key) {
        return key; //default implementation has nothing to do with passwd From Vault.
    }

    public IEncryptionLogger getLogger() {
        return logger;
    }

    public void setLogger(IEncryptionLogger logger) {
        this.logger = logger;
    }

    public void setEncryptionScheme(String encryptionScheme) {
        this.encryptionScheme = encryptionScheme;
    }

}
