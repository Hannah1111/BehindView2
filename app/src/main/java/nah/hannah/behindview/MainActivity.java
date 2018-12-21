package nah.hannah.behindview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.hello_world_salute)
    TextView helloText;

    @BindView(R.id.stock_updates_recycler_view)
    RecyclerView recyclerView;

    private StockDataAdapter stockDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Observable.just("Hello!")
                .subscribe(s->{helloText.setText(s);});

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        stockDataAdapter = new StockDataAdapter();
        recyclerView.setAdapter(stockDataAdapter);


        Observable.just(
                new StockUpdate("GOOGLE",BigDecimal.valueOf(12.43),new Date()),
                new StockUpdate("APPL",BigDecimal.valueOf(645.1),new Date()),
                new StockUpdate("TWTR",BigDecimal.valueOf(1.43),new Date())
        ).subscribe(stockUpdate -> {
            Log.e("---"," NEW UPDATE "+stockUpdate.getStockSymbol());
            stockDataAdapter.add(stockUpdate);
        });

        flowObserver();
    }



    public void larmbda(){
        Observable.just("꺄하하하하하ㅏ ")
                .subscribe(s->{helloText.setText(s);});

        Observable.just("1","@")
                .map(s -> s+" mapped")
                .flatMap(s -> Observable.just("flat- "+s))
                .doOnNext(s -> Log.e("---- ","on next "+s))
                .subscribe(s-> Log.e("---- ","hello "+s), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ERROR",throwable.getLocalizedMessage());
                    }
                });
    }

    public void scheduler(){
        Observable.just("First Item","Second item")
                .subscribeOn(Schedulers.io())
                .doOnNext(e ->Log.e("--- ","on next "+Thread.currentThread().getName()+" : "+e)) //Background
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e -> Log.e("---","subscribe "+Thread.currentThread().getName()+ " : "+e)); //onPost
    }

    private void log(String stage, String item){
        Log.e("---",stage + " : "+Thread.currentThread().getName()+" : "+item);
    }
    private void log(String stage){
        Log.e("---",stage + " : "+Thread.currentThread().getName());
    }

    public void flowObserver(){
        Observable.just("1","2")
                .subscribeOn(Schedulers.io())
                .doOnDispose(()->log("doOndispose"))
                .doOnComplete(()->log("doOnComplete"))
                .doOnNext(e->log("doOnNext ",e))
                .doOnEach(e->log("doOnEach"))
                .doOnSubscribe((e)->log("doOnSubscribe "))
                .doOnTerminate(()->log("doOnTerminate"))
                .doFinally(()->log("doFinally"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(e->log("subscribe ",e));

    }

}
