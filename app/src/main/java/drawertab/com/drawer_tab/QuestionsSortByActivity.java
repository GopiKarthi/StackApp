package drawertab.com.drawer_tab;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import drawertab.com.drawer_tab.adapter.QListView_Recycler_Adapter;
import drawertab.com.drawer_tab.holders.RecyclerItemClickListener;


public class QuestionsSortByActivity extends Fragment {
	private static final String BUNDLE_RECYCLER_LAYOUT = "recycler.layout";

	private static View view;
	private static RecyclerView listRecyclerView;
	private static ArrayList<Questions> QuestionsList;
	private static QListView_Recycler_Adapter qadapter;


	// String array for title, location, year
	private static RelativeLayout bottomLayout;
	private static LinearLayoutManager mLayoutManager;
	public Parcelable recyclerViewState;

	// Variables for scroll listener
	private boolean userScrolled = true;
	int pastVisiblesItems, visibleItemCount, totalItemCount;
	int page_next;
	ProgressBar spinner;


	public QuestionsSortByActivity() {

	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.question_sort_by_activity, container,
				false);
		page_next = 1; // First time on postvolley to get first page

		spinner = (ProgressBar)view.findViewById(R.id.progressBar2);

		spinner.setVisibility(View.VISIBLE);
		QuestionsList = new ArrayList<>();
		init();
		PostVolley(page_next);
		return view;
	}

	// Initialize the view
	private void init() {

		bottomLayout = (RelativeLayout) view
				.findViewById(R.id.loadItemsLayout_recyclerView);

		mLayoutManager = new LinearLayoutManager(getActivity());
		listRecyclerView = (RecyclerView) view
				.findViewById(R.id.linear_recyclerview);
		listRecyclerView.setLayoutManager(mLayoutManager);// for
															// linear
															// data
															// display
															// we
															// use
															// linear
															// layoutmanager
		listRecyclerView.addItemDecoration(
				new DividerItemDecoration(getActivity(), R.drawable.divider));
		listRecyclerView.setHasFixedSize(true);
		listRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
											 int newState) {

				super.onScrollStateChanged(recyclerView, newState);

				// If scroll state is touch scroll then set userScrolled
				// true
				if (newState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
					userScrolled = true;

				}

			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx,
								   int dy) {

				super.onScrolled(recyclerView, dx, dy);
				// Here get the child count, item count and visibleitems
				// from layout manager

				visibleItemCount = mLayoutManager.getChildCount();
				totalItemCount = mLayoutManager.getItemCount();
				pastVisiblesItems = mLayoutManager
						.findFirstVisibleItemPosition();

				// Now check if userScrolled is true and also check if
				// the item is end then update recycler view and set
				// userScrolled to false
				if (userScrolled
						&& (visibleItemCount + pastVisiblesItems) == totalItemCount) {
					userScrolled = false;

					updateRecyclerView();
				}

			}

		});
		listRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(),
				new RecyclerItemClickListener.OnItemClickListener() {
					TextView tags;

					public void onItemClick(View childView, int position) {
						tags = (TextView)childView.findViewById(R.id.question_tag);
						Toast.makeText(getActivity(),"Tags: "+tags.getText().toString(),Toast.LENGTH_LONG).show();

					}

					@Override
					public void onItemLongPress(View childView, int position) {
						tags = (TextView)childView.findViewById(R.id.question_tag);
						Toast.makeText(getActivity(),"Tags: "+tags.getText().toString(),Toast.LENGTH_LONG).show();

						TextView usernameItem = (TextView)childView.findViewById(R.id.question);
						TextView quesLink = (TextView)childView.findViewById(R.id.QuestionLink);
//						usernameItem.setOnLongClickListener(new View.OnLongClickListener() {
//							@Override
//							public boolean onLongClick(View v) {
//
//								Toast.makeText(getContext(), "Not Avaialable", Toast.LENGTH_LONG).show();
//								return false;
//							}
//						});
//						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(quesLink.toString()));
						String url = quesLink.getText().toString();
						Uri uri = Uri.parse("googlechrome://navigate?url="+url);
						Intent i = new Intent(Intent.ACTION_VIEW, uri);
						i.setAction("android.intent.action.VIEW");
						i.addCategory("android.intent.category.BROWSABLE");
						i.setData(uri);
						try {

							try
							{
								startActivity(i);
							}
							catch (Exception e)
							{
								e.printStackTrace();
								i = new Intent(getActivity(), OAuthLogin.class);
								i.putExtra("QuestionLink", url);
								try {

									try
									{
										startActivity(i);
									}
									catch (Exception ex)
									{
										e.printStackTrace();
									}
								}catch (Exception ex) {

								}
							}
						}catch (Exception ex) {

						}
//						Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//						intent.putExtra(SearchManager.QUERY, usernameItem.getText().toString());
//						intent.putExtra(SearchManager.QUERY, ((AppCompatActivity) getActivity()).getSupportActionBar().getTitle());
						// catch event that there's no activity to handle intent
//						if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
//							startActivity(intent);
//						} else {
//							Toast.makeText(getContext(), "Not Avaialable", Toast.LENGTH_LONG).show();
//						}

					}
				}));

	}

	// Implement scroll listener
//	private void implementScrollListener() {
//		listRecyclerView
//				.addOnScrollListener(new RecyclerView.OnScrollListener() {
//
//					@Override
//					public void onScrollStateChanged(RecyclerView recyclerView,
//													 int newState) {
//
//						super.onScrollStateChanged(recyclerView, newState);
//
//						// If scroll state is touch scroll then set userScrolled
//						// true
//						if (newState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//							userScrolled = true;
//
//						}
//
//					}
//
//					@Override
//					public void onScrolled(RecyclerView recyclerView, int dx,
//										   int dy) {
//
//						super.onScrolled(recyclerView, dx, dy);
//						// Here get the child count, item count and visibleitems
//						// from layout manager
//
//						visibleItemCount = mLayoutManager.getChildCount();
//						totalItemCount = mLayoutManager.getItemCount();
//						pastVisiblesItems = mLayoutManager
//								.findFirstVisibleItemPosition();
//
//						// Now check if userScrolled is true and also check if
//						// the item is end then update recycler view and set
//						// userScrolled to false
//						if (userScrolled
//								&& (visibleItemCount + pastVisiblesItems) == totalItemCount) {
//							userScrolled = false;
//
//							updateRecyclerView();
//						}
//
//					}
//
//				});
//
//	}

	// Method for repopulating recycler view
	private void updateRecyclerView() {
		// Show Progress Layout
		bottomLayout.setVisibility(View.VISIBLE);

		// Handler to show refresh for a period of time you can use async task
		// while commnunicating serve

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				page_next+=1;
				PostVolley(page_next);
				// Toast for task completion
//				Toast.makeText(getActivity(), "Items Updated.",
//						Toast.LENGTH_SHORT).show();

				// After adding new data hide the view.
				bottomLayout.setVisibility(View.GONE);

			}
		}, 5000);
	}
	public  void PostVolley(int page){
		String JSON_URL = "https://api.stackexchange.com/2.2/questions?page="+page+"&order=desc&sort=activity&site=stackoverflow";
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try{


							JSONArray ja = response.getJSONArray("items");
							JsonPraser jsonPraser = new JsonPraser();
							if(!jsonPraser.JsonPraser(ja).isEmpty())
							{

								recyclerViewState = listRecyclerView.getLayoutManager().onSaveInstanceState();
								QuestionsList.addAll(jsonPraser.JsonPraser(ja));
								qadapter = new QListView_Recycler_Adapter(MyApplication.getInstance().getApplicationContext(),QuestionsList);
								listRecyclerView.setAdapter(qadapter);// set adapter on recyclerview

								qadapter.notifyDataSetChanged();// Notify the adapter
								listRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
							}
							else {
								Toast.makeText(MyApplication.getInstance().getApplicationContext(),"Reached End",Toast.LENGTH_SHORT).show();
							}


							spinner.setVisibility(View.GONE);
						}catch(JSONException e){e.printStackTrace();

							Log.v("Chumma", e.toString());
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e("Volley", Error.class.toString());
						spinner.setVisibility(View.GONE);
						Toast.makeText(MyApplication.getInstance().getApplicationContext(),"Loaded from Preset, Error at Server api's",Toast.LENGTH_SHORT).show();
						////////////////////// ADDING THIS FOR TESTING PURPOSE/////////////////////////////////////////////
						QuestionsList.clear();
						for(int i=0;i<20;i++) {
							QuestionsList.add(new Questions("javascript cloudflare vue-resource", "reputation", "user_id", "user_type", "accept_rate", "profile_image", "Gopi Karthi",
									"link", "is_answered", "view_count", "protected_date", "accepted_answer_id", "80", "150", "last_activity_date",
									"25656468", "last_edit_date", "question_id", "http://stackoverflow.com/questions/38962522/ajax-response-data-to-vue-resource-served-via-cloudflare-not-being-parsed-from-j", "title"));

							QuestionsList.add(new Questions("javascript cloudflare vue-resource", "reputation", "user_id", "user_type", "accept_rate", "profile_image", "Anto Vinish",
									"link", "is_answered", "view_count", "protected_date", "accepted_answer_id", "80", "150", "last_activity_date",
									"25656468", "last_edit_date", "question_id", "http://stackoverflow.com/questions/38962521/app-crash-in-ipad-need-help-in-understanding-the-crash-report", "title"));
						}
						qadapter = new QListView_Recycler_Adapter(getActivity(),QuestionsList);
						listRecyclerView.setAdapter(qadapter);// set adapter on recyclerview

						qadapter.notifyDataSetChanged();// Notify the adapter
						listRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
						////////////////////// ADDING THIS FOR TESTING PURPOSE/////////////////////////////////////////////



					}
				}
		);
		RequestQueue requestQueue = Volley.newRequestQueue(MyApplication.getInstance().getApplicationContext());
		requestQueue.add(jsonObjectRequest);
	}

}
