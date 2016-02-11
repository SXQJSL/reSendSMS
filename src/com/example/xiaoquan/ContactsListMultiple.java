package com.example.xiaoquan;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class ContactsListMultiple extends ListActivity implements OnClickListener{  
    
  private final int UPDATE_LIST=1;  
  ArrayList<String> contactsList; //得到的所有联系人  
  ArrayList<String> getcontactsList; //选择得到联系人  
  private Button okbtn;  
  private Button cancelbtn;  
  private ProgressDialog proDialog;  

  Thread getcontacts;  
  Handler updateListHandler = new Handler() {  
          public void handleMessage(Message msg) {  
               switch (msg.what) {  
                 
               case UPDATE_LIST:  
                   if (proDialog != null) {  
                          proDialog.dismiss();  
                      }  
                   updateList();   
               }  
          }  
   };  
  public void onCreate(Bundle savedInstanceState) {  
      super.onCreate(savedInstanceState);  
      setContentView(R.layout.contactslist);  
      contactsList=new ArrayList<String>();  
      getcontactsList=new ArrayList<String>();  

      final ListView listView = getListView();  
      listView.setItemsCanFocus(false);  
      listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);  
      okbtn=(Button)findViewById(R.id.contacts_done_button);  
      cancelbtn=(Button)findViewById(R.id.contact_back_button);  
      okbtn.setOnClickListener(this);  
      cancelbtn.setOnClickListener(this);  
        
      getcontacts=new Thread(new GetContacts());  
      getcontacts.start();  
      proDialog = ProgressDialog.show(ContactsListMultiple.this, "loading","loading", true, true);  

  }  
    
  @Override  
  protected void onResume() {  
      // TODO Auto-generated method stub  
      super.onResume();  
            
  }  


  void updateList(){  
      if(contactsList!=null)  
      setListAdapter(new ArrayAdapter<String>(this,  
             android.R.layout.simple_list_item_multiple_choice, contactsList));  
        
  }  

    
  @Override  
  protected void onListItemClick(ListView l, View v, int position, long id) {  
      // TODO Auto-generated method stub  
      if(((CheckedTextView)v).isChecked()){  
            
          CharSequence num=((CheckedTextView)v).getText();  
          getcontactsList.add(num.toString());  
      }  
          if(!((CheckedTextView)v).isChecked()){     
              CharSequence num=((CheckedTextView)v).getText();  
              if((num.toString()).indexOf("[")>0){  
                  String phoneNum=num.toString().substring(0, (num.toString()).indexOf("\n"));  
                  getcontactsList.remove(phoneNum);  
                  Log.d("remove_num", ""+phoneNum);  
                  }else{  
                       getcontactsList.remove(num.toString());  
                      Log.d("remove_num", ""+num.toString());  
                  }  
      }  
      super.onListItemClick(l, v, position, id);    
  }  
 class GetContacts implements Runnable{  
     @Override  
  public void run() {  
      // TODO Auto-generated method stub  
         Uri uri = ContactsContract.Contacts.CONTENT_URI;  
         String[] projection = new String[] {  
                  ContactsContract.Contacts._ID,  
                  ContactsContract.Contacts.DISPLAY_NAME,  
                  ContactsContract.Contacts.PHOTO_ID  
          };  
          String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";  
          String[] selectionArgs = null;  
          String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";  
          Cursor cursor=getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);  
          Cursor phonecur = null;  

          while (cursor.moveToNext()){    
                
              // 取得联系人名字    
              int nameFieldColumnIndex = cursor.getColumnIndex(android.provider.ContactsContract.PhoneLookup.DISPLAY_NAME);    
              String name = cursor.getString(nameFieldColumnIndex);    
              // 取得联系人ID    
              String contactId = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.Contacts._ID));    
              phonecur = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "  + contactId, null, null);     
              // 取得电话号码(可能存在多个号码)    
              while (phonecur.moveToNext()){    
                  String strPhoneNumber = phonecur.getString(phonecur.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER));    
                  if(strPhoneNumber.length()>4)  
                      contactsList.add(name+","+strPhoneNumber);  
                  //contactsList.add(strPhoneNumber+"\n"+name+"");  
            
              }      
          }    
          if(phonecur!=null)  
           phonecur.close();  
          cursor.close();   

          Message msg1=new Message();  
          msg1.what=UPDATE_LIST;  
          updateListHandler.sendMessage(msg1);  
  }   
 }  
@Override  
protected void onPause() {  
  // TODO Auto-generated method stub  
  super.onPause();  
    
}  

@Override  
protected void onDestroy() {  
  contactsList.clear();  
  getcontactsList.clear();  
  super.onDestroy();  
}  


@Override  
public void onClick(View v) {  
  // TODO Auto-generated method stub  
  switch (v.getId()) {  
  case R.id.contacts_done_button:  
         Intent i = new Intent();   
     
             i.putStringArrayListExtra("GET_CONTACT", getcontactsList);    
       
          setResult(2, i);    
          ContactsListMultiple.this.finish();  
      break;  
  case R.id.contact_back_button:  
      ContactsListMultiple.this.finish();  
      break;  
  default:  
      break;  
  }  
}  
@Override  
public boolean onKeyDown(int keyCode, KeyEvent event) {  
  // TODO Auto-generated method stub  
  if(keyCode==KeyEvent.KEYCODE_BACK){  
         Intent i = new Intent();   
         i.putStringArrayListExtra("GET_CONTACT", getcontactsList);     
          setResult(RESULT_OK, i);    
  }  
  return super.onKeyDown(keyCode, event);  
}  
}  
