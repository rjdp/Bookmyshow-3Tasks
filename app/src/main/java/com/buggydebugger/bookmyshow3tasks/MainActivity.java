

package com.buggydebugger.bookmyshow3tasks;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.buggydebugger.bookmyshow3tasks.expandlistview.CustomArrayAdapter;
import com.buggydebugger.bookmyshow3tasks.expandlistview.ExpandableListItem;
import com.buggydebugger.bookmyshow3tasks.expandlistview.ExpandingListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new LaunchpadSectionFragment();
                case 1:
                    return new PostFragment();
                case 2:
                    return  new VideoFragment();

                default:
                    // The other sections of the app are dummy placeholders.
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Task #" + (position + 1);
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class LaunchpadSectionFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_splash, container, false);

            // Demonstration of a collection-browsing activity.
            rootView.findViewById(R.id.demo_splash)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                            startActivity(intent);
                        }
                    });



            return rootView;
        }
    }


    public static class VideoFragment extends Fragment {
        private View rootView;
        private VideoView videoView;
        public VideoFragment(){

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
           rootView = inflater.inflate(R.layout.video_fragment_layout, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
             videoView =(VideoView)rootView.findViewById(R.id.videoView1);

            //Creating MediaController
            MediaController mediaController= new MediaController(getContext());
            mediaController.setAnchorView(videoView);

            //specify the location of media file


            //Setting MediaController and URI, then starting the videoView
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/"
                    + R.raw.vdo));
//            videoView.requestFocus();


        }
        @Override
        public void setMenuVisibility(final boolean visible) {
            super.setMenuVisibility(visible);
            if (visible) {
                videoView.start();
            }
            else{
                if(videoView!=null)
                videoView.stopPlayback();
            }
        }

    }

    /**
     * Posts Fragment
     */

    public static class PostFragment extends Fragment {
        private ExpandingListView mListView;
        private ProgressBar spinner;
        private CustomArrayAdapter adapter;
        private Button retryButton;
   private View rootView;
       private List<ExpandableListItem> mData;
        public PostFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Add this line in order for this fragment to handle menu events.
            setHasOptionsMenu(true);

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.postfragment, menu);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.action_refresh) {
                populate();
                return true;
            }
            if(id==R.id.show_dummy_data){show_dummy();}
            return super.onOptionsItemSelected(item);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Create some dummy data for the ListView.  Here's a sample weekly forecast



             mData = new ArrayList<ExpandableListItem>();

            adapter = new CustomArrayAdapter(getActivity(), R.layout.item_post, mData);
             rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mListView = (ExpandingListView) rootView.findViewById(R.id.listview_post);
            mListView.setAdapter(adapter);
            mListView.setDivider(null);

//        ArrayList<Post> arrayOfPosts = new ArrayList<Post>();
//
//        // Now that we have some dummy forecast data, create an ArrayAdapter.
//        // The ArrayAdapter will take data from a source (like our dummy forecast) and
//        // use it to populate the ListView it's attached to.
//        mForecastAdapter =
//                new PostsAdapter(getActivity(),arrayOfPosts);

            rootView.findViewById(R.id.progressBar).setVisibility(View.GONE);
            retryButton=(Button)rootView.findViewById(R.id.retry_button);
            retryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    populate();

                }
            });
            spinner = (ProgressBar)rootView.findViewById(R.id.progressBar);


            return rootView;
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {

            populate();
            super.onViewCreated(view, savedInstanceState);
        }
public void show_dummy(){
    spinner.setVisibility(View.GONE);
    mListView.setVisibility(View.VISIBLE);
    retryButton.setVisibility(View.GONE);
    mData.clear();
    mData.add(new ExpandableListItem(1, 1, "Dummy title 1", 200, "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Temporibus quibusdam culpa, aliquid, adipisci tenetur, atque natus distinctio praesentium amet quas autem possimus sit laudantium explicabo blanditiis, libero quaerat provident pariatur?"));
    mData.add(new ExpandableListItem(2,2, "Dummy title 2",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ipsum veniam voluptates illo expedita laudantium, magni dolorem voluptatibus! Culpa similique dolore architecto totam, ullam molestiae nobis cum consequatur, rerum. Similique, quidem!"));
    mData.add(new ExpandableListItem(1,3, "Dummy title 3",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quis sed velit possimus esse explicabo, quas corrupti et earum id porro cum, distinctio ipsa adipisci alias iste doloribus molestiae incidunt natus."));
    mData.add(new ExpandableListItem(3,4, "Dummy title 4",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Pariatur reiciendis quos commodi laborum perferendis rem nam alias nulla nostrum adipisci neque beatae eaque vitae aut corrupti impedit ut, necessitatibus. Rem."));
    mData.add(new ExpandableListItem(4,5, "Dummy title 5",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Fugiat minus hic facilis pariatur suscipit beatae at provident nisi dicta laudantium facere incidunt rerum dolorum odio aliquid vero repellat, officiis quia."));
    mData.add(new ExpandableListItem(5,6, "Dummy title 6",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Temporibus quibusdam culpa, aliquid, adipisci tenetur, atque natus distinctio praesentium amet quas autem possimus sit laudantium explicabo blanditiis, libero quaerat provident pariatur?"));
    mData.add(new ExpandableListItem(2,7, "Dummy title 7",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ipsum veniam voluptates illo expedita laudantium, magni dolorem voluptatibus! Culpa similique dolore architecto totam, ullam molestiae nobis cum consequatur, rerum. Similique, quidem!"));
    mData.add(new ExpandableListItem(7,8, "Dummy title 8",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Quis sed velit possimus esse explicabo, quas corrupti et earum id porro cum, distinctio ipsa adipisci alias iste doloribus molestiae incidunt natus."));
    mData.add(new ExpandableListItem(3,9, "Dummy title 9",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Pariatur reiciendis quos commodi laborum perferendis rem nam alias nulla nostrum adipisci neque beatae eaque vitae aut corrupti impedit ut, necessitatibus. Rem."));
    mData.add(new ExpandableListItem(5,10, "Dummy title 10",200,"Lorem ipsum dolor sit amet, consectetur adipisicing elit. Fugiat minus hic facilis pariatur suscipit beatae at provident nisi dicta laudantium facere incidunt rerum dolorum odio aliquid vero repellat, officiis quia."));
adapter.addAll(mData);
}
public void populate(){
    if(isOnline()){
        mData.clear();
        rootView.findViewById(R.id.listview_post).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.retry_button).setVisibility(View.GONE);
        FetchPostTask postTask = new FetchPostTask();
        postTask.execute();

    }
    else{
        rootView.findViewById(R.id.listview_post).setVisibility(View.GONE);
        rootView.findViewById(R.id.retry_button).setVisibility(View.VISIBLE);
    }

}

        public  boolean isOnline() {
            ConnectivityManager cm =
                    (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return  ( activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting());
        }


        public class FetchPostTask extends AsyncTask<String, Void, List<ExpandableListItem>> {

            private final String LOG_TAG = FetchPostTask.class.getSimpleName();



            private String cncat(int a, int b,String c, String d) {



                return a+"/"+b+"/"+c+"/"+d;
            }


            private List<ExpandableListItem> getPostDataFromJson(String postJsonStr)
                    throws JSONException {

                final String ID = "id";
                final String USER_ID = "userId";
                final String TITLE = "title";
                final String BODY = "body";

                postJsonStr="{\"jarr\" : "+postJsonStr+"}";
                Log.v(LOG_TAG, "Forecast entry: " + postJsonStr);
                JSONObject forecastJson = new JSONObject(postJsonStr);
                JSONArray jsonArray = forecastJson.getJSONArray("jarr");
                List<ExpandableListItem> posts = new ArrayList<ExpandableListItem>(jsonArray.length());
                for(int i = 0; i < jsonArray.length(); i++) {

                    int id;
                    int userId;
                    String title;
                    String body;

                    JSONObject post = jsonArray.getJSONObject(i);

                    id = post.getInt(ID);
                    userId = post.getInt(USER_ID);
                    title = post.getString(TITLE);
                    body = post.getString(BODY);
                    posts.add(new ExpandableListItem(userId,id, title,200,body));

                }



                for (ExpandableListItem s : posts) {
                    Log.v(LOG_TAG, "Forecast entry: " + cncat(s.mid, s.muserId, s.mTitle, s.mText));
                }
                return posts;

            }
            @Override
            protected List<ExpandableListItem> doInBackground(String... params) {

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String postJsonStr = null;

                String format = "json";
                String units = "metric";
                int numDays = 7;

                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are avaiable at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast
                    final String POST_BASE_URL =
                            "http://jsonplaceholder.typicode.com";


                    Uri builtUri = Uri.parse(POST_BASE_URL).buildUpon()
                            .appendPath("posts")
                            .build();

                    URL url = new URL(builtUri.toString());

                    Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    postJsonStr = buffer.toString();

                    Log.v(LOG_TAG, "Forecast string: " + postJsonStr);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(LOG_TAG, "Error closing stream", e);
                        }
                    }
                }

                try {
                    return getPostDataFromJson(postJsonStr);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPreExecute() {
//            super.onPreExecute();

                spinner.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(List<ExpandableListItem> result) {
                if (result != null) {
                    adapter.clear();
//                for(Post post : result) {
                    adapter.addAll(result);

//                }

                    spinner.setVisibility(View.GONE);
                }
            }
        }
    }




    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
            Bundle args = getArguments();
            ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                    getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }





}
