package com.surmoni.surmoniclient.MenuFragments;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.surmoni.surmoniclient.Feature_Fragments.BatteryFragment;
import com.surmoni.surmoniclient.Feature_Fragments.ClientIdFragment;
import com.surmoni.surmoniclient.Feature_Fragments.HumiditySensorFragment;
import com.surmoni.surmoniclient.Feature_Fragments.MotionDetectionFragment;
import com.surmoni.surmoniclient.Feature_Fragments.TemperatureSensorFragment;
import com.surmoni.surmoniclient.R;
import com.surmoni.surmoniclient.Stream.PlayerFragment;
import com.surmoni.surmoniclient.Stream.StreamEnableFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Provides UI for the view with List.
 */
public class ListMenuContentFragment extends Fragment {
    private static String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_id=bundle.getString("user_id");
        }

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.main_menu_recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView description;

        private FirebaseDatabase mDatabase;
        private DatabaseReference mRefrence;

        public ViewHolder(LayoutInflater inflater, final ViewGroup parent) {
            super(inflater.inflate(R.layout.main_menu_item_list, parent, false));
            avator = (ImageView) itemView.findViewById(R.id.list_avatar);
            name = (TextView) itemView.findViewById(R.id.list_title);
            description = (TextView) itemView.findViewById(R.id.list_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Context context = v.getContext();
                    int pos=getAdapterPosition();
                    switch (pos)
                    {
                        case 0: {
                            TemperatureSensorFragment temperatureSensorFragment = new TemperatureSensorFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", user_id);
                            temperatureSensorFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout_menu, temperatureSensorFragment);
                            fragmentTransaction.commit();
                            break;
                        }
                        case 1:{
                            HumiditySensorFragment humiditySensorFragment = new HumiditySensorFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id",user_id);
                            humiditySensorFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout_menu,humiditySensorFragment);
                            fragmentTransaction.commit();
                            break;
                        }

                        case 2:{
                            mDatabase= FirebaseDatabase.getInstance();
                            mRefrence=mDatabase.getReference(user_id);
                            mRefrence.child("Stream").child("stream_enable_id").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String data=dataSnapshot.getValue(String.class);
                                    if(data.equals("nulll"))
                                    {
                                        StreamEnableFragment streamEnableFragment = new StreamEnableFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_id",user_id);
                                        streamEnableFragment.setArguments(bundle);

                                        FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_layout_menu,streamEnableFragment);
                                        fragmentTransaction.commit();
                                    }
                                    else
                                        {
                                            PlayerFragment playerFragment = new PlayerFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putString("user_id",user_id);
                                            playerFragment.setArguments(bundle);

                                            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.frame_layout_menu,playerFragment);
                                            fragmentTransaction.commit();
                                        }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            break;
                        }

                        case 3:{
                            MotionDetectionFragment motionDetectionFragment = new MotionDetectionFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id",user_id);
                            motionDetectionFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout_menu,motionDetectionFragment);
                            fragmentTransaction.commit();
                            break;
                        }

                        case 4:{
                            BatteryFragment batteryFragment = new BatteryFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("user_id",user_id);
                            batteryFragment.setArguments(bundle);

                            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout_menu,batteryFragment);
                            fragmentTransaction.commit();
                            break;
                        }
                        case 5:
                            {
                                ClientIdFragment clientIdFragment = new ClientIdFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("user_id",MenuMainActivity.getData());
                                clientIdFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame_layout_menu,clientIdFragment);
                                fragmentTransaction.commit();
                                break;
                            }
                    }
                    //Intent intent = new Intent(context, DetailActivity.class);
                    //intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    //context.startActivity(intent);
                }
            });
        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 6;
        private final String[] mMenus;
        private final String[] mMenuItemDesc;
        private final Drawable[] mMenuAvators;
        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mMenus = resources.getStringArray(R.array.menus);
            mMenuItemDesc = resources.getStringArray(R.array.menu_item_desc);
            TypedArray MenuItems = resources.obtainTypedArray(R.array.menu_items);
            mMenuAvators = new Drawable[MenuItems.length()];
            for (int i = 0; i < mMenuAvators.length; i++) {
                mMenuAvators[i] = MenuItems.getDrawable(i);
            }
            MenuItems.recycle();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.avator.setImageDrawable(mMenuAvators[position % mMenuAvators.length]);
            holder.name.setText(mMenus[position % mMenus.length]);
            holder.description.setText(mMenuItemDesc[position % mMenuItemDesc.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
