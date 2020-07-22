import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JavaCommonTest {

    /**
     * Recursive 递归
     */
    public static void recursive(int num, List<Integer> arr) {
        num = num / 2;
        if (num > 0) {
            arr.add(num);
            recursive(num, arr);
        } else {
            System.out.println(num);
            System.out.println(arr);
        }

    }

    private JSONArray parseTemplate(JSONArray jsonArray, JSONObject template) {
        JSONArray jsArray = new JSONArray();
        return jsArray;
    }


    public static void main(String[] args) {
//        List<Integer> arr = new ArrayList();
//        recursive(100,arr);
        String value = "".trim();
        System.out.println(value.equals(""));

    }
}
