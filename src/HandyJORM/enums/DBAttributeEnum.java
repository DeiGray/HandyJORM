package HandyJORM.enums;

/**
 * Created by DeiGray on 09/05/2017.
 *
 * Kinds of database fields type.
 *
 * @author DeiGray
 * @version 0.1
 */
public enum DBAttributeEnum {

    PrimaryKey("Primary Key"),
    ForeignKey("Foreign Key"),
    Attribute("Common Attribute"),
    PrimaryForeignKey("Primary and Foreign Key");


    private String _name;
    DBAttributeEnum(String name){
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }
}
