package webshop;

public class VatCalculator {
	private class VatRule {
		public String countryCode;
		public boolean universal;
		public Type type = null;
		public double rate;
		
		public VatRule(String countryCode, double rate) {
			//Constructor for universal vat rules			
			this.countryCode = countryCode;
			this.rate = rate;
			universal = true;
		}

		public VatRule(String countryCode, double rate, Type type) {
			//Constructor for type-specific vat rules
			this.countryCode = countryCode;
			this.rate = rate;
			this.type = type;
			universal = false;
		}	
		
	}
	
	//Define the different exchange rates. Further vat rules can be added to this array. 
	private final VatRule[] vatRules = {
		new VatRule("DK", 25),
		new VatRule("NO", 25),
		new VatRule("SE", 25),
		new VatRule("GB", 20),
		new VatRule("DE", 19, Type.online),
		new VatRule("DE", 12, Type.book)
	};
	

	public double GetVatRate(String countryCode, Type type) {
		for(VatRule vr : vatRules) {
			if(countryCode.equals(vr.countryCode) && (vr.universal || type == vr.type)) {
				return vr.rate;
			}
		}
		
		return 0;
	}
}
