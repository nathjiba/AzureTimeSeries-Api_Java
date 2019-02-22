# azuretimeseries-api
This application provides REST query APIs to query Time Series Insights environment in Microsoft Azure.

## Getting Started

* **Register a new application**: Go to Azure Portal - Azure Active Directory - App registrations - New application registration to register the application in Azure Active Directory.  `Application ID` is `client-id` in `application.properties`.
* **Grant permissions to the application**: After application registration succeeded, go to API ACCESS - Required permissions - DELEGATED PERMISSIONS, tick `Access the directory as the signed-in user` and `Sign in and read user profile`. Click `Grant Permissions` (Note: you will need administrator privilege to grant permission).
* **Create a client secret key for the application**: Go to API ACCESS - Keys to create a secret key (`client-secret`).

Use Application ID, Client Secret in Constants.java file as per your environment

### Prerequisites

1. Java 8 or newer
2. Maven
3. Your preferred IDE
Eclipse with the m2e plugin.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## API Reference - Azure 

* https://docs.microsoft.com/en-us/rest/api/time-series-insights/ga-query-api
