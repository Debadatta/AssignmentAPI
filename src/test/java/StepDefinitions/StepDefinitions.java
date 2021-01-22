package StepDefinitions;



import io.restassured.RestAssured.*;
import io.restassured.http.Method;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import java.util.List;
import java.util.Map;
 
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.deps.com.google.gson.Gson;
import gherkin.deps.com.google.gson.JsonObject;
import gherkin.deps.com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import org.json.*;
import org.junit.Assert;


public class StepDefinitions {
	
	private Response response;
	private String json;
	private RequestSpecification request;
    private String ENDPOINT_URL ;
    private String id1;
    private String id2;
    List < String > allStationIDs;
 
	@Given("I get the API url")
	public void i_get_api_url(){
		ENDPOINT_URL = "http://api.openweathermap.org/data/3.0/stations";
		
	}
 
	@When("I register station without API key")
	public void i_register_station_without_api_key(){
		JSONObject requestParams = new JSONObject();
		requestParams.put("external_id", "DEMO_TEST001"); 
		requestParams.put("name", "Interview Station <Random Number>");
		RequestSpecification request = RestAssured.given();
		requestParams.put("latitude", 33.33);
		requestParams.put("longitude", -111.43);
		requestParams.put("altitude",  444);
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		response = request.post(ENDPOINT_URL);
		System.out.println("response: " + response.getStatusCode());
	}
 
	
	

	//@Then("^I should get Http Response code as \"(.*?)\"$")
	@Then("^I should get Http Response code as (\\d+)$")
	public void i_should_get_Http_Response_code_as(int statusCode) throws Throwable {
		int statusCodeActual = response.getStatusCode();
		Assert.assertEquals(statusCodeActual, statusCode);

	}

	@Then("^I should get response message as \"(.*?)\"$")
	public void i_should_get_response_message_as(String message) throws Throwable {
		JsonParser parser = new JsonParser(); 
		JsonObject jsonObject = parser.parse(response.body().asString()).getAsJsonObject();
		System.out.println(jsonObject.get("message").toString());
		Assert.assertEquals("Actual message recieved", jsonObject.get("message").toString(), message);
	}
	
	@When("^I register station with details$")
	public void i_register_station_with_details() {
		request = RestAssured.given();
		JSONObject requestParams = new JSONObject();		
			
		requestParams.put("external_id", "DEMO_TEST001"); 
		requestParams.put("name", "Interview Station <Random Number>");
		 
		requestParams.put("latitude", 33.33);
		requestParams.put("longitude", -111.43);
		requestParams.put("altitude",  444);		
		
		JSONObject requestParams1 = new JSONObject();
		requestParams1.put("external_id", "Interview1"); 
		requestParams1.put("name", "Interview Station <Random Number>");
		
		requestParams1.put("latitude", 33.44);
		requestParams1.put("longitude", -12.44);
		requestParams1.put("altitude",  444);

		
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		response = request.request(Method.POST, ENDPOINT_URL+"?APPID=94ee89226ce499d45a755090f02e3e7b");
		JsonParser parser = new JsonParser(); 
		JsonObject jsonObject = parser.parse(response.body().asString()).getAsJsonObject();
		System.out.println(jsonObject.toString());
		id1 = jsonObject.get("ID").toString();
		
		request.body(requestParams1.toString());
		response = request.request(Method.POST, ENDPOINT_URL+"?APPID=94ee89226ce499d45a755090f02e3e7b");
		JsonObject jsonObject1 = parser.parse(response.body().asString()).getAsJsonObject();
		System.out.println(jsonObject1.get("ID").toString());
		id2 = jsonObject1.get("ID").toString();
		System.out.println("response: " + response.body().asString());
	}
	
	@When("^I get the stations with ids$")
	public void i_get_the_stations_with_ids() throws Throwable {
		request = RestAssured.given().queryParam("APPID", "94ee89226ce499d45a755090f02e3e7b");
		request.header("Content-Type", "application/json");
		response = request.request(Method.GET, ENDPOINT_URL+"/"+ id1);	
		JsonParser parser = new JsonParser(); 
		JsonObject jsonObject = parser.parse(response.body().asString()).getAsJsonObject();
		Assert.assertEquals(jsonObject.get("external_id"), "DEMO_TEST001");
		
			
		request = RestAssured.given().queryParam("APPID", "94ee89226ce499d45a755090f02e3e7b");
		request.header("Content-Type", "application/json");
		response = request.request(Method.GET, ENDPOINT_URL+"/"+ id2);	
		JsonParser parser1 = new JsonParser(); 
		JsonObject jsonObject1 = parser1.parse(response.body().asString()).getAsJsonObject();
		Assert.assertEquals(jsonObject1.get("external_id"), "Interview1");
		
	}

	@Then("^I should get the valid details$")
	public void i_should_get_the_valid_details() throws Throwable {
		if(response.prettyPrint().contains("DEMO_TEST001")) {
			System.out.println("DEMO_TEST001 record present");
		} else {
			System.out.println("DEMO_TEST001 record not present");
		}
		
		if(response.prettyPrint().contains("Interview1")) {
			System.out.println("Interview1 record present");
		} else {
			System.out.println("Interview1 record not present");
		}
	}

	@When("^I delete both the stations$")
	public void i_delete_both_the_stations() throws Throwable {
		JSONObject requestParams = new JSONObject();
		requestParams.put("appid", "94ee89226ce499d45a755090f02e3e7b");
		requestParams.put("id", allStationIDs.get(0));
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		response = request.delete(ENDPOINT_URL);
		
		
		JSONObject requestParams1 = new JSONObject();
		requestParams1.put("appid", "94ee89226ce499d45a755090f02e3e7b");
		requestParams1.put("id", allStationIDs.get(1));
		request.header("Content-Type", "application/json");
		request.body(requestParams1.toString());
		response = request.delete(ENDPOINT_URL);
	}

	@When("^I delete the station again$")
	public void i_delete_the_station_again() throws Throwable {
		JSONObject requestParams = new JSONObject();
		requestParams.put("appid", "94ee89226ce499d45a755090f02e3e7b");
		requestParams.put("id", allStationIDs.get(0));
		request.header("Content-Type", "application/json");
		request.body(requestParams.toString());
		response = request.delete(ENDPOINT_URL);
	}

}
