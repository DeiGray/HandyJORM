package HandyJORM.query;

import HandyJORM.exception.IllegalSoftQueryException;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.model.BaseModel;
import HandyJORM.model.ModelAttribute;
import HandyJORM.model.ModelAttributesCollection;
import HandyJORM.session.BaseDBSession;

import java.util.ArrayList;

/**
 * Created by Fabien on 11/05/2017.
 */
public class SoftQueryJoinState extends BaseSoftQueryState {
    public <T extends BaseModel> SoftQueryJoinState(BaseDBSession baseDBSession, String sqlQuery, ModelAttributesCollection cols, String tableName, T model){
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
        throw new IllegalSoftQueryException("from","join");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void from(SoftQuery sq, BaseModel model) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("from","join");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("join","join");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void where(SoftQuery sq, SqlFilter... filters) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("where","join");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void orderBy(SoftQuery sq, SqlOrder... orders) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("orderBy","join");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList all(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("one","join");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BaseModel one(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("all","join");
    }

}
