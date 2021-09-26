package org.telegram.crypto.currency_ui;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.crypto.Tools.RequestMan;
import org.telegram.crypto.models.Currency;
import org.telegram.crypto.models.CurrencyResponse;
import org.telegram.crypto.models.Data;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBarPopupWindow;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LayoutHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyList extends BaseFragment implements LifecycleOwner {
    private TextView status;
    private ScrollView scrollView;
    private Runnable cancelOnDestroyRunnable;

    private ActionBarPopupWindow.ActionBarPopupWindowLayout hintPopupLayout;
    private CurrencyAdapter currencyAdapter;
    private AnimatorSet actionBarAnimator;

    private int currentType;

    private boolean changingPasscode;
    private boolean exportingWords;
    private boolean resumeCreation;
    private String[] secretWords;
    private String[] hintWords;
    private int passcodeType;
    private String checkingPasscode;
    private CharSequence sendText;
    private boolean backToWallet;

    private boolean globalIgnoreTextChange;

    private CurrencyList fragmentToRemove;

    private long showTime;
    private ArrayList<Integer> checkWordIndices;

    public static final int TYPE_CREATE = 0;
    public static final int TYPE_KEY_GENERATED = 1;
    public static final int TYPE_READY = 2;
    public static final int TYPE_TOO_BAD = 3;
    public static final int TYPE_24_WORDS = 4;
    public static final int TYPE_WORDS_CHECK = 5;
    public static final int TYPE_IMPORT = 6;
    public static final int TYPE_PERFECT = 7;
    public static final int TYPE_SET_PASSCODE = 8;
    public static final int TYPE_SEND_DONE = 9;
    public boolean is_active = false;
    private static int item_logout = 1;


    HorizontalScrollView horizontalScrollView;
    ScrollView scrollViewX;

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

    public CurrencyList(int type) {
        super();
        currentType = type;
        showTime = SystemClock.uptimeMillis();
    }

    @Override
    public boolean onFragmentCreate() {
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (currentType == TYPE_WORDS_CHECK || currentType == TYPE_SET_PASSCODE) {
            AndroidUtilities.removeAdjustResize(getParentActivity(), classGuid);
        }
        if (currentType == TYPE_SET_PASSCODE) {
            getTonController().finishSettingUserPasscode();
        }
        if (cancelOnDestroyRunnable != null) {
            AndroidUtilities.cancelRunOnUIThread(cancelOnDestroyRunnable);
            cancelOnDestroyRunnable = null;
        }
        if (Build.VERSION.SDK_INT >= 23 && AndroidUtilities.allowScreenCapture() && currentType != TYPE_SEND_DONE && currentType != TYPE_CREATE) {
            AndroidUtilities.setFlagSecure(this, false);
        }
    }

    @Override
    public View createView(Context context) {
        if (swipeBackEnabled = canGoBack() && (currentType != TYPE_CREATE || !BuildVars.TON_WALLET_STANDALONE)) {
            actionBar.setBackButtonImage(R.drawable.ic_ab_back);
            if (currentType == TYPE_WORDS_CHECK) {
                swipeBackEnabled = false;
            }
        }
        actionBar.setBackgroundDrawable(null);
        actionBar.setTitleColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        actionBar.setItemsColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2), false);
        actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarWhiteSelector), false);
        actionBar.setCastShadows(false);
        actionBar.setAddToContainer(false);
        actionBar.setBackgroundColor(Color.CYAN);
        if (!AndroidUtilities.isTablet()) {
            actionBar.showActionModeTop();
        }
        if (currentType == TYPE_CREATE) {
            Log.d("wallet_", "create view");
            status = new TextView(context);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Toolbar toolbar = new Toolbar(context);
                toolbar.setTitle("Currency");
            }

            status.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText2));
            status.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            status.setText("Request");
            status.setGravity(Gravity.CENTER_VERTICAL);
            actionBar.addView(status, LayoutHelper.createFrame(LayoutHelper.WRAP_CONTENT,
                    LayoutHelper.WRAP_CONTENT,
                    Gravity.CENTER,
                    0, 0, 22, 0));
            status.setOnClickListener(v -> {
                sync_currencies(context);
            });

        }
        scrollView = new ScrollView(context);
        scrollView.setFillViewport(true);
        scrollView.setVerticalScrollBarEnabled(true);
        scrollView.setHorizontalScrollBarEnabled(true);
        FrameLayout frameLayout = new FrameLayout(context);
        scrollView.addView(frameLayout, LayoutHelper.createScroll(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT,  Gravity.TOP));

        frameLayout.addView(setup_ui(context), LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT,  Gravity.TOP, 0, 0, 0, 0));


        ViewGroup container = new ViewGroup(context) {
            private boolean ignoreLayout;

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = MeasureSpec.getSize(heightMeasureSpec);
                ignoreLayout = true;
                actionBar.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
                frameLayout.setPadding(0, actionBar.getMeasuredHeight(), 0, 0);
                scrollView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                ignoreLayout = false;

                setMeasuredDimension(width, height);
            }

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                actionBar.layout(0, 0, actionBar.getMeasuredWidth(), actionBar.getMeasuredHeight());
                int y = actionBar.getMeasuredHeight();
                scrollView.layout(0, 0, scrollView.getMeasuredWidth(), scrollView.getMeasuredHeight());
            }

            @Override
            public void requestLayout() {
                if (ignoreLayout) {
                    return;
                }
                super.requestLayout();
            }
        };

        container.addView(actionBar);
        container.addView(scrollView);
        fragmentView = container;
        return fragmentView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public boolean onBackPressed() {
        return canGoBack();
    }

    private boolean canGoBack() {
        return currentType != TYPE_READY && currentType != TYPE_SEND_DONE;
    }

    public RelativeLayout setup_ui(Context context) {
        horizontalScrollView = new HorizontalScrollView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(ScrollView.SCROLL_AXIS_HORIZONTAL);
        horizontalScrollView.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(params);
        RecyclerView currency_list = new RecyclerView(context);
        currency_list.setLayoutManager(new LinearLayoutManager(context));
        currency_list.setLayoutParams(params);
        currencyAdapter = new CurrencyAdapter(context, 0);
        currency_list.setAdapter(currencyAdapter);


        horizontalScrollView.addView(currency_list);
        relativeLayout.addView(horizontalScrollView);
        sync_currencies(context);

        relativeLayout.setBackgroundColor(Color.GRAY);
        return relativeLayout;
        //return currency_list;
    }
    public void sync_currencies(Context context) {
        if (!is_active) {
            status.setText("Loading");
            is_active = true;
            Call<CurrencyResponse> call = RequestMan.service(context)
                    .load_currency(
                            RequestMan.START,
                            RequestMan.LIMIT,
                            RequestMan.CONVERT,
                            RequestMan.TOKEN
                    );
            call.enqueue(new Callback<CurrencyResponse>() {
                @Override
                public void onResponse(@NonNull Call<CurrencyResponse> call,
                                       @NonNull Response<CurrencyResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        status.setText("successfully loaded");
                        Log.d("repos","success: ");
                        int error_code = response.body().getStatus().getError_code();
                        if (error_code == 0) {
                            CurrencyResponse raw = response.body();
                            if(raw.getData().size() > 0){
                                prepare_data(raw.getData());
                            }
                        }else {
                            Log.d("repos","error: "+error_code);
                        }
                    }else {
                        status.setText("Error! click me to refresh");
                        Log.d("repos","error: "+response.code());
                    }
                    is_active = false;
                }
                @Override
                public void onFailure(@NonNull Call<CurrencyResponse> call, @NonNull Throwable t) {
                    is_active = false;
                    status.setText("Error! click me to refresh");
                    Log.d("repos","error: "+t.toString());
                }
            });
        }
    }

    public void prepare_data(List<Data> data_list){
        List<Currency> currencies = new ArrayList<>();
        Currency c = new Currency(0, "", "Name","","","Price","24h%",
                "7d%","Market Cap","Volume(24h)");
        currencies.add(c);
        int x;

        for (Data data:data_list){
            currencies.add(toLocalModel(data));
        }
        Log.d("repos","raw list size: "+data_list.size());
        Log.d("repos","converted list size: "+currencies.size());
        if(currencies.size() > 0){
            currencyAdapter.setCurrencies(currencies);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(() -> {
                //doaOperation.update_currencies(currencies);
                handler.post(() -> {
                    //UI Thread work here
                });
            });
        }
    }
    public Currency toLocalModel(Data data){
        return new Currency(
                data.getId(),RequestMan.icon_url+ data.getId()+".png",data.getName(),data.getSymbol(),data.getSlug(),data.getQuote().getUsd().getPrice(),
                data.getQuote().getUsd().getPercent_change_24h(),data.getQuote().getUsd().getPercent_change_7d(),
                data.getQuote().getUsd().getMarket_cap(),data.getQuote().getUsd().getVolume_24h()
        );
    }
}
