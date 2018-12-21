package nah.hannah.behindview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hannah on 2018. 11. 14..
 */
public class StockDataAdapter extends RecyclerView.Adapter<StockUpdateViewHolder> {

    private final List<StockUpdate> data = new ArrayList<>();

    @NonNull
    @Override
    public StockUpdateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_update_item, viewGroup, false);
        StockUpdateViewHolder vh = new StockUpdateViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StockUpdateViewHolder stockUpdateViewHolder, int i) {
        StockUpdate stockUpdate = data.get(i);
        stockUpdateViewHolder.setStockSymbol(stockUpdate.getStockSymbol());
        stockUpdateViewHolder.setStockPrice(stockUpdate.getPrice());
        stockUpdateViewHolder.setStockDate(stockUpdate.getDate());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(StockUpdate update){
        data.add(update);
        notifyItemChanged(data.size()-1);
    }
}
