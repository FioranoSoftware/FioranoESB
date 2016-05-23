/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package com.fiorano.openesb.applicationcontroller;

import com.fiorano.openesb.application.application.Application;
import com.fiorano.openesb.application.application.RemoteServiceInstance;
import com.fiorano.openesb.utils.Constants;
import com.fiorano.openesb.utils.exception.FioranoException;

import java.util.*;

public class ChainLaunchHelper {
    //To store the list of applications referring particular component(key) and values(Event_Process)
    private HashMap<String, Set<String>> COMPONENTS_REFERRING_APPS;

    //To store the set of applications referring particular
    private HashMap<String,Set<String>> REFERRING_APPS_LIST;

    //To store the list of applications GUID__Versions which the 'key' application is depends on
    private HashMap<String, Set<String>> DEPEND_APP_LIST;

    private ApplicationController applicationController;

    ChainLaunchHelper(ApplicationController applicationController){
        COMPONENTS_REFERRING_APPS = new HashMap<>(Constants.INITIAL_CAPACITY);
        REFERRING_APPS_LIST = new HashMap<>(Constants.INITIAL_CAPACITY);
        DEPEND_APP_LIST = new HashMap<>(Constants.INITIAL_CAPACITY);
        this.applicationController = applicationController;
    }

    public Set<String> getReferringRunningApplications(String appGUID, float appVersion, String servInstName) throws FioranoException {
        Set<String> appGUIDSreferring = new HashSet<>();

        for (ApplicationHandle handle: applicationController.getApplicationHandles().values()) {
            Application application = handle.getApplication();
            for (Object o : application.getRemoteServiceInstances()) {
                RemoteServiceInstance extInstance = (RemoteServiceInstance) o;
                if (extInstance.getApplicationGUID().equalsIgnoreCase(appGUID) &&
                        extInstance.getApplicationVersion() == appVersion &&
                        extInstance.getRemoteName().equalsIgnoreCase(servInstName))
                    appGUIDSreferring.add(application.getGUID() + Constants.NAME_DELIMITER + application.getVersion());
            }
        }
        return appGUIDSreferring;
    }

    public Set<String> getAllReferringApplications(String appGUID, float appVersion, String serviceInstName) throws FioranoException{
        String searchKey = appGUID + Constants.NAME_DELIMITER + appVersion  + Constants.NAME_DELIMITER + serviceInstName;
        if (COMPONENTS_REFERRING_APPS.containsKey(searchKey)){
            return COMPONENTS_REFERRING_APPS.get(searchKey);
        }
        return COMPONENTS_REFERRING_APPS.get(searchKey);
    }

    public boolean isApplicationReferred(String appGUID, float appVersion) throws FioranoException{
        boolean isAppReferred = false;
        //check if any application depends on this appGUID
        Set<String> setOfApplicationDependsOnThisAppGUID = REFERRING_APPS_LIST.get(appGUID + Constants.NAME_DELIMITER + appVersion);
        if (setOfApplicationDependsOnThisAppGUID != null && !setOfApplicationDependsOnThisAppGUID.isEmpty()){
            isAppReferred = true;
        }
        return isAppReferred;
    }

    /**
     * update chain launch data structures .
     * @param application application dmi
     */
    void updateChainLaunchDS(Application application) {
        // logger.debug(Bundle.class, Bundle.EXECUTING_CALL, "updateApplicationReferringApps(" + application.getGUID() + ")");
        List remoteServiceInstances = application.getRemoteServiceInstances();
        if (!remoteServiceInstances.isEmpty()) {
            String app_version = application.getGUID() + Constants.NAME_DELIMITER + application.getVersion();
            for (Object obj : remoteServiceInstances) {
                RemoteServiceInstance oldRemote = (RemoteServiceInstance) obj;
                String key = oldRemote.getApplicationGUID() + Constants.NAME_DELIMITER + oldRemote.getApplicationVersion();
                String referredComponent = key + Constants.NAME_DELIMITER + oldRemote.getRemoteName() ;
                if (REFERRING_APPS_LIST.containsKey(key)) {
                    REFERRING_APPS_LIST.get(key).add(app_version);
                } else {
                    Set<String> referringApps = new LinkedHashSet<>();
                    referringApps.add(app_version);
                    REFERRING_APPS_LIST.put(key, referringApps);
                }

                if (DEPEND_APP_LIST.containsKey(app_version)) {
                    DEPEND_APP_LIST.get(app_version).add(key);
                } else {
                    Set<String> dependsList = new LinkedHashSet<>();
                    dependsList.add(key);
                    DEPEND_APP_LIST.put(app_version, dependsList);
                }
                //COMP_REF DETAILS CAN BE ADDED HERE instead of Save()
                if (COMPONENTS_REFERRING_APPS.containsKey(referredComponent)) {
                    COMPONENTS_REFERRING_APPS.get(referredComponent).add(app_version);
                } else {
                    LinkedHashSet<String> referringApps = new LinkedHashSet<>();
                    referringApps.add(app_version);
                    COMPONENTS_REFERRING_APPS.put(referredComponent, referringApps);
                }
            }
        }
    }

    /**
     * cleanup chain launch and chain shutdown data structures
     * @param oldApplication oldApplication
     */
    void removeChainLaunchDS(Application oldApplication) {
        removeChainLaunchDS(oldApplication.getGUID()+ Constants.NAME_DELIMITER+oldApplication.getVersion());
    }
    /**
     * cleanup chain launch and chain shutdown data structures
     * @param oldAppVersion oldAppVersion
     */
    void removeChainLaunchDS(String oldAppVersion){
        //Remove form  depend list
        DEPEND_APP_LIST.remove(oldAppVersion );

        //remove application in referring apps list
        if(REFERRING_APPS_LIST.size() >0){
            Set<String> allReferredApps = REFERRING_APPS_LIST.keySet() ;
            for( String eachApp : allReferredApps ){
                REFERRING_APPS_LIST.get(eachApp).remove(oldAppVersion);
            }
        }
        //Remove application from component referring list
        if(COMPONENTS_REFERRING_APPS.size() >0){
            Set<String> allReferredApps = COMPONENTS_REFERRING_APPS.keySet() ;
            for(String eachApp : allReferredApps )   {
                COMPONENTS_REFERRING_APPS.get(eachApp ).remove(oldAppVersion);
            }
        }
    }

    public boolean cyclicDependencyExists(Application application) throws FioranoException {
        boolean cyclicDependency = false;
        String actualAppGUID = application.getGUID();
        Set<String> remoteList = new LinkedHashSet<>();
        if (application.getRemoteServiceInstances().isEmpty())
            return false;
        String app_version = actualAppGUID + Constants.NAME_DELIMITER + application.getVersion();
        for (Object eachRemoteInstance : application.getRemoteServiceInstances()) {
            RemoteServiceInstance oldRemote = (RemoteServiceInstance) eachRemoteInstance;
            String key = oldRemote.getApplicationGUID() + Constants.NAME_DELIMITER + oldRemote.getApplicationVersion();
            remoteList.add(key);
            populateDependencyList(key, remoteList, app_version);
        }
        if (remoteList.contains(app_version))
            cyclicDependency = true;
        return cyclicDependency;
    }

    Set<String> populateDependencyList(String appGUID_Version, Set<String> remoteList, String originalApp){
        if (DEPEND_APP_LIST.containsKey(appGUID_Version)){
            for(String currentApp_Version : DEPEND_APP_LIST.get(appGUID_Version) ){
                if(! (currentApp_Version.equalsIgnoreCase(originalApp) || (currentApp_Version.equalsIgnoreCase(appGUID_Version))) ) {
                    populateDependencyList(currentApp_Version, remoteList, originalApp);
                }
                remoteList.add(currentApp_Version);
            }
        }
        return remoteList;
    }

    Set<String> populateReferringList(String app_version, Set<String> remoteList){
        if(REFERRING_APPS_LIST .containsKey(app_version) ){
            Set<String> referringList = REFERRING_APPS_LIST.get(app_version);
            for (String currentAppGUID_Version: referringList){
                populateReferringList(currentAppGUID_Version, remoteList);
                if (!remoteList.contains(currentAppGUID_Version))
                    remoteList.add(currentAppGUID_Version);
            }
        }
        return remoteList;
    }

    public Map<String, Set<String>> getReferringAppsList() {
        return REFERRING_APPS_LIST;
    }
}
