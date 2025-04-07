 HubSpot Integration API

API Spring Boot para integração completa com o HubSpot, incluindo autenticação OAuth2, gestão de contatos e webhooks.

  Pré-requisitos

- Java 17+
- Maven 3.8+
- [Conta de desenvolvedor no HubSpot (VianaDev)](https://developers.hubspot.com/)
- Credenciais OAuth (Client ID e Secret)

Configuração Rápida
Configure as credenciais** em `src/main/resources/application-dev.properties`:
properties
hubspot.client.id=seu-client-id-aqui
hubspot.client.secret=seu-client-secret-aqui
hubspot.redirect.uri=http://localhost:8080/api/auth/callback
hubspot.webhook.secret=seu-secret-aqui

Execute a aplicação:
mvn spring-boot:run -Dspring-boot.run.profiles=dev

Exempos de alguns Endpoints da API
Autenticação OAuth2
Obter URL de autorização

GET /api/auth/authorize

Exemplo:

curl -X GET "http://localhost:8080/api/auth/authorize"

Resposta:

"https://app.hubspot.com/oauth/authorize?client_id=12345&redirect_uri=http://localhost:8080/api/auth/callback&scope=contacts"

Processar call-back


GET /api/auth/callback?code={authorization_code}

Exemplo:

curl -X GET "http://localhost:8080/api/auth/callback?code=abc123def456"

Resposta:

Token de acesso obtido com sucesso: pat-na1-12345678-1234-1234-1234-123456789abc

Gestão de Contatos

Criar novo contato

POST /api/contacts
Content-Type: application/json


curl -X POST "http://localhost:8080/api/contacts" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "exemplo@empresa.com",
    "firstName": "João",
    "lastName": "Silva",
    "phone": "+5511999999999"
  }'

{
  "id": "12345",
  "status": "CREATED",
  "message": "Contato criado no HubSpot"
}

 Webhooks

Receber notificação de novo contato

POST /api/webhooks/contact-creation
X-HubSpot-Signature: {assinatura_hmac}
Content-Type: application/json

curl -X POST "http://localhost:8080/api/webhooks/contact-creation" \
  -H "X-HubSpot-Signature: abc123def456" \
  -H "Content-Type: application/json" \
  -d '{
    "eventType": "contact.creation",
    "objectId": 12345,
    "properties": {
      "email": "novo@empresa.com",
      "firstname": "Maria"
    }
  }'


Respostas:

✅ Sucesso (200): "Evento processado com sucesso"

🔒 Inválido (401): "Assinatura HMAC inválida"

 Arquitetura e Segurança

graph TD
    A[Cliente] --> B[[API]]
    B --> C[(HubSpot)]
    B --> D[Rate Limiter]
    D -->|100 reqs/10s| C

Princípios implementados:
Separação clara de controllers/services/repositórios
Validação HMAC para webhooks
Rate limiting (Bucket4j)
Tratamento de erros detalhado

Dependências Principais
Biblioteca	Versão	Finalidade
Spring Boot	3.2.4	Framework base
Bucket4j	8.1.0	Limitação de requisições
Spring Security	6.2.4	Autenticação OAuth2

Testando a API
Via cURL (exemplos acima)

Postman:
Importe a coleção: HubSpot-API.postman_collection.json
Swagger UI:
http://localhost:8080/swagger-ui.html

Motivação:

Seguir as boas práticas do HubSpot
Simplicidade para o escopo do teste

Possíveis Melhorias:

Adicionar persistência de tokens
Implementar dashboard de métricas
Criar Dockerfile para deploy
