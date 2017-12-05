package org.jssec.android.permission.permissionrequestingpermissionatruntime;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            List<ContactData> contactDataList = new ArrayList<>();
            while (cursor.moveToNext()) {
                ContactData contact = new ContactData();
                contact.name = cursor.getString(cursor.getColumnIndex("display_name"));
                contactDataList.add(contact);
            }
            cursor.close();

            ListView lv = new ListView(this);
            lv.setAdapter(new ContactListAdapter(this, contactDataList));
            setContentView(lv);
        }
    }

    private static class ContactData {
        String name;
    }

    private static class ContactListAdapter extends ArrayAdapter<ContactData> {
        private final LayoutInflater mInflater;

        public ContactListAdapter(Context context, List<ContactData> contactList) {
            super(context, R.layout.activity_contact_list);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            addAll(contactList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.activity_contact_list, parent, false);
                holder.name = (TextView) convertView.findViewById(R.id.contactName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ContactData data = getItem(position);
            holder.name.setText(data.name);

            return convertView;
        }
    }

    private static class ViewHolder {
        TextView name;
    }
}
