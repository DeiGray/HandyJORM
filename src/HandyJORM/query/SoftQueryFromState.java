package HandyJORM.query;

import HandyJORM.exception.IllegalSoftQueryException;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.model.BaseModel;
import HandyJORM.model.ModelAttribute;
import HandyJORM.session.BaseDBSession;

import java.util.ArrayList;

/**
 * Created by Fabien on 11/05/2017.
 *
 */
public class SoftQueryFromState extends BaseSoftQueryState {

    /**
     * Use this constructor to directly create a "SELECT *" query.
     * @param baseDBSession
     */
    public SoftQueryFromState(BaseDBSession baseDBSession){
        super(baseDBSession);
        _sqlQueryString = "SELECT *";
    }

    /**
     * Constructor use to switch to FROM state, from select state.
     * @param baseDBSession
     * @param _sqlString
     */
    public SoftQueryFromState(BaseDBSession baseDBSession, String _sqlString){
        super(baseDBSession);
        super._sqlQueryString = _sqlString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select(SoftQuery sq, ModelAttribute... modelAttributes) throws UnknowFieldModelException, IllegalSoftQueryException {
        throw new IllegalSoftQueryException("select","from");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void from(SoftQuery sq, BaseModel model) throws IllegalSoftQueryException {
        _model = model;
        _tableName = model.getTableName();
        _sqlQueryString += " FROM "+_tableName;
        sq.changeState(new SoftQueryWhereState(_baseDBSession,_sqlQueryString,_cols,_tableName,_model));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void join(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("join","from");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void where(SoftQuery sq, SqlFilter... filters) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("where","from");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void orderBy(SoftQuery sq, SqlOrder... orders) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("orderBy","from");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BaseModel one(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("one","from");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList all(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("all","from");
    }

}
