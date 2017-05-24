package techpark.websocket;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Варя on 17.04.2017.
 */
@SuppressWarnings({"NullableProblems", "unused"})
public class EventMessage {

    @NotNull
    private String type;
    @NotNull
    private String content;

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public String getContent() {
        return content;
    }

    public EventMessage() {
    }

    public EventMessage(@NotNull String type, @NotNull String content) {
        this.type = type;
        this.content = content;
    }

    public EventMessage(@NotNull Class clazz, @NotNull String content) {
        this(clazz.getName(), content);
    }
}
