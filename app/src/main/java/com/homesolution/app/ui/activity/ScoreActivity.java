package com.homesolution.app.ui.activity;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tech.freak.wizardpager.model.AbstractWizardModel;
import com.tech.freak.wizardpager.model.ModelCallbacks;
import com.tech.freak.wizardpager.model.Page;
import com.tech.freak.wizardpager.ui.PageFragmentCallbacks;
import com.tech.freak.wizardpager.ui.ReviewFragment;
import com.tech.freak.wizardpager.ui.StepPagerStrip;
import com.homesolution.app.Global;
import com.youtube.sorcjc.proyectoprofesionales.R;
import com.homesolution.app.io.response.CalificarResponse;
import com.homesolution.app.io.HomeSolutionApiAdapter;
import com.homesolution.app.ui.wizard.ScoreWizardModel;
import com.homesolution.app.ui.wizard.pages.CustomerInfoPage;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/*
    This activity works with the Wizard Pager library.
    It allows users to assign a rating to the selected professional.
 */
public class ScoreActivity extends AppCompatActivity implements
        PageFragmentCallbacks, ReviewFragment.Callbacks, ModelCallbacks {

    // Global variables
    private static Global global;
    private static Activity scoreActivity;

    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;

    private boolean mEditingAfterReview;

    private static AbstractWizardModel mWizardModel;
    private static Context context;

    private boolean mConsumePageSelectedEvent;

    private Button mNextButton;
    private Button mPrevButton;

    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;

    // Selected worker to qualify
    private static String pid;
    private String name;

    // User authenticated data
    private static String token;

    private static int getScorePoints(String quality) {
        final String very_good = scoreActivity.getString(R.string.score_very_good);
        final String good = scoreActivity.getString(R.string.score_good);
        final String bad = scoreActivity.getString(R.string.score_bad);
        // final String very_bad = getString(R.string.score_very_bad);

        if (quality.equals(very_good)) return 4;
        else if (quality.equals(good)) return 3;
        else if (quality.equals(bad)) return 2;
        else return 1;
    }

    private static int getCommendPoints(String commend) {
        if (commend.equals("Sí")) {
            return 3;
        } else if (commend.equals("No sé")) {
            return 1;
        } else { // "No"
            return 0;
        }
    }

    private static String getCategoryId(String categoryName) {
        return getGlobal().getCategoryId(categoryName);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Static
        scoreActivity = this;
        context = getApplicationContext();
        mWizardModel = new ScoreWizardModel(this);

        // Bundle parameters from previous activity
        if (pid == null) {
            Bundle b = getIntent().getExtras();
            pid = b.getString("pid");
            name = b.getString("name");
            loadAuthenticatedUser();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        toolbar.setTitle(name);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        // Global variables instance
        global = getGlobal();

        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }

        mWizardModel.registerListener(this);

        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip
                .setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
                    @Override
                    public void onPageStripSelected(int position) {
                        position = Math.min(mPagerAdapter.getCount() - 1,
                                position);
                        if (mPager.getCurrentItem() != position) {
                            mPager.setCurrentItem(position);
                        }
                    }
                });

        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);

        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);

                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }

                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    DialogFragment dg = new ConfirmDialogFragment();
                    dg.show(getSupportFragmentManager(), "place_order_dialog");
                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);
                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    private static Global getGlobal() {
        if (global == null)
            global = (Global) context;

        return global;
    }

    private void loadAuthenticatedUser() {
        token = getGlobal().getToken();
    }

    public static class ConfirmDialogFragment extends DialogFragment {

        public ConfirmDialogFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.submit_confirm_message)
                    .setPositiveButton(R.string.submit_confirm_button, new confirmButtonHandler())
                    .setNegativeButton(android.R.string.cancel, null).create();
        }

    }

    public static class confirmButtonHandler implements DialogInterface.OnClickListener, Callback<CalificarResponse> {

        public void onClick(DialogInterface dialog, int id) {
            // Score from 1 to 4 points
            String _puntualidad = mWizardModel.findByKey("Puntualidad").getData().getString(Page.SIMPLE_DATA_KEY);
            String _profesionalismo = mWizardModel.findByKey("Profesionalismo").getData().getString(Page.SIMPLE_DATA_KEY);
            String _cumplimiento = mWizardModel.findByKey("Cumplimiento").getData().getString(Page.SIMPLE_DATA_KEY);
            String _precio = mWizardModel.findByKey("Precio").getData().getString(Page.SIMPLE_DATA_KEY);
            // Log.d("Test/Score", "Puntualidad selected choice => " + _puntualidad);
            // Commend?
            String _commend = mWizardModel.findByKey("Precio").getData().getString(Page.SIMPLE_DATA_KEY);
            // Custom question
            String _category = mWizardModel.findByKey("Tipo de trabajo").getData().getString(CustomerInfoPage.CATEGORY_DATA_KEY);
            // Possible field
            String addComment = mWizardModel.findByKey("Desea añadir un comentario?").getData().getString(Page.SIMPLE_DATA_KEY);
            String comments = "";
            if (addComment!=null && addComment.equals("Sí"))
                comments = mWizardModel.findByKey("Sí:Comentarios").getData().getString(Page.SIMPLE_DATA_KEY);

            // Convert the selected options in points
            int puntualidad = getScorePoints(_puntualidad);
            int profesionalismo = getScorePoints(_profesionalismo);
            int cumplimiento = getScorePoints(_cumplimiento);
            int precio = getScorePoints(_precio);
            int commend = getCommendPoints(_commend);
            String categoryId = getCategoryId(_category);

            Call<CalificarResponse> call = HomeSolutionApiAdapter.getApiService(getGlobal().getCountry())
                                                                    .getCalificar(token, pid, puntualidad, profesionalismo, cumplimiento, commend, precio, categoryId, comments);
            call.enqueue(this);

            scoreActivity.finish();
        }

        @Override
        public void onResponse(Response<CalificarResponse> response, Retrofit retrofit) {
            if (response.body() == null)
                return;

            if (response.body().getStatus() == 0)
                Toast.makeText(scoreActivity, response.body().getError(), Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(scoreActivity, scoreActivity.getString(R.string.score_saved), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailure(Throwable t) {
            Toast.makeText(scoreActivity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 =
        // review
        // step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    private void updateBottomBar() {
        int position = mPager.getCurrentItem();
        if (position == mCurrentPageSequence.size()) {
            mNextButton.setText(R.string.finish);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
        } else {
            mNextButton.setText(mEditingAfterReview ? R.string.review
                    : R.string.next);
            mNextButton
                    .setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v,
                    true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton
                .setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);
            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {
                return new ReviewFragment();
            }

            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position,
                                   Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            return Math.min(mCutOffPage + 1, mCurrentPageSequence == null ? 1
                    : mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }
}
