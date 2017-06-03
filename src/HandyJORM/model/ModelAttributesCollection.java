package HandyJORM.model;


import HandyJORM.enums.ComparisonStrategyEnum;
import HandyJORM.enums.DirectionOrderEnum;
import HandyJORM.exception.InvalidDBPropertyTypeException;
import HandyJORM.exception.InvalidFieldNameGiven;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.query.SqlFilter;
import HandyJORM.query.SqlOrder;

import java.lang.reflect.Field;
import java.sql.SQLException;
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
 * @version 0.2
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
                    _primaryKey.add(attribute);
                    _foreignKeys.add(attribute);
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
        return null;
    }

    public ModelAttribute getByColName(String colName) throws InvalidFieldNameGiven {
        for (ModelAttribute ma :
                this) {
            if (ma.getColTableName().equals((colName)))
                return ma;
        }
        return null;
    }

    public ModelAttribute getForeignFieldByColName(String colName){
        if(null == _foreignKeys)
            return null;
        for(ModelAttribute ma : _foreignKeys){
            if(ma.getColTableName().equals(colName))
                return ma;
        }
        return null;
    }

    /**
     * Give a list of all ModelAttributes in the collection.
     * @return an ArrayList, never null, but can be empty
     * @throws IllegalAccessException
     * @throws UnknowFieldModelException
     */
    public ArrayList<SqlFilter> getFilters(boolean withPrimaryKey,boolean withForeignKey) throws IllegalAccessException, UnknowFieldModelException {
        ArrayList<SqlFilter> filters = new ArrayList<>();
        for (ModelAttribute ma:
             this) {
            //TODO : test condition
            if(!ma.isDefaultValue() &&(( withPrimaryKey && withForeignKey) ||
                    (!withPrimaryKey && !ma.isPrimaryKey() ) ||
                    (!withForeignKey && !ma.isForeignKey())))
            {
                filters.add(ma.getFilter(ComparisonStrategyEnum.Equals));
            }
        }
        return filters;
    }

    public String getFiltersString(boolean withPrimaryKey,boolean withForeignKey) throws UnknowFieldModelException, IllegalAccessException, InvalidDBPropertyTypeException {
        String sqlString = "";
        ArrayList<SqlFilter> filtersList = this.getFilters(withPrimaryKey,withForeignKey);
        SqlFilter [] filters = filtersList.toArray(new SqlFilter[filtersList.size()]);
        if(null != filters && filters.length > 0) {
            for (SqlFilter fil :
                    filters) {
                sqlString += fil.toSqlString() + ", ";
            }
            sqlString = sqlString.substring(0, sqlString.length() - 2);
        }
        return sqlString;
    }

    public ArrayList<ModelAttribute> getPrimaryKey(){
        return _primaryKey;
    }

    public ArrayList<ModelAttribute> getForeignKeys(){
        return _foreignKeys;
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
    public String toSqlColumnNamesString(){
        String s ="";
        for (ModelAttribute ma :
                _all) {
            s +=ma.toString() + ", ";
        }
        return s.substring(0,s.length() -2);
    }

    /**
     * Use to get a string of attributes, used to make select's columns name, in a SQL string.
     */
    public String toSqlColumnNamesString(boolean withPrimaryKey) throws IllegalAccessException, UnknowFieldModelException {
        String s ="";
        for (ModelAttribute ma :
                _all) {
            if(withPrimaryKey)
                s +=ma.getColTableName() + ", ";
            else if(!withPrimaryKey && !ma.isPrimaryKey() &&  !ModelAttributeBuilder.isDefaultValue(ma.getModel(),ma.getField()))
                s +=ma.getColTableName() + ", ";
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

    /**
     * Create a SQL Update query with the ModelAttributes.
     * @param tableName the model tableName
     * @return a sql update query string.
     */
    public String getUpdateString(String tableName) throws IllegalAccessException, InvalidDBPropertyTypeException, UnknowFieldModelException, SQLException {
        if (this.size() > 0) {
            String SET = this.getFiltersString(false, true);
            String WHERE = this.getWherePrimaryKeySqlString();
            String query = "UPDATE " + tableName + " SET " + SET +
                    " WHERE " + WHERE;
            return query;
        }
        else{
            throw new SQLException("No value to update : MAC Empty");
        }
    }

    private String getWherePrimaryKeySqlString() throws IllegalAccessException, InvalidDBPropertyTypeException {
        String WHERE = "";
        ArrayList<ModelAttribute> mas = this.getPrimaryKey();
        for (ModelAttribute ma : mas) {
            WHERE += ma.getFilter(ComparisonStrategyEnum.Equals).toSqlString() + ", ";
        }
        WHERE = WHERE.substring(0, WHERE.length() - 2);
        return WHERE;
    }

    /**
     * Create a SQL Save query with the ModelAttributes.
     * @param tableName the model tableName
     * @return a sql save query string.
     */
    public String getSaveString(String tableName) throws SQLException, InvalidDBPropertyTypeException, IllegalAccessException, UnknowFieldModelException {
        if(this.size() > 0){
        String query = "INSERT INTO "+tableName+"("+ this.toSqlColumnNamesString(false)+") values (";
            for (ModelAttribute ma :
                    this) {
                    if(!ma.isPrimaryKey() && !ModelAttributeBuilder.isDefaultValue(ma.getModel(),ma.getField())){
                        query += ma.toSqlValue() + ", ";
                    }
            }
            query = query.substring(0,query.length() -2) + ")";
            return query;
        }
        else{
            throw new SQLException("No value to insert : MAC Empty");
        }
    }
    /**
     * Create a SQL Delete query with the ModelAttributes.
     * @param tableName the model tableName
     * @return a sql delete query string.
     */
    public String getDeleteString(String tableName) throws InvalidDBPropertyTypeException, IllegalAccessException {
        return "DELETE FROM "+tableName+" WHERE "+this.getWherePrimaryKeySqlString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ModelAttribute> iterator() {
        return _all.iterator();
    }
}
