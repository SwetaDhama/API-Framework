import io.restassured.RestAssured;
import org.testng.annotations.DataProvider;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import files.payload;

public class DynamicJson {

	@Test
	public void addBook() {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json").body(payload.addBook())
				          .when()
				          .post("/Library/Addbook.php")
				          .then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = new JsonPath(response);
		String id = js.get("ID");
		System.out.println(id);
	}

	// Now if we add the same book again then we will get error. So to retest it
	// again we need to do send parameters to payload from test
	//This is Dynamically build json payload with external data inputs

	@Test(dataProvider="BooksData")
	public void addBook1(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json").body(payload.addBook1(isbn,aisle))
				          .when()
				          .post("/Library/Addbook.php")
				          .then().assertThat().statusCode(200).extract().response().asString();
		JsonPath js = new JsonPath(response);
		String id = js.get("ID");
		System.out.println(id);
	}
	
	//Parameterize data with multiple data sets
	@DataProvider(name="BooksData")
	public  Object[][] getData(){
		return new Object[][]{{"dfjdf","344"},{"sdkds","34393"},{"vcvnv","9834"}};
	}
	
}
