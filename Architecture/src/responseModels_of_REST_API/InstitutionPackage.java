package responseModels_of_REST_API;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstitutionPackage {
		@JsonProperty("InstitutionPackageId") 
	    public Integer institutionPackageId;
	    @JsonProperty("PackageId") 
	    public int packageId;
	    @JsonProperty("PackageName") 
	    public String packageName;
	    @JsonProperty("StartDate") 
	    public String startDate;
	    @JsonProperty("EndDate") 
	    public String endDate;
	    @JsonProperty("TimeFrame") 
	    public int timeFrame;
		
	    public InstitutionPackage() {
	    	
	    }
	    
	    public InstitutionPackage(Integer institutionPackageId, int packageId, String packageName, String startDate,
				String endDate, int timeFrame) {
			super();
			this.institutionPackageId = institutionPackageId;
			this.packageId = packageId;
			this.packageName = packageName;
			this.startDate = startDate;
			this.endDate = endDate;
			this.timeFrame = timeFrame;
		}

		public Integer getInstitutionPackageId() {
			return institutionPackageId;
		}

		public int getPackageId() {
			return packageId;
		}

		public String getPackageName() {
			return packageName;
		}

		public String getStartDate() {
			return startDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public int getTimeFrame() {
			return timeFrame;
		}

		public void setInstitutionPackageId(Integer institutionPackageId) {
			this.institutionPackageId = institutionPackageId;
		}

		public void setPackageId(int packageId) {
			this.packageId = packageId;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		public void setStartDate(String startDate) {
			this.startDate = startDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public void setTimeFrame(int timeFrame) {
			this.timeFrame = timeFrame;
		}
	    
	    
	    
	    
	    
}
