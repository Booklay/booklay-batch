package com.nhnacademy.booklay.batch.booklaybatch.config;

import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

import com.nhnacademy.booklay.batch.booklaybatch.dto.DatasourceInfo;
import com.nhnacademy.booklay.batch.booklaybatch.dto.SecretResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataBaseConfig {
    @Value("${booklay.secure.p12_password}")
    private String p12Password;

    @Value("${booklay.secure.url}")
    private String url;

    @Value("${booklay.secure.db_username}")
    private String username;

    @Value("${booklay.secure.db_password}")
    private String password;

    @Value("${booklay.secure.db_url}")
    private String dbUrl;

    /**
     * NHN Secure Manager를 통해 민감정보를 받습니다.
     * booklay.p12인증서를 통해 HTTPS 요청을 보냅니다.
     *
     * @author 조현진
     */
    @Bean
    public RestTemplate restTemplate()
        throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException,
        UnrecoverableKeyException, KeyManagementException {
        var clientStore = KeyStore.getInstance("PKCS12");

        try (InputStream inputStream = getClass().getClassLoader()
            .getResourceAsStream("booklay.p12")) {
            File tempFile = File.createTempFile(String.valueOf(inputStream.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            copyInputStreamToFile(inputStream, tempFile);

            clientStore.load(new FileInputStream(tempFile), p12Password.toCharArray());
        }

        var sslContext = SSLContextBuilder.create()
            .setProtocol("TLS")
            .loadKeyMaterial(clientStore, p12Password.toCharArray())
            .loadTrustMaterial(new TrustSelfSignedStrategy())
            .build();

        var sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .build();

        var requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        return new RestTemplate(requestFactory);
    }

    /**
     * 애플리케이션이 구동되는 동시에 NHN Secure Manager로부터 접속 정보를 가져옵니다.
     *
     * @param restTemplate
     * @return DB 접속 정보를 반환합니다.
     * @author 조현진
     */
    @Bean
    public DatasourceInfo datasourceInfo(RestTemplate restTemplate) {
        SecretResponse usernameResponse = Objects.requireNonNull(
            restTemplate.getForObject(url + this.username, SecretResponse.class));
        SecretResponse passwordResponse = Objects.requireNonNull(
            restTemplate.getForObject(url + this.password, SecretResponse.class));
        SecretResponse dbUrlResponse = Objects.requireNonNull(
            restTemplate.getForObject(url + this.dbUrl, SecretResponse.class));

        return DatasourceInfo.builder()
            .passwword(passwordResponse.getBody().getSecret())
            .dbUrl(dbUrlResponse.getBody().getSecret())
            .username(usernameResponse.getBody().getSecret())
            .build();

    }
}
