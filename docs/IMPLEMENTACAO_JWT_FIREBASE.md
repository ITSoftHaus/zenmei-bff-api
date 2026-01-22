# 🔐 IMPLEMENTAÇÃO JWT COM FIREBASE AUTH - ZENMEI

## 📋 Visão Geral

Implementar autenticação JWT usando **Firebase Authentication** no backend Spring Boot, garantindo segurança robusta e integração perfeita com o frontend.

---

## 🎯 ARQUITETURA

```
Frontend (React/Angular)
    ↓
Firebase Auth (Login/Register)
    ↓
Recebe JWT Token
    ↓
Envia Token no Header: Authorization: Bearer <token>
    ↓
BFF API (Spring Boot)
    ↓
FirebaseAuthFilter valida token
    ↓
SecurityContext configurado
    ↓
Controller protegido com @PreAuthorize
```

---

## 📦 PASSO 1: Adicionar Dependências

### pom.xml do BFF

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Firebase Admin SDK -->
<dependency>
    <groupId>com.google.firebase</groupId>
    <artifactId>firebase-admin</artifactId>
    <version>9.2.0</version>
</dependency>

<!-- JWT (se precisar manipular tokens manualmente) -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.5</version>
    <scope>runtime</scope>
</dependency>
```

---

## 🔑 PASSO 2: Configurar Firebase Admin SDK

### 2.1 Obter Service Account do Firebase

1. Acesse: https://console.firebase.google.com/
2. Selecione seu projeto (ou crie um novo)
3. Vá em **Project Settings** (ícone de engrenagem)
4. Aba **Service Accounts**
5. Clique em **Generate new private key**
6. Salve o arquivo JSON (ex: `firebase-service-account.json`)

### 2.2 Adicionar ao Projeto

```bash
# Criar diretório para credenciais
mkdir -p zenmei-bff-api/src/main/resources/firebase

# Copiar arquivo (NÃO commitar no Git!)
cp firebase-service-account.json zenmei-bff-api/src/main/resources/firebase/
```

### 2.3 Atualizar .gitignore

```gitignore
# Firebase credentials
**/firebase/firebase-service-account.json
**/*service-account*.json
```

---

## ⚙️ PASSO 3: Configuração do Firebase

### FirebaseConfig.java

```java
package br.inf.softhausit.zenite.zenmei.bff.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = new ClassPathResource(
                    "firebase/firebase-service-account.json"
                ).getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

                FirebaseApp.initializeApp(options);
                
                log.info("✅ Firebase Admin SDK inicializado com sucesso");
            }
        } catch (IOException e) {
            log.error("❌ Erro ao inicializar Firebase Admin SDK", e);
            throw new RuntimeException("Falha ao inicializar Firebase", e);
        }
    }
}
```

---

## 🔐 PASSO 4: Criar Filtro de Autenticação

### FirebaseAuthenticationFilter.java

```java
package br.inf.softhausit.zenite.zenmei.bff.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                   HttpServletResponse response,
                                   FilterChain filterChain) 
            throws ServletException, IOException {
        
        try {
            String token = extractToken(request);
            
            if (token != null) {
                FirebaseToken decodedToken = FirebaseAuth.getInstance()
                    .verifyIdToken(token);
                
                // Extrair informações do usuário
                String uid = decodedToken.getUid();
                String email = decodedToken.getEmail();
                String name = decodedToken.getName();
                
                // Extrair roles/permissions do custom claims
                Map<String, Object> claims = decodedToken.getClaims();
                List<SimpleGrantedAuthority> authorities = extractAuthorities(claims);
                
                // Criar principal com informações do usuário
                FirebaseUserDetails userDetails = FirebaseUserDetails.builder()
                    .uid(uid)
                    .email(email)
                    .name(name)
                    .claims(claims)
                    .build();
                
                // Configurar contexto de segurança
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                    );
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("✅ Usuário autenticado: {} ({})", email, uid);
            }
        } catch (FirebaseAuthException e) {
            log.error("❌ Token Firebase inválido: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token inválido ou expirado\"}");
            return;
        } catch (Exception e) {
            log.error("❌ Erro ao processar autenticação", e);
        }
        
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        
        if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        
        return null;
    }

    private List<SimpleGrantedAuthority> extractAuthorities(Map<String, Object> claims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // Adicionar role padrão
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        // Extrair roles customizadas do Firebase
        if (claims.containsKey("roles")) {
            Object roles = claims.get("roles");
            if (roles instanceof List) {
                ((List<?>) roles).forEach(role -> 
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()))
                );
            }
        }
        
        // Extrair permissões específicas
        if (claims.containsKey("admin") && Boolean.TRUE.equals(claims.get("admin"))) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        
        return authorities;
    }
}
```

---

## 👤 PASSO 5: Criar UserDetails Customizado

### FirebaseUserDetails.java

```java
package br.inf.softhausit.zenite.zenmei.bff.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseUserDetails {
    private String uid;
    private String email;
    private String name;
    private String picture;
    private Map<String, Object> claims;
    
    public boolean isAdmin() {
        return claims != null && 
               Boolean.TRUE.equals(claims.get("admin"));
    }
    
    public String getRole() {
        if (claims != null && claims.containsKey("role")) {
            return claims.get("role").toString();
        }
        return "USER";
    }
}
```

---

## 🔒 PASSO 6: Configurar Spring Security

### SecurityConfig.java

```java
package br.inf.softhausit.zenite.zenmei.bff.config;

import br.inf.softhausit.zenite.zenmei.bff.security.FirebaseAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final FirebaseAuthenticationFilter firebaseAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilitar CSRF (API Stateless com JWT)
            .csrf(csrf -> csrf.disable())
            
            // Configurar sessões (Stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configurar autorização
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/info").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/api/bff/v1/public/**").permitAll()
                
                // Endpoints de admin
                .requestMatchers("/api/bff/v1/admin/**").hasRole("ADMIN")
                
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            
            // Adicionar filtro Firebase antes do filtro padrão
            .addFilterBefore(
                firebaseAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
```

---

## 🎨 PASSO 7: Atualizar Controllers com Segurança

### MeiBffController.java (Exemplo)

```java
package br.inf.softhausit.zenite.zenmei.bff.controller;

import br.inf.softhausit.zenite.zenmei.bff.security.FirebaseUserDetails;
import br.inf.softhausit.zenite.zenmei.bff.service.MeiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/bff/v1/meis")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MeiBffController {

    private final MeiService meiService;

    @Operation(summary = "Listar MEIs do usuário autenticado")
    @GetMapping
    public ResponseEntity<?> listarMeus(
            @AuthenticationPrincipal FirebaseUserDetails user) {
        
        log.info("Listando MEIs do usuário: {} ({})", user.getEmail(), user.getUid());
        
        // Usar UID do Firebase para buscar dados
        return meiService.listarPorUsuario(user.getUid());
    }

    @Operation(summary = "Buscar MEI por ID")
    @GetMapping("/{id}")
    @PreAuthorize("@meiSecurityService.podeAcessarMei(#id, authentication.principal)")
    public ResponseEntity<?> buscarMei(
            @PathVariable UUID id,
            @AuthenticationPrincipal FirebaseUserDetails user) {
        
        log.info("Usuário {} buscando MEI {}", user.getEmail(), id);
        return meiService.buscarMei(id);
    }

    @Operation(summary = "Criar novo MEI")
    @PostMapping
    public ResponseEntity<?> criarMei(
            @RequestBody Object mei,
            @AuthenticationPrincipal FirebaseUserDetails user) {
        
        log.info("Usuário {} criando novo MEI", user.getEmail());
        
        // Associar MEI ao usuário Firebase
        return meiService.criarMei(mei, user.getUid());
    }

    @Operation(summary = "Atualizar MEI")
    @PutMapping("/{id}")
    @PreAuthorize("@meiSecurityService.podeEditarMei(#id, authentication.principal)")
    public ResponseEntity<?> atualizarMei(
            @PathVariable UUID id,
            @RequestBody Object mei,
            @AuthenticationPrincipal FirebaseUserDetails user) {
        
        log.info("Usuário {} atualizando MEI {}", user.getEmail(), id);
        return meiService.atualizarMei(id, mei);
    }

    @Operation(summary = "Deletar MEI")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @meiSecurityService.podeEditarMei(#id, authentication.principal)")
    public ResponseEntity<Void> deletarMei(
            @PathVariable UUID id,
            @AuthenticationPrincipal FirebaseUserDetails user) {
        
        log.info("Usuário {} deletando MEI {}", user.getEmail(), id);
        return meiService.deletarMei(id);
    }

    // Endpoint de admin
    @Operation(summary = "Listar TODOS os MEIs (Admin)")
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarTodos(
            @AuthenticationPrincipal FirebaseUserDetails user) {
        
        log.info("Admin {} listando todos os MEIs", user.getEmail());
        return meiService.listarTodos();
    }
}
```

---

## 🛡️ PASSO 8: Criar Service de Segurança

### MeiSecurityService.java

```java
package br.inf.softhausit.zenite.zenmei.bff.security;

import br.inf.softhausit.zenite.zenmei.bff.client.MeiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("meiSecurityService")
@RequiredArgsConstructor
@Slf4j
public class MeiSecurityService {

    private final MeiClient meiClient;

    /**
     * Verifica se o usuário pode acessar o MEI
     */
    public boolean podeAcessarMei(UUID meiId, FirebaseUserDetails user) {
        try {
            // Admin pode acessar tudo
            if (user.isAdmin()) {
                return true;
            }
            
            // Buscar MEI e verificar se pertence ao usuário
            var mei = meiClient.buscarMei(meiId);
            
            // Supondo que o MEI tenha um campo firebaseUid
            // Ajustar conforme seu modelo
            return mei != null && 
                   user.getUid().equals(getMeiFirebaseUid(mei));
                   
        } catch (Exception e) {
            log.error("Erro ao verificar permissão para MEI {}", meiId, e);
            return false;
        }
    }

    /**
     * Verifica se o usuário pode editar o MEI
     */
    public boolean podeEditarMei(UUID meiId, FirebaseUserDetails user) {
        // Por enquanto, mesma regra de acesso
        return podeAcessarMei(meiId, user);
    }

    private String getMeiFirebaseUid(Object mei) {
        // Implementar extração do UID do MEI
        // Depende da estrutura do seu objeto
        return null;
    }
}
```

---

## 🗄️ PASSO 9: Atualizar Entidade Mei

### Adicionar campo Firebase UID

```java
@Entity
@Table(name = "meis")
public class Mei {
    // ...campos existentes...
    
    @Column(name = "firebase_uid", nullable = false, unique = true)
    private String firebaseUid;
    
    // ...restante do código...
}
```

### Migration SQL

```sql
-- V4__add_firebase_uid_to_meis.sql
ALTER TABLE meis ADD COLUMN firebase_uid VARCHAR(255);
ALTER TABLE meis ADD CONSTRAINT uk_meis_firebase_uid UNIQUE (firebase_uid);

-- Criar índice para performance
CREATE INDEX idx_meis_firebase_uid ON meis(firebase_uid);
```

---

## 📝 PASSO 10: Configurar application.yml

```yaml
# application.yml
spring:
  security:
    filter:
      order: 5

# Configurações Firebase (opcional)
firebase:
  enabled: true
  service-account-file: classpath:firebase/firebase-service-account.json

# Logging de segurança
logging:
  level:
    br.inf.softhausit.zenite.zenmei.bff.security: DEBUG
    com.google.firebase: INFO
```

---

## 🧪 PASSO 11: Testar Autenticação

### Teste Manual com Postman/Insomnia

1. **Obter Token do Firebase** (Frontend ou Firebase Console)

```javascript
// No frontend (exemplo React)
import { getAuth, signInWithEmailAndPassword } from 'firebase/auth';

const auth = getAuth();
const userCredential = await signInWithEmailAndPassword(
    auth, 
    'usuario@example.com', 
    'senha123'
);

const token = await userCredential.user.getIdToken();
console.log('Token:', token);
```

2. **Fazer Request no Postman**

```http
GET http://localhost:8080/api/bff/v1/meis
Authorization: Bearer <TOKEN-FIREBASE-AQUI>
```

---

## 🔧 PASSO 12: Configurar Custom Claims (Roles)

### No Firebase Console ou via Cloud Functions

```javascript
// Cloud Function para setar roles
const admin = require('firebase-admin');

exports.setUserRole = functions.https.onCall(async (data, context) => {
    // Verificar se quem está chamando é admin
    if (!context.auth.token.admin) {
        throw new functions.https.HttpsError(
            'permission-denied',
            'Apenas admins podem definir roles'
        );
    }
    
    // Setar custom claims
    await admin.auth().setCustomUserClaims(data.uid, {
        role: data.role,  // 'USER', 'ADMIN', 'MANAGER', etc
        admin: data.role === 'ADMIN'
    });
    
    return { success: true };
});
```

---

## 🎨 PASSO 13: Atualizar Swagger com Security

### OpenApiConfig.java

```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ZenMEI BFF API")
                .version("1.0")
                .description("Backend for Frontend com Firebase Auth"))
            .addSecurityItem(new SecurityRequirement()
                .addList("bearerAuth"))
            .components(new Components()
                .addSecuritySchemes("bearerAuth",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Token JWT do Firebase Authentication")));
    }
}
```

---

## 📊 PASSO 14: Criar Endpoint de Profile

### AuthController.java

```java
@RestController
@RequestMapping("/api/bff/v1/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<FirebaseUserDetails> getProfile(
            @AuthenticationPrincipal FirebaseUserDetails user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody Map<String, String> body) {
        // O refresh é feito no frontend com Firebase
        // Este endpoint é apenas informativo
        return ResponseEntity.ok(Map.of(
            "message", "Use Firebase SDK no frontend para refresh"
        ));
    }
}
```

---

## ✅ CHECKLIST DE IMPLEMENTAÇÃO

```markdown
- [ ] 1. Adicionar dependências (Firebase Admin, Spring Security)
- [ ] 2. Criar projeto no Firebase Console
- [ ] 3. Baixar service account JSON
- [ ] 4. Adicionar arquivo ao projeto (sem commitar)
- [ ] 5. Criar FirebaseConfig
- [ ] 6. Criar FirebaseAuthenticationFilter
- [ ] 7. Criar FirebaseUserDetails
- [ ] 8. Criar SecurityConfig
- [ ] 9. Atualizar Controllers com @PreAuthorize
- [ ] 10. Criar MeiSecurityService
- [ ] 11. Adicionar campo firebase_uid na entidade Mei
- [ ] 12. Criar migration SQL
- [ ] 13. Atualizar OpenApiConfig
- [ ] 14. Testar com Postman
- [ ] 15. Implementar no frontend
```

---

## 🚀 RESULTADO ESPERADO

Após implementação:

✅ **Autenticação JWT** via Firebase  
✅ **Autorização** baseada em roles  
✅ **Segurança robusta** em todos os endpoints  
✅ **Integração perfeita** com frontend  
✅ **Swagger protegido** e documentado  
✅ **Performance** (Firebase valida tokens rapidamente)  

---

## 🎯 PRÓXIMOS PASSOS

1. **Implementar no Frontend**
2. **Configurar Roles customizadas**
3. **Adicionar testes de segurança**
4. **Documentar fluxo de autenticação**
5. **Configurar renovação automática de tokens**

---

**Criado em:** 21 de Janeiro de 2026  
**Tempo estimado:** 3-4 horas  
**Prioridade:** 🔥🔥🔥🔥🔥 CRÍTICA

**Firebase Auth = Segurança Enterprise + Simplicidade** 🔐🚀
