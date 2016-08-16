package drawertab.com.drawer_tab;

/**
 * Created by gopinath.munusamy on 8/13/2016.
 */

        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter ;
        import android.support.v4.app.FragmentStatePagerAdapter;

public class QuestionsSortPagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public QuestionsSortPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                QuestionsSortByVotes byVotes = new QuestionsSortByVotes();
                return byVotes;
            case 1:
                QuestionsSortByActivity byActivity = new QuestionsSortByActivity();
                return byActivity;
            case 2:
                QuestionsSortByHot byHot = new QuestionsSortByHot();
                return byHot;
            case 3:
                QuestionsSortByDate byDate = new QuestionsSortByDate();
                return byDate;
            case 4:
                QuestionsSortByMonth byMonth = new QuestionsSortByMonth();
                return byMonth;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
