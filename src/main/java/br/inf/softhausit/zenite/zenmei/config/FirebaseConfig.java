package br.inf.softhausit.zenite.zenmei.config;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
public class FirebaseConfig {
    @Value("${firebase.credentials.json:}")
    private String firebaseCredentialsJson;
    @Value("${firebase.project-id:zenmei-app-8a181}")
    private String projectId;
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        log.info("Inicializando Firebase App...");
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseOptions options;
            if (firebaseCredentialsJson != null && !firebaseCredentialsJson.isEmpty()) {
                InputStream serviceAccount = new ByteArrayInputStream(
                    firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8)
                );
                options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(projectId)
                    .build();
            } else {
                log.info("Usando Google Application Default Credentials");
                options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .setProjectId(projectId)
                    .build();
            }
            FirebaseApp app = FirebaseApp.initializeApp(options);
            log.info("Firebase App inicializado com sucesso. Project ID: {}", projectId);
            return app;
        } else {
            log.info("Firebase App já estava inicializado");
            return FirebaseApp.getInstance();
        }
    }
    @Bean
    public FirebaseAuth firebaseAuth(FirebaseApp firebaseApp) {
        log.info("Criando bean FirebaseAuth");
        return FirebaseAuth.getInstance(firebaseApp);
    }
}
