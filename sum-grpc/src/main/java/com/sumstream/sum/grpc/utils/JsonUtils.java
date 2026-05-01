package com.sumstream.sum.grpc.utils;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.time.Instant;


public class JsonUtils {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .setPrettyPrinting().create();


    // Private Constructor so the class is not instantiated.
    private JsonUtils() {
    }

    /**
     * Serialize any object to JSON string
     * @param object The object to serialize
     * @return JSON string representation
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * Deserialize JSON string to object
     * @param json The JSON string
     * @param classOfT The target class
     * @return Deserialized object
     * @param <T> The type of object to return
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }


    /**
     * Pretty-print any object as formatted JSON
     *
     * @param object The object to format
     * @return Formatted JSON string
     */
    public static String toPrettyJson(Object object) {
        return gson.toJson(object);
    }


    /* Type Adapters for Java Time Classes */
    private static class InstantAdapter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        @Override
        public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return Instant.parse(json.getAsString());
        }
    }
}
