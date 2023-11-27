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