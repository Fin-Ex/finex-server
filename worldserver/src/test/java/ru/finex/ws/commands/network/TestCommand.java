package ru.finex.ws.commands.network;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import ru.finex.core.Version;
import ru.finex.core.command.AbstractNetworkCommand;
import ru.finex.ws.model.dto.InputTestDto;
import ru.finex.ws.model.dto.OutputTestDto;
import ru.finex.ws.network.GameSession;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Slf4j
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class TestCommand extends AbstractNetworkCommand {

    private final InputTestDto dto;
    private final GameSession session;

    @Override
    public void executeCommand() {
        log.info("Execute TestCommand: {}", dto.getVersion());

        Assertions.assertEquals(0x01, dto.getVersion());
        session.sendPacket(new OutputTestDto(Version.getImplVersion()));
    }

}
