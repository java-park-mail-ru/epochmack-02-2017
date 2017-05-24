package techpark.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Варя on 01.05.2017.
 */
public abstract class Resource {

    private String type;

    public Resource() {
    }

    public Resource(@JsonProperty("type") String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}