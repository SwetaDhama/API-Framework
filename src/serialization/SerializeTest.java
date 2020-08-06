package serialization;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.List;

import serialization.AddPlace;

public class SerializeTest {

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
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String res = given().queryParam("key", "qaclick123").body(p).when().post("/maps/api/place/add/json").then()
				.assertThat().statusCode(200).extract().response().asString();

	}
}