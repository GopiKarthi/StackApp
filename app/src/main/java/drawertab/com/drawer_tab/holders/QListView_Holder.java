package drawertab.com.drawer_tab.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import drawertab.com.drawer_tab.R;

public class QListView_Holder extends RecyclerView.ViewHolder implements
		OnClickListener,RecyclerView_OnClickListener.OnLongClickListener {
	// View holder for list recycler view as we used in listview
	public TextView upvote_txt, answer_txt, question_txt,date_txt,question_tag_txt,user_posted,questionLinkText;
	public ImageView list_imageView;
	public LinearLayout listLayout;

	private RecyclerView_OnClickListener.OnClickListener onClickListener;
	private RecyclerView_OnClickListener.OnLongClickListener onLongClickListener;

	public QListView_Holder(View view) {
		super(view);

		// Find all views ids
		this.upvote_txt = (TextView) view.findViewById(R.id.upvotes_count);
		this.answer_txt = (TextView) view.findViewById(R.id.answers_count);
		this.question_txt = (TextView) view.findViewById(R.id.question);
		this.question_tag_txt = (TextView) view.findViewById(R.id.question_tag);
		this.date_txt = (TextView) view.findViewById(R.id.question_date);
		this.user_posted = (TextView) view.findViewById(R.id.userPosted);
		this.questionLinkText = (TextView) view.findViewById(R.id.QuestionLink);
//		this.list_imageView = (ImageView) view
//				.findViewById(R.id.list_imageview);

		this.listLayout = (LinearLayout) view.findViewById(R.id.Qlist_layout);

		// Implement click listener over views that we need

		this.listLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		// setting custom listener
		if (onClickListener != null) {
			onClickListener.OnItemClick(v, getAdapterPosition());

		}

	}

	// Setter for listener
	public void setClickListener(
			RecyclerView_OnClickListener.OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public void setOnLongClickListener(RecyclerView_OnClickListener.OnLongClickListener onLongClickListener){
		this.onLongClickListener=onLongClickListener;
	}

	@Override
	public void OnLongItemClick(View view, int position) {
		if(onLongClickListener !=null){
			onLongClickListener.OnLongItemClick(view,getAdapterPosition());
		}
	}
}