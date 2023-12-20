package kth.numi.Controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import kth.numi.model.Encounter;
import kth.numi.model.User;
import org.junit.jupiter.api.Test;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;

@QuarkusTest
class UserControllerTest {

    @Test
    void testToGetPatientsByName() {
        // Test data
        String testName = "Nuh";

        // Perform the request and check the response
        Response response = given()
                .when().get("/v1/patients/name/{name}", testName)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        // Deserialize the response to a list of User
        List<User> userList = response.jsonPath().getList(".", User.class);

        // Test the data
        response.then()
                .body("size()", equalTo(userList.size()))
                .body("firstname", everyItem(equalTo(testName)))
                .body("lastname", everyItem(equalTo("Jama Mohamud")))
                .body("email", everyItem(equalTo("numoh.55@gmail.com")))
                .body("password", everyItem(equalTo("$2a$12$/C8fviFbdCDQhc9l3R7gl.I5hrWw6czE5JV3pp29WkiiScy4WQc6u")))
                .body("phone", everyItem(equalTo("0765641298")))
                .body("address", everyItem(equalTo("Edingekroken 13")))
                .body("role", everyItem(equalTo("PATIENT")));
    }

    @Test
    void testToGetPatientsByCondition() {
        // Test data
        String testCondition = "Fever";

        // Perform the request and check the response
        Response response = given()
                .when().get("/v1/patients/condition/{condition}", testCondition)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        // Deserialize the response to a list of User
        List<User> userList = response.jsonPath().getList(".", User.class);

        // Test the data
        response.then()
                .body("size()", equalTo(userList.size()))
                .body("id", everyItem(equalTo(1)))
                .body("firstname", everyItem(equalTo("Nuh")))
                .body("lastname", everyItem(equalTo("Jama Mohamud")))
                .body("email", everyItem(equalTo("numoh.55@gmail.com")))
                .body("phone", everyItem(equalTo("0765641298")))
                .body("address", everyItem(equalTo("Edingekroken 13")))
                .body("role", everyItem(equalTo("PATIENT")));
    }

    @Test
    public void testToGetPatientsByDoctorName() {
        // Test data
        String testDoctorName = "Doctor";

        // Request and check the response
        Response response = given()
                .when().get("/v1/doctor/patients/{name}", testDoctorName)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        // Deserialize the response to a list of User
        List<User> patientList = response.jsonPath().getList(".", User.class);

        // Test the data
        response.then()
                .body("size()", equalTo(patientList.size()))
                .body("id", everyItem(equalTo(1)))
                .body("firstname", everyItem(equalTo("Nuh")))
                .body("lastname", everyItem(equalTo("Jama Mohamud")))
                .body("email", everyItem(equalTo("numoh.55@gmail.com")))
                .body("phone", everyItem(equalTo("0765641298")))
                .body("address", everyItem(equalTo("Edingekroken 13")))
                .body("role", everyItem(equalTo("PATIENT")));
    }

    @Test
    public void testGetEncounterByDoctorNameEndpoint() {
        // Test data
        String testDoctorName = "Doctor";

        // Request and check the response
        Response response = given()
                .when().get("/v1/doctor/encounters/{name}", testDoctorName)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        // Deserialize the response to a list of Encounter
        List<Encounter> encounterList = response.jsonPath().getList(".", Encounter.class);

        // Test the data
        response.then()
                .body("size()", equalTo(encounterList.size()));
    }
}