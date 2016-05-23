/*
 * Copyright (c) Fiorano Software Pte. Ltd. and affiliates. All rights reserved. http://www.fiorano.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package com.fiorano.openesb.utils.logging;


import com.fiorano.openesb.utils.io.ReverseLineReader;
import com.fiorano.openesb.utils.logging.api.ILogIterator;

import java.io.*;
import java.nio.charset.Charset;

public class DefaultLogIterator implements ILogIterator
{
    // log owner
    private DefaultLogManager m_owner;

    // directory where log files are stored
    private String m_logDirectory;

    // file name filter
    private FilenameFilter m_fileFilter;

    // current opened log file
    private RandomAccessFile m_currentDataFile;

    // current log file
    private File m_currentFile;

    private long    m_currentFileTimeStamp;

    private int     BLOCK_SIZE = 10240;

    private byte[]  m_data = new byte[BLOCK_SIZE];

    // current index
    private long    m_currentIndex;
    private String encoding;

    private static final String FIORANO_LOG_FILE_ENCODING = (System.getProperty("fioranolog.file.encoding")!=null) ? System.getProperty("fioranolog.file.encoding") : null;

    /**
     * @param logDir
     * @param fileFilter
     * @param owner
     * @param encoding
     */
    public DefaultLogIterator(DefaultLogManager owner, String logDir, FilenameFilter fileFilter, String encoding)
    {
        m_owner = owner;
        m_logDirectory = logDir;
        m_fileFilter = fileFilter;
        this.encoding = encoding;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext()
    {
        try
        {
            if (hasData(getCurrentLogFile()))
                return true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();

            return false;
        }

        return false;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.) Uses log file number to check for the correct log file.
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext(int logFileNo)
    {
        try
        {
            if (getCurrentLogFile(logFileNo)!=null)
                return true;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();

            return false;
        }

        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration.
     */
    public String next()
    {
        try
        {
            if (!hasData(getCurrentLogFile()))
                return "";
//                throw new NoSuchElementException("No log records exists");

            byte[] data = null;

            int fetchSize = available(m_currentDataFile);

            if (fetchSize > BLOCK_SIZE)
            {
                fetchSize = BLOCK_SIZE;
                data = m_data;
            }
            else
                data = new byte[fetchSize];

            m_currentDataFile.read(data, 0, fetchSize);

            String logString = new String(data,encoding);
            m_owner.m_logger.info(DefaultLogIterator.class, "Read_data", m_logDirectory, logString);

            return logString;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * Returns the next element in the iteration. Uses log file number to fetch the correct log file
     *
     * @return the next element in the iteration.
     */
    public String next(int logFileNo)
    {
        try
        {
            if (!hasData(getCurrentLogFile(logFileNo)))
                return "";
//                throw new NoSuchElementException("No log records exists");

            byte[] data = null;

            int fetchSize = available(m_currentDataFile);

            if (fetchSize > BLOCK_SIZE)
            {
                fetchSize = BLOCK_SIZE;
                data = m_data;
            }
            else
                data = new byte[fetchSize];


            ReverseLineReader rlr = new ReverseLineReader(m_currentDataFile,fetchSize, FIORANO_LOG_FILE_ENCODING!=null ? Charset.forName(FIORANO_LOG_FILE_ENCODING) : Charset.defaultCharset());
            String temp=readRecord(rlr);
            String str = "";
            while (temp!=null && !temp.equalsIgnoreCase(""))
            {

               str = str+ temp + "\n";
               temp = readRecord(rlr);
            }
            data=str.getBytes();
            String logString = new String(data,encoding);
            m_owner.m_logger.info(DefaultLogIterator.class, "Read_data", m_logDirectory, logString);
            m_currentDataFile = _getNextLogFile(logFileNo);
            int length = logString.length();
            if(logString.length()!=0)
            {
            logString = logString.substring(0,length-1);
            }
            rlr.close();
            return logString;
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return "";
        }
    }
    private String readRecord(ReverseLineReader rlr) throws IOException {
        StringBuffer record = new StringBuffer();
        String tempString = null;
        while((tempString=rlr.readLine()) != null && !isDelimiter(tempString)) {
            record.insert(0,tempString).insert(0,"\n");
        }
        return record.toString().trim();
    }

    private boolean isDelimiter(String tempString) {
        return tempString.startsWith("!ENTRY");
    }

    /**
     * Close the resources opened for this iterator
     */
    public void close()
    {
        _close(m_currentDataFile);
    }

    /**
     * Returns <tt>true</tt> if this list iterator has more elements when
     * traversing the list in the reverse direction.  (In other words, returns
     * <tt>true</tt> if <tt>previous</tt> would return an element rather than
     * throwing an exception.)
     *
     * @return <tt>true</tt> if the list iterator has more elements when
     *	       traversing the list in the reverse direction.
     */
    public boolean hasPrevious()
    {
        return false;
    }

    /**
     * Returns the previous element in the list.  This method may be called
     * repeatedly to iterate through the list backwards, or intermixed with
     * calls to <tt>next</tt> to go back and forth.  (Note that alternating
     * calls to <tt>next</tt> and <tt>previous</tt> will return the same
     * element repeatedly.)
     *
     * @return the previous element in the list.
     */
    public String previous()
    {
        return "";
    }

    private RandomAccessFile getCurrentLogFile()
            throws IOException
    {
        if (hasData(m_currentDataFile))
            return m_currentDataFile;
        else
            _close(m_currentDataFile);

        m_currentDataFile = _getNextLogFile();


        m_owner.m_logger.info(DefaultLogIterator.class, "m_currentFile", m_logDirectory, m_currentFile);

        return m_currentDataFile;
    }

    /*
    Uses log file number to get the correct current log file.
     */
    private RandomAccessFile getCurrentLogFile(int logFileNo)
            throws IOException
    {   if (hasData(m_currentDataFile))
        return m_currentDataFile;
    else
        _close(m_currentDataFile);

        m_currentDataFile = _getNextLogFile(logFileNo);


        m_owner.m_logger.info(DefaultLogIterator.class, "m_currentFile", m_logDirectory, m_currentFile);

        return m_currentDataFile;
    }

    /**
     * Check Whether log directory exists or not
     *
     * @return boolean
     */
    private boolean logDirExists()
    {
        if (m_logDirectory == null)
        {
            return false;
        }

        File logdir = new File(m_logDirectory);

        if (logdir.exists() && logdir.isDirectory())
            return true;

        return false;
    }

    /**
     * Get a list of log files
     *
     * @return
     */
    private File[] listLogFiles()
    {
        if (!logDirExists())
        {

            m_owner.m_logger.info(DefaultLogIterator.class, "log_dir_does_not_exist", m_logDirectory);

            return new File[0];
        }

        File logDir = new File(m_logDirectory);

        File[] files = logDir.listFiles(m_fileFilter);

        if (files == null)
        {

            m_owner.m_logger.info(DefaultLogIterator.class, "No_file_found", m_logDirectory);
            files = new File[0];
        }


        m_owner.m_logger.info(DefaultLogIterator.class, "File_list_size", m_logDirectory, new Integer(files.length));

        return files;
    }

    private boolean hasData(RandomAccessFile dataFile)
            throws IOException
    {
        if (available(dataFile) > 0)
        {

            m_owner.m_logger.info(DefaultLogIterator.class, "Current_file_has_data", m_logDirectory);

            return true;
        }


        m_owner.m_logger.info(DefaultLogIterator.class, "Current_file_is_empty", m_logDirectory);

        return false;
    }

    private void _close(RandomAccessFile dataFile)
    {
        if (dataFile == null)
            return;

        try
        {
            dataFile.close();
        }
        catch (IOException ex)
        {
            // Ignore the exception
            ex.printStackTrace();
        }
    }

    private RandomAccessFile _getNextLogFile()
            throws IOException
    {
        File[] logFiles = listLogFiles();

        File nextFile = null;
        long nextMin = Long.MAX_VALUE;

        for (int i = 0; i < logFiles.length; i++)
        {
            long temp = logFiles[i].lastModified();

            if (
                    (temp > m_currentFileTimeStamp) &&
                            (temp < nextMin)
                    )
            {
                nextMin = temp;
                nextFile = logFiles[i];
            }
        }


        m_owner.m_logger.info(DefaultLogIterator.class, "Next_Log_File", m_logDirectory, nextFile);

        if (equal(nextFile, m_currentFile))
        {

            m_owner.m_logger.info(DefaultLogIterator.class, "Next_Log_File_equals_current_file", m_logDirectory);

            return null;
        }

        if (nextFile == null)
            return null;

        m_currentFile = nextFile;
        m_currentFileTimeStamp = nextMin;

        try
        {
            // Fixed N_4657
            //
            return new RandomAccessFile(nextFile, "r");
        }
        catch (FileNotFoundException ex)
        {
            // Ignore the exception.
            // May be this file got deleted
            //

            return _getNextLogFile();
        }
    }

    /*
    Uses log file number to get the next log file in sequence.
     */
    private RandomAccessFile _getNextLogFile(int logFileNo)
            throws IOException
    {
        File[] logFiles = listLogFiles();
        File nextFile = null;
        if(logFiles.length > logFileNo)
        {
            for(int i=0; i<logFiles.length;i++)
            {
                if(logFiles[i].toString().endsWith(String.valueOf(logFileNo)))
                {
                    nextFile=logFiles[i];
                }  else if(logFiles.length == 1)
                {
                    nextFile = logFiles[i];
                }

            }
        }

        m_owner.m_logger.info(DefaultLogIterator.class, "Next_Log_File", m_logDirectory, nextFile);

        if (equal(nextFile, m_currentFile))
        {

            m_owner.m_logger.info(DefaultLogIterator.class, "Next_Log_File_equals_current_file", m_logDirectory);

            return null;
        }

        if (nextFile == null)
            return null;

        m_currentFile = nextFile;
        try
        {
            // Fixed N_4657
            //
            return new RandomAccessFile(nextFile, "r");
        }
        catch (FileNotFoundException ex)
        {
            // Ignore the exception.
            // May be this file got deleted
            //

            return _getNextLogFile(logFileNo);
        }
    }

    private boolean equal(File f1, File f2)
    {
        if ((f1 == null) && (f2 == null))
            return true;

        if ((f1 == null) || (f2 == null))
            return false;

        return f1.equals(f2);
    }

    private int available(RandomAccessFile file)
            throws IOException
    {
        if (file == null)
            return -1;

        long currIndex = file.getFilePointer();
        long length = file.length();

        return (int) (length - currIndex);
    }
}
