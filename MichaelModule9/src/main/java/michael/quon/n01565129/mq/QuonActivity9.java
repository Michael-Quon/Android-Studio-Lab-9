// Michael Quon N01565129
package michael.quon.n01565129.mq;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class QuonActivity9 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

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




}
