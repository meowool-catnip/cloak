# Benchmarking

Basically, this directory is used to compare the reflection performance of **Cloak** library and **JDK**, as well as some other performance trap tests.



## Reference results

You can find the results at the beginning comment of each benchmark class in the `kotlin` folder.

> For example

[ReflectionObjectAccessTests.kt](kotlin/com/meowool/cloak/ReflectionObjectAccessTests.kt#L37-L58)


## Running the tests

> For Intellij / Android Studio:

You can open the Gradle panel in `View > Tools Windows > Gradle` , and then find and expand `Tasks > benchmark`, finally just double-click `benchmark`, which will run and print the benchmark results  in the `Run` window of your IDE.

> For Terminal:

```bash
$ ./gradlew benchmark
```

or

```bash
$ gradle benchmark
```

