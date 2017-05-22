package HandyJORM.enums;

/**
 * Created by DeiGray on 10/05/2017.
 *
 * Kind of orderBy (sql) parameters.
 *
 * @author DeiGray
 * @version 0.1
 */
public enum DirectionOrderEnum {
    ASC("ASC"),
    DESC("DESC");
    private String _name;
    DirectionOrderEnum(String name){
        _name = name;
    }

    @Override
    public String toString() {
        return _name;
    }
}
