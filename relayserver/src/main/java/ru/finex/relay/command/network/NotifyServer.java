package ru.finex.relay.command.network;

import jakarta.validation.Validator;
import org.redisson.api.RTopic;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.relay.network.RelayNetworkDto;
import ru.finex.relay.service.ClientSessionService;

import javax.inject.Inject;

/**
 * Server notification command.
 * <p>This command are validated {@link RelayNetworkDto DTO} and if validation is not break any violations,
 *  command notify subscribers by special topic name what resolved through {@link ClientSessionService session service}
 *  and {@link ru.finex.relay.service.TopicResolverService topic resolver}.
 * @author m0nster.mind
 */
public class NotifyServer extends AbstractNetworkCommand {

    private final RelayNetworkDto dto;
    private final ClientSession session;

    private final Validator validator;
    private final ClientSessionService sessionService;
    private final RTopic topic;

    @Inject
    public NotifyServer(RelayNetworkDto dto, ClientSession session, Validator validator,
        ClusterService clusterService, ClientSessionService sessionService) {
        this.dto = dto;
        this.session = session;
        this.validator = validator;
        this.sessionService = sessionService;
        this.topic = clusterService.getClient().getTopic(sessionService.getServerTopic(session, dto));
    }

    @Override
    public void executeCommand() {
        var violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Fail to validate '")
                .append(dto.toString())
                .append("', violations: \n");
            violations.forEach(e -> sb.append(" - ").append(e.getMessage()).append("\n"));

            throw new RuntimeException(sb.toString());
        }

        dto.setClientSessionId(sessionService.getSessionId(session));
        topic.publish(dto);
    }

}
