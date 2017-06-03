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
 * Created by Fabien on 11/05/2017.
 *
 * @author DeiGray
 * @version 0.2
 */
public class SoftQueryWhereState extends BaseSoftQueryState {
    public <T extends BaseModel> SoftQueryWhereState(BaseDBSession baseDBSession, String sqlQuery, ModelAttributesCollection cols, String tableName, T model){
        super(baseDBSession);
        _sqlQueryString = sqlQuery;
        _cols = cols;
        _tableName = tableName;
        _model = model;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void select(SoftQuery sq, ModelAttribute... modelAttributes) throws UnknowFieldModelException, IllegalSoftQueryException {
        throw new IllegalSoftQueryException("from","where");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void from(SoftQuery sq, BaseModel model) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("from","where");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("from","where");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void where(SoftQuery sq, SqlFilter... filters) throws IllegalSoftQueryException, InvalidDBPropertyTypeException {
        if(null != filters && filters.length > 0) {
            _sqlQueryString+=" WHERE ";
            for (SqlFilter fil :
                    filters) {
                _sqlQueryString += fil.toSqlString() + ", ";
            }
            _sqlQueryString = _sqlQueryString.substring(0, _sqlQueryString.length() - 2);
        }
        _filters = filters;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void orderBy(SoftQuery sq, SqlOrder... orders) throws IllegalSoftQueryException {
        sq.changeState(new SoftQueryOrderByState(_baseDBSession,_sqlQueryString,_cols,_tableName,_filters,_model));
        sq.orderBy(orders);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BaseModel one(SoftQuery sq) throws IllegalSoftQueryException {
        return _baseDBSession.executeSelectOneFromModel(_model,_sqlQueryString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<BaseModel> all(SoftQuery sq) throws IllegalSoftQueryException {
        return _baseDBSession.executeSelectAllFromModel(_model,_sqlQueryString);
    }
}
