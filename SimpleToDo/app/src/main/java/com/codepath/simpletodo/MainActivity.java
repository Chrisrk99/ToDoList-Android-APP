package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity<itemsAdapter> extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT="item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button ButtonAdd;
    EditText EnterItem;
    RecyclerView ReviewItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButtonAdd = findViewById(R.id.ButtonAdd);
        EnterItem= findViewById(R.id.EnterItem);
        ReviewItems= findViewById(R.id.ReviewItems);

        loadItems();


         ItemsAdapter.OnLongClickListener onLongClickListener= new ItemsAdapter.OnLongClickListener()

            {

            @Override
            public void onItemLongClicked(int position) {


                //Delete the item from the model
                items.remove(position);
                //Notify the adapter

                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
         ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
             @Override
             public void onItemClicked(int position) {

                 Log.d("Main Activity", "Single Click at position" + position);

                 //create the new activity
                 Intent i = new Intent(MainActivity.this,EditActivity.class);

                 //pass the data being edited
                 i.putExtra(KEY_ITEM_TEXT, items.get(position));
                 i.putExtra(KEY_ITEM_POSITION, position);
                 //display
                startActivityForResult(i,EDIT_TEXT_CODE);

             }
         };
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        ReviewItems.setAdapter(itemsAdapter);
        ReviewItems.setLayoutManager(new LinearLayoutManager(this));

        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String todoitem = EnterItem.getText().toString();
                // Add item to the model
                items.add(todoitem);
                //notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()- 1);
                EnterItem.setText(" ");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }
    //handle result of the edit
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode== EDIT_TEXT_CODE){

            //retrieve the updated text value

            assert data != null;
            String itemText= data.getStringExtra(KEY_ITEM_TEXT);

            //extract the original position of the edited item from the position key
            int position= data.getExtras().getInt(KEY_ITEM_POSITION);

            // Update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist the change
            saveItems();

            Toast.makeText(getApplicationContext(),"Item updated succesfully!", Toast.LENGTH_SHORT).show();
        }else{
            Log.w("MainActivity", "Unknown Call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(),"data.txt");

    }
    //this function will load item by reading every line of the data file
    private void loadItems(){
        try {

            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("MainActivity", "Error reading items",e);
            items = new ArrayList<>();

        }
    }
    //this function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e){
            Log.e("MainActivity", "Error writing items",e);
        }

    }
}