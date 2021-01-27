package pl.klolo.workshops.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.w3c.dom.ls.LSOutput;
import pl.klolo.workshops.domain.*;
import pl.klolo.workshops.domain.Currency;
import pl.klolo.workshops.mock.HoldingMockGenerator;

class WorkShop {

  /**
   * Lista holdingów wczytana z mocka.
   */
  private final List<Holding> holdings;

  // Predykat określający czy użytkownik jest kobietą
  private final Predicate<User> isWoman = user -> user.getSex().equals(Sex.WOMAN);


  WorkShop() {
    final HoldingMockGenerator holdingMockGenerator = new HoldingMockGenerator();
    holdings = holdingMockGenerator.generate();
  }

  /**
   * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma.
   */
  long getHoldingsWhereAreCompanies() {
    int result = 0;

    for(Holding holding : holdings) {
      if ((holding.getCompanies().size()) >= 1) {
        result += 1;
      }
    }
    return result;
    }

  /**
   * Metoda zwraca liczbę holdingów w których jest przynajmniej jedna firma. Napisz to za pomocą strumieni.
   */
  long getHoldingsWhereAreCompaniesAsStream() {

    return holdings.stream()
            .filter(holding -> (holding.getCompanies().size()) >= 1)
            .count();
  }

  /**
   * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy.
   */
  List<String> getHoldingNames() {
    List<String> holingsList = new ArrayList<>();
    for (Holding holding : holdings){
      holingsList.add(holding.getName().toLowerCase());
    }
    return holingsList;
  }

  /**
   * Zwraca nazwy wszystkich holdingów pisane z małej litery w formie listy. Napisz to za pomocą strumieni.
   */
  List<String> getHoldingNamesAsStream() {
    return holdings.stream()
            .map(holding -> holding.getName().toLowerCase())
            .collect(Collectors.toList());
  }

  /**
   * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico)
   */
  String getHoldingNamesAsString() {
    String result = "(";
    List<String> holdingsNameList = new ArrayList<>();
    for(int i =0; i <= holdings.size() -1; i++){
      holdingsNameList.add(holdings.get(i).getName());
    }
    Collections.sort(holdingsNameList);

    for(int x = 0; x <= holdingsNameList.size() -1; x++){
      if(x == holdingsNameList.size() -1){
        result += holdingsNameList.get(x) +")";
      }else
      result += holdingsNameList.get(x) + ", ";
    }
    return  result;

  }

  /**
   * Zwraca nazwy wszystkich holdingów sklejone w jeden string i posortowane. String ma postać: (Coca-Cola, Nestle, Pepsico). Napisz to za pomocą strumieni.
   */
  String getHoldingNamesAsStringAsStream() {
    String concatNames = holdings
            .stream()
            .map(holding -> holding.getName())
            .sorted(Comparator.comparing(n -> n))
            .reduce("(", (a, b) -> a + b + ", ");

    String result = concatNames.substring(0, concatNames.length() - 2);
    result += ")";

    return result;

  }

  /**
   * Zwraca liczbę firm we wszystkich holdingach.
   */
  long getCompaniesAmount() {
    int result = 0;
    for(Holding holding : holdings){
      result += holding.getCompanies().size();
    }
    return result;
  }

  /**
   * Zwraca liczbę firm we wszystkich holdingach. Napisz to za pomocą strumieni.
   */
  long getCompaniesAmountAsStream() {
    return holdings.stream()
            .mapToInt(holding -> holding.getCompanies().size())
            .sum();
  }

  /**
   * Zwraca liczbę wszystkich pracowników we wszystkich firmach.
   */
  long getAllUserAmount() {
    int result = 0;

    for(Holding holding : holdings) {
      for (int i = 0; i <= holding.getCompanies().size() -1; i++) {
        result += holding.getCompanies().get(i).getUsers().size();
      }
    }
      return result;
  }

  /**
   * Zwraca liczbę wszystkich pracowników we wszystkich firmach. Napisz to za pomocą strumieni.
   */
  long getAllUserAmountAsStream() {
    return holdings.stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .mapToInt(company -> company.getUsers().size())
            .sum();
  }

  /**
   * Zwraca listę wszystkich nazw firm w formie listy.
   */
  List<String> getAllCompaniesNames() {
    List<String> companiesNames = new ArrayList<>();

    for(Holding holding : holdings){
      for(Company company : holding.getCompanies()){
        companiesNames.add(company.getName());
      }
    }
    return companiesNames;
  }

  /**
   * Zwraca listę wszystkich nazw firm w formie listy. Tworzenie strumienia firm umieść w osobnej metodzie którą później będziesz wykorzystywać. Napisz to za
   * pomocą strumieni.
   */
  List<String> getAllCompaniesNamesAsStream() {
    return companyStream(holdings)
            .map(company -> company.getName())
            .collect(Collectors.toList());

  }



  /**
   * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList.
   */
  LinkedList<String> getAllCompaniesNamesAsLinkedList() {
    LinkedList<String> companiesNames = new LinkedList<>();

    for(Holding holding : holdings){
      for(Company company : holding.getCompanies()){
        companiesNames.add(company.getName());
      }
    }
    return companiesNames;

  }

  /**
   * Zwraca listę wszystkich firm jako listę, której implementacja to LinkedList. Obiektów nie przepisujemy po zakończeniu działania strumienia. Napisz to za
   * pomocą strumieni.
   */
  LinkedList<String> getAllCompaniesNamesAsLinkedListAsStream() {
    return companyStream(holdings)
            .map(company -> company.getName())
            .collect(Collectors.toCollection(LinkedList::new));
  }

  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+"
   */
  String getAllCompaniesNamesAsString() {
    String tempWord = "";

    for(Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
            tempWord += company.getName() + "+";
      }
    }
    String result = tempWord.substring(0, tempWord.length() - 1);

    return result;
  }

  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+" Napisz to za pomocą strumieni.
   */
  String getAllCompaniesNamesAsStringAsStream() {

    Stream<Company> companyStream = holdings.stream()
            .flatMap(holding -> holding.getCompanies().stream());

    String reduce = companyStream
            .map(company -> company.getName())
            .reduce("", (a, b) -> a + b + "+");

    String result = reduce.substring(0, reduce.length()-1);
    return result;

  }

  /**
   * Zwraca listę firm jako string gdzie poszczególne firmy są oddzielone od siebie znakiem "+". Używamy collect i StringBuilder. Napisz to za pomocą
   * strumieni.
   * <p>
   * UWAGA: Zadanie z gwiazdką. Nie używamy zmiennych.
   */
  String getAllCompaniesNamesAsStringUsingStringBuilder() {

    return companyStream(holdings)
            .map(company -> new StringBuilder().append(company.getName()))
            .collect(Collectors.joining("+"));

  }

  /**
   * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach.
   */
  long getAllUserAccountsAmount() {
    long result = 0;
    for(Holding holding : holdings){
      for(Company company : holding.getCompanies()){
        for(User user : company.getUsers()){
          result += user.getAccounts().size();
        }
      }
    }
    return result;
  }

  /**
   * Zwraca liczbę wszystkich rachunków, użytkowników we wszystkich firmach. Napisz to za pomocą strumieni.
   */
  long getAllUserAccountsAmountAsStream() {
    return holdings.stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .flatMap(company -> company.getUsers().stream())
            .mapToInt(user -> user.getAccounts().size())
            .sum();
  }

  /**
   * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane.
   */
  String getAllCurrencies() {

    //"CHF, EUR, PLN, USD"

    Set<String> currency = new TreeSet<>();

    for(Holding holding : holdings){
      for(Company company : holding.getCompanies()){
        for(User user : company.getUsers()){
            for(Account account : user.getAccounts()){
              currency.add(String.valueOf(account.getCurrency()));
            }
        }
      }
    }

    String result = "";

    for(String element : currency){
      result += element + ", ";
    }

    return result.substring(0, result.length() - 2);

  }

  /**
   * Zwraca listę wszystkich walut w jakich są rachunki jako string, w którym wartości występują bez powtórzeń i są posortowane. Napisz to za pomocą strumieni.
   */
  String getAllCurrenciesAsStream() {
    Set<String> currencies = holdings.stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .flatMap(company -> company.getUsers().stream())
            .flatMap(user -> user.getAccounts().stream())
            .map(currency -> String.valueOf(currency.getCurrency()))
            .collect(Collectors.toSet());

    Set<String> s = new TreeSet<>();

    for(String cur : currencies){
      s.add(cur);
    }

    String result = "";

    for(String element : s){
      result += element + ", ";
    }

    return result.substring(0, result.length() - 2);

  }

  /**
   * Metoda zwraca analogiczne dane jak getAllCurrencies, jednak na utworzonym zbiorze nie uruchamiaj metody stream, tylko skorzystaj z  Stream.generate.
   * Wspólny kod wynieś do osobnej metody.
   *
   * @see #getAllCurrencies()
   */
  String getAllCurrenciesUsingGenerate() {

    return null;
  }

  /**
   * Zwraca liczbę kobiet we wszystkich firmach.
   */
  long getWomanAmount() {

  long result = 0;

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          if (user.getSex().equals(Sex.WOMAN)) {
            result += 1;
          }
        }
      }
    }

    return result;
  }

  /**
   * Zwraca liczbę kobiet we wszystkich firmach. Powtarzający się fragment kodu tworzący strumień uzytkowników umieść w osobnej metodzie. Predicate określający
   * czy mamy doczynienia z kobietą inech będzie polem statycznym w klasie. Napisz to za pomocą strumieni.
   */
  long getWomanAmountAsStream() {
    return getUserStream()
            .filter(isWoman)
            .count();
  }


  /**
   * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Ustaw precyzje na 3 miejsca po przecinku.
   */
  BigDecimal getAccountAmountInPLN(final Account account) {

    float currency = account.getCurrency().rate;
    BigDecimal amount = account.getAmount();

    BigDecimal result = new BigDecimal(currency).multiply(amount);

    return result.setScale(3, RoundingMode.DOWN);
  }


  /**
   * Przelicza kwotę na rachunku na złotówki za pomocą kursu określonego w enum Currency. Napisz to za pomocą strumieni.
   */
  BigDecimal getAccountAmountInPLNAsStream(final Account account) {
    return null;


  }

  /**
   * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją.
   */
  BigDecimal getTotalCashInPLN(final List<Account> accounts) {
    return null;
  }

  /**
   * Przelicza kwotę na podanych rachunkach na złotówki za pomocą kursu określonego w enum Currency  i sumuje ją. Napisz to za pomocą strumieni.
   */
  BigDecimal getTotalCashInPLNAsStream(final List<Account> accounts) {
    return null;
  }

  /**
   * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek.
   */
  Set<String> getUsersForPredicate(final Predicate<User> userPredicate) {

    Set<String> users = new HashSet<>();
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          if (userPredicate.test(user)){
            users.add(user.getFirstName());

          }
        }
      }
    }
    return users;
  }

  /**
   * Zwraca imiona użytkowników w formie zbioru, którzy spełniają podany warunek. Napisz to za pomocą strumieni.
   */
  Set<String> getUsersForPredicateAsStream(final Predicate<User> userPredicate) {
    return getUserStream()
            .filter(userPredicate)
            .map(user -> user.getFirstName())
            .collect(Collectors.toSet());
  }

  /**
   * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy.
   */
  List<String> getOldWoman(final int age) {
    List<String> oldWoman = new ArrayList<>();

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          if (user.getAge() > age & user.getSex() != Sex.MAN & user.getSex() != Sex.OTHER) {
            oldWoman.add(user.getFirstName());
          }
        }
      }
    }

    return oldWoman;
  }

  /**
   * Metoda filtruje użytkowników starszych niż podany jako parametr wiek, wyświetla ich na konsoli, odrzuca mężczyzn i zwraca ich imiona w formie listy. Napisz
   * to za pomocą strumieni.
   */
  List<String> getOldWomanAsStream(final int age) {
    return getUserStream()
            .filter(user -> user.getAge() > age)
            .filter(user -> user.getSex() != Sex.MAN & user.getSex() != Sex.OTHER)
            .map(user -> user.getFirstName())
            .collect(Collectors.toList());
  }

  /**
   * Dla każdej firmy uruchamia przekazaną metodę.
   */
  void executeForEachCompany(final Consumer<Company> consumer) {
    companyStream(holdings).forEach(consumer);

  }

  /**
   * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach.
   */
  Optional<User> getRichestWoman() {

    List<User> userList = new ArrayList<>();
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          if (user.getSex().equals(Sex.WOMAN)){
            userList.add(user);
          } } }
    }
    TreeMap<BigDecimal, User> richestPerson = new TreeMap<>();

    for(User user : userList){
      List<Account> accounts = user.getAccounts();
      BigDecimal amount = new BigDecimal(0);
      for (Account account :  accounts){
          amount = amount.add(getAccountAmountInPLN(account));
          richestPerson.put(amount , user);
      }
    }

    return Optional.ofNullable(richestPerson.get(richestPerson.lastKey()));

  }

  /**
   * Wyszukuje najbogatsza kobietę i zwraca ja. Metoda musi uzwględniać to że rachunki są w różnych walutach. Napisz to za pomocą strumieni.
   */
  Optional<User> getRichestWomanAsStream() {
      return  null;
  }

  /**
   * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia.
   */
  Set<String> getFirstNCompany(final int n) {
    Set<String> companies = new HashSet<>();
    int temp =0;
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        if (temp >= n) {
          break;
        }else{
           companies.add(company.getName());
          temp++;
          }
        }
      }
     return companies;
      }


  /**
   * Zwraca nazwy pierwszych N firm. Kolejność nie ma znaczenia. Napisz to za pomocą strumieni.
   */
  Set<String> getFirstNCompanyAsStream(final int n) {
    return companyStream (holdings)
            .limit(n)
            .map(company -> company.getName())
            .collect(Collectors.toSet());
  }

  /**
   * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
   * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return.
   */
  AccountType getMostPopularAccountType() {
    return null;
  }

  /**
   * Metoda zwraca jaki rodzaj rachunku jest najpopularniejszy. Stwórz pomocniczą metdę getAccountStream. Jeżeli nie udało się znaleźć najpopularnijeszego
   * rachunku metoda ma wyrzucić wyjątek IllegalStateException. Pierwsza instrukcja metody to return. Napisz to za pomocą strumieni.
   */
  AccountType getMostPopularAccountTypeAsStream() {
    return null;
  }

  /**
   * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException.
   */
  User getUser(final Predicate<User> predicate) {

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          try {
            if (predicate.test(user) == true) {
              return user;
            }
          } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
          }
        }

      }
    }
    return null;
  }

  /**
   * Zwraca pierwszego z brzegu użytkownika dla podanego warunku. W przypadku kiedy nie znajdzie użytkownika wyrzuca wyjątek IllegalArgumentException. Napisz to
   * za pomocą strumieni.
   */
  User getUserAsStream(final Predicate<User> predicate) {
    try{
      List<User> collect = getUserStream()
              .filter(user -> predicate.test(user))
              .collect(Collectors.toList());
        return collect.get(0);
    }catch (IllegalArgumentException e) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników.
   */
  Map<String, List<User>> getUserPerCompany() {
    Map<String, List<User>> userPerCompany = new HashMap<>();

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
          userPerCompany.put(company.getName(), company.getUsers());
        }
    }
    return userPerCompany;

  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników. Napisz to za pomocą strumieni.
   */
  Map<String, List<User>> getUserPerCompanyAsStream() {
    return companyStream(holdings)
            .collect(Collectors.toMap(company -> company.getName(), company -> company.getUsers()));
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
   * Możesz skorzystać z metody entrySet.
   */
  Map<String, List<String>> getUserPerCompanyAsString() {
    Map<String, List<String>> userPerCompany = new HashMap<>();

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        List<String> users = new ArrayList<>();
        for (User user : company.getUsers()) {
          users.add(user.getFirstName() + " " + user.getLastName());
      }
        userPerCompany.put(company.getName(), users);
    }
  }
    return userPerCompany;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako string składający się z imienia i nazwiska. Podpowiedź:
   * Możesz skorzystać z metody entrySet. Napisz to za pomocą strumieni.
   */
  Map<String, List<String>> getUserPerCompanyAsStringAsStream() {
    return  null;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej
   * funkcji.
   */
  <T> Map<String, List<T>> getUserPerCompany(final Function<User, T> converter) {
    return null;
  }

  /**
   * Zwraca mapę firm, gdzie kluczem jest jej nazwa a wartością lista pracowników przechowywanych jako obiekty typu T, tworzonych za pomocą przekazanej funkcji.
   * Napisz to za pomocą strumieni.
   */
  <T> Map<String, List<T>> getUserPerCompanyAsStream(final Function<User, T> converter) {
    return null;
  }

  /**
   * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
   * jest natomiast zbiór nazwisk tych osób.
   */
  Map<Boolean, Set<String>> getUserBySex() {
    return null;
  }

  /**
   * Zwraca mapę gdzie kluczem jest flaga mówiąca o tym czy mamy do czynienia z mężczyzną, czy z kobietą. Osoby "innej" płci mają zostać zignorowane. Wartością
   * jest natomiast zbiór nazwisk tych osób. Napisz to za pomocą strumieni.
   */
  Map<Boolean, Set<String>> getUserBySexAsStream() {
    return null;
  }

  /**
   * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek.
   */
  Map<String, Account> createAccountsMap() {
    return null;
  }

  /**
   * Zwraca mapę rachunków, gdzie kluczem jesy numer rachunku, a wartością ten rachunek. Napisz to za pomocą strumieni.
   */
  Map<String, Account> createAccountsMapAsStream() {
    return null;
  }

  /**
   * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń.
   */
  String getUserNames() {
    return null;
  }

  /**
   * Zwraca listę wszystkich imion w postaci Stringa, gdzie imiona oddzielone są spacją i nie zawierają powtórzeń. Napisz to za pomocą strumieni.
   */
  String getUserNamesAsStream() {
    return null;
  }

  /**
   * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10.
   */
  Set<User> getUsers() {
    return null;
  }

  /**
   * zwraca zbiór wszystkich użytkowników. Jeżeli jest ich więcej niż 10 to obcina ich ilość do 10. Napisz to za pomocą strumieni.
   */
  Set<User> getUsersAsStream() {
    return null;
  }

  /**
   * Zwraca użytkownika, który spełnia podany warunek.
   */
  Optional<User> findUser(final Predicate<User> userPredicate) {
    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          if (userPredicate.test(user)) {
            return Optional.ofNullable(user);
          }
        }
      }
    }
    return null;

  }

  /**
   * Zwraca użytkownika, który spełnia podany warunek. Napisz to za pomocą strumieni.
   */
  Optional<User> findUserAsStream(final Predicate<User> userPredicate) {
    List<User> collect = getUserStream()
            .filter(user -> userPredicate.test(user))
            .collect(Collectors.toList());

    return Optional.ofNullable(collect.get(0));

  }

  /**
   * Dla podanego użytkownika zwraca informacje o tym ile ma lat w formie: IMIE NAZWISKO ma lat X. Jeżeli użytkownik nie istnieje to zwraca text: Brak
   * użytkownika.
   * <p>
   * Uwaga: W prawdziwym kodzie nie przekazuj Optionali jako parametrów. Napisz to za pomocą strumieni.
   */
  String getAdultantStatusAsStream(final Optional<User> user) {
    return null;
  }

  /**
   * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
   * Pasibrzuch, Adam Wojcik
   */
  void showAllUser() {

    List<String> users = new ArrayList<>();

    for (Holding holding : holdings) {
      for (Company company : holding.getCompanies()) {
        for (User user : company.getUsers()) {
          users.add(user.getFirstName() + " " + user.getLastName());
        }
      }
    }
    Collections.sort(users, new Comparator<String>() {
      @Override
      public int compare(String s1, String s2) {
        return s2.compareToIgnoreCase(s1);
      }
    });

    for (String employee  : users){
      if (users.get(users.size()-1) == employee){
        System.out.println(employee + ".");
        break;
      }
      System.out.print(employee +", ");
    }
  }

  /**
   * Metoda wypisuje na ekranie wszystkich użytkowników (imie, nazwisko) posortowanych od z do a. Zosia Psikuta, Zenon Kucowski, Zenek Jawowy ... Alfred
   * Pasibrzuch, Adam Wojcik. Napisz to za pomocą strumieni.
   */
  void showAllUserAsStream() {
   getUserStream()
              .map(user -> user.getFirstName() + " "+  user.getLastName() + ", ")
              .sorted(String::compareToIgnoreCase)
              .sorted(Comparator.reverseOrder())
              .forEach(employee -> System.out.print(employee));

  }

  /**
   * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki.
   */
  Map<AccountType, BigDecimal> getMoneyOnAccounts() {
    return null;
  }

  /**
   * Zwraca mapę, gdzie kluczem jest typ rachunku a wartością kwota wszystkich środków na rachunkach tego typu przeliczona na złotówki. Napisz to za pomocą
   * strumieni. Ustaw precyzje na 0.
   */
  Map<AccountType, BigDecimal> getMoneyOnAccountsAsStream() {
    return null;
  }

  /**
   * Zwraca sumę kwadratów wieków wszystkich użytkowników.
   */
  int getAgeSquaresSum() {
    return -1;
  }

  /**
   * Zwraca sumę kwadratów wieków wszystkich użytkowników. Napisz to za pomocą strumieni.
   */
  int getAgeSquaresSumAsStream() {
    return -1;
  }

  /**
   * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
   * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody).
   */
  List<User> getRandomUsers(final int n) {
    return null;
  }

  /**
   * Metoda zwraca N losowych użytkowników (liczba jest stała). Skorzystaj z metody generate. Użytkownicy nie mogą się powtarzać, wszystkie zmienną muszą być
   * final. Jeżeli podano liczbę większą niż liczba użytkowników należy wyrzucić wyjątek (bez zmiany sygnatury metody). Napisz to za pomocą strumieni.
   */
  List<User> getRandomUsersAsStream(final int n) {
    return null;
  }

  /**
   * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
   * na rachunku danego typu przeliczona na złotkówki.
   */
  Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMap() {
    return null;
  }

  /**
   * Stwórz mapę gdzie kluczem jest typ rachunku a wartością mapa mężczyzn posiadających ten rachunek, gdzie kluczem jest obiekt User a wartoscią suma pieniędzy
   * na rachunku danego typu przeliczona na złotkówki.  Napisz to za pomocą strumieni.
   */
  Map<AccountType, Map<User, BigDecimal>> getAccountUserMoneyInPLNMapAsStream() {
    return null;
  }

  /**
   * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
   * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach.
   */

  Map<Permit, List<User>> getUsersByTheyPermitsSorted() {
    return null;
  }

  /**
   * Podziel wszystkich użytkowników po ich upoważnieniach, przygotuj mapę która gdzie kluczem jest upoważnenie a wartością lista użytkowników, posortowana po
   * ilości środków na koncie w kolejności od największej do najmniejszej ich ilości liczonej w złotówkach. Napisz to za pomoca strumieni.
   */

  Map<Permit, List<User>> getUsersByTheyPermitsSortedAsStream() {
    return null;
  }

  /**
   * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
   * List<Users>
   */
  Map<Boolean, List<User>> divideUsersByPredicate(final Predicate<User> predicate) {
    return null;
  }

  /**
   * Podziel użytkowników na tych spełniających podany predykat i na tych niespełniających. Zwracanym typem powinna być mapa Boolean => spełnia/niespełnia,
   * List<Users>. Wykonaj zadanie za pomoca strumieni.
   */
  Map<Boolean, List<User>> divideUsersByPredicateAsStream(final Predicate<User> predicate) {
    return null;
  }



  /**
   * Zwraca zbiór walut w jakich są rachunki.
   */
  private Set<Currency> getCurenciesSet() {
   return holdings.stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .flatMap(company -> company.getUsers().stream())
            .flatMap(user -> user.getAccounts().stream())
            .map(currency -> currency.getCurrency())
            .collect(Collectors.toSet());
  }

  /**
   * Tworzy strumień rachunków.
   */
  private Stream<Account> getAccoutStream() {
    return null;
  }

  /**
   * Tworzy strumień użytkowników.
   */
  private Stream<User> getUserStream() {
    return holdings.stream()
            .flatMap(holding -> holding.getCompanies().stream())
            .flatMap(company -> company.getUsers()
                    .stream());
  }

 private Stream<Company> companyStream(List<Holding> com){
  return holdings.stream()
          .flatMap(holding -> holding.getCompanies().stream());
}
}