// KerML model demonstrating specialization
package Inheritance {
    abstract class Animal;

    class Dog :> Animal;
    class Cat :> Animal;

    class Mammal :> Animal;
    class Reptile :> Animal;
}
