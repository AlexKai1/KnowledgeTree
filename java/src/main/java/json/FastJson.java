package json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.avro.generic.GenericData;

import java.util.ArrayList;
import java.util.List;

public class FastJson {

    private void jsonArrTest() {
        Entry entry = new Entry();
        entry.setJsonArray(new JSONArray());
        System.out.println(entry.getJsonArray().isEmpty());
    }

    // 测试遍历json数组时候，往里面json对象添加字段，是否作用在原json数组上
    private void addItem(){
        JSONArray jsonArray = new JSONArray();
        for (int i = 0 ; i < 3 ; i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","alex");
            jsonObject.put("age",25);
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray);

        for (Object obj : jsonArray){
            JSONObject jsonObject = (JSONObject) obj;
            jsonObject.put("add","add");
        }

        System.out.println(jsonArray);
    }

    public static void main(String[] args) {
        FastJson fastJson = new FastJson();
        List<String> list = new ArrayList<String>();
        list.add("abc");
        list.add("test");
        System.out.println(list);
    }
}
