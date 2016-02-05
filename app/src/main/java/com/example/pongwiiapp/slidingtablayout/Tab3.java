package com.example.pongwiiapp.slidingtablayout;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pongwiiapp.db.SearchHelper;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
public class Tab3 extends Fragment implements SearchView.OnQueryTextListener,
        SearchView.OnCloseListener  {

    private ListView myList;
    private SearchView searchView;
    private SearchHelper mDbHelper;
    private MyCustomAdapter defaultAdapter;
    private ArrayList<String> nameList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_3,container,false);


        nameList = new ArrayList<String>();

        //for simplicity we will add the same name for 20 times to populate the nameList view
        for (int i = 0; i < SearchHelper.COLUMN_NAME.length(); i++) {
            nameList.add(SearchHelper.COLUMN_NAME);
        }


        for (int i = 0; i < 20; i++) {
            nameList.add("Diana" + i);
        }



        //relate the listView from java to the one created in xml
        myList = (ListView) v.findViewById(R.id.list);

        //show the ListView on the screen
        // The adapter MyCustomAdapter is responsible for maintaining the data backing this nameList and for producing
        // a view to represent an item in that data set.
        defaultAdapter = new MyCustomAdapter(getContext(), nameList);
        myList.setAdapter(defaultAdapter);

        //prepare the SearchView
        searchView = (SearchView) v.findViewById(R.id.search);

        //Sets the default or resting state of the search field. If true, a single search icon is shown by default and
        // expands to show the text field and other buttons when pressed. Also, if the default state is iconified, then it
        // collapses to that state when the close button is pressed. Changes to this property will take effect immediately.
        //The default value is true.
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);

        mDbHelper = new SearchHelper(getContext());
        mDbHelper.open();

        //Clear all names
        mDbHelper.deleteAllNames();

        // Create the list of names which will be displayed on search
        for (String name : nameList) {
            mDbHelper.createList(name);
        }
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mDbHelper  != null) {
            mDbHelper.close();
        }
    }

    @Override
    public boolean onClose() {
        myList.setAdapter(defaultAdapter);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        displayResults(query + "*");
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            displayResults(newText + "*");
        } else {
            myList.setAdapter(defaultAdapter);
        }

        return false;
    }

    private void displayResults(String query) {

        Cursor cursor = mDbHelper.searchByInputText((query != null ? query : "@@@@"));

        if (cursor != null) {

            String[] from = new String[]{SearchHelper.COLUMN_NAME};

            // Specify the view where we want the results to go
            int[] to = new int[]{R.id.search_result_text_view};

            // Create a simple cursor adapter to keep the search data
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.result_search_item, cursor, from, to);
            myList.setAdapter(cursorAdapter);

            // Click listener for the searched item that was selected
            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) myList.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String selectedName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    Toast.makeText(getContext(), selectedName, Toast.LENGTH_SHORT).show();

                    // Set the default adapter
                    myList.setAdapter(defaultAdapter);

                    // Find the position for the original list by the selected name from search
                    for (int pos = 0; pos < nameList.size(); pos++) {
                        if (nameList.get(pos).equals(selectedName)) {
                            position = pos;
                            break;
                        }
                    }
                    final int finalPosition = position;
                    // Create a handler. This is necessary because the adapter has just been set on the list again and
                    // the list might not be finished setting the adapter by the time we perform setSelection.
                    Handler handler = new Handler() {
                        @Override
                        public void close() {
                            myList.setSelection(finalPosition);
                        }

                        @Override
                        public void flush() {
                            myList.setSelection(finalPosition);
                        }

                        @Override
                        public void publish(LogRecord record) {
                            myList.setSelection(finalPosition);
                        }


                    };
                    searchView.setQuery("", true);
                }
            });

        }
    }


}
