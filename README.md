# FINT Flyt ACOS Discovery Gateway

Spring Boot WebFlux service that ingests ACOS form definitions from external clients, validates each structure, maps it
to Flyt `IntegrationMetadata`, and emits the resulting metadata onto Kafka for downstream discovery flows. It runs as an
OAuth2-protected gateway, enforces source-application access control, and reuses Flyt Kafka/topic tooling so new
integrations can be registered without touching downstream archive services.

## Highlights

- **External ACOS metadata ingress** — WebFlux controller under `/api/external/acos/metadata` accepts ACOS form
  definitions, authenticates/authorizes the caller, and streams the request through the validator + mapper pipeline.
- **Rich metadata mapping** — `AcosFormDefinitionMapper` turns steps, groups, and elements into Flyt
  `InstanceMetadataContent`, automatically generating value/object metadata and shared attachment structures.
- **Payload safeguards** — `AcosFormDefinitionValidator` combines Bean Validation with duplicate element ID detection so
  malformed forms surface as `422 Unprocessable Entity` responses with detailed error messages.
- **Kafka-first propagation** — `IntegrationMetadataProducerService` builds topic names via
  `EventTopicNameParameters`/`TopicNamePrefixParameters` and publishes integration metadata as a record other Flyt
  services can consume immediately.
- **Source-application enforcement** — `SourceApplicationAuthorizationService` extracts the caller’s source application
  ID from OAuth2 credentials so each published metadata item automatically references the trusted source.

## Architecture Overview

| Component                              | Responsibility                                                                                                                                  |
|----------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------|
| `AcosIntegrationMetadataController`    | Hosts the POST endpoint, logs incoming definitions, resolves the authenticated principal, and orchestrates validation → mapping → publishing.   |
| `AcosFormDefinitionValidator`          | Uses Jakarta Bean Validation plus duplicate ID detection to protect the system against malformed ACOS payloads.                                 |
| `AcosFormDefinitionMapper`             | Converts ACOS metadata (steps, groups, elements) into Flyt `IntegrationMetadata` objects with standardized PDF/attachment metadata structures.  |
| `IntegrationMetadataProducerService`   | Creates Kafka templates, resolves the `integration-metadata-received` topic with Flyt prefix conventions, and publishes metadata records.       |
| `IntegrationMetadata` model hierarchy  | Represents the Flyt metadata contract (`InstanceMetadataContent`, categories, value/object metadata) that downstream services rely on.          |
| `SourceApplicationAuthorizationService`| Provided by Flyt resource-server; ensures the gateway tags every integration with the caller’s source application ID for traceability.          |

## HTTP API

Base path: `/api/external/acos/metadata`

| Method | Path | Description | Request body | Response |
|--------|------|-------------|--------------|----------|
| `POST` | `/`  | Validates an ACOS form definition, converts it to Flyt `IntegrationMetadata`, and publishes it on Kafka. | `AcosFormDefinition` JSON (metadata + steps) | `202 Accepted` when the payload is queued, `401/403` on auth failures, `422 Unprocessable Entity` when validation fails. |

Example `AcosFormDefinition` payload:

```json
{
  "metadata": {
    "formId": "SKJEMA-488",
    "formDisplayName": "Søknad om barnehageplass",
    "formUri": "https://acos.example.com/forms/488",
    "version": 3
  },
  "steps": [
    {
      "displayName": "Foresatt",
      "groups": [
        {
          "displayName": "Personopplysninger",
          "elements": [
            { "id": "firstName", "displayName": "Fornavn", "type": "string" },
            { "id": "lastName", "displayName": "Etternavn", "type": "string" }
          ]
        }
      ]
    }
  ]
}
```

Validation errors return a JSON list of field-level issues and duplicate element IDs. Successful requests respond with
`202 Accepted` and an empty body because processing continues asynchronously on Kafka.

## Kafka Integration

- `IntegrationMetadataProducerService` builds a `ParameterizedTemplate<IntegrationMetadata>` using Flyt’s Kafka tooling.
- Events are published to the `integration-metadata-received` topic name, automatically prefixed with the organization
  and domain context from `TopicNamePrefixParameters`.
- The produced payload equals the mapped `IntegrationMetadata`, which downstream Flyt discovery components consume to
  register/update integrations without direct ACOS access.
- Kafka consumer/producer defaults (bootstrap servers, group IDs, topic replication) are set via the shared Flyt
  profiles so deployments inherit the standard cluster configuration.

## Scheduled Tasks

The gateway is entirely event-driven: the only work happens inside the HTTP request cycle and the Kafka producer.
There are no scheduled jobs or background cron tasks.

## Configuration

Spring profiles automatically include `flyt-kafka`, `flyt-logging`, and `flyt-resource-server`. Enable the optional
`local-staging` profile for local development overrides.

| Property | Description |
|----------|-------------|
| `fint.application-id` | Shared identifier that scopes Kafka group IDs and topic prefixes. |
| `novari.kafka.topic.domain-context` | Domain context used when building topic names (defaults to `flyt`). |
| `novari.kafka.topic.org-id` / `novari.kafka.default-replicas` | Local-staging overrides for organization/topic replication. |
| `spring.kafka.bootstrap-servers` | Kafka bootstrap URL (localhost in `local-staging`). |
| `novari.flyt.resource-server.security.api.external.enabled` | Toggles the external API surface. |
| `novari.flyt.resource-server.security.api.external.authorized-source-application-ids` | Whitelisted source application IDs permitted to call the endpoint. |
| `server.port` | Overridden to `8201` in `local-staging`. |
| `management.endpoints.web.exposure.include` | Exposes `health`, `info`, and `prometheus` actuator endpoints via the logging profile. |

Secrets referenced by Kustomize overlays must provide OAuth2 client credentials and any environment-specific Kafka
values.

## Running Locally

Prerequisites:

- Java 25+
- Docker (for local Kafka) or access to a dev cluster

Helpful commands:

```shell
./gradlew clean build
./gradlew test
SPRING_PROFILES_ACTIVE=local-staging ./gradlew bootRun
```

The `local-staging` profile expects Kafka on `localhost:9092`, binds HTTP to port `8201`, and keeps the external API
secured. Supply OAuth2 tokens from the configured IdP or temporarily relax checks only for isolated testing.

## Deployment

Kustomize layout:

- `kustomize/base/` — Contains the canonical `flais.yaml` Application manifest and shared resources.
- `kustomize/overlays/<org>/<env>/` — Apply namespace, ingress, env var, and scaling specifics per organization/env.

When configuration defaults change (image tags, env vars, OAuth clients), update the base and propagate overlays so CI/CD
pipelines render the correct manifests.

## Security

- OAuth2 resource server validates JWTs (issuer configured via Flyt defaults); invalid tokens receive `401`.
- `SourceApplicationAuthorizationService` cross-checks the caller’s source application ID with
  `authorized-source-application-ids` before any metadata is accepted.
- No metadata is persisted locally; instead, Kafka provides the audit trail and downstream consumers handle storage.

## Observability & Operations

- Liveness/readiness: `GET /actuator/health`
- Prometheus metrics: `GET /actuator/prometheus`
- Structured logging provided by the Flyt logging profile (JSON-friendly, correlation IDs enabled).
- Increase/decrease request logging by adjusting `logging.level.*` properties per environment.

## Development Tips

- Add/adjust validation rules in `AcosFormDefinitionValidator` and cover them with mapper/validator tests.
- Extend `AcosFormDefinitionMapper` when ACOS introduces new element types or metadata fields; update
  `AcosFormDefinitionMapperTest` accordingly.
- Reuse `IntegrationMetadata` builders when adding optional metadata so the Kafka payload stays backward compatible.
- Ensure any new endpoints continue to use `SourceApplicationAuthorizationService` for consistent auditing.

## Contributing

1. Create a feature branch.
2. Run `./gradlew test` (and any relevant integration checks) before opening a PR.
3. Update Kustomize overlays if deployment configuration changes.
4. Include tests or test adjustments that cover your change.

FINT Flyt ACOS Discovery Gateway is maintained by the FINT Flyt team. Reach out via the internal Slack channel or open
an issue in this repository for questions and feature requests.
