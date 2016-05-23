/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.tools;

import com.fiorano.openesb.utils.crypto.StringEncrypter;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.InputRequest;

import java.io.IOException;
import java.util.StringTokenizer;

public class RmiLoginInfo
{
    public boolean verboseFlag;
    public int      port = 2099;
    public String   user;
    public String   pwd;
    public String   hostname = "localhost";
    public String   FIORANO_HOME;
    boolean         infoComplete;
    public long     timeout = 180000;
    public String isEncrypted;

    /**
     */
    public RmiLoginInfo()
    {
        infoComplete = false;
        isEncrypted="false";
    }

    /**
     * Sets login info for object
     *
     * @param string
     */
    public void setLoginInfo(String string)
    {
        StringTokenizer st = new StringTokenizer(string, ";,", false);

        try {
            int tokenCount = st.countTokens();
            user = st.nextToken();
            pwd = st.nextToken();
            hostname = st.nextToken();
            port = Integer.parseInt(st.nextToken());
            timeout = Long.parseLong(st.nextToken());


            if (user.equals("{NOT_SPECIFIED}")) {
                System.console().printf("Enter Username :");
                user = System.console().readLine();
            }

            if (pwd.equals("{NOT_SPECIFIED}")) {
                System.console().printf("Enter Password [User=%s] :", user);
                pwd = String.valueOf(System.console().readPassword());
            }
            if (isEncrypted.equals("{NOT_SPECIFIED}")|| isEncrypted.isEmpty()) {
                System.console().printf("Enter if password is encrypted : ");
                isEncrypted=System.console().readLine();
            }
            if(! (isEncrypted.equalsIgnoreCase("true")|| isEncrypted.equalsIgnoreCase("false"))){
                System.err.println("Wrong input for is password encrypted value. Please use true / false");
                System.exit(0);
            }

            if(isEncrypted.equalsIgnoreCase("true"))
                try{
                    pwd= StringEncrypter.getDefaultInstance().decrypt(pwd)  ;
                }catch (Exception e){
                    System.err.println("Password failed to decrypt. Please provide correct encrypted password");
                    System.exit(0);
                }
                if (st.hasMoreTokens()){
                    String flag = st.nextToken();
                    if (flag.equalsIgnoreCase("true"))
                        verboseFlag = true;
                    else
                        verboseFlag = false;
                }
                if (st.hasMoreTokens())
                    FIORANO_HOME = st.nextToken();
                else
                    FIORANO_HOME = "";
                infoComplete = true;
        } catch (Exception ignore) {
            System.out.println(ignore.getMessage());
            //Ignore
        }
    }

    /**
     * @param task
     */
    public void askLogin(Task task)
    {
        if (!infoComplete) {
            try {
                if (user == null)
                    user = userInput("User Name", task);
                if (pwd == null)
                    pwd = userInput("User Password", task);
                if (isEncrypted==null)
                    isEncrypted=userInput("Is Password Encrypted",task)  ;

                    if (hostname == null)
                        hostname = userInput("Hostname", task);
                    if (port == -1)
                        port = Integer.parseInt(userInput("Port", task));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else
            System.out.println("Connecting To Rmi Server on : " + hostname +":"+port + " for User : " + user);
    }

    private String userInput(String query, Task task)
            throws IOException
    {
        InputRequest inr = new InputRequest("Enter " + query);

        task.getProject().getInputHandler().handleInput(inr);

        String str = inr.getInput();

        return str;
    }
}

