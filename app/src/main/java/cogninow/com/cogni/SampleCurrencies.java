package cogninow.com.cogni;

import java.util.ArrayList;
import java.util.List;

public class SampleCurrencies {
    public static List<CurrencyModel> currencies;

    static {
        currencies = new ArrayList<>();

        addItem(new CurrencyModel("US", "U.S.Dollar"));

        addItem(new CurrencyModel("MX", "Mexican New Pesos"));

    }

    private static void addItem(CurrencyModel item) {
        currencies.add(item);
    }
}
