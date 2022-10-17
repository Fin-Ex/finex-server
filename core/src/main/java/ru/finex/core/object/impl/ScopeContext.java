package ru.finex.core.object.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.object.GameObject;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author m0nster.mind
 */
@NoArgsConstructor
class ScopeContext {

    private final Deque<GameObjectContext> queue = new LinkedList<>();

    public void enterScope(GameObject gameObject) {
        GameObjectContext ctx = queue.peekLast();
        if (ctx != null && ctx.getGameObject() == gameObject) {
            ctx.retain();
        } else {
            ctx = new GameObjectContext(gameObject);
            queue.offerLast(ctx);
        }
    }

    public void exitScope(GameObject gameObject) {
        GameObjectContext ctx = queue.peekLast();
        if (ctx == null) {
            throw new RuntimeException("Called exit scope without enter to scope!");
        }

        GameObject scopedGameObject = ctx.getGameObject();
        if (scopedGameObject != gameObject) {
            throw new RuntimeException(String.format(
                "Fail to exit from scope for rId:%d, pId:%d, scoped object rId:%d, pId:%d",
                gameObject.getRuntimeId(), gameObject.getPersistenceId(),
                scopedGameObject.getRuntimeId(), scopedGameObject.getPersistenceId()
            ));
        }

        if (ctx.free()) {
            queue.pollLast();
        }
    }

    public GameObject getScopedObject() {
        GameObjectContext ctx = queue.peekLast();
        if (ctx == null) {
            return null;
        }

        return ctx.getGameObject();
    }

    @Data
    static class GameObjectContext {
        private GameObject gameObject;
        private int usages;

        GameObjectContext(GameObject gameObject) {
            this.gameObject = gameObject;
            this.usages = 1;
        }

        public void retain() {
            usages++;
        }

        public boolean free() {
            return --usages < 1;
        }
    }

}
