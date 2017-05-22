package HandyJORM.exception;



import HandyJORM.model.ModelAttribute;

import java.lang.reflect.Field;

/**
 * Created by Fabien on 09/05/2017. *
 *
 * Error when trying to access an inconsistent field.
 *
 * @author DeiGray
 * @version 0.1
 */
public class UnknowFieldModelException extends Exception {
    public UnknowFieldModelException(Field field){
        super("Unknow type given for field :"+field.getName());
    }
    public UnknowFieldModelException(ModelAttribute ma){
        super("Unknow type given for attribute :"+ma.getFieldName());
    }

}
