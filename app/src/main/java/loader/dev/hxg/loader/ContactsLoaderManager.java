package loader.dev.hxg.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

/**
 * Created by hexige on 2017/8/10.
 */

public class ContactsLoaderManager implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String[] PROJECTION = new String[]{
            Contacts._ID,
            Contacts.DISPLAY_NAME,
            Contacts.CONTACT_STATUS,
            Contacts.CONTACT_PRESENCE,
            Contacts.PHOTO_ID,
            Contacts.LOOKUP_KEY,
    };

    private static final String PARAM_FILTER = "param_filter";

    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;
    private ContactsCallback mCallback;

    public void init(FragmentActivity activity, ContactsCallback callback) {
        mContext = new WeakReference<Context>(activity);
        mCallback = callback;
        mLoaderManager = activity.getSupportLoaderManager();
        mLoaderManager.initLoader(0, null, this);
    }

    public void load(String filter) {
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_FILTER, filter);
        mLoaderManager.restartLoader(0, bundle, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri;
        if (args != null && !TextUtils.isEmpty(args.getString(PARAM_FILTER))) {
            baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                    Uri.encode(args.getString(PARAM_FILTER)));
        } else {
            baseUri = Contacts.CONTENT_URI;
        }

        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";
        return new CursorLoader(mContext.get(), baseUri,
                PROJECTION, select, null,
                Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Context context = mContext.get();
        if (null == context) {
            return;
        }

        mCallback.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Context context = mContext.get();
        if (null == context) {
            return;
        }

        mCallback.onLoaderReset();
    }

    public interface ContactsCallback {
        void onLoadFinished(Cursor data);

        void onLoaderReset();
    }
}
