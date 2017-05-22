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
 */
public class SoftQueryOrderByState extends BaseSoftQueryState {
    public <T extends BaseModel> SoftQueryOrderByState(BaseDBSession baseDBSession, String sqlQuery, ModelAttributesCollection cols, String tableName, SqlFilter[] filters, T model){
        super(baseDBSession);
        _sqlQueryString = sqlQuery;
        _cols = cols;
        _tableName = tableName;
        _filters = filters;
        _model = model;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void select(SoftQuery sq, ModelAttribute... modelAttributes) throws UnknowFieldModelException, IllegalSoftQueryException {
        throw new IllegalSoftQueryException("select","orderBy");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void from(SoftQuery sq, BaseModel model) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("from","select");

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void join(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("join","select");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void where(SoftQuery sq, SqlFilter... filters) throws IllegalSoftQueryException, InvalidDBPropertyTypeException {
        throw new IllegalSoftQueryException("where","select");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void orderBy(SoftQuery sq, SqlOrder... orders) throws IllegalSoftQueryException {
        _orders = orders;
        if(null != orders && orders.length > 0) {
            _sqlQueryString += " ORDER BY ";
            for (SqlOrder order :
                    orders) {
                _sqlQueryString += order.toSqlString() + ", ";
            }
            _sqlQueryString = _sqlQueryString.substring(0, _sqlQueryString.length() - 2);
        }
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
