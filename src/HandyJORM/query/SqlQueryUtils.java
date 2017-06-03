package HandyJORM.query;

import HandyJORM.enums.DBPropertyTypeEnum;
import HandyJORM.exception.InvalidDBPropertyTypeException;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by Fabien on 03/05/2017.
 *
 * Some utils query to create SQL String.
 *
 * @author DeiGray
 * @version 0.2
 */

 public class SqlQueryUtils {
    /**
     * Format values in String to use it in SQL.
     * @param type
     * @param value
     * @return
     * @throws InvalidDBPropertyTypeException
     */
     public static String toSqlValue(DBPropertyTypeEnum type, Object value) throws InvalidDBPropertyTypeException {
        String result = "";
        switch (type){
            case Integer :
                result += value.toString();
                break;
            case String:
                if(null != value)
                    result += "'" + value + "'";
                else
                    result+= "''";
                break;
            case Date:
                if(null != value)
                    result += "'"+value.toString()+"'";
                else
                    result+= "'"+(new Date(1l).toString())+"'";
                break;
            case Timestamp:
                if(null != value)
                    result += "'"+value.toString()+"'";
                else
                   result+= "'"+(new Timestamp(1l).toString())+"'";
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
