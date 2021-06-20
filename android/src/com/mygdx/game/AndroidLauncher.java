package com.mygdx.game;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.AdMetadataListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.mygdx.game.MyGdxGame;

import java.util.Arrays;

public class AndroidLauncher extends AndroidApplication implements AdsController, RewardedVideoAdListener {
	private RewardedVideoAd rewardedVideoAd;
	public boolean adWatchedFully = false;
	private boolean firstLoad = true;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MyGdxGame(this), config);

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});
		rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
		rewardedVideoAd.setRewardedVideoAdListener(this);
		loadRewardedAd();
		firstLoad = false;
	}

	@Override
	public void loadRewardedAd() {

//		rewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
		rewardedVideoAd.loadAd("ca-app-pub-5641955555269458/9570341526", new AdRequest.Builder().build());
	}

	@Override
	public void showRewardedAd() {
		adWatchedFully = false;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(rewardedVideoAd.isLoaded()){
					rewardedVideoAd.show();
				}else {
					loadRewardedAd();
				}
			}
		});

	}

	@Override
	public boolean adWatchedFully() {
		return adWatchedFully;
	}

	@Override
	public boolean adLoaded() {
		return rewardedVideoAd.isLoaded();
	}

	@Override
	public void onRewardedVideoAdLoaded() {
	}

	@Override
	public void onRewardedVideoAdOpened() {

	}

	@Override
	public void onRewardedVideoStarted() {

	}

	@Override
	public void onRewardedVideoAdClosed() {
		loadRewardedAd();
	}

	@Override
	public void onRewarded(RewardItem rewardItem) {

	}

	@Override
	public void onRewardedVideoAdLeftApplication() {

	}

	@Override
	public void onRewardedVideoAdFailedToLoad(int i) {
		if(!firstLoad) {
			Toast.makeText(this, "Servers are out of ads, try again soon!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onRewardedVideoCompleted() {
		adWatchedFully = true;
	}
}
