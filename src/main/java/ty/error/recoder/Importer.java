package ty.error.recoder;

import com.sagittarius.bean.common.ValueType;

import java.io.*;

public class Importer {
    String path = "metric.csv";

    public Importer(){
    }
    public Importer(String path){
        this.path = path;
    }
    public void register(){
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            tempString = reader.readLine();
            if(tempString == null){
                return;
            }
            while ((tempString = reader.readLine()) != null && line < 4436) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
                String [] strs = tempString.split(",");
                registerMetric(strs[0],strs[1]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public void registerMetric(String metricId, String typeInt){
        int typeId = Integer.parseInt(typeInt);
        ValueType valueType = getValueTypeByInt(typeId);
        MetaDataMap.put(metricId, valueType);
    }

    public ValueType getValueTypeByInt(int id){
        switch(id){
            case 1:
                return ValueType.INT;
            case 2:
                return ValueType.LONG;
            case 3:
                return ValueType.FLOAT;
            case 4:
                return ValueType.DOUBLE;
            case 5:
                return ValueType.BOOLEAN;
            default:
                return ValueType.STRING;
        }
    }
}
