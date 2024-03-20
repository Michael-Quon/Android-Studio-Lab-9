// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Mi1chaelFragment extends Fragment {

    private EditText courseNameEdt, courseDescEdt;
    private Button addBtn, deleteBtn;
    private RecyclerView courseRV;

    private ArrayList<CourseModal> courseModalArrayList;
    private CourseAdapter adapter;

    public Mi1chaelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_michael1, container, false);

        // Initializing UI components
        courseNameEdt = view.findViewById(R.id.Mic_idEdtCourseName);
        courseDescEdt = view.findViewById(R.id.Mic_idEdtCourseDescription);
        addBtn = view.findViewById(R.id.Mic_idBtnAdd);
        deleteBtn = view.findViewById(R.id.Mic_delete_button);
        courseRV = view.findViewById(R.id.Mic_idRVCourses);

        // Load data from SharedPreferences
        loadDataFromSharedPreferences();

        // Build RecyclerView
        buildRecyclerView();

        // Add button click listener
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add course to the list
                String name = courseNameEdt.getText().toString();
                String desc = courseDescEdt.getText().toString();
                if (!name.isEmpty() && !desc.isEmpty()) {
                    courseModalArrayList.add(new CourseModal(name, desc));
                    adapter.notifyDataSetChanged(); // Notify RecyclerView adapter
                    saveDataToSharedPreferences(); // Save data to SharedPreferences
                    courseNameEdt.setText(R.string.blank); // Clear course name field
                    courseDescEdt.setText(R.string.blank); // Clear course description field
                } else {
                    Toast.makeText(getContext(), (R.string.please_fill_in_both_fields), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete button click listener
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the list of courses
                courseModalArrayList.clear();
                adapter.notifyDataSetChanged(); // Notify RecyclerView adapter
                clearSharedPreferences(); // Clear data in SharedPreferences
                Toast.makeText(getContext(), (R.string.deleted_all_courses), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void buildRecyclerView() {
        adapter = new CourseAdapter(courseModalArrayList, getContext());
        courseRV.setLayoutManager(new LinearLayoutManager(getContext()));
        courseRV.setAdapter(adapter);
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.courses), null);
        Type type = new TypeToken<ArrayList<CourseModal>>() {}.getType();
        courseModalArrayList = gson.fromJson(json, type);

        if (courseModalArrayList == null) {
            courseModalArrayList = new ArrayList<>();
        }
    }

    private void saveDataToSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(courseModalArrayList);
        editor.putString(getString(R.string.courses), json);
        editor.apply();

        Toast.makeText(getContext(), (R.string.saved_data_to_sharedpreferences), Toast.LENGTH_SHORT).show();
    }

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_preferences), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.courses));
        editor.apply();
    }
}
