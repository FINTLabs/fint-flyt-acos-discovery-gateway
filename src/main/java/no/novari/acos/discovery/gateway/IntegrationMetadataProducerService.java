package no.novari.acos.discovery.gateway;

import lombok.extern.slf4j.Slf4j;
import no.novari.acos.discovery.gateway.model.fint.IntegrationMetadata;
import no.novari.kafka.model.ParameterizedProducerRecord;
import no.novari.kafka.producing.ParameterizedTemplate;
import no.novari.kafka.producing.ParameterizedTemplateFactory;
import no.novari.kafka.topic.name.EventTopicNameParameters;
import no.novari.kafka.topic.name.TopicNamePrefixParameters;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IntegrationMetadataProducerService {

    private final ParameterizedTemplate<IntegrationMetadata> template;
    private final EventTopicNameParameters formDefinitionEventTopicNameParameters;

    public IntegrationMetadataProducerService(ParameterizedTemplateFactory templateFactory) {
        this.template = templateFactory.createTemplate(IntegrationMetadata.class);
        this.formDefinitionEventTopicNameParameters = EventTopicNameParameters
                .builder()
                .topicNamePrefixParameters(TopicNamePrefixParameters
                        .stepBuilder()
                        .orgIdApplicationDefault()
                        .domainContextApplicationDefault()
                        .build()
                )
                .eventName("integration-metadata-received")
                .build();
    }

    public void publishNewIntegrationMetadata(IntegrationMetadata integrationMetadata) {
        template.send(
                ParameterizedProducerRecord
                        .<IntegrationMetadata>builder()
                        .topicNameParameters(formDefinitionEventTopicNameParameters)
                        .value(integrationMetadata)
                        .build()
        );
    }

}
