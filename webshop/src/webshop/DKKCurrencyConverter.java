package webshop;

public class DKKCurrencyConverter {
	
	private class CurrencyExchangeRate {
		public String countryCode;
		public double exchangeRate;
		
		public CurrencyExchangeRate(String countryCode, double exchangeRate) {
			this.countryCode = countryCode;
			this.exchangeRate = exchangeRate;
		}		
	}
	
	//Define the different exchange rates. Further exchange rates can be added to this array. 
	private final CurrencyExchangeRate[] exchangeRates = {
		new CurrencyExchangeRate("NOK", 73.50),
		new CurrencyExchangeRate("SEK", 70.23),
		new CurrencyExchangeRate("GBP", 891.07),
		new CurrencyExchangeRate("EUR", 743.93)
	};
	
	
	
	
	//Helper function to find currency rate from a specific countryCode
	private CurrencyExchangeRate FindByCountryCode(String countryCode) {
		for(CurrencyExchangeRate cer : exchangeRates) {
			if(cer.countryCode.equals(countryCode)) {
				return cer;
			}
		}
		
		return null;
	}
	
	public double ConvertTo(double valueInDkk, String countryCode) {
		if(countryCode == "DKK") return valueInDkk;
		
		double exchangeRate = FindByCountryCode(countryCode).exchangeRate;
		
		return (100*valueInDkk) / exchangeRate;
	}
	
	public double ConvertFrom(double value, String countryCode) {
		if(countryCode == "DKK") return value;
		
		double exchangeRate = FindByCountryCode(countryCode).exchangeRate;
		
		return (exchangeRate*value) / 100;
	}
}
