package ru.finex.ws.concurrent.game;

import lombok.Getter;
import ru.finex.core.model.GameObject;
import ru.finex.ws.model.Client;

import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author finfan
 */
public class GameScheduledFutureTask<V> implements RunnableScheduledFuture<V> {
	
	private final RunnableScheduledFuture<V> runnableScheduledFuture;
	
	@Getter
	private final Client client;
	
	@Getter
	private final GameObject gameObject;
	
	public GameScheduledFutureTask(RunnableScheduledFuture<V> runnableScheduledFuture, Client client, GameObject gameObject) {
		this.runnableScheduledFuture = runnableScheduledFuture;
		this.client = client;
		this.gameObject = gameObject;
	}
	
	@Override
	public boolean isPeriodic() {
		return runnableScheduledFuture.isPeriodic();
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		return runnableScheduledFuture.getDelay(unit);
	}
	
	@Override
	public int compareTo(Delayed o) {
		return runnableScheduledFuture.compareTo(o);
	}
	
	@Override
	public void run() {
		runnableScheduledFuture.run();
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return runnableScheduledFuture.cancel(mayInterruptIfRunning);
	}
	
	@Override
	public boolean isCancelled() {
		return runnableScheduledFuture.isCancelled();
	}
	
	@Override
	public boolean isDone() {
		return runnableScheduledFuture.isDone();
	}
	
	@Override
	public V get()
		throws InterruptedException, ExecutionException {
		return runnableScheduledFuture.get();
	}
	
	@Override
	public V get(long timeout, TimeUnit unit)
		throws InterruptedException, ExecutionException, TimeoutException {
		return runnableScheduledFuture.get(timeout, unit);
	}
}
