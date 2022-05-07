package A3;

class Person implements PersonInterface {

//    private static final Person single_instance = null;

    @Inject
    Logger logger;

    String firstname;
    String lastname;

    public Person() {
    }

    public Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

//    private static Person getInstance() {
//        if (single_instance == null)
//            single_instance = new Person();
//
//        return single_instance;
//    }

    @Override
    public String toString() {
        return "Person{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                '}';
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public void log(String msg) {
        logger.sendLog(msg);
    }
}
