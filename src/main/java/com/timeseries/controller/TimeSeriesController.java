package com.timeseries.controller;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.timeseries.auth.AuthHelper;
import com.timeseries.auth.HttpClientHelper;
import com.timeseries.constants.Constants;
import com.timeseries.model.Environment;
import com.timeseries.model.Environments;
import com.timeseries.model.ErrorMessage;
import com.timeseries.utils.JsonToJavaConverter;

/**
 * Timeseries Controller for all rest endpoints
 *
 */
@RestController
public class TimeSeriesController {

    /**
     * Obtain list of environments and get environment FQDN for the environment
     * of interest.
     * 
     * @return
     */
    @GetMapping("/environments")
    @ExceptionHandler(ErrorMessage.class)
    public ResponseEntity<Object> getEnvs() {

        Environments environments = null;
        HttpURLConnection conn = null;
        try {
            conn = AuthHelper.getHttpConnection(Constants.QUERY_ENV_FQDN, HttpMethod.GET);

            String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
            environments = (Environments) JsonToJavaConverter.convertJsonToJava(goodRespStr, new Environments());
            if (environments.getEnvironments().isEmpty()) {
                // List of user environments is empty, fallback to sample
                // environment.
                String environmentFqdn = "10000000-0000-0000-0000-100000000108.env.timeseries.azure.com";
                Environment environment = new Environment();
                environment.setEnvironmentFqdn(environmentFqdn);
                List<Environment> envList = new ArrayList<>();
                envList.add(environment);
                environments.setEnvironments(envList);
            }
            return new ResponseEntity<Object>(environments, HttpStatus.OK);
        } catch (Exception e) {
            throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @GetMapping("/getAccessToken")
    @ExceptionHandler(ErrorMessage.class)
    public ResponseEntity<Object> getAccessToken() {
        try {
            String accessToken = AuthHelper.getAccessToken();
            return new ResponseEntity<Object>(accessToken, HttpStatus.OK);
        } catch (Exception e) {
            throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @GetMapping("/availability")
    @ExceptionHandler(ErrorMessage.class)
    public ResponseEntity<Object> getAvailability() {

        JSONObject response = null;
        HttpURLConnection conn = null;
        try {
            conn = AuthHelper.getHttpConnection(Constants.QUERY_ENV_FQDN, HttpMethod.GET);
            String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

            Environments environments = (Environments) JsonToJavaConverter.convertJsonToJava(goodRespStr,
                    new Environments());

            if (environments.getEnvironments().isEmpty()) {
                // List of user environments is empty, fallback to sample
                // environment.
                String environmentFqdn = "10000000-0000-0000-0000-100000000108.env.timeseries.azure.com";
                Environment environment = new Environment();
                environment.setEnvironmentFqdn(environmentFqdn);
                List<Environment> envList = new ArrayList<>();
                envList.add(environment);
                environments.setEnvironments(envList);
            }

            String envFQDN = environments.getEnvironments().get(0).getEnvironmentFqdn();
            String availabilityURL = Constants.HTTPS + envFQDN + "/" + Constants.AZURE_TIMESERIES_AVAILABILITY
                    + Constants.AZURE_TIMESERIES_APIVERSION;
            conn = AuthHelper.getHttpConnection(availabilityURL, HttpMethod.GET);

            goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
            int responseCode = conn.getResponseCode();
            response = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);
            return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
        } catch (Exception e) {
            throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @PostMapping("/metadata/{startDateInMillis}/{endDateInMillis}")
    @ExceptionHandler(ErrorMessage.class)
    public ResponseEntity<Object> getMetaData(@PathVariable long startDateInMillis, long endDateInMillis) {

        HttpURLConnection conn = null;
        ErrorMessage errorMessage = new ErrorMessage();
        try {
            conn = AuthHelper.getHttpConnection(Constants.QUERY_METADATA_FQDN, HttpMethod.POST);
            String payload = "{searchSpan: { from:" + new Date(startDateInMillis).toInstant().toString() + ", to: "
                    + new Date(endDateInMillis).toInstant().toString() + " }}";

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            if (conn.getResponseCode() != HttpStatus.OK.value()) {
                throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                        HttpClientHelper.getResponseStringFromConn(conn, false));
            } else {

                String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
                int responseCode = conn.getResponseCode();
                JSONObject metaDataObj = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);

                return new ResponseEntity<Object>(metaDataObj, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    @GetMapping("/events/{startDateInMillis}/{endDateInMillis}")
    @ExceptionHandler(ErrorMessage.class)
    public ResponseEntity<Object> getEvents(@PathVariable String startDateInMillis,
            @PathVariable String endDateInMillis) {

        HttpURLConnection conn = null;
        ErrorMessage errorMessage = new ErrorMessage();
        try {
            conn = AuthHelper.getHttpConnection(Constants.QUERY_EVENTS_FQDN, HttpMethod.POST);
            String payload = "{\"searchSpan\": {\"from\": {\"dateTime\":\""
                    + new Date(Long.valueOf(startDateInMillis)).toInstant().toString() + "\"},\"to\": {\"dateTime\":\""
                    + new Date(Long.valueOf(endDateInMillis)).toInstant().toString() + "\"}}}";
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            if (conn.getResponseCode() != HttpStatus.OK.value()) {
                throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                        HttpClientHelper.getResponseStringFromConn(conn, false));
            } else {
                String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
                int responseCode = conn.getResponseCode();
                JSONObject eventsObj = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);

                return new ResponseEntity<Object>(eventsObj, HttpStatus.OK);
            }
        } catch (Exception e) {
            throw new ErrorMessage(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
