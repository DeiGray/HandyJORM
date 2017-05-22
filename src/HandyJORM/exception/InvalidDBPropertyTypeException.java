package HandyJORM.exception;

/**
 * Created by Fabien on 04/05/2017.
 *
 * An error when testing DbPropertyType value.
 *
 * @author DeiGray
 * @version 0.1
 */
public class InvalidDBPropertyTypeException extends Exception {
    public InvalidDBPropertyTypeException(Object type) {
        super("No valid DBPropertyTypeEnum given."+'\n'+type.getClass().toString()+" given.");
    }
}
