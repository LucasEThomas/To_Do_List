package com.example.To_Do_List;

// no changes!!
//CHANGES!!
import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.parse.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    ArrayList<ListItem> listOfItems = new ArrayList<ListItem>();
    ArrayAdapter<ListItem> adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //janitorial stuff
        Parse.initialize(this, "L4JcaEgxDWRgDbTDVLfHRgjjsYuqiFGBVomPOyjZ", "4Luvdm9g10oAwS5xyLpHIElCZUv7ok4mtbyouGzf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //hookup the widgets!!!!
        Button buttonAdd = (Button) findViewById(R.id.buttonAddItem);
        final EditText editTextAddItem = (EditText) findViewById(R.id.editTextItemAdd);
        ListView listViewItem = (ListView) findViewById(R.id.listViewItems);
        TextView textViewTitle = (TextView) findViewById(R.id.textViewTitle);

        //hookup the adapter
        adapter = new ArrayAdapter<ListItem>(this, android.R.layout.simple_list_item_1, listOfItems);
        listViewItem.setAdapter(adapter);

        //download objects from parse
        ParseQuery<ParseObject> query = ParseQuery.getQuery("To_Do_List");
        query.whereContains("toDoList", "");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(parseObjects != null){
                    listOfItems.clear();
                    for(ParseObject currentObject : parseObjects){
                        listOfItems.add(new ListItem(currentObject.getString("toDoList"), currentObject.getObjectId()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

        //Set the click listener for the button.

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextAddItem.getText().toString().length() > 0){
                    listOfItems.add(new ListItem( editTextAddItem.getText().toString()));
                    adapter.notifyDataSetChanged();
                    editTextAddItem.setText("");

                    for(ListItem currentItem : listOfItems) {
                        if(currentItem.objectID == null){
                            ParseObject testObject = new ParseObject("To_Do_List");
                            testObject.put("toDoList", currentItem.name);
                            try {
                                testObject.save();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            currentItem.objectID = testObject.getObjectId();
                        }
                    }
                }
            }
        });

        editTextAddItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if(editTextAddItem.getText().toString().length() > 0){
                        listOfItems.add(new ListItem(editTextAddItem.getText().toString()));
                        adapter.notifyDataSetChanged();
                        editTextAddItem.setText("");

                        for(ListItem currentItem : listOfItems) {
                            if(currentItem.objectID == null) {
                                ParseObject testObject = new ParseObject("To_Do_List");
                                testObject.put("toDoList", currentItem.name);
                                try {
                                    testObject.save();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                currentItem.objectID = testObject.getObjectId();
                                
                            }
                        }
                    }
                    return true;
                }
                return false;
            }
        });

        //setup context menu
        registerForContextMenu(listViewItem);
        listViewItem.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;

                menu.setHeaderTitle("Options");
                menu.add(0, v.getId(), 0, "Delete");
                menu.add(0, v.getId(), 1, "Edit");
                menu.add(0, v.getId(), 2, "Fire Blasters");
                menu.add(0, v.getId(), 3, "Deploy Shields");

            }
        });

    }

    private class ListItem {
        public String name;
        public String objectID;

        public ListItem(String nameIn, String objectIDIn){
            name = nameIn;
            objectID = objectIDIn;
        }

        public ListItem(String nameIn){
            name = nameIn;
        }

        @Override
        public String toString(){
            return name;
        }

    }


}
