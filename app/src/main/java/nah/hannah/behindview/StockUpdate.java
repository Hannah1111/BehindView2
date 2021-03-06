package nah.hannah.behindview;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by hannah on 2018. 11. 14..
 */
public class StockUpdate implements Serializable {

    private final String stockSymbol;
    private final BigDecimal price;
    private final Date date;

    public StockUpdate(String stockSymbol, BigDecimal price, Date date){
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.date = date;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }
}
