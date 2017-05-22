package HandyJORM.query;

import HandyJORM.exception.IllegalSoftQueryException;
import HandyJORM.exception.InvalidDBPropertyTypeException;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.model.BaseModel;
import HandyJORM.model.ModelAttribute;
import HandyJORM.model.ModelAttributesCollection;
import HandyJORM.session.BaseDBSession;

import java.util.ArrayList;

/**
 * Created by DeiGray on 11/05/2017.
 *
 * The Base to create a SoftQueryState.
 * Each SoftQueryState represent a "state", a part of a SQL Select query.
 *
 * @author DeiGray
 * @version 0.1
 */
public abstract class BaseSoftQueryState<T extends BaseModel>{
    protected T _model;
    protected BaseDBSession _baseDBSession;
    protected String _sqlQueryString;
    protected ModelAttributesCollection _cols;
    protected String _tableName;
    protected SqlFilter[] _filters;
    protected SqlOrder[] _orders;

    /**
     * a classical constructor with a dbSession to query the Database.
     * @param baseDBSession the dbSession.
     */
    BaseSoftQueryState(BaseDBSession baseDBSession){
        _baseDBSession = baseDBSession;
    }

    /**
     * Select part of a "SELECT" SQL Query.
     * @param sq the current SoftQuery instance.
     * @param modelAttributes ModelAttributes from a model
     * @throws UnknowFieldModelException
     * @throws IllegalSoftQueryException
     */
    public abstract void select(SoftQuery sq, ModelAttribute... modelAttributes) throws UnknowFieldModelException, IllegalSoftQueryException;

    /**
     * From part of a "SELECT" SQL Query.
     * @param sq the current SoftQuery instance.
     * @param model
     * @param <T>
     * @throws IllegalSoftQueryException
     */
    public abstract <T extends BaseModel> void from(SoftQuery sq, T model) throws IllegalSoftQueryException;

    /**
     * Join part of a "SELECT" SQL Query.
     * @param sq
     * @throws IllegalSoftQueryException
     */
    public abstract void join(SoftQuery sq) throws IllegalSoftQueryException;

    /**
     * Where part of a "SELECT" SQL Query.
     * @param sq the current SoftQuery instance.
     * @param filters
     * @throws IllegalSoftQueryException
     * @throws InvalidDBPropertyTypeException
     */
    public abstract void where(SoftQuery sq, SqlFilter... filters) throws IllegalSoftQueryException, InvalidDBPropertyTypeException;

    /**
     * OrderBy part of a "SELECT" SQL Query.
     * @param sq the current SoftQuery instance.
     * @param orders
     * @throws IllegalSoftQueryException
     */
    public abstract void orderBy(SoftQuery sq, SqlOrder... orders) throws IllegalSoftQueryException;

    /**
     * Return the first line result from the SELECT query, in a model depending of the Model given in from().
     * @param sq the current SoftQuery instance.
     * @return An instance of the given model class given in from, filled with information collected.
     * @throws IllegalSoftQueryException
     */
    public abstract <T extends BaseModel> T one(SoftQuery sq) throws IllegalSoftQueryException;

    /**
     * Give all model collected by the query.
     * @param sq the current SoftQuery instance.
     * @return An ArrayList of model instance from the current model, given in from state.
     * @throws IllegalSoftQueryException
     */
    public abstract <T extends BaseModel> ArrayList<T> all(SoftQuery sq) throws IllegalSoftQueryException;
}
