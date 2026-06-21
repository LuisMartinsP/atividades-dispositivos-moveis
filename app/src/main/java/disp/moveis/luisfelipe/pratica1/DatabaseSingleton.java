package disp.moveis.luisfelipe.pratica1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseSingleton{
    private static DatabaseSingleton instance;
    private SQLiteDatabase db;

    private static final String NOME_BANCO = "app.db";

    private static String script[] = new String[]{
            "CREATE TABLE Categoria (" +
                    "idCategoria INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT NOT NULL);",


            "CREATE TABLE Checkin (" +
                    "Local TEXT PRIMARY KEY," +
                    "qtdVisitas INTEGER NOT NULL," +
                    "cat INTEGER NOT NULL," +
                    "latitude TEXT NOT NULL," +
                    "longitude TEXT NOT NULL," +
                    "CONSTRAINT fkey0 FOREIGN KEY(cat) " +
                    "REFERENCES Categoria(idCategoria));",


            "INSERT INTO Categoria(nome) VALUES('Restaurante');",
            "INSERT INTO Categoria(nome) VALUES('Bar');",
            "INSERT INTO Categoria(nome) VALUES('Cinema');",
            "INSERT INTO Categoria(nome) VALUES('Universidade');",
            "INSERT INTO Categoria(nome) VALUES('Estádio');",
            "INSERT INTO Categoria(nome) VALUES('Parque');",
            "INSERT INTO Categoria(nome) VALUES('Outros');"
};

    private DatabaseSingleton() {

        Context ctx = MyApp.getAppContext();

        db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);

        Cursor c = buscar("sqlite_master", null, "type = 'table'", "");

        if (c.getCount() == 1) {
            for (String sql : script) {
                db.execSQL(sql);
            }
        }

        c.close();
    }

    public static DatabaseSingleton getInstance(Context context){
        if (instance == null){
            instance =  new DatabaseSingleton();
        }
        instance.abrir();
        return instance;
    }

    public Cursor buscar(String tabela, String[] colunas, String where, String orderBy) {

        Cursor c;

        if (!where.equals(""))
            c = db.query(tabela, colunas, where, null, null, null, orderBy);
        else
            c = db.query(tabela, colunas, null, null, null, null, orderBy);

        return c;
    }

    public long inserir(String tabela, ContentValues valores) {
        long id = db.insert(tabela, null, valores);
        Log.i("BANCO_DADOS", "Cadastrou registro com o id [" + id + "]");
        return id;

    }

    public int atualizar(String tabela, ContentValues valores, String where) {
        int count = db.update(tabela, valores, where, null);
        Log.i("BANCO_DADOS", "Atualizou [" + count + "] registros");
        return count;

    }

    public int deletar(String tabela, String where) {
        int count = db.delete(tabela, where, null);
        Log.i("BANCO_DADOS", "Deletou [" + count + "] registros");
        return count;

    }
    private void abrir() {
        Context ctx = MyApp.getAppContext();

        if (!db.isOpen()) {
            db = ctx.openOrCreateDatabase(NOME_BANCO, Context.MODE_PRIVATE, null);
            Log.i("BANCO_DADOS", "Abriu conexão com o banco.");
        }else{
            Log.i("BANCO_DADOS", "Conexão com o banco já estava aberta.");
        }
    }

    public void fechar() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.i("BANCO_DADOS", "Fechou conexão com o Banco.");
        }
    }

    public Cursor executarSQL(String sql){
        return db.rawQuery(sql, null);
    }

}
