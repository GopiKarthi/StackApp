package drawertab.com.drawer_tab;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main_4Drawer extends AppCompatActivity {
    private static final int PROFILE_SETTING = 1;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    Fragment fragment1 = new MyPost();
    Fragment fragment2 = new StackOverflowTabsFragment();
    Fragment fragment3 = new StackApps();
    SharedPreferences pref;
    public static final String MyPREFERENCES = "MyPrefs" ;
    Boolean Authenicated;
    String Profile_URL,UserName;
    IProfile User_Profile;
    PrimaryDrawerItem logout_DrawerItem;
    private String Userid;
    Toolbar toolbar;
    private Menu menu;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_down_out);

    }
    @Override
    protected void onResume(){
        super.onResume();
        Authenicated = pref.getBoolean("authComplete", false);
        if(!Authenicated){
//            Toast.makeText(this,"Resume",Toast.LENGTH_SHORT).show();
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.frame_container, fragment2).commit();
            result.setSelection(2,true);
        }
        else{
            PostVolley_Profile(pref.getString("auth_token", null));
        }
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_4_drawer);
        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        // Create a few sample profile
        User_Profile = new ProfileDrawerItem().withName("Guest User").withIcon(R.drawable.profile4);
        logout_DrawerItem = new PrimaryDrawerItem().withName(R.string.drawer_item_signout).withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(7).withEnabled(false);
//        IProfile profile = new ProfileDrawerItem().withName("Guest");

        Authenicated = pref.getBoolean("authComplete", false);
        if(Authenicated){
            PostVolley_Profile(pref.getString("auth_token", null));
        }

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.stackoverflow_ic_45);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, fragment2).commit();



        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
//                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        User_Profile
//                        ,profile2,
//                        profile3,
//                        profile4,
//                        profile5,
//                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
//                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_dark_primary_text)).withIdentifier(PROFILE_SETTING),
//                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_feed).withIcon(FontAwesome.Icon.faw_stack_exchange).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_stackOF).withIcon(FontAwesome.Icon.faw_stack_overflow).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_stackApps).withIcon(FontAwesome.Icon.faw_bar_chart).withIdentifier(3),
                        logout_DrawerItem,
                        new SectionDrawerItem().withName(R.string.drawer_item_extra),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withEnabled(false).withIdentifier(4),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).withEnabled(false).withIdentifier(5)
                )

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        if (drawerItem != null && drawerItem.getIdentifier() == 1) {
//                            Toast.makeText(getApplicationContext(), ((Nameable) drawerItem).getName().getText(Main_4Drawer.this), Toast.LENGTH_LONG).show();
                            invalidateOptionsMenu();
                            Authenicated = pref.getBoolean("authComplete", false);
                            if (!Authenicated) {
                                Call_Dialog();
                               // openDialog();
                            }
                            ft.replace(R.id.frame_container, fragment1);
                        }

                        if (drawerItem instanceof Nameable) {
                            toolbar.setTitle(((Nameable) drawerItem).getName().getText(Main_4Drawer.this));
                        }
                        if (drawerItem != null && drawerItem.getIdentifier() == 2) {
//                            Toast.makeText(getApplicationContext(), ((Nameable) drawerItem).getName().getText(Main_4Drawer.this), Toast.LENGTH_LONG).show();
                            invalidateOptionsMenu();
                            ft.replace(R.id.frame_container, fragment2);
                        }
                        if (drawerItem != null && drawerItem.getIdentifier() == 3) {
//                            Toast.makeText(getApplicationContext(), ((Nameable) drawerItem).getName().getText(Main_4Drawer.this), Toast.LENGTH_LONG).show();
                            invalidateOptionsMenu();
                            ft.replace(R.id.frame_container, fragment3);
                        }
                        if (drawerItem != null && drawerItem.getIdentifier() == 7) {
//                            Toast.makeText(getApplicationContext(), ((Nameable) drawerItem).getName().getText(Main_4Drawer.this), Toast.LENGTH_LONG).show();
//                            headerResult.setActiveProfile(profile1);

                            openDialog();
//                            if(YesLogout)
//                                LogOut();
                        }
                        ft.commit();
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        // set the selection to the item with the identifier 2
        if (savedInstanceState == null) {
            result.setSelection(2, false);
        }


        //set the back arrow in the toolbar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
      inflater.inflate(R.menu.main, menu);
        setOptionIcon(R.id.LogInOut, R.drawable.login);
        if(Authenicated){
            setOptionIcon(R.id.LogInOut,R.drawable.logout);
        }

        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle the click on the back arrow click
        Boolean value = pref.getBoolean("authComplete", false);
        switch (item.getItemId()) {
            case android.R.id.home:
//                onBackPressed();
                result.openDrawer();
                return true;
            case R.id.LogInOut:
                // create intent to perform web search for this planet
                if(!value) {
                    Intent i = new Intent(getApplicationContext(), OAuthLogin.class);
                    startActivity(i);
                }
                else{
                        openDialog();
//                        LogOut();

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void setOptionIcon(int id, int iconRes)
    {
        MenuItem item = menu.findItem(id);
        item.setIcon(iconRes);
    }

    private void LogOut() {
        MyApplication myApplication = MyApplication.getInstance();
        myApplication.clearApplicationData();
        Toast.makeText(getApplicationContext(), "Logout Successfully", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("authComplete", false);
        Authenicated =false;
        edit.commit();
//        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
//        if(!f.equals(fragment2)){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.frame_container,fragment2).setCustomAnimations(R.anim.push_left_in,R.anim.push_up_out).commit();
//
//        } result.setSelection(2,true); does this job well so commenting it out
        Profile_URL = null;
        UserName = null;

        List<IProfile> lp= new ArrayList<>();
        lp.add(new ProfileDrawerItem().withName("Guest").withIcon(R.drawable.profile4));
        headerResult.setProfiles(lp);
        result.setSelection(2, true);
        result.updateItem(logout_DrawerItem.withEnabled(false));
        invalidateOptionsMenu();



    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.push_right_in, R.anim.push_down_out);
        }
    }
    public void Call_Dialog(){
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .theme(Theme.DARK)
                .content("You need an account to view Posts.")
                .positiveText("Login")
                .positiveColor(getResources().getColor(R.color.colorPrimaryLight))
                .negativeColor(getResources().getColor(R.color.colorPrimaryLight))
                .negativeText("Not Now");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                // TODO
                Toast.makeText(getApplicationContext(), "Positive", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), OAuthLogin.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);
                startActivity(i);
            }
        })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        Toast.makeText(getApplicationContext(), "onNeutral", Toast.LENGTH_LONG).show();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
//                        Toast.makeText(getApplicationContext(), "Negative", Toast.LENGTH_LONG).show();
                    }
                })
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
//                        Toast.makeText(getApplicationContext(), "onAny", Toast.LENGTH_LONG).show();
                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        // TODO
//                        Toast.makeText(getApplicationContext(), "showListener", Toast.LENGTH_LONG).show();
                    }
                })
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_container, fragment2).setCustomAnimations(R.anim.push_left_in, R.anim.push_up_out).commit();
                        result.setSelection(2);
//                        Toast.makeText(getApplicationContext(), "cancelListener", Toast.LENGTH_LONG).show();
                    }
                })
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO
//                        Toast.makeText(getApplicationContext(), "dismissListener", Toast.LENGTH_LONG).show();


                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

    }
    private void openDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialoglayout);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnDismiss = (Button)dialog.getWindow().findViewById(R.id.dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btnLogout = (Button)dialog.getWindow().findViewById(R.id.LogOutButton);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LogOut();
                dialog.dismiss();

            }
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }
    public  void PostVolley_Profile(String accessToken){
        String JSON_URL = "https://api.stackexchange.com/2.2/me?access_token="+accessToken+"&site=stackoverflow&key=TKJxJqfiNKrcEL0VNCg74g((";
//        String JSON_URL = "https://api.stackexchange.com/2.2/me?access_token=dIyuZr4JjRLWYeCv(T5Xyw))&site=stackoverflow&key=TKJxJqfiNKrcEL0VNCg74g((";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{


                            JSONArray ja = response.getJSONArray("items");
                            JSONObject jsonObject = ja.getJSONObject(0);
                            UserName = jsonObject.getString("display_name");
                            Profile_URL = jsonObject.getString("profile_image");
                            //JSONObject owner_obj = jsonObject.getJSONObject("owner");
                            Userid = jsonObject.getString("user_id");

                            if(UserName!=null&&Profile_URL!=null) {
                                SharedPreferences.Editor edit = pref.edit();
                                edit.putString("UserName", UserName);
                                edit.putString("Profile_URL", Profile_URL);
                                edit.putString("Userid", Userid);
                                edit.commit();
                                User_Profile = new ProfileDrawerItem().withName(UserName).withIcon(Profile_URL);
                                List<IProfile> lp= new ArrayList<>();
                                lp.add(User_Profile);
                                headerResult.setProfiles(lp);
                                result.updateItem(logout_DrawerItem.withEnabled(true));
                            }

                            

                        }catch(JSONException e){e.printStackTrace();

                            Log.v("Chumma", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}