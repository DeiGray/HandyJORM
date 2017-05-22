package HandyJORM.model;


import HandyJORM.enums.DBAttributeEnum;
import HandyJORM.enums.DBPropertyTypeEnum;
import HandyJORM.exception.InvalidBindcolumnException;
import HandyJORM.exception.InvalidFieldNameGiven;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.session.DBResult;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by DeiGray on 05/05/2017.
 *
 * Contains useful methods to easily create a ModelAttribute.
 *
 * @author DeiGray
 * @version 0.1
 */
public class ModelAttributeBuilder {

    public final static String PRIMARY_KEY_SUFFIX = "_PK";
    public final static String ATTRIBUTE_KEY_SUFFIX = "_AT";
    public final static String FOREIGN_KEY_SUFFIX = "_FK";
    public final static String PRIMARY_FOREIGN_KEY_SUFFIX = "_PFK";



    /**
     * Search patterns of those : ..._PK, ..._AT , ..._FK, ..._PFK
     * @return true th field name given is an attribute name.
     */
    public static ModelAttribute initModelAttributes(Object obj , Field field,String[][] bindColumns){
        try {
            if (field.getName().endsWith(PRIMARY_KEY_SUFFIX)) {
                return new ModelAttribute(obj,initAttribute(obj, field),field, DBAttributeEnum.PrimaryKey,findColName(field.getName(),bindColumns));
            }else if (field.getName().endsWith(FOREIGN_KEY_SUFFIX ))
                return new ModelAttribute(obj,initAttribute(obj, field),field,DBAttributeEnum.ForeignKey,findColName(field.getName(),bindColumns));
            else if (field.getName().endsWith(PRIMARY_FOREIGN_KEY_SUFFIX ))
                return new ModelAttribute(obj,initAttribute(obj, field),field,DBAttributeEnum.PrimaryForeignKey,findColName(field.getName(),bindColumns));
            else if (field.getName().endsWith(ATTRIBUTE_KEY_SUFFIX ) )
                return new ModelAttribute(obj,initAttribute(obj, field),field,DBAttributeEnum.Attribute,findColName(field.getName(),bindColumns));
            else
                return null;
        }
        catch(IllegalAccessException ie){
            ie.printStackTrace();
            return null;
        }catch (UnknowFieldModelException me){
            me.printStackTrace();
            return null;
        } catch (InvalidBindcolumnException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Create and return a filled model with data given.
     * @param newModel
     * @param data
     * @param <T>
     * @throws IllegalAccessException
     */
    public static <T extends BaseModel> void  affectDataToModel(T newModel, ArrayList<DBResult> data) throws IllegalAccessException, InvalidFieldNameGiven {
        if(data.size() > 0) {
            ModelAttributesCollection mc = newModel._modelAttributes;
            for (int i = 0; i < data.size() - 1; i++) {
                Field field = mc.getFieldByColName(data.get(i).getName());
                field.setAccessible(true);
                field.set(newModel,data.get(i).getColumnValue());
            }
        }
    }

    /**
     * Catch the field type and return true if he is in his default value.
     */
    public static boolean isDefaultValue(Object o,Field field) throws UnknowFieldModelException, IllegalAccessException {
        if(null != field && field.isAccessible()){
            if (field.getType().isAssignableFrom(int.class)) {
                return ((int) field.get(o)) == -1;
            }
            else if (field.getType().isAssignableFrom(String.class)) {
                return field.get(o) == null;
            }
            else if (field.getType().isAssignableFrom(Date.class)){
                return field.get(o) == null;
            }
            else if (field.getType().isAssignableFrom(Timestamp.class)) {
                return field.get(o) == null;
            }
            else if (field.getType().isAssignableFrom(double.class)){
                return ((double) field.get(o) == -1D);
            }
            else
                throw new UnknowFieldModelException(field);
        }
        else{
            if(field == null)
                throw new NullPointerException("null field founded");
            else
                throw new IllegalAccessException("Impossible to access to the field");
        }
    }

    /**
     * Give the colName in DB of the given fieldName.
     * @param fieldName a fieldName.
     * @param bindColumns directly the bindColumns from the fieldname model given.
     * @return a DB table column name.
     * @throws InvalidBindcolumnException
     */
    private static String findColName(String fieldName,String[][] bindColumns) throws InvalidBindcolumnException {
        String colName = null;
        try {
                for (String[] col :
                    bindColumns) {
                    if(col[1].equals(fieldName))
                        colName =  col[0];
                }
        }catch (Exception e){
            throw new InvalidBindcolumnException();
        }
        finally {
            return colName;
        }
    }

    /**
     * Fill a field with the corresponding type default value.
     * @param o the model from the field given
     * @param field the field to fill
     * @return
     * @throws IllegalAccessException
     * @throws UnknowFieldModelException
     */
    private static DBPropertyTypeEnum initAttribute(Object o,Field field) throws IllegalAccessException, UnknowFieldModelException {
        if(null != field && field.isAccessible()){
            if (field.getType().isAssignableFrom(int.class)) {
                field.setInt(o, -1);
                return DBPropertyTypeEnum.Integer;
            }
            else if (field.getType().isAssignableFrom(String.class)) {
                field.set(o, null);
                return DBPropertyTypeEnum.String;
            }
            else if (field.getType().isAssignableFrom(Date.class)){
                field.set(o,null);
                return DBPropertyTypeEnum.Date;
            }
            else if (field.getType().isAssignableFrom(Timestamp.class)) {
                field.set(o, null);
                return DBPropertyTypeEnum.Timestamp;
            }
            else if (field.getType().isAssignableFrom(double.class)){
                field.set(o,-1D);
                return DBPropertyTypeEnum.Double;
            }
            else
               throw new UnknowFieldModelException(field);
        }
        else{
            if(field == null)
                throw new NullPointerException("null field founded");
            else
                throw new IllegalAccessException("Impossible to access to the field");
        }

    }
}
