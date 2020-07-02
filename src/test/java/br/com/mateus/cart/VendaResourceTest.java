package br.com.mateus.cart;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class VendaResourceTest {

	@Test
	public void testVendaEndpoint() {
		given().body("{\"tipo\": \"VENDA\", \"valortotal\": \"62.45\"}")
				.header("Content-Type", MediaType.APPLICATION_JSON).when().post("/fruits").then().statusCode(200)
				.body("$.size()", is(3));
	}

}