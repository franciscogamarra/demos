package br.caixa.loterias.util.keycloak;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class Main {
	
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Configura os headers
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("true", "Disable-Crypto"));
        headers.add(new BasicHeader("1", "Subcanal"));

        // Configura a requisição POST para o SSO
        URIBuilder uriBuilder = new URIBuilder("https://logindes.caixa.gov.br/auth/realms/internet/protocol/openid-connect/token");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("client_id", "cli-web-lce"));
        params.add(new BasicNameValuePair("username", "22587801010"));
        params.add(new BasicNameValuePair("password", "001100"));

        HttpPost ssoPostRequest = new HttpPost(uriBuilder.build());
        ssoPostRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
        ssoPostRequest.setHeaders(headers.toArray(new Header[headers.size()]));
        ssoPostRequest.setEntity(new UrlEncodedFormEntity(params));

        // Recupera o token do SSO
        boolean getToken = true;

        if (getToken) {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            try {
                org.apache.http.HttpResponse response = httpClient.execute(ssoPostRequest);
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println(responseBody);

                if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println("Salvando token e data de expiração");

                    // Extrai o access token e a data de expiração do corpo da resposta
                    Map<String, Object> responseJson = parseJson(responseBody);
                    String newAccessToken = responseJson.get("access_token").toString();
                    String expiresInSeconds = responseJson.get("expires_in").toString();

                    // Salva o access token e a data de expiração no ambiente
                    // pm.environment.set('currentAccessToken', newAccessToken);
                    // pm.environment.set('accessTokenExpiry', expiryDate.getTime());
                }
            } finally {
                httpClient.close();
            }
        }
    }

    private static Map<String, Object> parseJson(String json) throws IOException {
        // Utilize a biblioteca JSON de sua preferência para fazer o parsing do JSON
        // Aqui está um exemplo utilizando a biblioteca org.json.JSONObject
        org.json.JSONObject jsonObject = new org.json.JSONObject(json);
        return jsonObject.toMap();
    }
    
}