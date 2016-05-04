package tn.iac.mobiledevelopment.mekelti.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import tn.iac.mobiledevelopment.mekelti.Fragment.FragmentAddRecette;
import tn.iac.mobiledevelopment.mekelti.Fragment.FragmentFavoris;
import tn.iac.mobiledevelopment.mekelti.Fragment.FragmentNewsFeed;
import tn.iac.mobiledevelopment.mekelti.Fragment.ProposedFragment;
import tn.iac.mobiledevelopment.mekelti.Fragment.SearchFragment;
import tn.iac.mobiledevelopment.mekelti.Model.User;
import tn.iac.mobiledevelopment.mekelti.R;
import tn.iac.mobiledevelopment.mekelti.Service.CompteManager;
import tn.iac.mobiledevelopment.mekelti.Utils.Utils;

/**
 * Created by S4M37 on 17/04/2016.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private android.support.v4.app.FragmentManager fragmentManager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView userName;
    private User user;
    private Toolbar toolbar;
    private View headerLayout;

    public User getUser() {
        return user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user = CompteManager.getCurrentUser(this);
        insializeDrawer();
        insializeView();
        fragmentManager = getSupportFragmentManager();
        showFragment(new FragmentNewsFeed());
        Utils.token = CompteManager.getToken(this);
    }

    private void insializeView() {
        userName = (TextView) headerLayout.findViewById(R.id.name_user);
        userName.setText(user.getName());
    }

    private void insializeDrawer() {
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        headerLayout = navigationView.getHeaderView(0);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        toolbar.setSubtitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment != null) {
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main:
                showFragment(new FragmentNewsFeed());
                break;
            case R.id.addRecette:
                showFragment(new FragmentAddRecette());
                break;
            case R.id.logout:
                CompteManager.logout(MainActivity.this);
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
            case R.id.favoris:
                showFragment(FragmentFavoris.newInstance());
                break;
            case R.id.proposed:
                showFragment(new ProposedFragment());
                break;
            case R.id.search:
                showFragment(SearchFragment.newInstance());
                break;
        }
        closeDrawer();
        return true;
    }

    private void closeDrawer() {
        drawer.closeDrawers();
    }
}
