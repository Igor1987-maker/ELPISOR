package responseModels_of_REST_API;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@Builder
//@AllArgsConstructor
//@Setter
//@Getter
//@ToString
public class UserProfileResponse {

	
	@JsonProperty("UserId") 
 	private int userId;
    @JsonProperty("UserName") 
    private String userName;
    @JsonProperty("FirstName") 
    private String firstName;
    @JsonProperty("LastName") 
    private String lastName;
    @JsonProperty("Email") 
    private String email;
    @JsonProperty("LevelId") 
    private int levelId;
    @JsonProperty("LanguageId") 
    private int languageId;
    @JsonProperty("LanguageSupportLevelId") 
    private int languageSupportLevelId;
    @JsonProperty("CountryId") 
    private int countryId;
    @JsonProperty("BirthDate") 
    private Object birthDate;
    @JsonProperty("UpdateMe") 
    public int updateMe;
    @JsonProperty("Gender") 
    private String gender;
    @JsonProperty("CountryDescription") 
    private String countryDescription;
    @JsonProperty("LanguageDescription") 
    private String languageDescription;
    @JsonProperty("FirstNameEnabled") 
    private boolean firstNameEnabled;
    @JsonProperty("LastNameEnabled") 
    private boolean lastNameEnabled;
    @JsonProperty("UserNameEnabled") 
    private boolean userNameEnabled;
    @JsonProperty("EmailEnabled") 
    private boolean emailEnabled;
    @JsonProperty("BirthdayEnabled") 
    private boolean birthdayEnabled;
    @JsonProperty("LevelEnabled") 
    private boolean levelEnabled;
    @JsonProperty("SupportLanguageEnabled") 
    private boolean supportLanguageEnabled;
    @JsonProperty("LangSupLevelEnabled") 
    private boolean langSupLevelEnabled;
    @JsonProperty("NewFeatureEnabled") 
    private boolean newFeatureEnabled;
    @JsonProperty("CountryEnabled") 
    private boolean countryEnabled;
    @JsonProperty("PasswordEnabled") 
    private boolean passwordEnabled;
    @JsonProperty("WebPalsProfileEnabled") 
    private boolean webPalsProfileEnabled;
    @JsonProperty("YearOfBirth") 
    private Object yearOfBirth;
    @JsonProperty("MonthOfBirth") 
    private Object monthOfBirth;
    @JsonProperty("DayOfBirth") 
    private Object dayOfBirth;
    
	
	
	public int getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public int getLevelId() {
		return levelId;
	}
	public int getLanguageId() {
		return languageId;
	}
	public int getLanguageSupportLevelId() {
		return languageSupportLevelId;
	}
	public int getCountryId() {
		return countryId;
	}
	public Object getBirthDate() {
		return birthDate;
	}
	public int getUpdateMe() {
		return updateMe;
	}
	public String getGender() {
		return gender;
	}
	public String getCountryDescription() {
		return countryDescription;
	}
	public String getLanguageDescription() {
		return languageDescription;
	}
	public boolean isFirstNameEnabled() {
		return firstNameEnabled;
	}
	public boolean isLastNameEnabled() {
		return lastNameEnabled;
	}
	public boolean isUserNameEnabled() {
		return userNameEnabled;
	}
	public boolean isEmailEnabled() {
		return emailEnabled;
	}
	public boolean isBirthdayEnabled() {
		return birthdayEnabled;
	}
	public boolean isLevelEnabled() {
		return levelEnabled;
	}
	public boolean isSupportLanguageEnabled() {
		return supportLanguageEnabled;
	}
	public boolean isLangSupLevelEnabled() {
		return langSupLevelEnabled;
	}
	public boolean isNewFeatureEnabled() {
		return newFeatureEnabled;
	}
	public boolean isCountryEnabled() {
		return countryEnabled;
	}
	public boolean isPasswordEnabled() {
		return passwordEnabled;
	}
	public boolean isWebPalsProfileEnabled() {
		return webPalsProfileEnabled;
	}
	public Object getYearOfBirth() {
		return yearOfBirth;
	}
	public Object getMonthOfBirth() {
		return monthOfBirth;
	}
	public Object getDayOfBirth() {
		return dayOfBirth;
	}
	
	
	public UserProfileResponse() {
		
	}
		
	
	public UserProfileResponse(int userId, String userName, String firstName, String lastName, String email,
			int levelId, int languageId, int languageSupportLevelId, int countryId, Object birthDate, int updateMe,
			String gender, String countryDescription, String languageDescription, boolean firstNameEnabled,
			boolean lastNameEnabled, boolean userNameEnabled, boolean emailEnabled, boolean birthdayEnabled,
			boolean levelEnabled, boolean supportLanguageEnabled, boolean langSupLevelEnabled,
			boolean newFeatureEnabled, boolean countryEnabled, boolean passwordEnabled, boolean webPalsProfileEnabled,
			Object yearOfBirth, Object monthOfBirth, Object dayOfBirth) {
		//super();
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.levelId = levelId;
		this.languageId = languageId;
		this.languageSupportLevelId = languageSupportLevelId;
		this.countryId = countryId;
		this.birthDate = birthDate;
		this.updateMe = updateMe;
		this.gender = gender;
		this.countryDescription = countryDescription;
		this.languageDescription = languageDescription;
		this.firstNameEnabled = firstNameEnabled;
		this.lastNameEnabled = lastNameEnabled;
		this.userNameEnabled = userNameEnabled;
		this.emailEnabled = emailEnabled;
		this.birthdayEnabled = birthdayEnabled;
		this.levelEnabled = levelEnabled;
		this.supportLanguageEnabled = supportLanguageEnabled;
		this.langSupLevelEnabled = langSupLevelEnabled;
		this.newFeatureEnabled = newFeatureEnabled;
		this.countryEnabled = countryEnabled;
		this.passwordEnabled = passwordEnabled;
		this.webPalsProfileEnabled = webPalsProfileEnabled;
		this.yearOfBirth = yearOfBirth;
		this.monthOfBirth = monthOfBirth;
		this.dayOfBirth = dayOfBirth;
	}
	
	
    
	
}
