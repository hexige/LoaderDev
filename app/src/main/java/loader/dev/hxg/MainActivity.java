package loader.dev.hxg;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import loader.dev.hxg.loader.ContactsLoaderManager;

public class MainActivity extends AppCompatActivity implements TextWatcher, ContactsLoaderManager.ContactsCallback {

    private ContactsLoaderManager mManager;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS},
                new int[]{android.R.id.text1, android.R.id.text2}, 0);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(mAdapter);

        EditText ed = (EditText) findViewById(R.id.ed_filter);
        ed.addTextChangedListener(this);

        mManager = new ContactsLoaderManager();
        mManager.init(this, this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mManager.load(editable.toString());
    }

    @Override
    public void onLoadFinished(Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset() {
        mAdapter.swapCursor(null);
    }
}
