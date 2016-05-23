/*
 * Copyright (c) Fiorano Software and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.microservice.launch.impl.cl;


import com.fiorano.openesb.microservice.bundle.Activator;
import com.fiorano.openesb.utils.FileUtil;
import com.fiorano.openesb.utils.I18NUtil;
import com.fiorano.openesb.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class ESBClassLoaderRepository {

    public final static String ATTRS_EXTN = ".attrs";
    public final static String RARFILE_PROP = "rarFile";

    // <File, ClassLoader>
    private final static HashMap classLoaders = new HashMap();

    // <File, Vector<ChangeListener>>
    private final static HashMap<File, Vector<ESBClassLoaderListener>> listeners = new HashMap<>();
    public static final String FIORANO_HOME = System.getProperty("user.dir");
    private static Logger logger = LoggerFactory.getLogger(Activator.class);

    /**
     * Adds a feature to the ChangeListener attribute of the ESBClassLoaderRepository class
     *
     * @param rarFile  The feature to be added to the ChangeListener attribute
     * @param listener The feature to be added to the ChangeListener attribute
     * @param priority The feature to be added to the ChangeListener attribute
     * @throws IOException
     */
    public static synchronized void addChangeListener(File rarFile, ESBClassLoaderListener listener, int priority)
            throws IOException {
        if (rarFile != null)
            rarFile = rarFile.getCanonicalFile();
        if (classLoaders.containsKey(rarFile)) {
            Vector<ESBClassLoaderListener> list = listeners.get(rarFile);

            if (list == null)
                listeners.put(rarFile, list = new Vector<>());
            if (priority == 0 && list.size() != 0)
                list.insertElementAt(listener, 0);
            else
                list.add(listener);
        }
    }

    public static synchronized void removeChangeListener(File rarFile, ESBClassLoaderListener listener)
            throws IOException {
        rarFile = rarFile.getCanonicalFile();

        Vector list = listeners.get(rarFile);

        if (list != null) {
            list.remove(listener);
            if (list.size() == 0)
                listeners.remove(rarFile);
        }
    }

    public static synchronized ClassLoader createClassLoader(File rarFile, boolean recreate)
            throws IOException {
        return createClassLoader(rarFile, Thread.currentThread().getContextClassLoader(), recreate);
    }

    public static synchronized ClassLoader createClassLoader(File rarFile)
            throws IOException {
        return createClassLoader(rarFile, Thread.currentThread().getContextClassLoader());
    }


    public static synchronized ClassLoader createClassLoader(File rarFile, ClassLoader parent)
            throws IOException {
        return createClassLoader(rarFile, parent, false);
    }

    public static synchronized ClassLoader createClassLoader(File rarFile, ClassLoader parent, boolean recreate)
            throws IOException {
        rarFile = rarFile.getCanonicalFile();

        if (!recreate) {
            ClassLoader loader = (ClassLoader) classLoaders.get(rarFile);
            if (loader != null)
                return loader;
        }

        File files[] = rarFile.listFiles();
        if (files == null || files.length == 0)
            return ESBClassLoaderRepository.class.getClassLoader();
        ArrayList<URL> urls = new ArrayList<>();

        for (File file : files) {
            if (file.isDirectory())
                continue;
            String name = file.getName().toUpperCase();

            if (name.endsWith(".JAR") || name.endsWith(".ZIP"))
                urls.add(FileUtil.file2URL(file));
        }
        if (urls.size() == 0)
            return ESBClassLoaderRepository.class.getClassLoader();
        else {
            ClassLoader loader = new ESBURLClassLoader(urls.toArray(new URL[urls.size()]), parent);
//            addClassLoader(rarFile, loader);
            return loader;
        }
    }

    public static synchronized ClassLoader createClassLoader(Set<File> rarFileSet, ClassLoader parent, boolean recreate,
                                                             boolean directoriesWithResources) {
        if (!recreate) {
            ClassLoader loader = (ClassLoader) classLoaders.get(rarFileSet);
            if (loader != null)
                return loader;
        }
       ArrayList<URL> urls = new ArrayList<>();
        for (File rarFile : rarFileSet) {
            addLibraries(rarFile, urls, directoriesWithResources);
        }
        addPeerLevelURLs(urls);
        if(parent != null) {
            return new URLClassLoader(urls.toArray(new URL[urls.size()]), parent);
        } else {
            return new URLClassLoader(urls.toArray(new URL[urls.size()]));
        }
    }

    private static void addPeerLevelURLs(ArrayList<URL> urls) {
        File configFile = new File(FIORANO_HOME + File.separator + "etc" + File.separator +
                "com.fiorano.inmemory.classpath.properties");
        try (BufferedReader r = new BufferedReader(new FileReader(configFile))) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.replace("FIORANO_HOME", FIORANO_HOME);
                File file = new File(line);
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    if (files != null) {
                        for (File child : files) {
                            loadJarURLs(urls,child);
                        }
                    }
                } else {
                    loadJarURLs(urls,file);
                }
                String s = file.getName().toUpperCase();
                if(!s.endsWith(".JAR") && !s.endsWith(".ZIP")) {
                    urls.add(FileUtil.file2URL(file));
                }
            }
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
        }
    }

    public static ClassLoader fetchClassLoader(String srcFile)
            throws Exception {
        ClassLoader loader = null;
        //read the cf.attrs files
        //the rarFile attrib is used to load the classloader for the MCF
        File cfAttrsFile = new File(new URI(srcFile + ATTRS_EXTN));
        Properties cfAttrs = new Properties();
        try (FileInputStream csTFileInputStream = new FileInputStream(cfAttrsFile)) {
            cfAttrs.load(csTFileInputStream);
        }

        String rarFilePath = cfAttrs.getProperty(RARFILE_PROP);
        if (!StringUtil.isEmpty(rarFilePath)) {
            String componentDir = System.getProperty("COMPONENTS_DIR");

            if (componentDir == null) {
                String fioranoHome = FIORANO_HOME;

                if (fioranoHome == null)
                    fioranoHome = System.getProperty("FMQ_DIR") + "/..";
                componentDir = fioranoHome + "/" + System.getProperty("ESB_REPOSITORY_DIRECTORY") + "/components";
            }

            File components = new File(componentDir);
            Map favorites = Collections.singletonMap("COMPONENTS", components.getCanonicalFile());
            File rarFile = FileUtil.resolve(cfAttrsFile.getParentFile(), rarFilePath, favorites);
            loader = ESBClassLoaderRepository.createClassLoader(rarFile);
        }
        return loader;
    }

    private static void addLibraries(File rarFile, List<URL> urls, boolean directoriesWithResources) {
        if (rarFile.isDirectory()) {
            if (directoriesWithResources) {
                URL url = FileUtil.file2URL(rarFile);
                urls.add(url);
            } else {
                File files[] = rarFile.listFiles();
                if (files == null || files.length == 0)
                    return;
                for (File file : files) {
                    if (file.isDirectory())
                        continue;
                    loadJarURLs(urls, file);
                }
            }
        } else {
            loadJarURLs(urls, rarFile);
        }
    }

    private static void loadJarURLs(List<URL> urls, File file) {
        String name = file.getName().toUpperCase();
        if (name.endsWith(".JAR") || name.endsWith(".ZIP")) {
            URL u = populateJarURL(file);
            urls.add(u);
        }
    }

    private static URL populateJarURL(File file) {
        URL url = FileUtil.file2URL(file);
        URL u;
        try {
            u = new URL("jar", "", -1, url.toString() + "!/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(I18NUtil.getMessage(ESBClassLoaderRepository.class, "error.in.creating.url.for.resource.0", url.toString()), e);
        }
        return u;
    }


    /**
     * Adds a feature to the ClassLoader attribute of the ESBClassLoaderRepository class
     *
     * @param loader  The feature to be added to the ClassLoader attribute
     * @param rarFile The feature to be added to the ClassLoader attribute
     */
    private static void addClassLoader(Object rarFile, ClassLoader loader) {
        ClassLoader oldClassLoader = (ClassLoader) classLoaders.get(rarFile);

        Vector list = listeners.get(rarFile);

        if (list != null) {
            list = (Vector) list.clone();
            for (Object aList : list) {
                ((ESBClassLoaderListener) aList).beforeUpdatingClassLoder(oldClassLoader, loader);
            }
        }

        list = listeners.get(null);
        if (list != null) {
            list = (Vector) list.clone();
            for (Object aList : list) {
                ((ESBClassLoaderListener) aList).beforeUpdatingClassLoder(oldClassLoader, loader);
            }
        }

        classLoaders.put(rarFile, loader);

        if (oldClassLoader == null)
            return;

        list = listeners.get(rarFile);

        if (list != null) {
            list = (Vector) list.clone();
            for (int i = list.size() - 1; i >= 0; i--)
                ((ESBClassLoaderListener) list.get(i)).afterUpdatingClassLoder(oldClassLoader, loader);
        }

        list = listeners.get(null);
        if (list != null) {
            list = (Vector) list.clone();
            for (int i = list.size() - 1; i >= 0; i--)
                ((ESBClassLoaderListener) list.get(i)).afterUpdatingClassLoder(oldClassLoader, loader);
        }
    }
}
