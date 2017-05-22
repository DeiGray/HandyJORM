package HandyJORM.model;


import HandyJORM.enums.ComparisonStrategyEnum;
import HandyJORM.enums.DirectionOrderEnum;
import HandyJORM.exception.InvalidFieldNameGiven;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.query.SqlFilter;
import HandyJORM.query.SqlOrder;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by DeiGray on 09/05/2017.
 *
 * Collection of attribute in a model. This collection represent in a group each model-attributes,
 * and store information's about modelAttribute coategorie (primary key, foreign key, etc.
 *
 * Each model get one MAC to store informations about its own attributes.
 *
 * Implements Iterable, so you can foreach the into the class.
 *
 * @author DeiGray
 * @version 0.1
 */
public class ModelAttributesCollection implements Iterable<ModelAttribute>{
    private ArrayList<ModelAttribute> _primaryKey;
    private ArrayList<ModelAttribute> _foreignKeys;
    private ArrayList<ModelAttribute> _primaryForeignKeys;
    private ArrayList<ModelAttribute> _attributes;
    private  ArrayList<ModelAttribute> _all;

    /**
     * A casual constructor.
     */
    public ModelAttributesCollection(){
        _primaryKey = new ArrayList<>();
        _foreignKeys = new ArrayList<>();
        _attributes = new ArrayList<>();
        _primaryForeignKeys = new ArrayList<>();
        _all = new ArrayList<>();
    }

    /**
     * To add an attribute in a model.
     * @param attribute
     * @throws UnknowFieldModelException
     */
    public void addAttribute(ModelAttribute attribute) throws UnknowFieldModelException {
        if(attribute != null){
            _all.add(attribute);
            switch (attribute.getAttributeType()){
                case PrimaryKey :
                    _primaryKey.add(attribute);
                    break;
                case PrimaryForeignKey:
                    _primaryForeignKeys.add(attribute);
                    break;
                case ForeignKey:
                    _foreignKeys.add(attribute);
                    break;
                case Attribute:
                    _attributes.add(attribute);
                    break;
                default:
                    //throw new UnknowFieldModelException(attribute);
                    break;
            }
        }
//        else throw new NullPointerException("No attribute in addAttribute");
    }

    /**
     * Give the field in the collection linked with the given colName.
     * @param colName
     * @return
     */
    public Field getFieldByColName(String colName) throws InvalidFieldNameGiven {
        for (ModelAttribute ma :
                this) {
            if (ma.getColTableName().equals((colName)))
                return ma.getField();
        }
        throw new InvalidFieldNameGiven(colName);
    }

    /**
     * Give a list of all ModelAttributes in the collection.
     * @return an ArrayList, never null, but can be empty
     * @throws IllegalAccessException
     * @throws UnknowFieldModelException
     */
    public ArrayList<SqlFilter> getFilters() throws IllegalAccessException, UnknowFieldModelException {
        ArrayList<SqlFilter> filters = new ArrayList<>();
        for (ModelAttribute ma:
             this) {
            if(!ma.isDefaultValue())
                filters.add(ma.getFilter(ComparisonStrategyEnum.Equals));
        }
        return filters;
    }

    /**
     * Give a list of all ModelAttributes in the collection.
     * @return an ArrayList, never null, but can be empty
     * @throws IllegalAccessException
     * @throws UnknowFieldModelException
     */
    public ArrayList<SqlOrder> getOrders() throws IllegalAccessException, UnknowFieldModelException {
        ArrayList<SqlOrder> orders = new ArrayList<>();
        for (ModelAttribute ma :
                this) {
            if (! ma.isDefaultValue())
                ma.getOrder(DirectionOrderEnum.ASC);
        }
        return orders;
    }

    /**
     * A casual toString method, for debugging or testing.
     * @return
     */
    public String toString(){
        String s ="";
        for (ModelAttribute pk :
                _primaryKey) {
            s +=pk.toString() + "-";
        }
        s+= "_";
        for (ModelAttribute pk :
                _foreignKeys) {
            s +=pk.toString() + "-";
        }
        s+= "_";
        for (ModelAttribute pk :
                _primaryForeignKeys) {
            s +=pk.toString() + "-";
        }
        s+= "_";
        for (ModelAttribute pk :
                _attributes) {
            s +=pk.toString() + "-";
        }
        s+='\n';
        return s;
    }

    /**
     * Use to get a string of attributes, used to make select's columns name, in a SQL string.
     */
    public String toSqlString(){
        String s ="";
        for (ModelAttribute pk :
                _primaryKey) {
            s +=pk.toString() + ", ";
        }
        for (ModelAttribute pk :
                _foreignKeys) {
            s +=pk.toString() + ", ";
        }
        for (ModelAttribute pk :
                _primaryForeignKeys) {
            s +=pk.toString() + ", ";
        }
        for (ModelAttribute pk :
                _attributes) {
            s +=pk.toString() + ", ";
        }
        return s.substring(0,s.length() -2);
    }

    /**
     * Get the number of modelAttributes in the collection.
     * @return
     */
    public int size(){
        return _all.size();
    }

    @Override
    public Iterator<ModelAttribute> iterator() {
        return _all.iterator();
    }
}
