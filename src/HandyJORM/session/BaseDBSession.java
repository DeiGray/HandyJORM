package HandyJORM.session;

import HandyJORM.model.BaseModel;
import HandyJORM.model.ModelAttributeBuilder;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Fabien on 01/05/2017.
 *
 * A Database Session.
 *
 * Used to interact with the database.
 * @author DeiGray
 * @version 0.2
 */
public abstract class BaseDBSession {
    protected ResultSet cursor;
    protected static Statement statement;
    protected static Connection connection;

    protected static String DRIVER;
    //keep in mind to grant remote access to this database.
    protected static String CONNECTION_STRING;
    protected static String LOGIN;
    protected static String PASSWORD ;

    /**
     * Casual constructor. Called by child.
     * @param pilote
     * @param connectionString
     * @param login
     * @param password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public BaseDBSession(String pilote, String connectionString, String login, String password) throws ClassNotFoundException, SQLException {
        DRIVER = pilote;
        CONNECTION_STRING = connectionString;
        LOGIN = login;
        PASSWORD = password;

        //charge la classe du pilote
        Class.forName(DRIVER);

        connection = DriverManager.getConnection(CONNECTION_STRING,LOGIN,PASSWORD);

        //print des infos sur la base à la connection.
        DatabaseMetaData metaData = connection.getMetaData();
        System.out.println(metaData.getDatabaseProductName());
        System.out.println(metaData.getDatabaseProductVersion());

        // objet de creation d'instructions
        statement = connection.createStatement();
    }

    /**
     * Select all the line in the given Select Query.
     * @param req a SQL Query String.
     * @return Each lines in ArrayList of DB Result.
     */
    public ArrayList<ArrayList<DBResult>> executeSelectAll(String req) {
        ArrayList<ArrayList<DBResult>> result = new ArrayList<>();
        try {
            this.cursor = statement.executeQuery(req);
            int nbCol = this.cursor.getMetaData().getColumnCount();

            while (cursor.next()) {
                ArrayList<DBResult> row = new ArrayList<>();
                for (int i = 1; i <= nbCol; i++) {
                    row.add(new DBResult(cursor.getMetaData().getColumnLabel(i),cursor.getObject(i)));
                }
                result.add(row);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * A Select SQL Query that give only the first result line.
     * @param req a SQL Query String.
     * @return the resutl line in ArrayList of DB Result.
     */
    public ArrayList<DBResult> executeSelectOne(String req) {

        ArrayList<DBResult> data = null;
        try {
            this.cursor = statement.executeQuery(req);
            int nbCol = this.cursor.getMetaData().getColumnCount();

            if(cursor.next()) {
                data = new ArrayList<>();
                for (int i = 1; i <= nbCol; i++) {
                    data.add(new DBResult(cursor.getMetaData().getColumnLabel(i),cursor.getObject(i)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            return data;
        }
    }

    /**
     * Select the first result in a filled model, in an instance of the given model.
     * @param model a model example (only used to get the class)
     * @param query a SQL Query String.
     * @return
     */
    public <T extends BaseModel> T executeSelectOneFromModel(T model, String query){
        T newModel=null;
        try{
            if(null != model){
                newModel = (T) model.getClass().newInstance();
                ArrayList<DBResult> data = executeSelectOne(query);
                if(null != data ){
                    if(data.size() > 0 ){
                        ModelAttributeBuilder.affectDataToModel(newModel,data);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return newModel;

    }

    /**
     * Select results in a filled ArrayList of models, in an instance of the given model.
     * @param model a model example (only used to get the class)
     * @param query a SQL Query String.
     * @return
     */
    public <T extends BaseModel> ArrayList<T> executeSelectAllFromModel(T model, String query){
        ArrayList<T> newModels=new ArrayList<>();
        try{
            if(null != model){
                ArrayList<ArrayList<DBResult>> datas = executeSelectAll(query);
                if(datas.size() > 0 ){
                    for (ArrayList<DBResult> data:
                         datas) {
                        T newModel = (T) model.getClass().newInstance();
                        ModelAttributeBuilder.affectDataToModel(newModel,data);
                        newModels.add(newModel);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return newModels;
    }


    /**
     * Launch a Insert, update or delete query.
     * @param req The query to execute
     * @return True if the query was correctly execute.
     */
    public boolean executeUpdate(String req){
        try
        {
            int result = this.statement.executeUpdate(req) ;
            //si result = 0, requete échoue.
            if (result==0)
                return false;
            else
                return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
}
