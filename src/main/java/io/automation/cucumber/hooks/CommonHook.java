package io.automation.cucumber.hooks;

import java.util.List;

import io.automation.service.RequestSpec;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.specification.RequestSpecification;

import io.automation.ScenarioContext;
import io.automation.service.RequestManager;

public class CommonHook {

    public static final String CONTEXT_REQUEST_SPEC = "REQUEST_SPEC";

    private final ScenarioContext context;

    public CommonHook(final ScenarioContext context) {
        this.context = context;
    }

    @Before
    public void beforeScenario() {
        RequestSpecification requestSpecification = RequestSpec.getRequestSpec();
        context.set(CONTEXT_REQUEST_SPEC, requestSpecification);
    }

    @After(value = "@cleanData")
    public void afterScenario() {
        RequestSpecification requestSpec = (RequestSpecification) context.get(CONTEXT_REQUEST_SPEC);
        List<String> endpoints = context.getEndpoints();
        for (String endpoint : endpoints) {
            RequestManager.delete(requestSpec, endpoint);
        }
    }

}
