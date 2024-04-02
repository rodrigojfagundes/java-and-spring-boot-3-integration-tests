package br.com.erudio.integrationtests.swagger;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;

//TEST DE INTEGRACAO - para verificar se a PAG do SWAGGER ta carregando

//herdando o ABSTRACTINTEGRATIONTEST q é onde tem as CONFIG para
//rodar o CONTAINER com o MYSQL
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTest {
	
	
	//TEST para verificar se a pagina do SWAGGER ta carregando
	//localhost:8888/swagger-ui/index.html
	//
	//nest caso nos NAO vamos usar o MOCK e sim o FLUENTAPI do REST ASSURENT
	//
	//test[System Under Test]_[Condition or State Change]_[Expected Result]
	@DisplayName("Should Display Swagger Ui Page")
	@Test
	void testShouldDisplaySwaggerUiPage() {
		//pedindo para acessar o LOCALHOST:8888/swagger-ui/index.html
		//a PORTA nos vamos usar a q ta na CLASSE TESTCONFIG
		//
		//WHEN().get -> quando for feita uma requisicao do TIPO GET
		//para o LOCALHOST:8888/swagger... THEN() -> ENTAO esperamos ter um
		//STATUSCODE (200) ou seja SUCESSO... E queremos EXTRACT.BODY ->
		//extrair o q ta no CONTEUDO da PAGINA como uma STRING
		//e o retorno vai ficar salvo na VAR CONTENT
		
		var content = given().basePath("/swagger-ui/index.html")
		.port(TestConfigs.SERVER_PORT)
		.when()
			.get()
		.then()
			.statusCode(200)
		.extract()
			.body()
				.asString();

		//verificando o q ta na VAR CONTENT se CONTEM a palavra SWAGGER UI
		//existe essa palavra so q so aparece se ver o CODIGO FONTE
		assertTrue(content.contains("Swagger UI"));
		
	}
	
}
