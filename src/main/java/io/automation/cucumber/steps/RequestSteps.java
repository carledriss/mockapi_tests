package io.automation.cucumber.steps;

import java.util.Map;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;

import io.automation.ScenarioContext;
import io.automation.cucumber.hooks.CommonHook;
import io.automation.service.DynamicIdHelper;
import io.automation.service.RequestManager;

import static org.testng.Assert.assertEquals;

public class RequestSteps {

    private static final String KEY_LAST_ENDPOINT = "LAST_ENDPOINT";
    private static final String KEY_LAST_RESPONSE = "LAST_RESPONSE";

    private Response response;
    private final ScenarioContext context;

    public RequestSteps(final ScenarioContext context) {
        this.context = context;
    }

    @ParameterType("POST|PUT|PATCH|GET|DELETE")
    public Method method(final String method) {
        return Method.valueOf(method.toUpperCase());
    }

    @Given("I send a {method} request to {string} with json body")
    public void iSendARequestToWithJsonBody(final Method method, final String endpoint,
                                            final String jsonBody) {
        RequestSpecification requestSpecification = (RequestSpecification) context.get(CommonHook.CONTEXT_REQUEST_SPEC);
        String builtEndpoint = DynamicIdHelper.buildEndpoint(context, endpoint);
        response = RequestManager.doRequest(method, requestSpecification, builtEndpoint,
                DynamicIdHelper.replaceIds(context, jsonBody));
        context.set(KEY_LAST_ENDPOINT, builtEndpoint);
        context.set(KEY_LAST_RESPONSE, response);
    }

    @When("I send a {method} request to {string}")
    public void iSendARequestTo(final Method method, final String endpoint) {
        RequestSpecification requestSpecification = (RequestSpecification) context.get(CommonHook.CONTEXT_REQUEST_SPEC);
        String builtEndpoint = DynamicIdHelper.buildEndpoint(context, endpoint);
        response = RequestManager.doRequest(method, requestSpecification, builtEndpoint);
        context.set(KEY_LAST_ENDPOINT, builtEndpoint);
        context.set(KEY_LAST_RESPONSE, response);
    }

    @Then("I validate the response has status code {int}")
    public void iValidateTheResponseHasStatusCode(int expectedStatusCode) {
        int statusCode = response.getStatusCode();
        assertEquals(statusCode, expectedStatusCode);
    }

    @And("I validate the response contains {string} equals {string}")
    public void iValidateTheResponseContainsEquals(final String attribute, final String expectedValue) {
        String actualProjectName = response.jsonPath().getString(attribute);
        assertEquals(actualProjectName, expectedValue);
    }

    @And("I validate the response array contains {string} equals {string}")
    public void iValidateTheResponseArrayContains(final String attribute, final String expectedValue) {
        String replaceId = DynamicIdHelper.replaceIds(context, expectedValue);
        response.then()
                .assertThat()
                .body(attribute, CoreMatchers.hasItem(replaceId));
    }

    @And("I save the response as {string}")
    public void iSaveTheResponseAs(final String key) {
        context.set(key, response);
    }

    @And("I save the request endpoint for deleting")
    public void iSaveTheRequestEndpointForDeleting() {
        String lastEndpoint = (String) context.get(KEY_LAST_ENDPOINT);
        String lastResponseId = ((Response) context.get(KEY_LAST_RESPONSE)).jsonPath().getString("id");
        String finalEndpoint = String.format("%s/%s", lastEndpoint, lastResponseId);
        context.addEndpoint(finalEndpoint);
    }

    @And("I validate the response contains:")
    public void iValidateTheResponseContains(final Map<String, String> validationMap) {
        Map<String, Object> responseMap = response.jsonPath().getMap(".");
        for (Map.Entry<String, String> data: validationMap.entrySet()) {
            if (responseMap.containsKey(data.getKey())) {
                assertEquals(String.valueOf(responseMap.get(data.getKey())), data.getValue(), data.getKey() + " doesn't match:");
            }
        }
    }
}
