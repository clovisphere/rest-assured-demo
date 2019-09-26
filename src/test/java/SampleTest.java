import com.google.gson.Gson;
import io.restassured.response.Response;
import org.junit.*;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

/**
 * A Demo class
 */
public class SampleTest {
    // define the endpoint
    private String url = "https://reqres.in/api/";

    @Test
    public void test_validate_header_details() {
        given()
            .when()
                .get(url.concat("users/").concat("1"))
            .then()
                .assertThat().statusCode(200)
            .and()
                .contentType(ContentType.JSON)
            .and()
                .header("Connection", "keep-alive");
    }
    
    @Test
    public void test_find_user(){
        given()
            .when()
                .get(url.concat("users/").concat("1"))
            .then()
                .body("data.first_name", equalTo("George"));
    }
    
    @Test
    public void test_create_user() {
        Gson gson = new Gson();
        // define payload
        Map<String, String> payload = new HashMap<String, String>();
        payload.put("name", "Elena");
        payload.put("job", "Tester");

        given()
            .contentType(ContentType.JSON)
            .body(gson.toJson(payload))
            .when()
                .post(url.concat("users"))
            .then()
                .assertThat().statusCode(201);
    }
    
    @Test
    public void test_find_user_hacker_version() {
        Response response =
                given()
                .when()
                    .get(url.concat("users/").concat("1"))
                .then()
                    .statusCode(200)
                    .extract()
                        .response();

        // retrieve that info, and use it
        String id = response.path("data.id").toString();

        given()
            .when()
                .get(url.concat("users/" + id))
            .then()
                .body("data.first_name", equalTo("George"));

    }
}

