package com.bryanching.hotslogsample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bryanching.hotslogsample.Presenter.HotsLogPresenter;
import com.bryanching.hotslogsample.squidb.HotsLogUser;
import com.bryanching.hotslogsample.squidb.MmrLog;
import com.jakewharton.rxbinding.view.RxView;
import com.yahoo.squidb.data.SquidCursor;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotsLogFragment extends Fragment {

    public static int MASTERS = 0;
    public static int DIAMOND = 1;
    public static int PLATINUM = 2;
    public static int GOLD = 3;
    public static int SILVER = 4;
    public static int BRONZE = 5;

    HotsLogPresenter mHotsLogPresenter;

    static Map<Integer, Integer> leagueToColor = new HashMap<>();
    static Map<Integer, Integer> leagueToIcon = new HashMap<>();

    static {
        leagueToColor.put(MASTERS, R.color.amber900);
        leagueToColor.put(DIAMOND, R.color.amber700);
        leagueToColor.put(PLATINUM, R.color.amber500);
        leagueToColor.put(GOLD, R.color.amber300);
        leagueToColor.put(SILVER, R.color.amber200);
        leagueToColor.put(BRONZE, R.color.amber100);

        leagueToIcon.put(MASTERS, R.drawable.icon_master);
        leagueToIcon.put(DIAMOND, R.drawable.icon_diamond);
        leagueToIcon.put(PLATINUM, R.drawable.icon_platinum);
        leagueToIcon.put(GOLD, R.drawable.icon_gold);
        leagueToIcon.put(SILVER, R.drawable.icon_silver);
        leagueToIcon.put(BRONZE, R.drawable.icon_bronze);
    }

    @BindView(R.id.update_button)
    Button mUpdateButton;

    @BindView(R.id.last_updated)
    TextView mLastUpdatedTv;

    @BindView(R.id.qmSort)
    Button mQmSortButton;

    @BindView(R.id.hlSort)
    Button mHlSortButton;

    @BindView(R.id.tlSort)
    Button mTlSortButton;

    @BindView(R.id.my_recycler_view)
    RecyclerView mMmrTable;

    @BindView(R.id.id_edit_text)
    EditText mEditText;

    @BindView(R.id.add_id_button)
    Button mInsertButton;

    public HotsLogFragment() {
        // Required empty public constructor
    }

    public static HotsLogFragment newInstance() {
        return new HotsLogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hots_log, container, false);
        ButterKnife.bind(this, view);

        RxView.clicks(mUpdateButton).subscribe(aVoid -> {
            mHotsLogPresenter.onUpdatePressed();
        });

        RxView.longClicks(mUpdateButton).subscribe(aVoid -> {
            mHotsLogPresenter.addKnownUsers();
        });

        RxView.clicks(mQmSortButton).subscribe(aVoid -> {
            mHotsLogPresenter.sortByQm();
        });

        RxView.clicks(mHlSortButton).subscribe(aVoid -> {
            mHotsLogPresenter.sortByHl();
        });

        RxView.clicks(mTlSortButton).subscribe(aVoid -> {
            mHotsLogPresenter.sortByTl();
        });

        RxView.clicks(mInsertButton).subscribe(aVoid -> {
            mHotsLogPresenter.onInsertId(mEditText.getText().toString());
            mEditText.getText().clear();
            // Check if no view has focus:
            View focusedView = getActivity().getCurrentFocus();
            if (focusedView != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService
                        (Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        mHotsLogPresenter = new HotsLogPresenter(getActivity(), this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mMmrTable.setLayoutManager(mLayoutManager);
        return view;
    }

    public void swapCursor(SquidCursor<HotsLogUser> cursor) {
        MmrTableAdapter mAdapter = new MmrTableAdapter(cursor);
        mMmrTable.swapAdapter(mAdapter, false);
    }

    public class MmrTableAdapter extends RecyclerView.Adapter<MmrTableAdapter.ViewHolder> {
        private SquidCursor<HotsLogUser> mHotsLogUserCursor;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            @BindView(R.id.name)
            TextView mNameTv;

            @BindView(R.id.qmMmrTv)
            TextView mQmMmrTv;
            @BindView(R.id.qmMmrLinearLayout)
            LinearLayout mQmMmrLinearLayout;
            @BindView(R.id.qmMmrIv)
            ImageView mQmMmrIcon;

            @BindView(R.id.hlMmrTv)
            TextView mHlMmrTv;
            @BindView(R.id.hlMmrLinearLayout)
            LinearLayout mHlMmrLinearLayout;
            @BindView(R.id.hlMmrIv)
            ImageView mHlMmrIcon;

            @BindView(R.id.tlMmrTv)
            TextView mTlMmrTv;
            @BindView(R.id.tlMmrLinearLayout)
            LinearLayout mTlMmrLinearLayout;
            @BindView(R.id.tlMmrIv)
            ImageView mTlMmrIcon;

            public ViewHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MmrTableAdapter(SquidCursor<HotsLogUser> hotsLogUserCursor) {
            mHotsLogUserCursor = hotsLogUserCursor;
            setHasStableIds(true);
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MmrTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_main, parent,
                    false);
            // set the view's size, margins, paddings and layout parameters
            return new ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            mHotsLogUserCursor.moveToPosition(position);

            Long PlayerID = mHotsLogUserCursor.get(HotsLogUser.PLAYER_ID);
            String Name = mHotsLogUserCursor.get(HotsLogUser.PLAYER_NAME);
//            Integer CurrentMMR = mHotsLogUserCursor.get(MmrLog.HL_LEAGUE_ID);

            RxView.clicks(holder.mNameTv).subscribe(aVoid -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("http://hotslogs.com/Player/Profile?PlayerID=" + PlayerID));
                startActivity(i);
            });
            holder.mNameTv.setText(Name);
            setupTv(holder.mQmMmrLinearLayout, holder.mQmMmrTv, holder.mQmMmrIcon,
                    mHotsLogUserCursor
                            .get(MmrLog.QM_MMR), mHotsLogUserCursor.get(MmrLog.QM_LEAGUE_ID));
            setupTv(holder.mHlMmrLinearLayout, holder.mHlMmrTv, holder.mHlMmrIcon,
                    mHotsLogUserCursor
                            .get(MmrLog.HL_MMR), mHotsLogUserCursor.get(MmrLog.HL_LEAGUE_ID));
            setupTv(holder.mTlMmrLinearLayout, holder.mTlMmrTv, holder.mTlMmrIcon,
                    mHotsLogUserCursor
                            .get(MmrLog.TL_MMR), mHotsLogUserCursor.get(MmrLog.TL_LEAGUE_ID));
        }

        private void setupTv(LinearLayout linearLayout, TextView textView, ImageView imageView,
                             Integer currentMmr, Integer leagueId) {
            textView.setText(String.valueOf(currentMmr));
            Integer backgroundColorRes = leagueToColor.get(leagueId);
            if (backgroundColorRes == null) {
                backgroundColorRes = android.R.color.white;
            }
            linearLayout.setBackgroundColor(ContextCompat.getColor(getContext(),
                    backgroundColorRes));
            Integer iconRes = leagueToIcon.get(leagueId);
            if (iconRes != null) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), iconRes));
            } else {
                imageView.setImageDrawable(null);
            }
        }

        @Override
        public int getItemCount() {
            return mHotsLogUserCursor.getCount();
        }

        @Override
        public long getItemId(int position) {
            mHotsLogUserCursor.moveToPosition(position);
            Long PlayerID = mHotsLogUserCursor.get(HotsLogUser.PLAYER_ID);
            String Name = mHotsLogUserCursor.get(HotsLogUser.PLAYER_NAME);
            Integer CurrentMMR = mHotsLogUserCursor.get(MmrLog.HL_LEAGUE_ID);
            int result = PlayerID != null ? PlayerID.hashCode() : 0;
            result = 31 * result + (Name != null ? Name.hashCode() : 0);
            result = 31 * result + (CurrentMMR != null ? CurrentMMR : 0);
            return result;
        }
    }

    public void setLastUpdated(String formattedTime) {
        mLastUpdatedTv.setText(formattedTime);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHotsLogPresenter.onResume();
    }
}
