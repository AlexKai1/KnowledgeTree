package json;

import com.alibaba.fastjson.JSONArray;

public class Entry {
    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    private JSONArray jsonArray;

}
