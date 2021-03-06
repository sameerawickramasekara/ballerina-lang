
function testCyclicReferenceWithDefaultable () returns int {
    Person p = new();
    Employee em = new;
    em.age = 89;
    p.emp = em;
    Employee e = <Employee>p.emp;

    return e.age;
}

type Person object {
    public int age = 0;
    public Employee? emp = ();
};

type Employee object {
    public int age = 0;
    public Foo? foo = ();
    public Bar? bar = ();
};

type Foo object {
    public int calc = 0;
    public Bar? bar1 = ();
};

type Bar record {
    int barVal = 0;
    string name = "";
    Person? person = ();
};
