package webshop;

import java.text.DecimalFormat;
import java.util.HashMap;

public class Calculation {
	//Declarations
	private int amount;
	private double price;
	private Type type;
	private HashMap<String,String> arguments = new HashMap<String,String>();
	private String inputCurrency = "DKK";
	private String outputCurrency = "DKK";
	private String vat = null;
	
	private DKKCurrencyConverter currencyConverter = new DKKCurrencyConverter();
	private VatCalculator vatCalculator = new VatCalculator();
	
	
	//Constructor. Unpacks the command line arguments into class variables.
	public Calculation(String[] args) throws Exception {
		//Check if the correct number of arguments have been supplied. 
		if(args.length<3) {
			throw new Exception("Invalid number of arguments. Expected 3, found " + args.length);
		}
		
		//Unpack the required arguments
		amount = Integer.parseInt(args[0]);
		price = Double.parseDouble(args[1]);
		type = Type.valueOf(args[2]);
		
		//Unpack additional arguments
		for(int i = 3; i < args.length; i++) {
			String[] keyvaluepair = args[i].split("=");
			
			//Error handling in case of wrong formatting
			if(keyvaluepair.length != 2) {
				throw new Exception("Argument " + args[i] + "could not be parsed");
			}
			
			arguments.put(keyvaluepair[0], keyvaluepair[1]);
		}
		
		//Set Currencies
		if(arguments.containsKey("--input-currency")) {
			inputCurrency = arguments.get("--input-currency");
		}
		
		if(arguments.containsKey("--output-currency")) {
			outputCurrency = arguments.get("--output-currency");
		}
		
		//Set VAT
		if(arguments.containsKey("--vat")) {
			vat = arguments.get("--vat");
		}
	}

	//Middleware interface for defining rules for calculating the final price. 
	private interface CalculationRule {
		double enforce(double Price);
	}
	
	//Array of CalculationRules that will be used for calculating the final price. The rules will be applied in the order defined here.
	private CalculationRule[] rules = {
		(price) -> {
			//Convert from input currency to DKK
			return currencyConverter.ConvertFrom(price, inputCurrency);
		},
		(price) -> {
			//Add freight
			if(type != Type.online) {
				return price + (amount / 10) * 25 + 50;
			} else {
				return price;
			}
		},
		(price) -> {
			//Apply VAT
			if(vat == null) return price;
			
			return price * (100 + vatCalculator.GetVatRate(vat, type)) / 100;
		},
		(price) -> {
			//Convert from DKK to output currency
			return currencyConverter.ConvertTo(price, outputCurrency);
		}
	};
	
	private final DecimalFormat decimalFormat = new DecimalFormat("0.00");
	
	//Applies the calculation rules, and outputs the price and currency as one string. 
	public String GenerateOutput() {
		double currentPrice = price * amount;
		for(CalculationRule rule : rules) {
			currentPrice = rule.enforce(currentPrice);
		}
		
		//Formats the final price to have exactly 2 decimals.
		return "" + decimalFormat.format(currentPrice) + " " + outputCurrency;
	}

	//Getters and Setters
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getInputCurrency() {
		return inputCurrency;
	}

	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}

	public String getOutputCurrency() {
		return outputCurrency;
	}

	public void setOutputCurrency(String outputCurrency) {
		this.outputCurrency = outputCurrency;
	}

	public String getVat() {
		return vat;
	}

	public void setVat(String vat) {
		this.vat = vat;
	}

	public HashMap<String, String> getArguments() {
		return arguments;
	}

	public void setArguments(HashMap<String, String> arguments) {
		this.arguments = arguments;
	}
	
}
