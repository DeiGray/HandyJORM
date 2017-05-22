package HandyJORM.query;


import HandyJORM.enums.DirectionOrderEnum;

/**
 * Created by DeiGray on 10/05/2017.
 *
 * A Sql ORDER BY state.
 *
 * @author DeiGray
 * @version 0.1
 */
public class SqlOrder {
    private String _colName;
    private DirectionOrderEnum _direction;

    public SqlOrder(String colName,DirectionOrderEnum dir){
        _colName = colName;
        _direction = dir;
    }

    public String toSqlString(){
        return _colName + " " + _direction.toString();
    }

}
