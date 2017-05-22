package HandyJORM.session;

/**
 * On 21/05/2017.
 *
 * @author DeiGray
 * @version 0.1
 */
public class DBResult {
    private Object _columnValue;
    private String _columnName;

    /**
     * Casual constructor
     */
    public DBResult(String name,Object column){
        this._columnValue = column;
        this._columnName = name;
    }

    /*
     * getters
     */

    public Object getColumnValue(){
        return _columnValue;
    }

    public String getName(){
        return _columnName;
    }
}
