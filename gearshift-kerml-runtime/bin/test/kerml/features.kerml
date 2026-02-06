// KerML model demonstrating features
package Features {
    class Person {
        feature name;
        feature age;
        derived feature fullName;
        composite feature address;
    }

    class Address {
        feature street;
        feature city;
        feature zipCode;
    }
}
