// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class QuonActivity9 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Michael1Fragment()).commit();
            navigationView.setCheckedItem(R.id.Mic_nav_toggle);
        }

        applyTheme();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.Mic_nav_toggle) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Michael1Fragment()).commit();
        } else if (itemId == R.id.Mic_nav_search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Quon2Fragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            } else {
                return true; // Consumed the back button press event
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);
        int savedMode = sharedPreferences.getInt(getString(R.string.key_mode), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        int systemNightMode = AppCompatDelegate.getDefaultNightMode();

        if (savedMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            // If the saved mode is set to follow system, use the system's night mode setting
            AppCompatDelegate.setDefaultNightMode(systemNightMode);
        } else {
            // Otherwise, use the saved mode
            AppCompatDelegate.setDefaultNightMode(savedMode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Mic_togglemenu) {
            toggleMode();
            return true;
        }
        if (id == R.id.Mic_searchmenu) {
            showSearchDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleMode() {
        // retrieve what current mode is light/dark
        int currentMode = AppCompatDelegate.getDefaultNightMode();

        // switch/toggle mode
        int newMode = (currentMode == AppCompatDelegate.MODE_NIGHT_YES) ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES;

        // save updated mode into sharedpref
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.key_mode), newMode);
        editor.apply();

        AppCompatDelegate.setDefaultNightMode(newMode);
    }


    private EditText input;
    // AlertDialog for search
    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create a custom layout for the dialog
        View dialogLayout = getLayoutInflater().inflate(R.layout.search_dialog, null);
        builder.setView(dialogLayout);

        // Find views in the custom layout
        input = dialogLayout.findViewById(R.id.Mic_edit_text_search);
        Button searchButton = dialogLayout.findViewById(R.id.Mic_button_search);

        // Set the click listener for the search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = input.getText().toString().trim();
                if (!TextUtils.isEmpty(searchText)) {
                    launchGoogleSearch(searchText);
                    // Dismiss the dialog after launching search
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.please_enter_a_search_phrase, Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // launch Google Search
    private void launchGoogleSearch(String searchText) {
    // close keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);

        // launch google search
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, searchText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), (R.string.no_app_can_handle_this_request), Toast.LENGTH_SHORT).show();
        }
    }

}
