package br.com.erudio.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;

//TEST DE INTEGRACAO - para verificar se a PAG do SWAGGER ta carregando
//
//herdando o ABSTRACTINTEGRATIONTEST q é onde tem as CONFIG para
//rodar o CONTAINER com o MYSQL
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {
	
	
	//TEST para verificar se a pagina do SWAGGER ta carregando
	//localhost:8888/swagger-ui/index.html
	//
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("Should Display Swagger Ui Page")
	@Test
	void testShouldDisplaySwaggerUiPage() {
		var content = given().basePath("/swagger-ui/index.html")
		.port(TestConfigs.SERVER_PORT)
		.when()
			.get()
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();

		assertTrue(content.contains("Swagger UI"));
		
	}
	
}
