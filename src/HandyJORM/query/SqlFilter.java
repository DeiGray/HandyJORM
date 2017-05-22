package HandyJORM.query;


import HandyJORM.enums.ComparisonStrategyEnum;
import HandyJORM.enums.DBPropertyTypeEnum;
import HandyJORM.exception.InvalidDBPropertyTypeException;

/**
 * Created by DeiGray on 03/05/2017.
 * A Sql WHERE condition.
 *
 * Called a filter to make it user-friendly.
 *
 * @author DeiGray
 * @version 0.1
 * */

public class SqlFilter {
    private String colName;
    private DBPropertyTypeEnum colType;
    private Object value;
    private ComparisonStrategyEnum comparison;

    public SqlFilter(String colName, DBPropertyTypeEnum colType, Object value, ComparisonStrategyEnum comparison){
        this.colName = colName;
        this.colType = colType;
        this.value = value;
        this.comparison = comparison;
    }

    public String toSqlString() throws InvalidDBPropertyTypeException {
        return this.colName + this.comparison.toString() + SqlQueryUtils.toSqlValue(this.colType,this.value);
    }
}
