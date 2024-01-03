# camel-salesforce-example
A simple example for how to integrate Salesforce using Apache Camel.

# Prerequisites
(Refer to the [Getting Started Documentation](https://camel.apache.org/camel-core/getting-started/index.html) for more details)

- Install Apache Maven and a JDK. 
- Setup environment and path variables as per the official documentation.

# Salesforce Setup
1. Use an existing Salesforce Developer account or create a new one [here](https://developer.salesforce.com/signup).

2. Create a new connected app in Salesforce. 
    - Enable OAuth Settings
    - Add the following scopes: Manage user data via APIs (api), Perform requests on your behalf at any time (refresh_token, offline_access)
    - Add the following callback URL: http://localhost:8080/callback

3. View the connected app and do the following
    - Click "Manage Consumer Details" and note down the Consumer Key and Consumer Secret.
    - Click "Manage" and "Edit Policies" and set OAuth policies to "All users may self-authorize", and "Relax IP restrictions".

4. In setup go to "OAuth and OpenID Connect Settings" and enable Allow OAuth Username-Password Flow.

5. Click on your profile and note down your username, password and security token.

6. Go to "My Domain" in setup and note down your My Domain URL.

*Note that these security settings are not recommended for production environments. They are used here for simplicity.*


# Camel Setup
1. Create a new maven project based on a basic camel maven archetype:
```bash
mvn archetype:generate -B -DarchetypeGroupId=org.apache.camel.archetypes -DarchetypeArtifactId=camel-archetype-java -DarchetypeVersion=LATEST -Dpackage=com.example -DgroupId=com.example -DartifactId=camel-salesforce-example -Dversion=1.0.0-SNAPSHOT
```

2. Test the project by running the following two commands. You should see "INFO UK message" and "INFO Other message" in the console :
```bash
mvn clean package
```
```bash
mvn camel:run
```

3. Add the salesforce dependency to the pom.xml file:
```xml
<dependency>
    <groupId>org.apache.camel</groupId>
    <artifactId>camel-salesforce</artifactId>
</dependency>
```

4. Create a new class called SalesforceRouteBuilder.java in the src/main/java/com/example directory. This class will contain the camel route that will be used to integrate with Salesforce. The code for this class is as follows:
```java
package com.example;

import org.apache.camel.builder.RouteBuilder;


public class SalesforceRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // Create and subscribe to a new topic
        // Topic triggers on Contact creation and querries ID and Name
        // ID and Name are logged to the console
        from("salesforce:subscribe:CamelTestTopic?notifyForFields=ALL&notifyForOperationCreate=true&sObjectName=Contact&updateTopic=true&sObjectQuery=SELECT Id, Name FROM Contact")
            .log("New Contact created - ${body}");
    }
}
```

5. Add the SalesforceRoute inside the MainApp.java class by adding the following line to the main function. You may remove the existing example route if you want.:
```java
main.configure().addRoutesBuilder(new SalesforceRoute());
```

6. Create a application.properties file in the src/main/resources directory. This file will contain the Salesforce credentials you noted down previously and other configuration properties. The contents of this file are as follows:
```properties
camel.component.salesforce.clientId=<Consumer Key>
camel.component.salesforce.clientSecret=<Consumer Secret>
camel.component.salesforce.userName=<Username>
camel.component.salesforce.password=<Password><Security Token>
camel.component.salesforce.loginUrl=<My Domain URL>
camel.component.salesforce.authenticationType=USERNAME_PASSWORD
camel.component.salesforce.rawPayload=true
```

7. Prepare the project by running the following command.
```bash
mvn clean package
```

# Testing the Integration
1. Run the project by running the following command.
```bash
mvn camel:run
```

2. You should not receive any errors in the console. 

3. Login to Salesforce and create a new Contact. You should see the ID and Name of the new Contact in the console.


