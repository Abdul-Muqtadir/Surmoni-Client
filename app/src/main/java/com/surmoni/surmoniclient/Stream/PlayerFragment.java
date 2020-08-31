package com.surmoni.surmoniclient.Stream;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.surmoni.surmoniclient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.firekast.FKError;
import io.firekast.FKPlayer;
import io.firekast.FKPlayerView;
import io.firekast.FKStream;
import io.firekast.Firekast;

/**
 * Created by Francois Rouault on 01/11/2016.
 */
public class PlayerFragment extends Fragment implements FKPlayer.Callback, View.OnClickListener {

    private static final String TAG = PlayerFragment.class.getSimpleName();

    ////////database work///////////
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence;
    ////////database work///////////
    @Nullable
    public static String latestStreamId = null;
    private EditText mEditTextUrl;
    private FKPlayerView mPlayerView;
    private TextView mTextViewState;

    private ProgressDialog mLoading;
    private FKPlayer mPlayer;
    private String user_id;
    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id=bundle.getString("user_id");
        }
        return inflater.inflate(R.layout.fragment_player, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mEditTextUrl = view.findViewById(R.id.editText_url);
        mPlayerView = view.findViewById(R.id.playerView);
        mTextViewState = view.findViewById(R.id.textViewState);
        view.findViewById(R.id.button_get_time).setOnClickListener(this);
        view.findViewById(R.id.button_play).setOnClickListener(this);
        view.findViewById(R.id.pauseButton).setOnClickListener(this);
        view.findViewById(R.id.button_start).setOnClickListener(this);
        view.findViewById(R.id.button_stop).setOnClickListener(this);
        view.findViewById(R.id.button_editKey).setOnClickListener(this);
        view.findViewById(R.id.resumeButton).setOnClickListener(this);

        mPlayer = mPlayerView.getPlayer();
        mPlayer.setCallback(this);
        mPlayer.setShowPlaybackControls(true);
        mEditTextUrl.setHint("Stream id (ex. H1N_D8eex)");
        mEditTextUrl.setText(latestStreamId == null ? "bhcb7k9d5fbbielgw" : latestStreamId);
        mTextViewState.setText("" + mPlayer.getState());
        new AlertDialog.Builder(PlayerFragment.this.getContext())
                .setTitle("To use this feature:")
                .setMessage("You need to have private-key obtained from firekast.io.You can edit or enter your private-key here to initialize live stream feature.First Click START to initialize streaming on Server and after 60 secs(1 minute or on slower network wait for 5 minutes) or more.Click PLAY to watch Stream.For more info read help section.")
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayer.release();
        mDatabase = FirebaseDatabase.getInstance();
        mRefrence = mDatabase.getReference(user_id);
        mRefrence.child("Stream").child("stream_request").setValue("no");
        mRefrence.child("Stream").child("stream_status").setValue("stop");
    }

    public void showLoading() {
        mLoading = ProgressDialog.show(getContext(), null, "Loading stream...");
    }

    public void hideLoading() {
        if (mLoading == null) {
            return;
        }
        mLoading.dismiss();
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h, m, s);
    }

    // ------
    // ON CLICK
    // ------

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                onClickPlay();
                break;
            case R.id.button_start:
                onClickStart();
                break;
            case R.id.button_stop:
                onClickStop();
                break;
            case R.id.button_editKey:
                onClickEditKey();
                break;
            case R.id.button_get_time:
                onClickGetTime();
                break;
            case R.id.resumeButton:
                mPlayer.resume();
                break;
            case R.id.pauseButton:
                mPlayer.pause();
                break;

        }
    }
    void onClickStart()
    {
        mDatabase = FirebaseDatabase.getInstance();
        mRefrence = mDatabase.getReference(user_id);
        mRefrence.child("Stream").child("stream_enable_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data=dataSnapshot.getValue(String.class);
                Firekast.initialize(getContext(), data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRefrence.child("Stream").child("stream_request").setValue("yes");
        mRefrence.child("Stream").child("stream_status").setValue("start");
        ////////database work///////////
        mRefrence.child("Stream").child("stream_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Stream_id=dataSnapshot.getValue().toString();
                mEditTextUrl.setText(Stream_id);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ////////database work///////////
    }
    void onClickStop()
    {
        mDatabase = FirebaseDatabase.getInstance();
        mRefrence = mDatabase.getReference(user_id);
        mRefrence.child("Stream").child("stream_request").setValue("no");
        mRefrence.child("Stream").child("stream_status").setValue("stop");
    }

    void onClickEditKey()
    {
        StreamEnableFragment streamEnableFragment = new StreamEnableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("user_id",user_id);
        streamEnableFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = ((AppCompatActivity)getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_menu,streamEnableFragment);
        fragmentTransaction.commit();
    }
    void onClickGetTime() {
        long position = mPlayer.getCurrentPosition() / 1000L;
        String toHMmSs = convertSecondsToHMmSs(position);
        Toast.makeText(this.getContext(), "Position: " + toHMmSs, Toast.LENGTH_SHORT).show();
    }

    void onClickPlay() {
        String streamId = mEditTextUrl.getText().toString().trim();
        FKStream current = mPlayer.getCurrentStream();
        showLoading();
        if (TextUtils.isEmpty(streamId)) {
            mEditTextUrl.setError("Need a StreamID");
        } else if (current == null || !current.getId().equals(streamId)) {
            FKStream stream = FKStream.newEmptyInstance(streamId);
            mPlayer.play(stream);
        } else {
            mPlayer.play(current);
        }
    }

    // --------
    // FKPLayer Callback
    // --------

    @Override
    public void onPlayerWillPlay(@NonNull FKStream stream, @Nullable FKError error) {
        Log.v(TAG, "onPlayerWillPlay: stream " + stream + " unless error: " + error);
        if (error != null) {
            Toast.makeText(getContext(), "FKError: " + error, Toast.LENGTH_LONG).show();
        }
        hideLoading();
    }

    @Override
    public void onPlayerStateChanged(FKPlayer.State state) {
        Log.v(TAG, "onPlayerStateChanged: " + state);
        mTextViewState.setText("" + mPlayer.getState());
    }

}

