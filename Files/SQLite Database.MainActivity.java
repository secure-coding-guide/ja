package org.jssec.android.sqlite;

import org.jssec.android.sqlite.task.DataDeleteTask;
import org.jssec.android.sqlite.task.DataInsertTask;
import org.jssec.android.sqlite.task.DataSearchTask;
import org.jssec.android.sqlite.task.DataUpdateTask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {
    private SampleDbOpenHelper       mSampleDbOpenHelper;   //�f�[�^�x�[�X�I�[�v���w���p�[
    private Cursor                   mCursor = null;        //��ʕ\���Ɏg�p����J�[�\��
    
    //�ݒ蒆�̌�������
    private String  mItemIdNoSearch = null;
    private String  mItemNameSearch = null;
    private String  mItemInfoSearch = null;   
    
    //���ݑI������Ă���s�̏��
    private String  mItemIdNo = null;
    private String  mItemName = null;
    private String  mItemInfo = null;   
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //OpenHelper�̏���
        mSampleDbOpenHelper = SampleDbOpenHelper.newHelper(this);
        mSampleDbOpenHelper.openDatabaseWithHelper();
    }
    
    @Override 
    protected void onStart() {
        super.onStart();
        
        //���X�g���I�����ꂽ���̏���
        ListView lv = (ListView)findViewById(R.id.DataList);
        lv.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                setCurrentData(arg1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                //�I���s�f�[�^�̃N���A
                clearCurrentData();
            }
        });

        //���X�g���N���b�N���ꂽ���̏���
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //�I�����ꂽ�s�̃f�[�^���Z�b�g����
                setCurrentData(arg1);
                //�ҏW��ʂ̌Ăяo��
                startEditActivity();
            }
        });

        //���������ꂽ���̏���
        lv.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //�I�����ꂽ�s�̃f�[�^���Z�b�g����
                setCurrentData(arg1);
                return false;
            }
        });

        //�R���e�N�X�g���j���[�̏���
        lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu arg0, View arg1, ContextMenuInfo arg2) {
                //�^�C�g���̐ݒ�
                String strTitle = mItemIdNo + "/" + mItemName; 
                arg0.setHeaderTitle(strTitle);
                // ���j���[�̍쐬
                arg0.add(0, EDIT_MENUITEM_ID, 1, R.string.EDIT_MENUITEM);
                arg0.add(0, DELETE_MENUITEM_ID, 2, R.string.DELETE_MENUITEM);
            }
        });
        
        //�����f�[�^�쐬
        initData();

        //DB�f�[�^�̕\��
        showDbData();
    }

    //���ݑI������Ă���s�̏����Z�b�g����
    private void setCurrentData(View currentLine) {
        mItemIdNo   = ((TextView)currentLine.findViewById(R.id.dlc_IdNo)).getText().toString();
        mItemName = ((TextView)currentLine.findViewById(R.id.dlc_Name)).getText().toString();
        mItemInfo = ((TextView)currentLine.findViewById(R.id.dlc_Info)).getText().toString();      
    }

    //���ݑI������Ă���s�̏����N���A����
    private void clearCurrentData() {
        mItemIdNo   = null;
        mItemName = null;
        mItemInfo = null;       
    }

    //DB�ŏ����f�[�^�쐬�i�e�X�g�p�j
    private void initData() {
        //�f�[�^���P���ł���������f�[�^�������Ȃ�
        Cursor cur = mSampleDbOpenHelper.getDb().rawQuery("SELECT * FROM " + CommonData.TABLE_NAME + " LIMIT 1", null);
        if (cur.getCount() > 0) {
            cur.close();
            return;   //�f�[�^������̂ŏ����������Ȃ�
        }

        //�����f�[�^�̍쐬
        ContentValues insertValues = new ContentValues();
        for (int i = 1; i <= 31; i++) {
            insertValues.put("idno", String.valueOf(i));
            insertValues.put("name", "Name of User-" + String.valueOf(i));
            insertValues.put("info", "Info of User-" + String.valueOf(i));
            mSampleDbOpenHelper.getDb().insert(CommonData.TABLE_NAME, null, insertValues);
        }
    }
    
    //��n��
    @Override
    protected void onPause() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
            mCursor = null;
        }

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mSampleDbOpenHelper.closeDatabase();
        super.onDestroy();
    } 

    //-----------------------------------------------------------------------------
    // ���j���[�֘A
    //-----------------------------------------------------------------------------
    private static final int INSERT_MENUITEM_ID = Menu.FIRST + 1;
    private static final int SEARCH_MENUITEM_ID = Menu.FIRST + 2;
    private static final int EDIT_MENUITEM_ID   = Menu.FIRST + 3;
    private static final int DELETE_MENUITEM_ID = Menu.FIRST + 4;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem item = menu.add(0, INSERT_MENUITEM_ID, 0, R.string.INSERT_MENUITEM);
        item.setIcon(android.R.drawable.ic_menu_add);
        item = menu.add(0, SEARCH_MENUITEM_ID, 0, R.string.SEARCH_MENUITEM);
        item.setIcon(android.R.drawable.ic_menu_search);
        return true;
    }
 
    /**
     * ���j���[�I����
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_MENUITEM_ID:
            //�ҏW��ʂ�V�K�ǉ����[�h�ŌĂяo��
            startAddActivity();
            break;
        case SEARCH_MENUITEM_ID:
            //������ʂ�V�K�ǉ����[�h�ŌĂяo��
             startSearchActivity();
            break;
        case EDIT_MENUITEM_ID:
            //�ҏW��ʂ�ҏW���[�h�ŌĂяo��
            startEditActivity();
            break;
        case DELETE_MENUITEM_ID:
            //�폜�m�F��ʂ��Ăяo��
            startDeleteActivity();
            break;
        default:
            break;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    //-----------------------------------------------------------------------------
    //  �q��ʂ̌Ăяo��
    //-----------------------------------------------------------------------------
    //�V�K��ʂ̌Ăяo��
    private void startAddActivity() {
        Intent intentAdd = new Intent(this, EditActivity.class);
        intentAdd.putExtra(CommonData.EXTRA_REQUEST_MODE, CommonData.REQUEST_NEW);
        startActivityForResult(intentAdd, CommonData.REQUEST_NEW);
    }

    private Intent newIntent(Context context, Class<?> cls,
            String idno, String name, String info) {
        Intent intent = new Intent(context, cls);

        //�f�[�^��Intent�ɐݒ肷��B
        intent.putExtra(CommonData.EXTRA_ITEM_IDNO, idno);
        intent.putExtra(CommonData.EXTRA_ITEM_NAME, name);
        intent.putExtra(CommonData.EXTRA_ITEM_INFO, info);            

        return intent;
    }
    
    //�����J�n��ʂ̌Ăяo��
    private void startSearchActivity() {
        Intent intentSearch = newIntent(this, SearchActivity.class, 
                                    mItemIdNoSearch, mItemNameSearch, mItemInfoSearch);
           
        startActivityForResult(intentSearch, CommonData.REQUEST_SEARCH);
    }
    
    //�ҏW��ʂ̌Ăяo��
    private void startEditActivity() {
        Intent intentEdit = newIntent(this, EditActivity.class, 
                                            mItemIdNo, mItemName, mItemInfo);
        intentEdit.putExtra(CommonData.EXTRA_REQUEST_MODE, CommonData.REQUEST_EDIT);

        startActivityForResult(intentEdit, CommonData.REQUEST_EDIT);
    }

    //�폜��ʂ̌Ăяo��
    private void startDeleteActivity() {
        Intent intentDelete = newIntent(this, DeleteActivity.class, 
                                            mItemIdNo, mItemName, mItemInfo);

        startActivityForResult(intentDelete, CommonData.REQUEST_DELETE);
    }

    /* �ҏW�E�폜�E������ʂ���߂������̏��� */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        //�L�����Z���̏ꍇ�͉������Ȃ�
        if (resultCode == Activity.RESULT_CANCELED) {
            showDbData();            //���[�U�[���̕\��
            return;
        }

        //���N�G�X�g�R�[�h���ɏ����𕪂���
        switch(requestCode) {
        case CommonData.REQUEST_NEW:
            addUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO),
                        data.getStringExtra(CommonData.EXTRA_ITEM_NAME),
                        data.getStringExtra(CommonData.EXTRA_ITEM_INFO));
            break;
        case CommonData.REQUEST_SEARCH:
              searchUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO), 
                      data.getStringExtra(CommonData.EXTRA_ITEM_NAME), 
                      data.getStringExtra(CommonData.EXTRA_ITEM_INFO));
            break;
        case CommonData.REQUEST_EDIT:
            editUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO), 
                         data.getStringExtra(CommonData.EXTRA_ITEM_NAME), 
                         data.getStringExtra(CommonData.EXTRA_ITEM_INFO));
            break;
        case CommonData.REQUEST_DELETE:
            deleteUserData(data.getStringExtra(CommonData.EXTRA_ITEM_IDNO));
            break;
        default:
            break;
        }
    }
    
    //�f�[�^����ʂɔ��f����
    public void updateCursor(Cursor cur) {
        //�A�_�v�^�[�̍쐬
        String  cols2[]  =   {"idno","name","info"};
        int     views[] = {R.id.dlc_IdNo, R.id.dlc_Name, R.id.dlc_Info};

        //���X�g�r���[�̎擾
        ListView lv = (ListView)findViewById(R.id.DataList);

        //�ȑO�̃J�[�\������������j��
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }

        mCursor = cur;

        //��ʕ\���p�f�[�^�̐ݒ�
        CursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.data_list_contents, mCursor, cols2, views, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        lv.setAdapter(adapter);
    }


   //---------------------------------------
    //DB�ɑ΂��鏈��
    //---------------------------------------
    //DB�f�[�^�\������
    public void showDbData() {
        //��ʕ\�������i�񓯊��^�X�N�j
        DataSearchTask task = new DataSearchTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(mItemIdNoSearch, mItemNameSearch, mItemInfoSearch);
    }
    
    //�ǉ�����
    private void addUserData(String idno, String name, String info) {
        //�f�[�^�ǉ�����
        DataInsertTask task = new DataInsertTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno, name, info);        
    }
    
    //��������
    private void searchUserData(String idno, String name, String info) {
        mItemIdNoSearch   = idno; 

        mItemNameSearch = name;
        mItemInfoSearch = info;

        //�f�[�^��������
        DataSearchTask task = new DataSearchTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno, name, info);
        }
    
    //�X�V����
    private void editUserData(String idno, String name, String info) {
        DataUpdateTask task = new DataUpdateTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno, name, info);
    }

    //�폜����
    private void deleteUserData(String idno) {     
        DataDeleteTask task = new DataDeleteTask(mSampleDbOpenHelper.getDb(), this);
        task.execute(idno);
    }
}
