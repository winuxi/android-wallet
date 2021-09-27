package org.telegram.crypto.currency_ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.crypto.models.Currency;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.os.FileUtils.copy;

@SuppressLint("ResourceType")
public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ContentHolder> {
    private List<Currency> currencies = new ArrayList<>();
    public Context context;
    public int request;
    public OnItemClickListener listener;

    public CurrencyAdapter(Context context, int request) {
        this.context = context;
        this.request = request;
    }

    @NonNull
    @Override
    public CurrencyAdapter.ContentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrencyAdapter.ContentHolder(create_item(context));
    }

    public void toWhite(TextView view){
        view.setTextColor(Color.WHITE);
    }
    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ContentHolder holder, int position) {
        Currency currency = currencies.get(position);

        String title_kit = currency.getName();
        if (position == 0) {
            holder.rank.setText("#");
            toWhite(holder.rank);
            toWhite(holder.name);
            toWhite(holder.today);
            toWhite(holder.week);
            toWhite(holder.market);
            toWhite(holder.volume);
            toWhite(holder.price);
            holder.getRow().setBackgroundColor(Color.DKGRAY);
            holder.market.setText(currency.getMarketCap());
            holder.volume.setText(currency.getVolume());
            holder.volumex.setVisibility(View.GONE);
            holder.price.setText(currency.getPrice());
        } else {
            holder.rank.setText(position+".");
            title_kit = "<strong>" + currency.getName() + "</strong><font color='#F48221'>  " + currency.getSymbol() + "</font>";
            holder.today.setTextColor(evaluate(currency.getToday()));
            holder.week.setTextColor(evaluate(currency.getWeek()));

            holder.price.setText(toCurrency(currency.getPrice()));
            ImageLoadTask loadTask = new ImageLoadTask(currency.getIcon(), holder.icon);
            loadTask.execute();
            holder.market.setText(toCurrency(currency.getMarketCap()));
            holder.volume.setText(toCurrency(currency.getVolume()));
            String ext = currency.getCirculating_supply()+" "+currency.getSymbol();
            holder.volumex.setText(ext);
        }
        holder.name.setText(Html.fromHtml(title_kit));


        holder.today.setText(toPercent(currency.getToday()));

        holder.week.setText(toPercent(currency.getWeek()));

    }
    public String toCurrency(String num) {
        return "$".concat(num);
    }
    public String toPercent(String num) {
        return num.concat("%");
    }


    public int evaluate(String data) {
        data.trim();
        int stat = Color.GREEN;
        if (data.startsWith("-")) {
            stat = Color.RED;
        } else if (data.startsWith("0")) {
            stat = Color.GRAY;
        }
        return stat;
    }

    @Override
    public int getItemCount() {
        return currencies.size();
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
        notifyDataSetChanged();
    }

    public class ContentHolder extends RecyclerView.ViewHolder {
        TextView name,rank,symbol,cmd,price,today,week,market,volume,volumex;
        ImageView icon;
        View row;

        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView;
            rank = itemView.findViewById(0);
            name = itemView.findViewById(2);
            symbol = itemView.findViewById(3);
            cmd = itemView.findViewById(4);
            price = itemView.findViewById(5);
            today = itemView.findViewById(6);
            week = itemView.findViewById(7);
            market = itemView.findViewById(8);
            volume = itemView.findViewById(9);
            volumex = itemView.findViewById(10);
            icon = itemView.findViewById(1);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currencies.get(position));
                }
            });
        }

        public View getRow() {
            return row;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Currency blogs);
    }

    public void onItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public RelativeLayout create_item(Context context) {
        TextView title,rank,symbol,cmd,price,today,week,market,volume,volumex;
        ImageView icon;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams lin = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100);
        RelativeLayout.LayoutParams pr = new RelativeLayout.LayoutParams(40, 40);

        lin.setMargins(5, 1, 5, 1);
        relativeLayout.setPadding(2,25,2,2);


        relativeLayout.setBackgroundColor(Color.WHITE);
        relativeLayout.setLayoutParams(lin);
        icon = new ImageView(context);
        icon.setId(1);
        icon.setLayoutParams(pr);
        relativeLayout.addView(icon);
        rank = new TextView(context);
        rank.setId(0);
        relativeLayout.addView(rank);
        title = new TextView(context);
        title.setId(2);
        relativeLayout.addView(title);

        symbol = new TextView(context);
        symbol.setId(3);
        cmd = new TextView(context);
        cmd.setId(4);
        today = new TextView(context);
        today.setId(6);
        relativeLayout.addView(today);
        week = new TextView(context);
        week.setId(7);
        relativeLayout.addView(week);

        price = new TextView(context);
        price.setId(5);
        relativeLayout.addView(price);
        market = new TextView(context);
        market.setId(8);
        relativeLayout.addView(market);
        volume = new TextView(context);
        volume.setId(9);
        relativeLayout.addView(volume);
        volumex = new TextView(context);
        volumex.setId(10);
        relativeLayout.addView(volumex);

        config_item(relativeLayout);
        return relativeLayout;
    }

    public void config_item(RelativeLayout root) {
        TextView text;
        ImageView icon;
        for (int x = 0; x < 11; x++) {
            root.getChildCount();
            if(x == 1){
                text = root.findViewById(0);
                icon = root.findViewById(x);
                RelativeLayout.LayoutParams pri = (RelativeLayout.LayoutParams) icon.getLayoutParams();
                pri.addRule(RelativeLayout.END_OF, 0);
                pri.setMargins(50,5,2,2);
                icon.setLayoutParams(pri);
                //continue;
            }else {
                text = root.findViewById(x);
            }
            if (text != null) {
                text.setPadding(5, 0, 5, 0);
                RelativeLayout.LayoutParams pr = (RelativeLayout.LayoutParams) text.getLayoutParams();
                text.setTextColor(Color.BLACK);
                text.setTextSize(13);
                pr.height = 30;
                text.setTypeface(null, Typeface.BOLD);
                if (x > 2) {
                    if (x != 5) {
                        if (x > 8){
                            if(x == 10){
                                pr.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                                pr.setMargins(3, 0, 3, 1);
                                pr.addRule(RelativeLayout.BELOW, x - 1);
                                pr.addRule(RelativeLayout.END_OF, x - 2);
                                text.setTextColor(Color.BLUE);
                            }
                            if(x == 9){
                                pr.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                pr.setMargins(3, 0, 3, 1);
                                pr.addRule(RelativeLayout.END_OF, x - 1);
                                text.setTextColor(Color.BLACK);
                            }
                        }else {
                            pr.addRule(RelativeLayout.END_OF, x - 1);
                        }
                    } else {
                        pr.addRule(RelativeLayout.END_OF, x - 3);
                    }
                    pr.width = 300;
                    text.setGravity(Gravity.RIGHT);
                } else if( x != 1){
                    if(x == 0){
                        pr.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        pr.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                    }else {
                        pr.addRule(RelativeLayout.END_OF, 1);
                        pr.width = 200;
                    }
                }
                pr.addRule(RelativeLayout.CENTER_VERTICAL);
                pr.setMargins(3, 3, 3, 2);
            } else {
                Log.d("no_view", "childes:  " + root.getChildCount() + " at: " + x);
            }
        }
    }

    public static class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}