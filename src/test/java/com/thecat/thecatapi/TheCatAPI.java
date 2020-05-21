package com.thecat.thecatapi;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TheCatAPI extends MassaDeDados {


            @BeforeClass
            public static void urlbase(){
                RestAssured.baseURI = "https://api.thecatapi.com/v1/";
            }

            @Test
            public void cadastro() {

                Response response = given().contentType("application/json").body(corpoCadastro).
                        when().post(urlCadastro);
                response.then().body("message", containsString("SUCCESS")).statusCode(200);
                System.out.println("Retorno => " + response.body().asString());

            }

            @Test
            public void votacao() {

                Response response =
                        given().contentType("application/json")
                                .body(corpoVotacao).
                                when().post("votes/");
                response.then().body("message", containsString("SUCCESS")).statusCode(200);
                System.out.println("Retorno => " + response.body().asString());

                String id = response.jsonPath().getString("id");
                vote_id = id;
                System.out.println("ID => " + id);
            }

            @Test
            public void deletaVotacao() {
                votacao();
                deletaVoto();
            }

            private void deletaVoto() {

                Response response =
                        given()
                                .contentType("application/json")
                                .header("x-api-key", "2e47b834-5e7e-4115-ab6b-9951639f466a")
                                .pathParam("vote_id", vote_id)
                                .when().delete("votes/{vote_id}");

                System.out.println("Retorno => " + response.body().asString());
                response.then().body("message", containsString("SUCCESS")).statusCode(200);

            }

            @Test
            public void favoritaDesfavorita() {
                favorita();
                desfavorita();
            }

            private void favorita() {
                Response response = given().contentType("application/json")
                        .header("x-api-key", "e3324f4d-d053-42a7-b96b-130efc1c0b92").body(corpoFavorita).when()
                        .post("favourites");
                String id = response.jsonPath().getString("id");
                vote_id = id;
                validacao(response);
            }

            private void desfavorita() {
                Response response = given().contentType("application/json")
                        .header("x-api-key", "e3324f4d-d053-42a7-b96b-130efc1c0b92").pathParam("favourite_id", vote_id).when()
                        .delete(corpoDesfavorita);
                validacao(response);

            }

            public void validacao(Response response) {

                response.then().statusCode(200).body("message", containsString("SUCCESS"));
                System.out.println("RETORNO DA API => " + response.body().asString());
                System.out.println("-----------------------------------------------");
            }

        }