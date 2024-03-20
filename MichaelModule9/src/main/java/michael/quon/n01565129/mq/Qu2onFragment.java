// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Qu2onFragment extends Fragment {
    private ToggleButton fileType;
    private EditText fileName, fileContents;

    public Qu2onFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quon2, container, false);

        // Initialize UI components
        fileName = view.findViewById(R.id.Mic_activity_internalstorage_filename);
        fileContents = view.findViewById(R.id.Mic_activity_internalstorage_filecontents);
        fileType = view.findViewById(R.id.Mic_activity_internalstorage_filetype);
        fileType.setChecked(true);

        // Set onClickListeners for buttons
        view.findViewById(R.id.Mic_activity_internalstorage_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(requireContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.Mic_activity_internalstorage_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(requireContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.Mic_activity_internalstorage_write).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeFile(requireContext(), fileType.isChecked());
            }
        });

        view.findViewById(R.id.Mic_activity_internalstorage_read).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFile(requireContext(), fileType.isChecked());
            }
        });

        return view;
    }

    // Method to create a file
    private void createFile(Context context, boolean isPersistent) {
        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
                Toast.makeText(context, String.format(getString(R.string.file_has_been_created), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, String.format(getString(R.string.file_creation_failed), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, String.format(getString(R.string.file_already_exists), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to write data to a file
    private void writeFile(Context context, boolean isPersistent) {
        try {
            FileOutputStream fileOutputStream;
            if (isPersistent) {
                fileOutputStream = context.openFileOutput(fileName.getText().toString(), Context.MODE_PRIVATE);
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileOutputStream = new FileOutputStream(file);
            }
            fileOutputStream.write(fileContents.getText().toString().getBytes(Charset.forName("UTF-8")));
            Toast.makeText(context, String.format(getString(R.string.write_to_successful), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, String.format(getString(R.string.write_to_file_failed), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to read data from a file
    private void readFile(Context context, boolean isPersistent) {
        try {
            FileInputStream fileInputStream;
            if (isPersistent) {
                fileInputStream = context.openFileInput(fileName.getText().toString());
            } else {
                File file = new File(context.getCacheDir(), fileName.getText().toString());
                fileInputStream = new FileInputStream(file);
            }

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            fileContents.setText(TextUtils.join("\n", lines));
            Toast.makeText(context, String.format(getString(R.string.read_from_file_successful), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, String.format(getString(R.string.read_from_file_failed), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
            fileContents.setText(getString(R.string.blank));
        }
    }

    // Method to delete a file
    private void deleteFile(Context context, boolean isPersistent) {
        File file;
        if (isPersistent) {
            file = new File(context.getFilesDir(), fileName.getText().toString());
        } else {
            file = new File(context.getCacheDir(), fileName.getText().toString());
        }
        if (file.exists()) {
            file.delete();
            Toast.makeText(context, String.format(getString(R.string.file_deleted), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, String.format(getString(R.string.file_not_existent), fileName.getText().toString()), Toast.LENGTH_SHORT).show();
        }
    }
}
