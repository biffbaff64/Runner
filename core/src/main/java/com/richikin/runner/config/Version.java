package com.richikin.runner.config;

import com.richikin.utilslib.logging.Trace;

/**
 * Major Version - 0 == Development Version
 * - 1 == Alpha release
 * - 2 == Beta release
 * - 3 == Master release
 * <p>
 * Minor Version - 0 ==
 * - 1 ==
 * - 2 ==
 * - etc...
 * <p>
 * App Version details
 * ------------------------------------------------------------------
 *
 * @version 0.0.1 Internal       initial issue
 */
public final class Version
{
    public static final int majorVersion = 0;
    public static final int minorVersion = 0;
    public static final int issueNumber  = 3;

    static final String appVersion  = "" + majorVersion + "." + minorVersion + "." + issueNumber;
    static final String projectID   = "Jetman";
    static final String googleAppID = "146820815538";

    //
    // Release Version
    // TODO: 10/12/2020
    static final String clientID = "146820815538-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com";
    static final String sha1     = "00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00";

    //
    // Debug Version
    // TODO: 10/12/2020
    static final String clientID_debug = "146820815538-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com";
    static final String sha1_debug     = "00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00";

    //
    // Google Base64-encoded RSA public key
    // TODO: 10/12/2020
    static final String googleRsaPublicKey = "ABCDefghIJKLmnopQRSTuvwxYZ";

    // ------------------------------------------------------

    public static void appDetails()
    {
        Trace.divider('*', 80);
        Trace.divider(80);

        Trace.dbg(getDisplayVersion());

        Trace.divider(80);
        Trace.divider('*', 80);
    }

    /**
     * Gets the app Version string for displaying on the settings screen
     *
     * @return String holding the version details.
     */
    public static String getDisplayVersion()
    {
        return "Version  " + googleAppID + " : " + appVersion + " : " + projectID;
    }

    /**
     * Gets the app Version string
     *
     * @return String holding the version details.
     */
    public static String getAppVersion()
    {
        return "V." + appVersion;
    }

    public static String getProjectID()
    {
        return projectID;
    }
}
