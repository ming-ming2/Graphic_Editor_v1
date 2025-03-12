package command;

import java.util.HashMap;
import java.util.Map;

import global.ContextKey;

public class CommandContext {
    private Map<ContextKey, Object> commandContexts = new HashMap<>();

    public void put(ContextKey key, Object value) {
        commandContexts.put(key, value);
    }

    public Object get(ContextKey key) {
        return commandContexts.get(key);
    }

    public <T> T get(ContextKey key, Class<T> type) {
        Object value = commandContexts.get(key);
        if (value == null) {
            return null;
        }
        return type.cast(value);
    }
}
