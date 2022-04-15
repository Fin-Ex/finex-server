package ru.finex.core.command;

/**
 * Единый интерфейс исполнения команд.
 * Команды вызываются (как правило) из так называемой "очереди команд".
 * Пример команды:
 * <pre>{@code
 *  @NetworkCommandScoped
 *  @RequiredArgsConstructor(onConstructor_ = { @Inject })
 *  public class TestCommand extends AbstractNetworkCommand {
 *
 *     private final TestDto dto;
 *
 *     @Override
 *     public void executeCommand() {
 *         log.info("Execute TestCommand: {}", dto.getVersion());
 *     }
 *
 *  }
 * }</pre>
 * Где {@code TestDto} это и есть данные для исполнения команды.
 * Аннотация {@code NetworkCommandScoped} отвечает за скоуп (контекст инжектирования).
 *
 * @author m0nster.mind
 */
public interface Command {

    /**
     * Execute command.
     */
    void executeCommand();

}
