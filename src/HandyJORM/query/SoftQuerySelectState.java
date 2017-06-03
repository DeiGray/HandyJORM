package HandyJORM.query;

import HandyJORM.exception.IllegalSoftQueryException;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.model.BaseModel;
import HandyJORM.model.ModelAttribute;
import HandyJORM.model.ModelAttributesCollection;
import HandyJORM.session.BaseDBSession;

import java.util.ArrayList;

/**
 * Etat select d'une SoftQuery
 */
public class SoftQuerySelectState extends BaseSoftQueryState {


    public SoftQuerySelectState(BaseDBSession baseDBSession){
        super(baseDBSession);
        _sqlQueryString = "";
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void select(SoftQuery sq, ModelAttribute... modelAttributes) throws UnknowFieldModelException {
        _sqlQueryString ="SELECT ";
        _cols = new ModelAttributesCollection();
        for (ModelAttribute ma :
                modelAttributes) {
            _cols.addAttribute(ma);
        }
        _sqlQueryString += _cols.toSqlColumnNamesString() + " ";
        sq.changeState(new SoftQueryFromState(_baseDBSession,super._sqlQueryString));
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
        throw new IllegalSoftQueryException("from","select");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void where(SoftQuery sq, SqlFilter... filters) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("where","select");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void orderBy(SoftQuery sq, SqlOrder... orders) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("orderBy","select");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public BaseModel one(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("one","select");
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList all(SoftQuery sq) throws IllegalSoftQueryException {
        throw new IllegalSoftQueryException("all","select");
    }

}
