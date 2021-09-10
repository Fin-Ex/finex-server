package ru.finex.ws.concurrent.game;

import javax.inject.Singleton;

/**
 * FIXME Временное решение для теста.
 *
 * @author finfan
 */
@Singleton
public class GameTickServiceImpl {
	
//	private final WorldService worldService;
//	private final ScheduledFuture<?> tickTask;
//
//	@Inject
//	public GameTickServiceImpl(WorldService worldService, GameExecutorService executorService) {
//		this.worldService = worldService;
//		tickTask = executorService.execute(new RunnableGameTask(this::tick), 50, 50, TimeUnit.MILLISECONDS);
//	}
//
//	public void tick() {
//		List<Component> components = worldService.getGameObjects().stream()
//			.flatMap(e -> e.getComponents().stream())
//			.sorted(Component.COMPARATOR)
//			.collect(Collectors.toList());
//
//		GameWorkerThread thread = (GameWorkerThread) Thread.currentThread();
//		doUpdate(thread, components, Component::onPreUpdate);
//		doUpdate(thread, components, Component::onUpdate);
//		doUpdate(thread, components, Component::onPostUpdate);
//	}
//
//	private void doUpdate(GameWorkerThread thread, List<Component> components, Consumer<Component> consumer) {
//		for (int i = 0; i < components.size(); i++) {
//			Component component = components.get(i);
//
//			GameObjectImpl gameObject = component.getGameObject();
//			thread.setGameObject(gameObject);
//
//			ClientComponent clientComponent = gameObject.getComponent(ClientComponent.class);
//			if (clientComponent != null) {
//				Client client = clientComponent.getClient();
//				thread.setClient(client);
//			}
//
//			consumer.accept(component);
//		}
//	}
	
}
