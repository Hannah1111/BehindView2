package nah.hannah.behindview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hannah on 2018. 11. 14..
 */
public class StockUpdateViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.stock_item_symbol)
    TextView stockSymbol;
    @BindView(R.id.stock_item_price)
    TextView stockPrice;
    @BindView(R.id.stock_item_date)
    TextView stockDate;

    private static final NumberFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    public StockUpdateViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }

    public void setStockSymbol(String stockSymbol){
        this.stockSymbol.setText(stockSymbol);
    }

    public void setStockPrice(BigDecimal stockPrice){
        this.stockPrice.setText(PRICE_FORMAT.format(stockPrice.floatValue()));
    }

    public void setStockDate(Date stockDate){
        this.stockDate.setText(DateFormat.format("yyyy-mm-dd hh:mm",stockDate));
    }
}
