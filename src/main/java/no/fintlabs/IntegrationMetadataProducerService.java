package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.kafka.event.EventProducer;
import no.fintlabs.kafka.event.EventProducerFactory;
import no.fintlabs.kafka.event.EventProducerRecord;
import no.fintlabs.kafka.event.topic.EventTopicNameParameters;
import no.fintlabs.model.fint.IntegrationMetadata;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IntegrationMetadataProducerService {

    private final EventProducer<IntegrationMetadata> formDefinitionEventProducer;
    private final EventTopicNameParameters formDefinitionEventTopicNameParameters;

    public IntegrationMetadataProducerService(EventProducerFactory eventProducerFactory) {
        this.formDefinitionEventProducer = eventProducerFactory.createProducer(IntegrationMetadata.class);
        this.formDefinitionEventTopicNameParameters = EventTopicNameParameters.builder()
                .eventName("integration-metadata-received")
                .build();
    }

    public void publishNewIntegrationMetadata(IntegrationMetadata integrationMetadata) {
        formDefinitionEventProducer.send(
                EventProducerRecord.<IntegrationMetadata>builder()
                        .topicNameParameters(formDefinitionEventTopicNameParameters)
                        .value(integrationMetadata)
                        .build()
        );
    }

}
