package HBase;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.sql.execution.columnar.DOUBLE;

import java.io.IOException;


// create 'dw:table_interface_info','inf'
//
public class HBaseCommonTest {
    HBaseConfiguration hBaseConfiguration = new HBaseConfiguration();

    public void putTest() throws IOException {
        JSONObject jsonobj = new JSONObject();
        jsonobj.put("key", "value");
        String json = jsonobj.toJSONString();

        Connection connection = ConnectionFactory.createConnection(hBaseConfiguration);
        Table table = connection.getTable(TableName.valueOf("dw:table_interface_info"));
        Put put = new Put(Bytes.toBytes("gt_dw:ykw_test"));
//        put.addColumn(Bytes.toBytes("inf"), Bytes.toBytes("update_user"), Bytes.toBytes("alex"));
        put.addColumn(Bytes.toBytes("inf"), Bytes.toBytes("template"), Bytes.toBytes(json));
        table.put(put);

    }

    public void getTest() throws IOException {
        Connection connection = ConnectionFactory.createConnection(hBaseConfiguration);
        Table table = connection.getTable(TableName.valueOf("dw:table_interface_info"));
        Get get = new Get(Bytes.toBytes("gt_dw:ykw_test"));
        Result result = table.get(get);
        for (Cell cell : result.rawCells()) {
            String k = Bytes.toString(CellUtil.cloneQualifier(cell));
            String v = Bytes.toString(CellUtil.cloneValue(cell));
            if (k.equals("tamplate")) {
                JSONObject o = JSONObject.parseObject(v);
                System.out.println(o.get("key"));
                System.out.println("=====");
            }
            System.out.println(k);
            System.out.println(v);
        }


    }

    public <T> T convert(byte a, Class<T> clazz) {

        return clazz.cast(a);
    }

    public static void main(String[] args) throws IOException {
        HBaseCommonTest commonTest = new HBaseCommonTest();
        String t = "20200725";
        System.out.println(t.substring(1));
        System.out.println(String.join("|","1","2","3"));
    }
}


