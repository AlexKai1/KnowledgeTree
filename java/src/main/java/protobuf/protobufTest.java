package protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import protobuf.protoClass.PersonModel;

import java.util.HashMap;


class protobufTest {
    public void testN() throws InvalidProtocolBufferException {
        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setId(1);
        builder.setName("jihite");
        builder.setEmail("jihite@jihite.com");

        PersonModel.Person person = builder.build();
        System.out.println("before:" + person);

        System.out.println("===Person Byte:");
        for (byte b : person.toByteArray()) {
            System.out.print(b);
        }
        System.out.println("================");

        byte[] byteArray = person.toByteArray();
        PersonModel.Person p2 = PersonModel.Person.parseFrom(byteArray);
        System.out.println("after id:" + p2.getId());
        System.out.println("after name:" + p2.getName());
        System.out.println("after email:" + p2.getEmail());

    }

    // 执行解析
    public void yukwTest() throws InvalidProtocolBufferException {
        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setId(2);
        builder.setName("alex");
        builder.setEmail("alex5634@163.com");
        PersonModel.Person person = builder.build();
        byte[] bytes = person.toByteArray();

        HashMap<String, String> outPutMap = new HashMap<String, String>();
        System.out.println("old = " + PersonModel.Person.parseFrom(bytes).toString());
        String[] strArr = PersonModel.Person.parseFrom(bytes).toString().split("\n");
        if (strArr.length > 0) {
            for (String str : strArr) {
                String[] kv = str.split(":");
                outPutMap.put(kv[0], removeQuotationMarks(kv[1]));
            }
        }
        System.out.println(outPutMap);
    }

    // 清除携带的引号
    public String removeQuotationMarks(String input) {
        if (input.split("\"").length > 1) {
            input = input.split("\"")[1];
        }
        return input.trim();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        protobufTest t = new protobufTest();
        t.yukwTest();

    }
}