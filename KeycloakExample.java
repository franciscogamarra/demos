import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.authentication.ClientCredentialsProviderUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public class KeycloakExample {
    public static void main(String[] args) {
        // Configuração do cliente Keycloak
        String serverUrl = "http://localhost:8080/auth";
        String realm = "seu-realm";
        String clientId = "seu-cliente";
        String clientSecret = "seu-secret";

        // Criação do cliente Keycloak
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();

        // Obtendo informações do realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource usersResource = realmResource.users();

        // Obtendo todos os usuários do realm
        List<UserRepresentation> users = usersResource.list();
        for (UserRepresentation user : users) {
            System.out.println("ID do usuário: " + user.getId());
            System.out.println("Nome do usuário: " + user.getUsername());
            System.out.println("Email do usuário: " + user.getEmail());
            System.out.println();
        }

        // Criando um novo usuário
        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername("novo-usuario");
        newUser.setEmail("novo-usuario@example.com");
        newUser.setEnabled(true);
        usersResource.create(newUser);

        // Obtendo informações de um usuário específico
        UserResource userResource = usersResource.get("id-do-usuario");
        UserRepresentation user = userResource.toRepresentation();
        System.out.println("ID do usuário: " + user.getId());
        System.out.println("Nome do usuário: " + user.getUsername());
        System.out.println("Email do usuário: " + user.getEmail());

        // Atualizando informações de um usuário
        user.setUsername("novo-nome");
        user.setEmail("novo-email@example.com");
        userResource.update(user);

        // Excluindo um usuário
        userResource.remove();

        // Fechando o cliente Keycloak
        keycloak.close();
    }
}
/*
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-servlet-filter-adapter</artifactId>
    <version>14.0.0</version>
</dependency>
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-admin-client</artifactId>
    <version>14.0.0</version>
</dependency>
*/