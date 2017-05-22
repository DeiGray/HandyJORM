package HandyJORM.query;

import HandyJORM.exception.IllegalSoftQueryException;
import HandyJORM.exception.InvalidDBPropertyTypeException;
import HandyJORM.exception.UnknowFieldModelException;
import HandyJORM.model.BaseModel;
import HandyJORM.model.ModelAttribute;
import HandyJORM.session.BaseDBSession;

import java.util.ArrayList;

/**
 * A SQL "SELECT" query.
 * Each method is a "state" of the query.
 *
 * Example :
 *
 * SoftQuery sq = new SoftQuery(dbSess);
 * sq.select(modelAtb)
 *   .from(model)
 *   .all();
 *
 * @author DeiGray
 * @version 0.1
 */
public class SoftQuery {

    private BaseDBSession _baseDBSession;
    private BaseSoftQueryState _state;

    public SoftQuery(BaseDBSession baseDBSession){
        _baseDBSession = baseDBSession;
    }


    void changeState(BaseSoftQueryState state) {
        _state = state;
    }

    /**
     * The Select state.
     * @param modelAttributes
     * @return return the softQuery object in the next state (from)
     * @throws UnknowFieldModelException
     * @throws IllegalSoftQueryException
     */
    public SoftQuery select(ModelAttribute... modelAttributes) throws UnknowFieldModelException, IllegalSoftQueryException {
        if(null == _state){
            _state = new SoftQuerySelectState(_baseDBSession);
        }

        _state.select(this,modelAttributes);
        return this;
    }

    /**
     * The from state
     * @param model
     * @param <T>
     * @return return the softQuery object in the next state (where)
     * @throws IllegalSoftQueryException
     */
    public <T extends BaseModel> SoftQuery from(T model) throws IllegalSoftQueryException {
        if(null == _state){
            _state = new SoftQueryFromState(_baseDBSession);
        }
        _state.from(this,model);
        return this;
    }

    /**
     * The join state
     * @return
     * @throws Exception
     */
    public SoftQuery join() throws Exception {
        throw new Exception("not implemented functionnality");
    }

    /**
     * The where state
     * @param filters
     * @return
     * @throws IllegalSoftQueryException
     * @throws InvalidDBPropertyTypeException
     */
    public SoftQuery where(SqlFilter... filters) throws IllegalSoftQueryException, InvalidDBPropertyTypeException {
        if(null == _state)
            throw new NullPointerException("No state given.");
        _state.where(this,filters);
        return this;
    }

    /**
     * The orderBy state.
     * @param orders
     * @return
     * @throws IllegalSoftQueryException
     */
    public SoftQuery orderBy(SqlOrder... orders) throws IllegalSoftQueryException {
        if(null != orders){
            _state.orderBy(this,orders);
            return this;
        }
        throw new NullPointerException("No orders given");
    }

    /**
     * Give the first model found, with the current SQL query created.
     * @return a model.
     * @throws IllegalSoftQueryException
     */
    public <T extends BaseModel> T one() throws IllegalSoftQueryException {
        return (T) _state.one(this);
    }

    /**
     * Give all models found with this select SQL query.
     * @return
     * @throws IllegalSoftQueryException
     */
    public <T extends BaseModel> ArrayList<T> all() throws IllegalSoftQueryException {
        return (ArrayList<T>) _state.all(this);
    }
    
}
