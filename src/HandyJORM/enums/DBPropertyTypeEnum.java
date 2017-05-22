package HandyJORM.enums;

/**
 * Created by DeiGray on 03/05/2017.
 *
 * Kind of ModelAttribute variables type supported.
 *
 * @author DeiGray
 * @version 0.1
 */

public enum DBPropertyTypeEnum {
    Integer("int"),
    String("string"),
    Date("date"),
    Timestamp("timestamp"),
    Double("double");

    private String name;

    DBPropertyTypeEnum(String name){
        this.name = name;
    }
    public String toString(){
        return this.name;
    }
}
