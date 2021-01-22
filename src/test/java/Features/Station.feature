Feature: Create Station

	Scenario: Validate that attempt to register a weather station without an API key will return the following in message body.
		Given I get the API url
		When I register station without API key
		Then I should get Http Response code as 401
		And I should get response message as "Invalid API key. Please see http://openweathermap.org/faq#error401 for more info."
		
	Scenario: Successfully register two stations with the following details and verify that HTTP response code is 201.
	
		Given I get the API url
		When I register station with details
		Then I should get Http Response code as 201
		When I fetch the stations with ids I found the details
		When I delete both the stations 
		Then I should get Http Response code as 204
		When I delete the station again I found the Response code 404001 and message "Station not found"
  
  	
