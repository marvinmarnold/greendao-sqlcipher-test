package im.unplugged.greentest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import de.greenrobot.dao.database.SQLCipherDatabase;
import de.greenrobot.daoexample.DaoMaster;
import de.greenrobot.daoexample.DaoSession;
import de.greenrobot.daoexample.Note;
import de.greenrobot.daoexample.NoteDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase.loadLibs(this);
        SQLiteOpenHelper helper = new SQLiteOpenHelper(this, "dbname", null, DaoMaster.SCHEMA_VERSION) {

            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
                DaoMaster.createAllTables(new SQLCipherDatabase(sqLiteDatabase), false);
            }

            @Override
            public void onUpgrade(SQLiteDatabase dbIn, int currentVersion, int lastestVersion) {
                SQLCipherDatabase db = new SQLCipherDatabase(dbIn);
                // Do what you need to here
            }

        };

        SQLiteDatabase db = helper.getWritableDatabase("password");
        DaoMaster daoMaster = new DaoMaster(new de.greenrobot.dao.database.SQLCipherDatabase(db));

        DaoSession daoSession = daoMaster.newSession();
        NoteDao noteDao = daoSession.getNoteDao();

        Note note = new Note();
        note.setText("first test");

        Log.d("greenDAO + sqlcipher", "Started with " + noteDao.loadAll().size());

        noteDao.insertOrReplace(note);
        Log.d("greenDAO + sqlcipher", "Ended with " + noteDao.loadAll().size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
