package org.telegram.crypto.currency_ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.crypto.models.Currency;
import org.telegram.messenger.R;

import java.util.ArrayList;
import java.util.List;
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
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_blogs,parent,false);

        return new CurrencyAdapter.ContentHolder(one_row(context));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ContentHolder holder, int position) {
        Currency currency = currencies.get(position);
        String title_kit = currency.getName();
        if(position == 0){
            holder.getRow().setBackgroundColor(Color.GRAY);
            holder.market.setText(currency.getMarketCap());
            holder.volume.setText(currency.getVolume());
        }else {
            title_kit = "<strong>"+currency.getName() +"</strong><font color='#F48221'>  "+currency.getSymbol()+"</font>";
            holder.price.setTextColor(evaluate(currency.getPrice()));
            holder.today.setTextColor(evaluate(currency.getToday()));
            holder.week.setTextColor(evaluate(currency.getWeek()));Drawable res = ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.common_google_signin_btn_icon_dark_normal, null);
            holder.icon.setImageDrawable(res);
            holder.market.setText(toCurrency(currency.getMarketCap()));
            //holder.market.setTextColor(evaluate(currency.getMarketCap()));

            holder.volume.setText(toCurrency(currency.getVolume()));
        }
        holder.name.setText(Html.fromHtml(title_kit));

        holder.price.setText(currency.getPrice());

        holder.today.setText(currency.getToday());

        holder.week.setText(currency.getWeek());

    }
    public String toCurrency(String num){
        return "$".concat(num);
    }
    public int evaluate(String data){
        data.trim();
        int stat = Color.GREEN;
        if(data.startsWith("-")){
            stat = Color.RED;
        }else if(data.startsWith("0")){
            stat = Color.GRAY;
        }
        return stat;
    }

    public void settup_textVews(RelativeLayout root) {
        TextView text;

        for (int x = 2; x < 10; x++) {
            root.getChildCount();
            text = root.findViewById(x);
            if (text != null) {
                text.setPadding(5, 5, 5, 5);
                RelativeLayout.LayoutParams pr = (RelativeLayout.LayoutParams) text.getLayoutParams();
                text.setTextColor(Color.BLACK);
                text.setTextSize(13);
                pr.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                pr.addRule(RelativeLayout.CENTER_VERTICAL);
                pr.setMargins(3, 3, 3, 2);
                if (x != 2) {
                    if(x != 5){
                        pr.addRule(RelativeLayout.END_OF, x - 1);
                    }else {
                        pr.addRule(RelativeLayout.END_OF, x - 3);
                    }
                    text.setTextColor(Color.BLUE);
                    pr.width = 300;
                    text.setGravity(Gravity.RIGHT);
                }else {
                    pr.addRule(RelativeLayout.END_OF, 1);
                        pr.width = 200;
                }
                // root.addView(text);
            } else {
                Log.d("no_view", "childes:  " + root.getChildCount() + " at: " + x);
            }
        }
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
        TextView name;
        ImageView icon;
        TextView symbol;
        TextView cmd;

        TextView price;
        TextView today;
        TextView week;
        TextView market;
        TextView volume;
        View row;
        public ContentHolder(@NonNull View itemView) {
            super(itemView);
            row = itemView;
            name = itemView.findViewById(2);
            symbol = itemView.findViewById(3);
            cmd = itemView.findViewById(4);
            price = itemView.findViewById(5);
            today = itemView.findViewById(6);
            week = itemView.findViewById(7);
            market = itemView.findViewById(8);
            volume = itemView.findViewById(9);
            icon = itemView.findViewById(1);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(currencies.get(position));
                }
            });
        }
        public View getRow(){
            return row;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Currency blogs);
    }

    public void onItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public RelativeLayout one_row(Context context) {
        TextView title;
        ImageView icon;
        TextView symbol;
        TextView cmd;

        TextView price;
        TextView today;
        TextView week;
        TextView market;
        TextView volume;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams lin = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                70);
        RelativeLayout.LayoutParams pr = new RelativeLayout.LayoutParams(70,70);

        lin.setMargins(5,5,5,5);
        relativeLayout.setLayoutParams(lin);
        icon = new ImageView(context);
        icon.setId(1);
        pr.addRule(Gravity.CENTER);
        icon.setLayoutParams(pr);
        relativeLayout.addView(icon);
        title = new TextView(context);
        title.setId(2);
        relativeLayout.addView(title);

        symbol = new TextView(context);
        symbol.setId(3);
        //relativeLayout.addView(symbol);
        cmd = new TextView(context);
        cmd.setId(4);
        //relativeLayout.addView(cmd);

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
        settup_textVews(relativeLayout);
        return relativeLayout;
    }
}