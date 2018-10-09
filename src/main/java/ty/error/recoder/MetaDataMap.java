package ty.error.recoder;

import com.sagittarius.bean.common.ValueType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MetaDataMap {
    private static Map<String, ValueType> map = new ConcurrentHashMap<String, ValueType>();

    public static void put(String k, ValueType v){
        map.put(k,v);
    }

    public static ValueType get(String k){
        return map.get(k);
    }
}
