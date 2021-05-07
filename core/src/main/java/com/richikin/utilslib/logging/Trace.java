package com.richikin.utilslib.logging;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.StringBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.GregorianCalendar;

public class Trace
{
    /*
     * NB: Do not make these final, as they can be modified
     */
    private static final String  debugTag = "DEBUG";
    private static final String  errorTag = "ERROR";
    private static       File    logFile;
    private static       boolean isWriteFileActive;

    /**
     * Write a debug string to logcat or console.
     * The string can contain format options.
     *
     * @param formatString The string, or format string, to display.
     * @param args         Arguments for use in format strings.
     */
    public static void dbg(String formatString, Object... args)
    {
        if (Gdx.app.getLogLevel() == Application.LOG_DEBUG)
        {
            StringBuilder debugString = new StringBuilder();

            debugString.append("     [ ");
            debugString.append(formatString);

            if (args.length > 0)
            {
                for (Object arg : args)
                {
                    debugString.append(" ");
                    debugString.append(arg);
                }
            }

            debugString.append(" ]");

            Gdx.app.debug(debugTag, debugString.toString());

            writeToFile(debugString.toString());
        }
    }

    /**
     * Write an error string to logcat or console.
     *
     * @param string The string to write.
     */
    public static void err(String string)
    {
        if (Gdx.app.getLogLevel() != Application.LOG_ERROR)
        {
            StringBuilder sb = new StringBuilder();

            sb.append("     [ ");
            sb.append(string);
            sb.append(" ]");

            Gdx.app.debug(errorTag, sb.toString());
        }
    }

    /**
     * Write the line number of the Trace call
     * to logcat or console.
     */
    public static void __LINE__(Object... args)
    {
        StringBuilder sb = new StringBuilder("Line: ");

        sb.append(new Throwable().getStackTrace()[1].getLineNumber());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the filename and line number of the Trace call
     * to logcat or console.
     */
    public static void __FILE_LINE__()
    {
        dbg
            (
                "Func: %s%s%d",
                new Throwable().getStackTrace()[1].getFileName(),
                "::",
                new Throwable().getStackTrace()[1].getLineNumber()
            );
    }

    /**
     * Write the current filename to logcat or console.
     */
    public static void __FILE__()
    {
        dbg("File: %s", new Throwable().getStackTrace()[1].getFileName());
    }

    /**
     * Write the current method name to logcat or console.
     */
    public static void __FUNC__(Object... args)
    {
        StringBuilder sb = new StringBuilder("Func: ");

        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the current method name to logcat or console,
     * with a dividing line beforehand.
     */
    public static void __FUNC_WithDivider(Object... args)
    {
        dbg("--------------------------------------------------");

        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the current filename and method name to logcat or console.
     */
    public static void __FILE_FUNC(Object... args)
    {
        StringBuilder sb = new StringBuilder("Func: ");

        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Write the current filename and method name to logcat or console,
     * with a dividing line beforehand.
     */
    public static void __FILE_FUNC_WithDivider(Object... args)
    {
        dbg("--------------------------------------------------");

        StringBuilder sb = new StringBuilder("Func: ");

        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Writes the current file name to logcat or console.
     * Also writes the current method name plus the current line number.
     */
    public static void __FILE_FUNC_LINE(Object... args)
    {
        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getLineNumber());

        if (args.length > 0)
        {
            for (Object object : args)
            {
                sb.append(" ").append(object);
            }
        }

        dbg(sb.toString());
    }

    /**
     * Writes a divider line to logcat or console.
     */
    public static void divider()
    {
        divider(50);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _length The length of the divider.
     */
    public static void divider(int _length)
    {
        divider('-', _length);
    }

    /**
     * Writes a divider line to logcat or console.
     *
     * @param _length The length of the divider.
     * @param _char   The character to use for the divider.
     */
    public static void divider(char _char, int _length)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < _length; i++)
        {
            sb.append(_char);
        }

        dbg(sb.toString());
    }

    /**
     * Writes two divider lines, with a message between them, to logcat or console.
     *
     * @param string The message to write.
     */
    public static void megaDivider(String string)
    {
        divider(50);
        divider(50);
        dbg(string);
        divider(50);
        divider(50);
    }

    /**
     * Writes a 'Finished' message to output window.
     */
    public static void finishedMessage()
    {
        StringBuilder sb = new StringBuilder("Func: ");
        sb.append(new Throwable().getStackTrace()[1].getFileName());
        sb.append("::");
        sb.append(new Throwable().getStackTrace()[1].getMethodName());
        sb.append("::...Finished...");

        dbg(sb.toString());
    }

    /**
     * Enables or Disables write to file.
     */
    public static void enableWriteToFile(boolean enable)
    {
        isWriteFileActive = enable;
    }

    /**
     * Opens a physical file for writing copies of debug messages to.
     * Note: Only messages output via Trace#dbg() are written to file.
     * Note: An option to specify target directory is to be added.
     *
     * @param fileName       - The filename. This should be filename only,
     *                       - and the file will be created in the working directory.
     * @param deleteExisting - True to delete existing copies of the file.
     *                       - False to append to existing file.
     */
    public static void openDebugFile(String fileName, boolean deleteExisting)
    {
        boolean isSuccess = true;

        if ((Gdx.app.getType() == Application.ApplicationType.Desktop)
            && isWriteFileActive)
        {
            logFile = new File(fileName);

            if (logFile.exists())
            {
                if (deleteExisting)
                {
                    if (logFile.delete())
                    {
                        Gdx.app.debug(debugTag, "Existing logfile deleted.");
                    }
                }
            }

            if (!logFile.exists())
            {
                try
                {
                    if (isSuccess = logFile.createNewFile())
                    {
                        Gdx.app.debug(debugTag, "LOGGER: Logfile created: " + logFile.getName());
                    }
                }
                catch (IOException ioe)
                {
                    err("Cannot create log file: " + logFile);
                    Stats.incMeter(Meters._IO_EXCEPTION.get());
                    isSuccess = false;
                }
            }

            if (isSuccess)
            {
                GregorianCalendar c = new GregorianCalendar();

                writeToFile("-----------------------------------------------------------");
                writeToFile("Filename: " + logFile.toString());
                writeToFile("Created: " + c.getTime().toString());
                writeToFile("-----------------------------------------------------------");
            }
        }
    }

    /**
     * Writes text to the logFile, if it exists.
     *
     * @param text String holding the text to write.
     */
    public static void writeToFile(String text)
    {
        if (isWriteFileActive && (logFile != null) && logFile.exists())
        {
            try
            {
                //
                // BufferedWriter for performance.
                // Pass TRUE to set append to file flag.
                BufferedWriter bw = new BufferedWriter(new FileWriter(logFile, true));

                bw.append(text);
                bw.write('\r');
                bw.close();
            }
            catch (IOException ioe)
            {
                Stats.incMeter(Meters._IO_EXCEPTION.get());
            }
        }
    }
}
