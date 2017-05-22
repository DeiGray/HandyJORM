package HandyJORM.exception;

/**
 * @author DeiGray
 *         on 21/05/2017.
 */
public class InvalidFieldNameGiven extends Exception {
    public InvalidFieldNameGiven(String invalidFieldName){
        super("Given field name : " + invalidFieldName + " is not a correct field name.");
    }
}
