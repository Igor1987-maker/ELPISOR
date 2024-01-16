package Enums;

 public enum AdministratorTitle {

    CREATE_ADMINISTRATION("CREATE ADMINISTRATION"),
    CREATE_ADMINISTRATIONS_AND_SESSIONS("CREATE ADMINISTRATIONS & SESSIONS");

    public String value;

     AdministratorTitle(String value){
        this.value = value;
    }

}
