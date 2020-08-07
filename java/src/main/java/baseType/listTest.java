package baseType;

import java.util.ArrayList;
import java.util.List;

public class listTest {
    public static void main(String[] args) {
        List<String> l = new ArrayList<String>();
        for (int i = 0 ; i <= 9 ; i ++){
            l.add(""+i);
        }
        System.out.println(l.toString());
    }
}
