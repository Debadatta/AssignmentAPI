package StepDefinitions;

import io.restassured.http.Method;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.*;
import org.junit.Assert;


public class StepDefinitions {
	
	private Response response;
	private RequestSpecification request;
    private String ENDPOINT_URL ;
    private String id1;
    private String id2;
    static final String APPID = "94ee89226ce499d45a755090f02e3e7b"; 
 
	@Given("I get the API url")
	public void i_get_api_url(){
		ENDPOINT_URL = "http://api.openweathermap.org/data/3.0/stations";
		request = RestAssured.given();
	}
 
	@When("I register station without API key")
	public void i_register_station_without_api_key(){
		try {
			JSONObject requestParams = new JSONObject();
			requestParams.put("external_id", "DEMO_TEST001"); 
			requestParams.put("name", "Interview Station <Random Number>");
			
			requestParams.put("latitude", 33.33);
			requestParams.put("longitude", -111.43);
			requestParams.put("altitude",  444);
			request.header("Content-Type", "application/json");
			request.body(requestParams.toString());
			response = request.post(ENDPOINT_URL);
			System.out.println("response: " + response.getStatusCode());
		} catch( Exception e ) {
			System.out.println("Stations created successfully");
		}
	}
 
	
	@Then("^I should get Http Response code as (\\d+)$")
	public void i_should_get_Http_Response_code_as(int statusCode) throws Throwable {
		int statusCodeActual = response.getStatusCode();
		Assert.assertEquals(statusCodeActual, statusCode);

	}

	@Then("^I should get response message as \"(.*?)\"$")
	public void i_should_get_response_message_as(String message) throws Throwable {
		JsonPath extractor = response.jsonPath();	
		System.out.println(extractor.get("message").toString());
		Assert.assertEquals("Actual message recieved", extractor.get("message").toString(), message);
	}
	
	@When("^I register station with details$")
	public void i_register_station_with_details() {
		try {
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
			response = request.queryParam("APPID", APPID).request(Method.POST, ENDPOINT_URL);
			
			JsonPath extractor = response.jsonPath();	
			id1 = extractor.get("ID");
			
			request.body(requestParams1.toString());
			response = request.queryParam("APPID", APPID).request(Method.POST, ENDPOINT_URL);
			JsonPath extractor1 = response.jsonPath();
			id2 = extractor1.get("ID");
			System.out.println("Stations created: "+ id1 + ", "+ id2);
		} catch( Exception e ) {
			System.out.println("Stations not created");
		}
	}
	
	@When("^I fetch the stations with ids I found the details$")
	public void i_get_the_stations_with_ids() throws Throwable {
		try {			
			String strID1 = ENDPOINT_URL+"/"+ id1;
			response = request.queryParam("APPID", APPID).request(Method.GET,  strID1);
			
			JsonPath extractor = response.jsonPath();		
			Assert.assertEquals(extractor.get("external_id"), "DEMO_TEST001");		
				
			request.header("Content-Type", "application/json");
			String strID2 = ENDPOINT_URL+"/"+ id2;	
			response = request.queryParam("APPID", APPID).request(Method.GET,  strID2);
			
			JsonPath extractor1 = response.jsonPath();		
			Assert.assertEquals(extractor1.get("external_id"), "Interview1");	
		} catch( Exception e ) {
			System.out.println("Stations not found");
		}
		
	}

	@When("^I delete both the stations$")
	public void i_delete_both_the_stations() throws Throwable {
		try {
			String strID1 = ENDPOINT_URL+"/"+ id1;
			response = request.queryParam("APPID", APPID).request(Method.DELETE, strID1);			
			
			String strID2 = ENDPOINT_URL+"/"+ id2;
			response = request.queryParam("APPID", APPID).request(Method.DELETE, strID2);
		} catch ( Exception e ) {
			System.out.println("Stations could not deleted");
		}
		
	}

	@When("^I delete the station again I found the Response code (\\d+) and message \"(.*?)\"$")
	public void i_delete_the_station_again(int code, String message) throws Throwable {
		try {
			String strID1 = ENDPOINT_URL+"/"+ id1;
			response = request.queryParam("APPID", APPID).request(Method.DELETE, strID1);	
			JsonPath extractor = response.jsonPath();	
			Assert.assertEquals(extractor.get("code"), code );	
			Assert.assertEquals(extractor.get("message"), message );
			
			String strID2 = ENDPOINT_URL+"/"+ id2;
			response = request.queryParam("APPID", APPID).request(Method.DELETE, strID2);
			JsonPath extractor1 = response.jsonPath();	
			Assert.assertEquals(extractor1.get("code"), code);	
			Assert.assertEquals(extractor1.get("message"), message);
			
		} catch ( Exception e ) {
			System.out.println("Stations could not deleted");
		}
		
	}

}
