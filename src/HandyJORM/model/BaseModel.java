package HandyJORM.model;


import HandyJORM.exception.IllegalSoftQueryException;
import HandyJORM.exception.InvalidDBPropertyTypeException;
import HandyJORM.exception.InvalidFieldNameGiven;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.query.SoftQuery;
import HandyJORM.query.SqlFilter;
import HandyJORM.query.SqlOrder;
import HandyJORM.session.BaseDBSession;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by DeiGray on 03/05/2017.
 *
 * The base model. Use it to simply interact with database, just by extending it.
 *
 * One thing to do before using models : calling initDbSession(BaseDBSession).
 *
 *
 * A model is link with a Database table. With a model, you can link each DB field with a model field.
 * To link fields, you need to correctly name your fields, and then overrides bindColumns() method.
 *
 * To fill a model with db values, you can use findOneLikeMe(), or the static find() method (see examples in methods descriptions).
 *
 * @author DeiGray
 * @version 0.2
 */

public abstract class BaseModel {
    /**
     * The only dbSession used to interact with database.
     */
    protected static BaseDBSession _adbSess;

    /**
     * fields in DB linked with a model field.
     */
    public ModelAttributesCollection modelAttributes;

    /**
     * Basic constructor called by childs.
     * @throws UnknowFieldModelException field name issue, or bindColumns issue.
     */
    public BaseModel() throws UnknowFieldModelException {
        modelAttributes = new ModelAttributesCollection();


        Field[] fields = this.getClass().getFields();
        for (Field field :
                fields) {
            field.setAccessible(true);
            modelAttributes.addAttribute(ModelAttributeBuilder.initModelAttributes(this,field,bindColumns()));

        }
    }

    /**
     * Depreciated
     */
    public abstract String getTableName();

    /**
     * method that link DB field name with Model field name.
     *
     * public class ExampleModel extends BaseModel {
     *
     *
     * public int a_field_PK;
     * public String an_other_field_AT;
     * public int a_foreign_field_FK;
     *
     * @Override
     * public String[][] bindColumns() {
     * String[][] columns = {
     *
     * //Must declare in the same order than in field declaration.
     * {"a_db_table_field_name", "a_field_PK"},
     * {"an_other_db_table_field_name", "an_other_field_AT"},
     * {"an_other_db_table_field_name", "a_foreign_field_FK"},
     * ...
     * }
     * return columns
     * }
     *
     * }
     *
     * @return
     */
    public abstract String[][] bindColumns();

    /**
     * Array to describe foreign Keys
     *
     * each row is format as : {"inner_colname","joined_table_colname","joined_table_classname"}
     * @return
     */
    public String[][] references(){
        return new String[0][];
    }

    /**
     * Find the model link inner model by a foreignKey.
     * @param targetKlass
     * @return A targetClass "persistant" instance, linked to the caller.
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvalidFieldNameGiven
     */
    //TODO : TEST IT
    public <T extends BaseModel> T hasOne(Class targetKlass) throws IllegalAccessException, InstantiationException, InvalidFieldNameGiven {
        T targetModel = (T) targetKlass.newInstance();

        ModelAttribute[] mas = getForeignKeyMASToSet(this,targetModel);
        if(null != mas){
            mas[1].setValue(mas[0].getValue());
            return targetModel.findOneLikeMe();
        }else{
            return null;
        }
    }

    public <T extends BaseModel> ArrayList<T> hasMany(Class targetKlass) throws IllegalAccessException, InstantiationException, InvalidFieldNameGiven {
        T targetModel = (T) targetKlass.newInstance();

        ModelAttribute[] mas = getForeignKeyMASToSet(targetModel,this);
        if(null != mas){
            mas[0].setValue(mas[1].getValue());
            return targetModel.findAllLikeMe();
        }else{
            return null;
        }
    }

    /**
     * Find a couple of ModelAttributes to get and set value of the foreignKeyField;
     *
     * @param modelReferences the primaryKey model
     * @param modelBindColumns the foreignKey model
     * @return Give an Array of 2 ModelAttributes : the first from inner class, the second from foreignKey Class.
     * @throws InvalidFieldNameGiven
     */
    private static <T extends BaseModel> ModelAttribute[] getForeignKeyMASToSet(T modelReferences, T modelBindColumns) throws InvalidFieldNameGiven {
        String[][] references = modelReferences.references();
        if(references.length <= 0)
            return null;
        for (String [] ref : references) {
            if(3 != ref.length)
                return null;
            else{
                ModelAttribute ma = modelBindColumns.modelAttributes.getByColName(ref[1]);
                if(null != ma){
                    ModelAttribute[] mas = new ModelAttribute[2];
                    mas[0] = modelReferences.modelAttributes.getByColName(ref[0]);
                    mas[1] = ma;
                    return mas;
                }
            }
        }
        return null;
    }

    /**
     * The first method to call before anything : instanciate the dbSession to Models.
     * @param dbSess
     */
    public static void initDbSession(BaseDBSession dbSess){
         _adbSess = dbSess;
    }

    /**
     * Make model update his line in db.
     * Update in database, the line with this model ID.
     * @return true if the query works
     */
    public boolean update() throws UnknowFieldModelException, IllegalAccessException, InvalidDBPropertyTypeException, SQLException {
        String query = this.modelAttributes.getUpdateString(this.getTableName());
        return _adbSess.executeUpdate(query);
    }

    /**
     * store the model in a line in the table linked.
     *
     * @return true if the query works
     */
    public boolean save() throws IllegalAccessException, SQLException, InvalidDBPropertyTypeException, UnknowFieldModelException {
        String query = this.modelAttributes.getSaveString(this.getTableName());
        return _adbSess.executeUpdate(query);
    }

    /**
     * Delete the line that represent the model in the db table.
     * Delete in database, the line with this model ID.
     * @return true if the query works
     */
    public boolean delete() throws InvalidDBPropertyTypeException, IllegalAccessException {
        String query = this.modelAttributes.getDeleteString(this.getTableName());
        return _adbSess.executeUpdate(query);
    }

    /**
     * Give a SoftQuery to search a model of the class given.
     * select and from are already done.
     *
     * Example :
     * //ChildModel extends BaseModel
     * SoftQuery sq = ChildModel.find();
     *
     * //or  directly
     *
     * ChildModel model = ChildModel.find().where(...).one();
     *
     * @param klass
     * @param <T>
     * @return a SoftQuery instance with the from() done depending of the model class given in param.
     */
    public static <T extends BaseModel> SoftQuery find(Class<T> klass){
        SoftQuery findQuery = null;
        try {
            T model = klass.newInstance();
            findQuery = new SoftQuery(_adbSess);
            findQuery.from(model);
        } catch (IllegalSoftQueryException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return findQuery;
    }


    /**
     * Give the first "persistant" model corresponding of the filled field in the model. (in ASC order).
     * Example :
     * //ChildModel extends BaseModel
     * ChildModel cmodel = new ChildModel();
     *
     * cmodel.an_id_PK = 1;
     * ChildModel newModel = cmodel.findOneLikeMe();
     *             //newModel contains all the db values for the cmodel given.
     *
     * @param <T> the class model needed.
     * @return the first model found
     */
    public <T extends BaseModel> T findOneLikeMe(){
        T model = null;
        try {
                ArrayList<SqlFilter> filters = this.modelAttributes.getFilters(true,true);
                ArrayList<SqlOrder> orders = this.modelAttributes.getOrders();
                model = find(this.getClass())
                        .where(filters.toArray(new SqlFilter[filters.size()]))
                        .orderBy(orders.toArray(new SqlOrder[orders.size()]))
                        .one();

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnknowFieldModelException e) {
            e.printStackTrace();
        } catch (IllegalSoftQueryException e) {
            e.printStackTrace();
        } catch (InvalidDBPropertyTypeException e) {
            e.printStackTrace();
        }

        return model;
    }
    /**
     * Give all "persistants" models corresponding of the filled field in the model. (in ASC order)
     * Example :
     * //ChildModel extends BaseModel
     * ChildModel cmodel = new ChildModel();
     *
     * cmodel.an_id_PK = 1;
     * ArrayList<ChildModel> newModels = cmodel.findAllLikeMe();
     *             //newModels contains all the db values for the cmodel given.
     *
     * @param <T> class of models needed
     * @return All the models found
     */

    public <T extends BaseModel> ArrayList<T> findAllLikeMe(){
        ArrayList<T> models = new ArrayList<>();
        try {
            ArrayList<SqlFilter> filters = this.modelAttributes.getFilters(true,true);
            ArrayList<SqlOrder> orders = this.modelAttributes.getOrders();
            models = find(this.getClass())
                    .where(filters.toArray(new SqlFilter[filters.size()]))
                    .orderBy(orders.toArray(new SqlOrder[orders.size()]))
                    .all();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvalidDBPropertyTypeException e) {
            e.printStackTrace();
        } catch (UnknowFieldModelException e) {
            e.printStackTrace();
        } catch (IllegalSoftQueryException e) {
            e.printStackTrace();
        }

        return models;
    }


    /**
     * Check if the model exist in the database.
     */
    public boolean exist(){
        return this.findOneLikeMe() != null ;
    }
}
