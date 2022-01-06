package io.automation.service;

import io.automation.Environment;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public final class RequestSpec {

    private static final Environment ENV = Environment.getInstance();

    private RequestSpec() {
    }

    private static RequestSpecification getRequestWithLogger(final RequestSpecification requestSpecification) {
        return requestSpecification
                .log().method()
                .log().uri()
                .log().params()
                .log().body();
    }

    public static RequestSpecification getRequestSpec() {
        return getRequestWithLogger(new RequestSpecBuilder()
                .setBaseUri(ENV.getValue("baseUri")).build());
    }

}
