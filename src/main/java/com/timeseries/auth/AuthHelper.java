package com.timeseries.auth;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.naming.ServiceUnavailableException;

import org.springframework.http.HttpMethod;

import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.timeseries.constants.Constants;

public final class AuthHelper {

    private static String accessToken = null;

    private static long expiresIn;

    private AuthHelper() {
    }

    /**
     * Get access token
     * @return access token
     * @throws MalformedURLException
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws ServiceUnavailableException
     */
    public static String getAccessToken()
            throws MalformedURLException, InterruptedException, ExecutionException, ServiceUnavailableException {

        ExecutorService service = Executors.newFixedThreadPool(1);
        String authority = Constants.AUTHLOGIN_WINDOWS + Constants.Tenant;
        ClientCredential clientCredential = new ClientCredential(Constants.ApplicationClientId,
                Constants.ApplicationClientSecret);
        AuthenticationContext authContext = new AuthenticationContext(authority, false, service);
        Future<AuthenticationResult> future = authContext.acquireToken(Constants.AZURE_TIMESERIES_ENDPOINT,
                clientCredential, null);
        AuthenticationResult authResult = future.get();
        service.shutdown();

        if (authResult == null) {
            throw new ServiceUnavailableException("authentication result was null");
        }
        accessToken = authResult.getAccessToken();
        expiresIn = authResult.getExpiresOnDate().getTime();
        return accessToken;
    }

    /**
     * Check if token valid
     * 
     * @return true if token valid else false
     */
    private static boolean isTokenValid() {
        boolean result = false;
        Date now = new Date();
        long currentTime = now.getTime();

        if (currentTime < expiresIn) {
            result = true;
        }

        return result;
    }

    /**
     * Get Http URL Connection object
     * @param urlRequest
     * @param httpMethod
     * @return httpUrlConnection
     * @throws IOException
     * @throws ServiceUnavailableException
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static HttpURLConnection getHttpConnection(String urlRequest, HttpMethod httpMethod)
            throws IOException, ServiceUnavailableException, InterruptedException, ExecutionException {
        URL url = new URL(urlRequest);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(httpMethod.name());
        conn.setRequestProperty("Authorization", "Bearer " + AuthHelper.getAccessToken());
        conn.setRequestProperty("Accept", "application/json");
        return conn;
    }
}
