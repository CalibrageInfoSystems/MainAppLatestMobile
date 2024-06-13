package com.oilpalm3f.mainapp.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import android.widget.ListView;
import android.widget.Toast;


public class MultipleSelectionSpinner extends AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {
    String[] _items = null;
    int[] mIds = null;
    boolean[] mSelection = null;
    ArrayAdapter<String> simple_adapter;
    AlertDialog dialog;
    private OnItemSelectedListener listener;

    public MultipleSelectionSpinner(Context context) {
        super(context);
        init(context);
    }

    public MultipleSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        simple_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            ListView listView = ((AlertDialog) dialog).getListView();

            if (which == 0) { // "Select All" option clicked
                for (int i = 0; i < mSelection.length; i++) {
                    mSelection[i] = isChecked;
                    listView.setItemChecked(i, isChecked);
                }
            } else {
                mSelection[which] = isChecked;
                if (!isChecked) {
                    mSelection[0] = false;
                    listView.setItemChecked(0, false);
                } else {
                    boolean allSelected = true;
                    for (int i = 1; i < mSelection.length; i++) {
                        if (!mSelection[i]) {
                            allSelected = false;
                            break;
                        }
                    }
                    if (allSelected) {
                        mSelection[0] = true;
                        listView.setItemChecked(0, true);
                    }
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds");
        }
    }

    public void selectAll() {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = true;
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void clearSelection() {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }
    @Override
    public boolean performClick() {
        if (_items == null || _items.length == 0) {
            simple_adapter.clear();
            simple_adapter.add("Tap to select");
            Toast.makeText(getContext(), "Items are not available", Toast.LENGTH_SHORT).show();
            return true;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(_items, mSelection, this);
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean anySelected = false;
                for (boolean selected : mSelection) {
                    if (selected) {
                        anySelected = true;
                        break;
                    }
                }

                if (!anySelected) {
                    // Manually add "Tap to select" when no items are selected
                    simple_adapter.clear();
                    simple_adapter.add("Tap to select");
                }
                else {
                    simple_adapter.clear();
                    simple_adapter.add(buildSelectedItemString());
                }

                if (listener != null) {
                    listener.onItemSelected(MultipleSelectionSpinner.this, -1, getSelectedItemsWithIds(), true);
                }
                dialog.dismiss();
            }
        });

        dialog = builder.show();
        return true;
    }

//    @Override
//    public boolean performClick() {
////        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
////        builder.setMultiChoiceItems(_items, mSelection, this);
////        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                simple_adapter.clear();
////                simple_adapter.add(buildSelectedItemString());
////                if (listener != null) {
////                    listener.onItemSelected(MultipleSelectionSpinner.this, which, getSelectedItemsWithIds(), true);
////                }
////
////            }
////        });
////        dialog = builder.show();
////        return true;
//        if (_items == null || _items.length == 0) {
//            //     Toast.makeText(getContext(), "Items are not available", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMultiChoiceItems(_items, mSelection, this);
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (_items == null || _items.length == 0) {
//                    return;
//                }
//
//                simple_adapter.clear();
//                simple_adapter.add(buildSelectedItemString());
////                simple_adapter.clear();
////                simple_adapter.add(buildSelectedItemString());
////                if (listener != null) {
////                    listener.onItemSelected(MultipleSelectionSpinner.this, which, getSelectedItemsWithIds(), true);
////                }
//                if (_items == null || _items.length == 0) {
//          Toast.makeText(getContext(), "Items are not available", Toast.LENGTH_SHORT).show();
//                } else {
//                    simple_adapter.clear();
//                    simple_adapter.add(buildSelectedItemString());
//                    if (listener != null) {
//                        listener.onItemSelected(MultipleSelectionSpinner.this, -1, getSelectedItemsWithIds(), true);
//                    }
//                    dialog.dismiss();
//                }
//
//            }
//        });
//        dialog = builder.show();
//        return true;
//    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException("setAdapter is not supported by MultipleSelectionSpinner.");
    }


    public void setItems(List<String> items, List<Integer> ids) {
        _items = items.toArray(new String[0]);
        mSelection = new boolean[_items.length];
        mIds = new int[_items.length]; // Initialize mIds array
        simple_adapter.clear();
        simple_adapter.add("Tap to select");
        Arrays.fill(mSelection, false);

        // Populate mIds array with corresponding IDs
        for (int i = 0; i < _items.length; i++) {
            if (i < ids.size()) {
                mIds[i] = ids.get(i);
            } else {
                // If there are fewer IDs than items, set corresponding ID to 0 or any default value
                mIds[i] = 0; // Default value
            }
        }
    }

    public void setSelection(String[] selection) {
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndices) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int index : selectedIndices) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
            } else {
                throw new IllegalArgumentException("Index " + index + " is out of bounds.");
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 1; i < _items.length; ++i) { // Start from 1 to skip "Select All"
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    public List<Integer> getSelectedIndices() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 1; i < _items.length; ++i) { // Start from 1 to skip "Select All"
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 1; i < _items.length; ++i) { // Start from 1 to skip "Select All"
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        return buildSelectedItemString();
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public LinkedHashMap<String, Integer> getSelectedItemsWithIds() {
        LinkedHashMap<String, Integer> selectedItemsWithIds = new LinkedHashMap<>();
        for (int i = 0; i < _items.length; i++) {
            if (mSelection[i]) {
                selectedItemsWithIds.put(_items[i], mIds[i]); // Assuming mIds array stores corresponding IDs
            }
        }
        return selectedItemsWithIds;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(View view, int position, LinkedHashMap<String, Integer> selectedItemsWithIds, boolean isSelected);
    }
}
