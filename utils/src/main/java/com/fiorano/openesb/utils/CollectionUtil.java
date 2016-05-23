/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public final class CollectionUtil {
    public static final Enumeration EMPTY_ENUMERATION = new EmptyEnumeratorAndIterator();
    public static final Iterator EMPTY_ITERATOR = (Iterator)EMPTY_ENUMERATION;

    public static Collection toCollection(Enumeration _enum, Collection c){
        if(c==null)
            c = new ArrayList();
        while(_enum.hasMoreElements())
            c.add(_enum.nextElement());
        return c;
    }

    public static ArrayList toCollection(Enumeration _enum){
        return (ArrayList)toCollection(_enum, new ArrayList());
    }

    public static Collection toCollection(Iterator iter, Collection c){
        if(c==null)
            c = new ArrayList();
        while(iter.hasNext())
            c.add(iter.next());
        return c;
    }

    public static ArrayList toCollection(Iterator iter){
        return (ArrayList)toCollection(iter, new ArrayList());
    }

    private static final class EmptyEnumeratorAndIterator implements Enumeration, Iterator{
        public boolean hasMoreElements(){ return false; }
        public Object nextElement(){ throw new NoSuchElementException(); }
        public boolean hasNext(){ return false; }
        public Object next(){ throw new NoSuchElementException(); }
        public void remove(){ throw new UnsupportedOperationException(); }
    }

    public int[] getSortedIndxes(final List list){
        Integer indexes[] = new Integer[list.size()];
        for(int i=0; i<indexes.length; i++)
            indexes[i] = new Integer(i);

        Comparator cmp = new Comparator(){
            public int compare(Object o1, Object o2){
                int i = ((Integer)o1).intValue();
                int j = ((Integer)o2).intValue();
                return ((Comparable)list.get(i)).compareTo(list.get(j));
            }
        };
        Arrays.sort(indexes, cmp);

        int index[] = new int[indexes.length];
        for(int i=0; i<index.length; i++)
            index[i] = indexes[i].intValue();

        return index;
    }

    public static boolean containsStrictly(List list, Object obj){
        int index = list.indexOf(obj);
        return index==-1 ? false : list.get(index)==obj;
    }

    public static String suggest(Map map/*<String, Object>*/, String prefix, boolean key){
        return suggest(key ? map.keySet() : map.values(), prefix);
    }

    public static String suggest(Collection c, String prefix){
        int i=1;
        while(c.contains(prefix+i))
            i++;
        return prefix+i;
    }

    public static void set(List list, int index, Object value){
        while(index>=list.size())
            list.add(null);
        list.set(index, value);
    }

    public static Object getKeyWithValue(Map map, Object value){
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            if(Util.equals(entry.getValue(), value))
                return entry.getKey();
        }
        return null;
    }

    public static Properties loadProperties(File file, Properties props) throws IOException {
        if(props==null)
            props = new Properties();
        FileInputStream fis = null;
        try{
            props.load(fis = new FileInputStream(file));
        } finally{
            if(fis!=null)
                fis.close();
        }
        return props;
    }

    /*-------------------------------------------------[ String convertors ]---------------------------------------------------*/

    public static String toString(Enumeration/*<String>*/ enumer, String separator){
        if(separator==null)
            separator = ","; //NOI18N
        StringBuffer buf = new StringBuffer();
        while(enumer.hasMoreElements()){
            if(buf.length()!=0)
                buf.append(separator);
            buf.append(enumer.nextElement());
        }
        return buf.toString();
    }

    public static String toString(Collection c/*<String>*/, String separator){
        return toString(Collections.enumeration(c), separator);
    }

    /*-------------------------------------------------[ Permutation ]---------------------------------------------------*/

    public static void reorder(List originalList, int perm[], List newList){
        newList.clear();
        int len = originalList.size();
        int[] newPerm = new int[len];
        for(int i = 0; i<len; i++)
            newPerm[perm[i]] = i;

        for(int i = 0; i<len; i++)
            newList.add(originalList.get(newPerm[i]));
    }
}

