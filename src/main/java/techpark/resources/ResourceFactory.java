package techpark.resources;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.io.Resources;
import techpark.exceptions.ResourceException;
import techpark.game.avatar.Square;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Варя on 01.05.2017.
 */
@SuppressWarnings({"OverlyBroadCatchBlock", "SameParameterValue"})
public class ResourceFactory {

    private final ObjectMapper objectMapper;

    public ResourceFactory() {
        this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T extends Resource> T get(String path, Class<T> clazz) {
        final URL resourceDescriptor = getUrl(path);

        final T resource;
        try {
            resource = objectMapper.readValue(resourceDescriptor, clazz);
        } catch (IOException e) {
            throw new ResourceException("Failed constructing resource object " + path + " of type " + clazz.getName(), e);
        }

        if(!resource.getType().equals(clazz.getName())) {
            throw new ResourceException("type mismatch for resource " + path + ". Expected " + clazz.getName() +
                    " , but got " + resource.getType());
        }

        return resource;
    }

    private URL getUrl(String path) {
        try {
            return Resources.getResource(path);
        } catch (IllegalArgumentException ex) {
            throw new ResourceException("Unable to find resource " + path, ex);
        }
    }

    public HashMap jsonToMap(String path){
        final URL resourceDescriptor = getUrl(path);
        final HashMap resource;
        try {
           resource = objectMapper.readValue(resourceDescriptor, HashMap.class);
        } catch (IOException e) {
            throw new ResourceException("Failed constructing resource object " + path + " of type " +
                    HashMap.class.getName(), e);
        }
        return resource;
    }

    public RawResource getRaw(String path){
        final URL resourceDescriptor = getUrl(path);
        try {
            return objectMapper.readValue(resourceDescriptor, RawResource.class);
        } catch (IOException e) {
            throw new ResourceException("Unable to parse resource " + resourceDescriptor.getPath(), e);
        }
    }


    public List<Square> jsonToSquareList(String path){
        final URL resourceDescriptor = getUrl(path);
        final List<Square> resource;
        try {
            resource = objectMapper.readValue(resourceDescriptor,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,
                            Square.class));
        } catch (IOException e) {
            throw new ResourceException("Failed constructing resource object " + path + " of type " +
                    HashMap.class.getName(), e);
        }
        return resource;
    }
}
