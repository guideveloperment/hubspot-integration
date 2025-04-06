# hubspot-integration
"API de integração com HubSpot"

HubSpot Integration API

Conta de Desenvolvedor: VianaDev Aplicação: HubSpot Integration API

Passos realizados na conta VianaDev: Criação do App

Criação do App Acessado o Portal de Desenvolvedores → "Create app"

Nome do app: HubSpot Integration API

Domínio: http://localhost:8080 (para desenvolvimento)

Configurações OAuth

Redirect URIs:

Copy http://localhost:8080/api/auth/callback Scopes:

contacts (leitura/escrita)

content (leitura)

Webhooks

Evento assinado: contact.creation

Endpoint: http://localhost:8080/api/webhooks/contact-creation

Secret key: [CHAVE_SECRETA_GERADA_NO_HUBSPOT]

Decisões Técnicas Fluxo OAuth 2.0 Implementação:
Biblioteca spring-security-oauth2-client

Fluxo authorization_code padrão do HubSpot

Tokens armazenados em memória (para demonstração)

Motivação:

Seguir as boas práticas do HubSpot

Simplicidade para o escopo do teste

Melhorias futuras:

Persistência em banco de dados (ex: Redis)

Rotação automática de tokens

Webhooks Validação de assinatura:

HMAC-SHA256 com a chave secreta da conta VianaDev

Suporte às versões v1 e v3 do HubSpot

Exemplo de implementação:

java Copy private boolean isValidSignature(String payload, String signature) { String computedSignature = hmacSHA256(payload, webhookSecret); return constantTimeEquals(computedSignature, signature); } Rate Limiting Configuração (baseada nos limites da VianaDev):

properties Copy

application.properties
rate-limiter.capacity=100 # Limite do HubSpot (100 requests/10s) rate-limiter.refill-amount=10 # Tokens recarregados a cada 10 segundos 3. Bibliotecas Utilizadas Biblioteca Versão Motivo Spring Boot 3.2.4 Framework principal Bucket4j 8.1.0 Rate limiting Lombok 1.18.30 Redução de boilerplate Spring Security 6.2.4 Segurança/OAuth 4. Instruções para Execução Pré-requisitos Credenciais da VianaDev no application-dev.properties:

properties Copy hubspot.client.id=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
hubspot.client.secret=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
hubspot.webhook.secret=seu_secret_aqui
Comandos: bash Copy mvn spring-boot:run -Dspring-boot.run.profiles=dev 5. Melhorias Futuras Para produção:

Substituir localhost por URL pública (ex: ngrok durante testes)

Adicionar IP whitelisting no HubSpot

Monitoramento:

Logs das chamadas à API HubSpot

Métricas com Prometheus

Segurança reforçada:

Criptografia das credenciais (ex: Jasypt)

Referências Documentação Oficial HubSpot
Dashboard da Aplicação VianaDev

