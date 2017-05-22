package HandyJORM.exception;

/**
 * Created by DeiGray on 11/05/2017.
 *
 * Illegal use of a method in a specific sql query context.
 * Ex : SoftQuery sq = new SoftQuery(...);
 *      sq.from(...).select(...);
 *
 * @author DeiGray
 * @version  0.1
 */
public class IllegalSoftQueryException extends Exception{
    public IllegalSoftQueryException(String methodName, String sqlState) {
        super("Nonsense using of method "+methodName+" in mode"+sqlState);
    }
}
