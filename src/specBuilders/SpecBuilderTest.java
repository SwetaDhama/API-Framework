package specBuilders;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;


import java.util.ArrayList;
import java.util.List;


import serialization.AddPlace;
import serialization.Location;

public class SpecBuilderTest {

	public static void main(String[] args) {
		AddPlace p = new AddPlace();
		p.setAccuracy(50);
		p.setAddress("29, side road");
		p.setLanguage("French-IN");
		p.setPhone_number("39849344");
		p.setWebsite("https://rahulshettyacademy.com");
		p.setName("Frontline house");
		List<String> myList = new ArrayList<String>();
		myList.add("shoe park");
		myList.add("shop");
        p.setTypes(myList);
        Location l=new Location();
        l.setLat(-38.54545);
        l.setLng(33.56656);
		p.setLocation(l); 
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
		.setContentType(ContentType.JSON).build();
		
		ResponseSpecification resp = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification res = given().spec(req).body(p);
				
			Response response =res.when().post("/maps/api/place/add/json").then()
				.spec(resp).extract().response();
			String responseString = response.asString();
			System.out.println(responseString);

	}
}