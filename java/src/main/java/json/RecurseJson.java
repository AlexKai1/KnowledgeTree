package json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.avro.generic.GenericData;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class RecurseJson {
    /**
     * 初始化JSONArr
     *
     * @return
     */
    private JSONArray initJsonArr() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 5; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("row", "20200721");
            jsonObject.put("id", i);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 克隆JSONArr
     *
     * @param jsonArray
     * @return
     */
    public JSONArray cloneJsonArr(JSONArray jsonArray) {
        JSONArray outputJsonArr = new JSONArray();
        for (Object obj : jsonArray) {
            JSONObject x = JSON.parseObject(JSON.toJSONString(obj));
            outputJsonArr.add(x);
        }
        return outputJsonArr;
    }

    /**
     * 为JSONArr添加单一的列与列值
     *
     * @param jsonArray
     * @param fieldName
     * @param value
     * @return
     */
    private JSONArray appendSingleValue(JSONArray jsonArray, String fieldName, Object value) {
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            jsonObject.put(fieldName, value);
        }
        return jsonArray;
    }

    /**
     * 为JSONArr添加多个列值
     *
     * @param jsonArray
     * @param fieldName
     * @param inputValue
     * @return
     */
    private JSONArray appendMultiValue(JSONArray jsonArray, String fieldName, List<Object> inputValue) {
        JSONArray outJsonArr = new JSONArray();
        for (Object key : inputValue) {
            JSONArray tempJsonArr = cloneJsonArr(jsonArray);
            appendSingleValue(tempJsonArr, fieldName, key);
            outJsonArr.addAll(tempJsonArr);
        }
        return outJsonArr;
    }

    /**
     * 提供测试方法
     *
     * @param args
     */
    public static void main(String[] args) {
        RecurseJson j = new RecurseJson();
        JSONArray jsonArray = j.initJsonArr();
        System.out.println(j.appendSingleValue(jsonArray, "name", "alex"));

        List<Object> listValue = new ArrayList<Object>();
        for (int i = 0; i < 10; i++) {
            listValue.add(i * i);
        }
        System.out.println(j.appendMultiValue(jsonArray, "result", listValue));
    }
}