package ru.finex.relay.command.network;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.redisson.api.RTopic;
import ru.finex.core.cluster.ClusterService;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.core.model.event.RelayEvent;
import ru.finex.network.netty.model.ClientSession;
import ru.finex.network.netty.model.NetworkDto;
import ru.finex.relay.service.ClientSessionService;

import javax.inject.Inject;

/**
 * Server notification command.
 * <p/>
 * This command are validated {@link RelayEvent DTO} and if validation is not break any violations,
 *  command notify subscribers by special topic name what resolved through {@link ClientSessionService session service}
 *  and {@link ru.finex.relay.service.TopicResolverService topic resolver}.
 * @param <T> payload implementation of {@link RelayEvent RelayEvent} and {@link NetworkDto NetworkDto}
 * @author m0nster.mind
 */
public class NotifyServer<T extends RelayEvent & NetworkDto> extends AbstractNetworkCommand {

    private final T dto;
    private final ClientSession session;

    private final Validator validator;
    private final ClientSessionService sessionService;
    private final RTopic topic;

    @Inject
    public NotifyServer(T dto, ClientSession session, Validator validator,
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
            StringBuilder sb = new StringBuilder("Fail to validate '").append(dto.toString()).append("', violations: \n");
            violations.forEach(e -> sb.append(" - ").append(e.getMessage()).append(": ").append(e.getPropertyPath()).append("\n"));

            throw new ConstraintViolationException(sb.toString(), violations);
        }

        dto.setClientSessionId(sessionService.getSessionId(session));
        topic.publish(dto);
    }

}
