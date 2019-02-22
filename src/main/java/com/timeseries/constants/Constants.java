package com.timeseries.constants;

/**
 * Timeseries constants
 *
 */
public class Constants {

	public final static String AZURE_TIMESERIES_ENDPOINT = "https://api.timeseries.azure.com/";

	public final static String AZURE_TIMESERIES_APIVERSION = "?api-version=2016-12-12";

	public final static String AZURE_TIMESERIES_ENVIRONMENTS = "environments";

	public final static String AZURE_TIMESERIES_AVAILABILITY = "availability";

	public final static String AZURE_TIMESERIES_METADATA = "metadata";

	public final static String AZURE_TIMESERIES_EVENTS = "events";

	public static final String HTTPS = "https://";

	/**
	 * # has to be replaced with proper data access fqdn value of your environment
	 */
	public static final String DATA_ACCESS_FQDN_TSI = "#.env.timeseries.azure.com";

	public static String QUERY_METADATA_FQDN = HTTPS + DATA_ACCESS_FQDN_TSI + "/" + AZURE_TIMESERIES_ENVIRONMENTS
			+ AZURE_TIMESERIES_APIVERSION;

	public static String QUERY_EVENTS_FQDN = HTTPS + DATA_ACCESS_FQDN_TSI + "/" + AZURE_TIMESERIES_EVENTS
			+ AZURE_TIMESERIES_APIVERSION;

	public static String QUERY_ENV_FQDN = AZURE_TIMESERIES_ENDPOINT + AZURE_TIMESERIES_ENVIRONMENTS
			+ AZURE_TIMESERIES_APIVERSION;

	// SET the application ID of application registered in your Azure Active
	// Directory
	public static String ApplicationClientId = "DummyClientId";

	// SET the application key of the application registered in your Azure
	// Active Directory
	public static String ApplicationClientSecret = "ClientSecret";

	// SET the Azure Active Directory tenant. # has to be replaced with proper
	// tenant value
	public static String Tenant = "#.onmicrosoft.com";

	// Could be this too. Test this one also https://login.windows.net/
	public static String AUTHLOGIN_WINDOWS = "https://login.microsoftonline.com/";
}
