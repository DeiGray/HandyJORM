package HandyJORM.model;


import HandyJORM.enums.ComparisonStrategyEnum;
import HandyJORM.enums.DBAttributeEnum;
import HandyJORM.enums.DBPropertyTypeEnum;
import HandyJORM.enums.DirectionOrderEnum;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.query.SqlFilter;
import HandyJORM.query.SqlOrder;

import java.lang.reflect.Field;

/**
 * A model field with a table column, like key statue (primary, foreign or common attribute)
 *
 * @author DeiGray
 * @version 0.1
 */
public class ModelAttribute {
    private DBAttributeEnum _attributeType;
    private DBPropertyTypeEnum _type;
    private Field _field;
    private String _colTableName;
    private Object _model;

    public ModelAttribute(Object model,DBPropertyTypeEnum type, Field field, DBAttributeEnum atType,String colName){
        _field = field;
        _type = type;
        _attributeType = atType;
        _colTableName = colName;
        _model = model;
    }

    /**
     * @return true if the modelAttribute is in default Value.
     * @throws IllegalAccessException
     * @throws UnknowFieldModelException
     */
    public boolean isDefaultValue() throws IllegalAccessException, UnknowFieldModelException {
        _field.setAccessible(true);
        boolean result =  ModelAttributeBuilder.isDefaultValue(this.getModel(),this.getField());
        _field.setAccessible(false);
        return result;
    }

    /* Classic getters */

    public String getColTableName(){
        return _colTableName;
    }

    public DBAttributeEnum getAttributeType() {
        return _attributeType;
    }

    public Field getField() { return this._field; }

    public String getFieldName() {
        return _field.getName();
    }

    public Object getModel(){
        return _model;
    }


    /**
     * Create a filter with attributes given.
     * @param comparison
     * @return
     * @throws IllegalAccessException
     */
    public SqlFilter getFilter(ComparisonStrategyEnum comparison) throws IllegalAccessException {
        return new SqlFilter(_colTableName,_type,this.getValue(),comparison);
    }

    /**
     * Give model's field value
     * @return
     * @throws IllegalAccessException
     */
    public Object getValue() throws IllegalAccessException {
        _field.setAccessible(true);
        return _field.get(_model);
    }

    /**
     * Create an order of this modelAttribute
     * @param dir
     * @return
     */
    public SqlOrder getOrder (DirectionOrderEnum dir){
        return new SqlOrder(_colTableName, dir);
    }

    /**
     * A classical toString (useful to debug and test).
     * @return
     */
    public String toString(){
        return _attributeType.toString() + " " + _type.toString() + " " + _field.getName();
    }
}
