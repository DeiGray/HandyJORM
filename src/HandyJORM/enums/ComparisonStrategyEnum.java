package HandyJORM.enums;

/**
 * Created by DeiGray on 03/05/2017.
 *
 * Kinds of sql comparison.
 *
 * @author DeiGray
 * @version 0.1
 */

public enum ComparisonStrategyEnum {
    Equals("="),
    Like("LIKE");

    private String name;

    ComparisonStrategyEnum(String name){
        this.name = name;
    }

    public String toString(){
        return this.name;
    }
}
