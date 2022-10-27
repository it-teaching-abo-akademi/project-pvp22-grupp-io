package pvp.models.interfaces;

import org.json.JSONObject;

public interface JSONSerializable {
    public static <T> T getObjectFromJson(JSONObject json) {
        return null;
    }

    public JSONObject getJsonOfObject();
}
