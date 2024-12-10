package Stepsdefinition;

import Utilities.ExtentTestManager;
import Utilities.HttpUtil;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.fail;

public class ApiServiceSteps {
    private String apiUrl;
    private String createdBrandId;
    private int initialBrandCount;

    @Given("API endpoint is available at {string}")
    public void theApiEndpointIsAvailableAt(String url) {
        this.apiUrl = url;
        ExtentTestManager.getTest().pass("API endpoint set to: " + url);
    }

    @When("I create a new brand with name {string} and slug {string}")
    public void createNewBrandWithNameAndSlug(String name, String slug) throws IOException {
        // Step 1: Retrieve initial list of brands to get initial count
        HttpClient client = HttpClients.createDefault();
        HttpResponse getResponse = HttpUtil.sendGetRequest(apiUrl);
        Assert.assertEquals(200, getResponse.getStatusLine().getStatusCode());

        String getJson = HttpUtil.getResponseBody(getResponse);
        List<Map<String, Object>> initialBrands = new ObjectMapper().readValue(getJson, new TypeReference<>() {});
        initialBrandCount = initialBrands.size();

        // Step 2: Create the new brand with POST request
        String jsonBody = new ObjectMapper().writeValueAsString(Map.of("name", name, "slug", slug));
        HttpPost postRequest = HttpUtil.createPostRequest(apiUrl, jsonBody);

        // Execute the POST request and handle response
        try (CloseableHttpResponse postResponse = (CloseableHttpResponse) client.execute(postRequest)) {
            int statusCode = postResponse.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(postResponse.getEntity());

            if (statusCode == 422) {
                // Check if the response body contains the expected slug validation error
                if (responseBody.contains("The slug field must only contain letters, numbers, dashes, and underscores")) {
                    ExtentTestManager.getTest().pass("Validation error as expected: " + responseBody);
                    return;
                } else {
                    // If the 422 status is due to another reason, log it as a failure
                    ExtentTestManager.getTest().pass("Brand with the same slug already exists: " + responseBody);
                    fail("Brand with the same slug already exists: " + responseBody);
                }
            }
            // Assert that the status code is 201 for successful creation
            Assert.assertEquals(201, statusCode);

            // Parse the JSON response and extract the ID
            Map<String, Object> createdBrand = new ObjectMapper().readValue(responseBody, new TypeReference<>() {});
            createdBrandId = createdBrand.get("id").toString();

            // Log the response in ExtentTest
            ExtentTestManager.getTest().pass("Request to create new brand with name: " + name + " and slug: " + slug + ".<br> Response body: " + responseBody);
        }
    }

    @Then("I can see the new brand name {string} and slug {string} in the list and the total count should increase by 1")
    public void verifyNewBrandAddedToList(String brandName, String brandSlug) throws IOException {
        // Retrieve the updated list of brands using the HttpUtil method
        HttpResponse getResponse = HttpUtil.sendGetRequest(apiUrl);
        Assert.assertEquals(200, getResponse.getStatusLine().getStatusCode());

        // Parse JSON response to get updated brand list
        String getJson = EntityUtils.toString(getResponse.getEntity());
        List<Map<String, Object>> updatedBrands = new ObjectMapper().readValue(getJson, new TypeReference<>() {});

        // Check if the count increased by 1
        int expectedCount = initialBrandCount + 1;
        int actualCount = updatedBrands.size();
        Assert.assertEquals(expectedCount, actualCount);

        if (actualCount != expectedCount) {
            ExtentTestManager.getTest().fail("Expected brand count to be: " + expectedCount + ", but found: " + actualCount);
            fail("Brand count did not increase as expected.");
        }

        // Validate that the new brand exists in the updated list
        boolean brandExists = updatedBrands.stream()
                .anyMatch(brand -> brand.get("id").equals(createdBrandId)
                        && brand.get("name").equals(brandName)
                        && brand.get("slug").equals(brandSlug));
        Assert.assertTrue("Newly created brand not found in list.", brandExists);

        // Logging success message
        ExtentTestManager.getTest().pass("Brand with <b>name:</b> " + brandName + " , <b>slug:</b> " + brandSlug + " and <b>ID:</b> " + createdBrandId + " is successfully created");
    }


    @Then("Change name {string} and slug {string} to {string} and {string}")
    public void changeBrandNameAndSlugNameTo(String existingName, String existingSlug, String newName, String newSlug) throws Exception {
        HttpResponse getResponse = HttpUtil.sendGetRequest(apiUrl);
        Assert.assertEquals(200, getResponse.getStatusLine().getStatusCode());

        // Step 2: Parse the JSON response to get the list of brands
        String getJson = EntityUtils.toString(getResponse.getEntity());
        List<Map<String, Object>> brandList = new ObjectMapper().readValue(getJson, new TypeReference<>() {});

        String foundObjectId = null;
        boolean duplicateSlugExists = false;
        boolean oldBrandExists = false;

        // Step 3: Check for existing brand with the same slug
        for (Map<String, Object> brand : brandList) {
            String currentSlug = brand.get("slug").toString();

            // Check if another brand already has the new slug (to prevent duplicate slugs)
            if (currentSlug.equals(newSlug)) {
                duplicateSlugExists = true;
                break; // No need to check further; we found a duplicate slug
            }

            // Check if the brand matches the existing name and slug
            if (brand.get("name").toString().equals(existingName) && currentSlug.equals(existingSlug)) {
                foundObjectId = brand.get("id").toString();
                oldBrandExists = true;
            }
        }

        // Step 4: Handle scenarios for duplicate slug
        if (duplicateSlugExists) {
            ExtentTestManager.getTest().pass("Duplicate entry: Another brand already uses slug '" + newSlug + "'.");
            ExtentTestManager.getTest().info("It is not possible to update the brand with an existing slug.");
            return;
        }

        if (!oldBrandExists) {
            ExtentTestManager.getTest().fail("Brand with name: " + existingName + " and slug: " + existingSlug + " was not found.");
            return;
        }

        ExtentTestManager.getTest().info("Found ID for brand with name: " + existingName + " and slug: " + existingSlug + " is " + foundObjectId);

        // Step 5: Send a PUT request to update the brand with the new name and slug
        String updateUrl = apiUrl + "/" + foundObjectId;
        String jsonBody = new ObjectMapper().writeValueAsString(Map.of("name", newName, "slug", newSlug));
        HttpResponse putResponse = HttpUtil.sendPutRequest(updateUrl, jsonBody);

        // Handle the response from the PUT request
        int statusCode = putResponse.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(putResponse.getEntity());

        if (statusCode == 422) {
            ExtentTestManager.getTest().pass("Failed to update the brand due to validation error: " + responseBody);
            return;
        } else if (statusCode != 200) {
            throw new Exception("Failed to update the brand. HTTP Status code: " + statusCode);
        }

        ExtentTestManager.getTest().pass("Brand updated: " + existingName + " successfully to name: " + newName + " and slug: " + newSlug);

        // Step 6: Send a GET request to fetch the updated brand and validate the fields
        HttpResponse getUpdatedResponse = HttpUtil.sendGetRequest(updateUrl);
        String updatedBrandResponse = HttpUtil.getResponseBody(getUpdatedResponse);

        JSONObject updatedBrandJson = new JSONObject(updatedBrandResponse);

        // Validate the updated fields
        Assert.assertEquals(newName, updatedBrandJson.getString("name"));
        Assert.assertEquals(newSlug, updatedBrandJson.getString("slug"));

        // Log the final verification
        ExtentTestManager.getTest().pass("Verified updated brand with name: " + newName + " and slug: " + newSlug);
    }
}
