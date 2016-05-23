/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl.cl;

import com.fiorano.openesb.application.service.Resource;
import com.fiorano.openesb.application.service.Service;
import com.fiorano.openesb.application.service.ServiceRef;
import com.fiorano.openesb.microservice.bundle.Activator;
import com.fiorano.openesb.microservice.launch.LaunchConfiguration;
import com.fiorano.openesb.microservice.repository.MicroServiceRepoManager;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.exception.FioranoException;
import com.fiorano.openesb.utils.logging.api.FioranoClientLogger;
import com.fiorano.openesb.utils.logging.api.IFioranoLogger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ClassLoaderManager implements IClassLoaderManager {
    private final static String ID_SEPERATOR = "__";
    Hashtable<String, ClassLoader> classLoaderList;
    Hashtable<String, ClassLoader> loaders;
    Hashtable<String, Integer> classLoaderRefCount;
    private MicroServiceRepoManager componentRepository;
    private String componentRepositoryDir;


    private static final Map<String, File> favorites = Collections.singletonMap("FIORANO_HOME", new File(System.getProperty("user.dir")));

    private boolean usecache;

    private IFioranoLogger classLoaderMgrLogger = null;

    /**
     * Constructor which sets the component repository - needed for reading the SPS.
     */
    public ClassLoaderManager() throws FioranoException {

        componentRepository = MicroServiceRepoManager.getInstance();
        classLoaderList = new Hashtable<>();
        loaders = new Hashtable<>();
        classLoaderRefCount = new Hashtable<>();
        classLoaderMgrLogger = new FioranoClientLogger().getLogger("CLM");
    }

    /*
     * find all the libraries in the specified directory.
     */
    private static boolean findLibraries(File rarFile) {
        File files[] = rarFile.listFiles();
        ArrayList<URL> urls = new ArrayList<URL>();

        if (files == null || files.length == 0)
            return false;

        for (File file : files) {
            if (file.isDirectory())
                continue;
            String name = file.getName();

            if (!(name.endsWith(".jar") || name.endsWith(".zip")))
                continue;
            try {
                urls.add(file.toURI().toURL());
            } catch (MalformedURLException e) {
                //never happens
            }
        }
        return (urls.size() == 0);
    }

    /**
     * Caluculationg URL class path of service and it's dependencies recusively
     * constructiong URLClass Loader from hashset built.
     *
     * @param sps Service
     * @return Class Loader
     * @throws FioranoException FioranoException
     */
    private ClassLoader getSingleClassLoader(Service sps, ClassLoader parent) throws FioranoException {
        Set<String> urlhashset = new LinkedHashSet<String>();
        updatePaths(sps, urlhashset);
        //Create URL Class loader using url hashset
        ClassLoader componentClassLoader = createComponentClassLoader(urlhashset,parent);
        return componentClassLoader;
    }

    private ClassLoader getHierarchicalClassLoader(Service sps) throws FioranoException {
        classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 2, getUniqueComponentIdentifier(sps)));

        // Set the component repository
        componentRepositoryDir = componentRepository.getRepositoryLocation();

        // it the component's classloader has already been created. Return it.
        if (classLoaderList.containsKey(getUniqueComponentIdentifier(sps))) {
            classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 1, getUniqueComponentIdentifier(sps)));
            Integer count = classLoaderRefCount.get(getUniqueComponentIdentifier(sps));
            classLoaderRefCount.put(getUniqueComponentIdentifier(sps), count + 1);
            return classLoaderList.get(getUniqueComponentIdentifier(sps));
        }

        // Start creating the hierarchy tree.
        Iterator serviceDependencies = sps.getDeployment().getServiceRefs().iterator();
        Vector<ClassLoader> parentClassLoaderList = null;
        while (serviceDependencies.hasNext()) {
            ServiceRef sd = (ServiceRef) serviceDependencies.next();

            if (parentClassLoaderList == null)
                parentClassLoaderList = new Vector<>();

            Service dependentSPS = componentRepository.readMicroService(sd.getGUID(), String.valueOf(sd.getVersion()));

            if (!classLoaderList.containsKey(getUniqueComponentIdentifier(dependentSPS))) {
                // recursive call which makes a Depth first creation of the Classloader Hierarchy tree.
                // class loader is returned null in case the SPS doesnt have any libs which is required for execution.
                ClassLoader loader = getHierarchicalClassLoader(dependentSPS);
                if (loader != null) {
                    parentClassLoaderList.addElement(loader);
                }
                // have all the parent classloaders stored in a vector to create a join classloader on all of them
                // and set as the current classloaders parent.
            } else {
                classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 3, getUniqueComponentIdentifier(dependentSPS)));
                // at each stage in the search look for already created classloaders.
                parentClassLoaderList.addElement(classLoaderList.get(getUniqueComponentIdentifier(dependentSPS)));
                Integer count = classLoaderRefCount.get(getUniqueComponentIdentifier(dependentSPS));
//                int cc =count.intValue()+1;
//                System.out.println("Referring"+getUniqueComponentIdentifier(dependentSPS) +" Count:"+cc);
                classLoaderRefCount.put(getUniqueComponentIdentifier(dependentSPS), count + 1);
            }

        }

        ClassLoader joinParentClassLoader = null;

        // If there are parent classloaders, then create a join classloader of all them
        if (parentClassLoaderList != null) {
            joinParentClassLoader = new JoinClassLoader(parentClassLoaderList, classLoaderMgrLogger);
            classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 4, joinParentClassLoader.toString()));
        }

        Set<String> urlhashset = new LinkedHashSet<String>();
        addResources(sps, urlhashset);
        // create the classloader for the current component with the join classloader of all its dependencies as its parent.
        ClassLoader componentClassLoader = createComponentClassLoader(urlhashset, joinParentClassLoader);

        // componentClassLoader can be null when
        // we try to create a classloader using ESBClassLoaderRepository.createClassLoader() and the fileHashSet doesnt have any
        // libraries in it. Take a example for Encryption which has no libraries of its own but depends fully on EncryptDecrypt
        // library and hence the library search in URL List of such classloaders will be nul and hency the function returns with
        // FPS classloader and its parent being the SUN APP class loader. to prevent this dont create a empty classloader for such
        // component instead give the Join parent classloaders as its classloader.
        if (componentClassLoader == null) {
            classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 5, getUniqueComponentIdentifier(sps)));
            return joinParentClassLoader;
        }

        // store it in the list.
        classLoaderList.put(getUniqueComponentIdentifier(sps), componentClassLoader);
        classLoaderRefCount.put(getUniqueComponentIdentifier(sps), new Integer(1));
        classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 7, getUniqueComponentIdentifier(sps)));
        // return the complete classloader
        return componentClassLoader;

    }

    public ClassLoader getClassLoader(Service sps, LaunchConfiguration launchConfiguration,ClassLoader parent) throws FioranoException {

        classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 2, getUniqueComponentIdentifier(sps)));
        // Set the component repository
        componentRepositoryDir = componentRepository.getRepositoryLocation();

        if (!usecache) {
            ClassLoader singleClassLoader = getSingleClassLoader(sps,parent);
            loaders.put(launchConfiguration.getServiceName(),singleClassLoader);
            return singleClassLoader;
        } else
            return getHierarchicalClassLoader(sps);

    }

    /*
    * UnLoads the class loader if there are no running components that are referring to it
    */
    public void unloadClassLoader(Service sps, LaunchConfiguration launchConfiguration) throws FioranoException {
        if (!usecache) {
            URLClassLoader remove = (URLClassLoader) loaders.remove(launchConfiguration.getServiceName());
            try {
                remove.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        Integer count = classLoaderRefCount.get(getUniqueComponentIdentifier(sps));
        if (count == null) return;
        if (count == 1) {
            classLoaderList.remove(getUniqueComponentIdentifier(sps));
            classLoaderRefCount.remove(getUniqueComponentIdentifier(sps));
        } else {
            classLoaderRefCount.put(getUniqueComponentIdentifier(sps), count - 1);
        }

        for (ServiceRef sd : sps.getDeployment().getServiceRefs()) {
            Service dependentSPS = componentRepository.readMicroService(sd.getGUID(), String.valueOf(sd.getVersion()));
            if (classLoaderList.containsKey(getUniqueComponentIdentifier(dependentSPS))) {
                unloadClassLoader(dependentSPS,launchConfiguration);
            }
        }
    }


    String getUniqueComponentIdentifier(Service sps) {
        return sps.getGUID() + ID_SEPERATOR + String.valueOf(sps.getVersion());
    }

    ClassLoader createComponentClassLoader(Set<String> hashset, ClassLoader parentClassLoader) throws FioranoException {

        Set<File> fileHashSet = getFileHashset(hashset);
        boolean emptyClassLoader = findEmptyClassLoader(fileHashSet);

        // we try to create a classloader using ESBClassLoaderRepository.createClassLoader() and the fileHashSet doesnt have any
        // libraries in it. Take a example for Encryption which has no libraries of its own but depends fully on EncryptDecrypt
        // library and hence the library search in URL List of such classloaders will be nul and hency the function returns with
        // peer classloader and its parent being the SUN APP class loader. to prevent this dont create a empty classloader for such
        // component instead give the Join parent classloaders as its classloader.
        if (emptyClassLoader)
            return parentClassLoader;
        return ESBClassLoaderRepository.createClassLoader(fileHashSet, parentClassLoader, true, false);

    }

    /**
     * Get component base directory
     *
     * @param componentGUID    uniquely specifies a component that need to be launched
     * @param componentVersion specifies version of launchable component
     * @return String Base directory of the component
     */
    private String getComponentBaseDir(String componentGUID, String componentVersion) {
        return componentRepositoryDir + File.separator +
                componentGUID + File.separator + componentVersion;
    }

    /*
     * Get a LinkedHashSet of files passed a LinkedHashSet of filenames.
     */
    private Set<File> getFileHashset(Set<String> hashSet) {
        Iterator<String> iterator = hashSet.iterator();
        Set<File> fileSet = new LinkedHashSet<File>();

        while (iterator.hasNext()) {
            File f = new File(iterator.next());

            //System.out.println(f.getAbsolutePath());
            if (f.exists())
                fileSet.add(f);
        }
        return fileSet;
    }

    /**
     * Determines whether the fileHashset contains any libraries.
     * The findLibraries returns ((validurls.size()==0). If it returns true then it means that the
     * file hashset doesnt contain any valid libs. If there is nothing in the fileHashset also
     * this function will return true.
     *
     * @param fileHashSet LinkedHashset
     * @return boolean
     */
    private boolean findEmptyClassLoader(Set<File> fileHashSet) {
        boolean emtpyClassLoader = true;
        Iterator<File> iterator = fileHashSet.iterator();

        while (iterator.hasNext()) {
            emtpyClassLoader = findLibraries(iterator.next());
        }
        return emtpyClassLoader;
    }

    /*
     * Add the component base dir and the service resources into the Linkedhashset.
     */
    private void updatePaths(Service sps, Set<String> hashset) throws FioranoException {

        Iterator serviceDependencies = sps.getDeployment().getServiceRefs().iterator();
        while (serviceDependencies.hasNext()) {
            ServiceRef sd = (ServiceRef) serviceDependencies.next();
            Service dependentSPS = componentRepository.readMicroService(sd.getGUID(), String.valueOf(sd.getVersion()));
            updatePaths(dependentSPS, hashset);
        }
        addResources(sps, hashset);
        classLoaderMgrLogger.trace(LogHelper.getOutMessage("CLM", 9, getUniqueComponentIdentifier(sps), hashset.toString()));
    }

    /*
    * Iterate through the resources and add them all to the Linkedhashset.
    */
    private void addResources(Service sps, Set<String> hashset) {
        Iterator resources = sps.getDeployment().getResources().iterator();

        // get base dir
        String componentBaseDir = getComponentBaseDir(sps.getGUID(),
                String.valueOf(sps.getVersion()));

        while (resources.hasNext()) {
            Resource resource = (Resource) resources.next();
            String resc = resource.getName();
            if (resource.isRequiredForExecution()) {
                String completeResc = FileUtil.resolve(new File(componentBaseDir), resc, favorites).getAbsolutePath();

                if ((resc.endsWith(".jar") || resc.endsWith(".zip") || resc.endsWith(".class"))) {
                    hashset.add(completeResc);
                } else if (resc.endsWith(".dll") || resc.endsWith(".so") || resc.endsWith(".sl")) {
                    hashset.add(new File(completeResc).getParent());
                }
            }
        }
    }

    /*
     * Determine whetehr the resource specified by resc is native or not.
     */
//    private boolean hasNativeResource(Service componentPS, String resc)
//    {
//        if (resc.endsWith(".dll"))
//            return true;
//
//        if (resc.endsWith(".so"))
//            return true;
//
//        return resc.endsWith(".sl");
//    }
    void setClassLoaderMgrLogger(IFioranoLogger classLoaderMgrLogger) {
        this.classLoaderMgrLogger = classLoaderMgrLogger;
    }
}
