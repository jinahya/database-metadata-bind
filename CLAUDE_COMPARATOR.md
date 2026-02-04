# COMPARATORS

Check the `comparingInSpecifiedOrder` in Attribute.java.

This is the new strategy.

* Callers should decide the case-sensitivity of the database, via the `operator` argument.
* Callers should handle the null preference/safety via the `comparatør` argument.
    * See the `@see ContextUtils#nullOrdered(Context, Comparator)`
* Do not remove existing methods.
* Add new methods.
    * We should check the specification.
    * MIND TOKEN CONSUMPTION!
    * Check online directly, if it's cheaper.
    * See doc/DatabaseMetaData Javadoc Pages/DatabaseMetaData (Java SE 25 & JDK 25).html, if it's cheaper.
* New methods shall be remained as package-private, those methods should be reviewed carefully by human eyeballs.
* Do not modify test module.
