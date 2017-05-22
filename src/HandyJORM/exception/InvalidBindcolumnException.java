package HandyJORM.exception;

/**
 * Created by Fabien on 11/05/2017.
 *
 * Method bindColumns (BaseModel) error.
 *
 * @author DeiGray
 * @version 0.1
 */
public class InvalidBindcolumnException extends Exception {
    public InvalidBindcolumnException(){
        super("method bindColumns from model isn't valid");
    }
}
