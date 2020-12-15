public class Account {
    private final int id;
    @NameInReport(name = "Имя")
    private final String ownerName;
    @NameInReport(name = "Фамилия")
    private final String ownerSurname;
    @NameInReport(name = "Остаток на счете")
    private final double balance;

    public Account(int id, String ownerName, String ownerSurname, double balance) {
        this.id = id;
        this.ownerName = ownerName;
        this.ownerSurname = ownerSurname;
        this.balance = balance;
    }
}
