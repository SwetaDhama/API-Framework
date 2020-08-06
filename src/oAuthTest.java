import static io.restassured.RestAssured.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.restassured.path.json.JsonPath;

public class oAuthTest {

	public static void main(String[] args) throws InterruptedException {

		// Mandatory fields for GetAuthorization Code Request-
		System.setProperty("webdriver.chrome.driver","C:\\Development_Avecto\\chromedriver_win32 (1)\\chromedriver.exe");
		WebDriver driver =new ChromeDriver();
		driver.get("");  //authorization server url
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("dhama1712@gmail.com");
		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("password");
		driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		//String url = driver.getCurrentUrl();
		
		//In 2020 google disabled the automation of authorization URLs, so use below line instead of above webdriver command -
		String url ="https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2FvAHBQUZU6o4WJ719NrGBzSELBFVBI9XbxvOtYpmYpeV47bFVExkaxWaF_XR14PHtTZf7ILSEeamywJKwo_BYs9M&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&session_state=0c32992f0d47e93d273922018ade42d1072b9d1f..a35c&prompt=none#";
		
		//Parsing the URL to get the code
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		System.out.println(code);
		
		//Rest Assured encodes the special characters by itself so we need to tell that dont modify the special characters
		//Mandatory fields for GetAccess Token Request-
		String accessTokenResponse = given().urlEncodingEnabled(false)
		.queryParams("code",code)
		.queryParams("client_id","")
		.queryParams("client_secret","")
		.queryParams("redirect_uri","")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
		
		//Code for hitting the actual request
		String response = given().queryParam("access_token",accessToken)
		.when().get("https://rahulshettyacademy.com/getCourse.php").asString();
		System.out.println(response);
		
	}

}
