package tests;

import enums.Currency;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;


public class ApiOnlinerTest {

    @DataProvider(name = "rates")
    public Object[][] currencyProvider() {
        return new Object[][] {
            new Object[] {Currency.USD},
            new Object[] { Currency.EUR },
            new Object[] { Currency.RUB }
        };
    }

    @Test(dataProvider = "rates")
    public void chekRates(Currency currency) {
        String url = "https://profile.onliner.by/sdapi/kurs/api/bestrate?currency=" + currency.getCurrency() + "&type=nbrb";
        String responseBody = given()
                .log().all()
                .when()
                .get(url)
                .then()
                .log().all()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/rate_schema.json"))
                .header("Content-Type", containsString("application/json"))
                .body("$", hasKey("banks"))
                .body("grow",notNullValue())
                .extract().body().asString();
    }
}
