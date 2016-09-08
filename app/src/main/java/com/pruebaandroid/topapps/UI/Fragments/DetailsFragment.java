package com.pruebaandroid.topapps.UI.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.viewanimator.ViewAnimator;
import com.pruebaandroid.topapps.DataObjects.Entry;
import com.pruebaandroid.topapps.EventBus.EntryBus;
import com.pruebaandroid.topapps.R;
import com.pruebaandroid.topapps.Utils.ImageTransroundedcorner;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {


    TextView mAplicationName,mSummary,mArtis,mPriceAmount,mRelaseDetail,mCategory;
    ImageView mAppIcon;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_appication_details, container, false);
        mAplicationName = (TextView)rootView.findViewById(R.id.aplication_name);
        mSummary = (TextView)rootView.findViewById(R.id.sumary);
        mArtis = (TextView)rootView.findViewById(R.id.artist);
        mPriceAmount = (TextView)rootView.findViewById(R.id.priceamount);
        mRelaseDetail = (TextView)rootView.findViewById(R.id.relasedate);
        mCategory = (TextView)rootView.findViewById(R.id.category);

        mAppIcon = (ImageView)rootView.findViewById(R.id.appiconaplication);

        return rootView;
    }
    @Override public void onResume() {
        super.onResume();
        EntryBus.getInstance().register(this);
    }

    @Override public void onPause() {
        super.onPause();
        EntryBus.getInstance().unregister(this);
    }
    @Subscribe
    public void onEntryChanged(Entry entry) {
        ViewAnimator
                .animate(rootView)
                .translationX(-1000, 0)
                .duration(300)
                .alpha(0,1).start();
        mAplicationName.setText(entry.getName());
        Picasso.with(getContext())
                .load(entry.getUrlimagen())
                .resize(150, 150)
                .transform(new ImageTransroundedcorner())
                .into(mAppIcon);
        mArtis.setText(entry.getArtist());
        mCategory.setText(String.format(getString(R.string.category),entry.getCategory()));
        mPriceAmount.setText(entry.getPricecurrency()+entry.getPriceamount());
        mSummary.setText(String.format(getString(R.string.sumary),entry.getSummary()));
        mRelaseDetail.setText(String.format(getString(R.string.relasedate),entry.getReleaseDate()));

    }
}
