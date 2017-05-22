package HandyJORM.query;

import HandyJORM.enums.DBPropertyTypeEnum;
import HandyJORM.exception.InvalidDBPropertyTypeException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Fabien on 03/05/2017.
 *
 * Some utils query to create SQL String.
 */

 class SqlQueryUtils {
    /**
     * Format values in String to use it in SQL.
     * @param type
     * @param value
     * @return
     * @throws InvalidDBPropertyTypeException
     */
     static String toSqlValue(DBPropertyTypeEnum type, Object value) throws InvalidDBPropertyTypeException {
        String result = "";
        switch (type){
            case Integer :
                result += value.toString();
                break;
            case String:
                result += "'" + value + "'";
                break;
            case Date:
                result += value.toString();
                break;
            case Timestamp:
                result += value.toString();
                break;
            default:
                throw new InvalidDBPropertyTypeException(type);
        }
        return result;
    }

    static Object getDBValueByType(DBPropertyTypeEnum type, ResultSet cursor,String colLabel) throws SQLException, InvalidDBPropertyTypeException {
        Object res;
        switch (type){
            case Integer :
                res = cursor.getInt(colLabel);
                break;
            case String :
                res = cursor.getString(colLabel);
                break;
            case Date :
                res = cursor.getDate(colLabel);
                break;
            case Timestamp :
                res = cursor.getTimestamp(colLabel);
                break;
            default :
                throw new InvalidDBPropertyTypeException(type);
        }
        return res;
    }
}
