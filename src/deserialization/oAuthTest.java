package deserialization;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;

public class oAuthTest {

	public static void main(String[] args) throws InterruptedException {

		// Mandatory fields for GetAuthorization Code Request-
		System.setProperty("webdriver.chrome.driver","C:\\Development_Avecto\\chromedriver_win32 (1)\\chromedriver.exe");
		WebDriver driver =new ChromeDriver();
		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");  //authorization server url
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
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all()
		.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js=new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
		
		//Code for Deserialization
		GetCourse gc = given().queryParam("access_token",accessToken).expect().defaultParser(Parser.JSON)
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

        System.out.println(gc.getLinkedIn());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
		
		List<Api> apiCourses = gc.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++){
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices Testing")){
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		//Get courses name of Web Automation
		String[] courseTitles ={"Selenium","Cypress","RPA"};
		ArrayList<String> a= new ArrayList<String>();
		List<WebAutomation> w = gc.getCourses().getWebAutomation();
		for(int j=0;j<w.size();j++){
			a.add(w.get(j).getCourseTitle());
		}
		List<String> expectedTitles = Arrays.asList(courseTitles);
		Assert.assertTrue(a.equals(expectedTitles));
		
	}

}
