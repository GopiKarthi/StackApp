package drawertab.com.drawer_tab.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import drawertab.com.drawer_tab.MyApplication;
import drawertab.com.drawer_tab.Questions;
import drawertab.com.drawer_tab.R;
import drawertab.com.drawer_tab.holders.QListView_Holder;
import drawertab.com.drawer_tab.holders.RecyclerView_OnClickListener;
import drawertab.com.drawer_tab.holders.RecyclerView_OnClickListener.OnClickListener;


public class QListView_Recycler_Adapter extends
		RecyclerView.Adapter<QListView_Holder> {// Recyclerview will extend to
												// recyclerview adapter
	private ArrayList<Questions> arrayList;
	private Context context;

	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


	public QListView_Recycler_Adapter(Context context,
									  ArrayList<Questions> arrayList) {
		this.context = context;
		this.arrayList = arrayList;

	}

	@Override
	public int getItemCount() {
		return (null != arrayList ? arrayList.size() : 0);

	}

	@Override
	public QListView_Holder onCreateViewHolder(ViewGroup parent, int viewType) {

		// This method will inflate the custom layout and return as viewholder
		LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

		ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
				R.layout.question_list_item, parent, false);
		QListView_Holder listHolder = new QListView_Holder(mainGroup);
		return listHolder;
	}

	@Override
	public void onBindViewHolder(QListView_Holder holder, int position) {
		final Questions model = arrayList.get(position);

		QListView_Holder mainHolder = (QListView_Holder) holder;// holder

//		Bitmap image = BitmapFactory.decodeResource(context.getResources(),
//				model.getImage());// This will convert drawbale image into
		// bitmap

		// setting data over views
		mainHolder.question_txt.setText(model.getTitle());
		mainHolder.answer_txt.setText(model.getAnswer_count());

		String toK= model.getScore();
		if(toK.length()>=5)
		{
			toK = toK.substring(0,2)+"."+toK.substring(2,3)+"k";
		}
		mainHolder.upvote_txt.setText(toK);
		String dateAsText = getTimeAgo(Long.parseLong(model.getCreation_date()));
		mainHolder.date_txt.setText(dateAsText);
		mainHolder.user_posted.setText(model.getDisplay_name());
		mainHolder.question_tag_txt.setText(model.getTags());
		mainHolder.questionLinkText.setText(model.getLink());
//		mainHolder.list_imageView.setImageBitmap(image);

		// Implement click listener over layout
		mainHolder.setClickListener(new OnClickListener() {

			@Override
			public void OnItemClick(View view, int position) {
				switch (view.getId()) {
					case R.id.Qlist_layout:

						// Show a toast on clicking layout
						Toast.makeText(context,
								"Tags" + model.getTags(),
								Toast.LENGTH_LONG).show();
						break;

				}
			}

		});
		mainHolder.setOnLongClickListener(new RecyclerView_OnClickListener.OnLongClickListener() {

			@Override
			public void OnLongItemClick(View view, int position) {
				switch (view.getId()) {
					case R.id.Qlist_layout:

						// Show a toast on Long clicking layout
						Toast.makeText(context,
								"Long Click Tags" + model.getTags(),
								Toast.LENGTH_LONG).show();
						Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLink()));
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.setPackage("com.android.chrome");
						try {
							MyApplication.getInstance().startActivity(i);

						}catch (ActivityNotFoundException ex){
							i.setPackage(null);
							MyApplication.getInstance().startActivity(i);

						}

						break;

				}
			}

		});

	}
	public static String getTimeAgo(long time) {
		String thisYear = new SimpleDateFormat("yyyy").format(new Date());
		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return "just now";
		} else if (diff < 2 * MINUTE_MILLIS) {
			return "a minute ago";
		} else if (diff < 50 * MINUTE_MILLIS) {
			return diff / MINUTE_MILLIS + " minutes ago";
		} else if (diff < 90 * MINUTE_MILLIS) {
			return "an hour ago";
		} else if (diff < 24 * HOUR_MILLIS) {
			return diff / HOUR_MILLIS + " hours ago";
		} else if (diff < 48 * HOUR_MILLIS) {
			return "yesterday";
		} else if(diff / DAY_MILLIS < 10) {
			return diff / DAY_MILLIS + " days ago";
		}
		else if(thisYear ==new SimpleDateFormat("yyyy").format(new Date(time))){
			return new SimpleDateFormat("E, MMM dd").format(new Date(time));
		}

		else {

			return new SimpleDateFormat("E, MMM dd yy").format(new Date(time));
		}
	}

}
