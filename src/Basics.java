import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.path.json.JsonPath;

import files.payload;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;

public class Basics {

	public static void main(String[] args) {
		// given all input details
		// when submit the API
		// then validation
		//Add Place 
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all()
		.queryParam("key","qaclick123")
		.header("Content-Type","application/json")
		.body(payload.AddPlace())
		.when()
		.post("maps/api/place/add/json")
		.then()
		.assertThat()
		.statusCode(200)
		.body("scope",equalTo("APP"))
		.header("server","Apache/2.4.18 (Ubuntu)")
		.extract()
		.response().asString();
		System.out.println(response);
		
		JsonPath js=new JsonPath(response);
		String placeId=js.getString("place_id");
		System.out.println(placeId);
		 
		//Update Place
		String newAddress="Summer Walk, Africa";
		given().log().all()
		.queryParam("key","qaclick123")
		.header("Content-Type","application/json")
		.body("{\r\n" + 
				"\"place_id\":\""+placeId+"\",\r\n" + 
				"\"address\":\""+newAddress+"\",\r\n" + 
				"\"key\":\"qaclick123\"\r\n" + 
				"}")
		.when()
		.put("maps/api/place/update/json")
		.then()
		.assertThat().log().all()
		.statusCode(200)
		.body("msg",equalTo("Address successfully updated"));
		
		//Get Place
		String getPlaceResponse = given().log().all()
		.queryParam("key","qaclick123")
		.queryParam("place_id",placeId)
		.when()
		.get("maps/api/place/get/json")
		.then()
		.assertThat().log().all()
		.statusCode(200)
		.extract().response().asString();
		JsonPath js1 =new JsonPath(getPlaceResponse);
		String actualAddress=js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(actualAddress,newAddress);
		
	}

}
