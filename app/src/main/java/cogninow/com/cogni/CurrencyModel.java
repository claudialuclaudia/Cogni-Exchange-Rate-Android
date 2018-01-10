package cogninow.com.cogni;

/**
 * Created by Lenovo on 1/9/2018.
 */

public class CurrencyModel {
    private String abbrev;
    private String fullName;

    public CurrencyModel() {
    }

    public CurrencyModel(String abbrev, String fullName) {
        this.abbrev = abbrev;
        this.fullName = fullName;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public void setAbbrev(String abbrev) {
        this.abbrev = abbrev;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "CurrencyModel{" +
                "abbrev='" + abbrev + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
