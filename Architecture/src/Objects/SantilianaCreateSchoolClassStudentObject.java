package Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;



public class SantilianaCreateSchoolClassStudentObject {

    public User user;
    public School school;
    public ArrayList<ClassItem> classItems;

    public static class User {
        public String id;
        public String username;
        public String firstname;
        public String lastname;
        public String email;
        public String role;
        public String city;
        public String residence_country_code;
        public String date_of_birth;
        public String gender;
        public String native_country_code;
        public String native_language_code;
    }

    public static class School {
        public String id;
        public String name;
        public String country;
        public String businessDivision;
    }

    public static class ClassItem {
        public String id;
        public String name;
        @JsonProperty("package")
        public String packageValue;
        public int numberOfStudents;
    }
}
