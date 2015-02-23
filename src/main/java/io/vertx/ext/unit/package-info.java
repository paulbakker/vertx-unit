/**
 * = Vertx unit
 *
 * Asynchronous polyglot unit testing.
 *
 * == Introduction
 *
 * Vertx Unit aims to make Vertx applications testable. It provides a polyglot API for writing asynchronous
 * tests easily. Vertx Unit Api borrows from existing test frameworks like http://junit.org[JUnit] or http://qunitjs.com[QUnit]
 * and follows the Vert.x practices.
 *
 * Vertx Unit can be used in different ways and run anywhere your code runs, it is just a matter of reporting
 * the results the right way, this example shows the bare minimum test suite:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#test_01}
 * ----
 *
 * The {@code run} method will execute the suite and go through all the
 * tests of the suite. The suite can fail or pass, this does not matter if the outter world is not aware
 * of the test result.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#test_02}
 * ----
 *
 * When executed, the test suite now reports to the console the steps of the test suite:
 *
 * ----
 * Begin test suite the_test_suite
 * Begin test my_test
 * Passed my_test
 * End test suite the_test_suite , run: 1, Failures: 0, Errors: 0
 * ----
 *
 * The {@code reporters} option configures the reporters used by the suite runner for reporting the execution
 * of the tests, see the <<reporting>> section for more info.
 *
 * == Writing a test suite
 *
 * A test suite is a named collection of test case, a test case is a straight callback to execute. The suite can
 * have lifecycle callbacks to execute _before_ and/or _after_ the test cases or the test suite that are used for
 * initializing or disposing services used by the test suite.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_01}
 * ----
 *
 * The API is fluent and therefore the test can be chained:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_02}
 * ----
 *
 * Vertx Unit provides _before_ and _after_ callbacks for doing global setup or cleanup:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_03}
 * ----
 *
 * The declaration order of the method does not matter, the example declares the _before_ callback before
 * the test cases and _after_ callback after the test cases but it could be anywhere, as long as it is done before
 * running the test suite.
 *
 * The _before_ callback is executed before any tests, when it fails, the test suite execution will stop and the
 * failure is reported. The _after_ callback is the last callback executed by the testsuite, unless
 * the _before_ callback reporter a failure.
 *
 * Likewise, Vertx Unit provides the _beforeEach_ and _afterEach_ callback that do the same but are executed
 * for each test case:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writing_test_suite_04}
 * ----
 *
 * The _beforeEach_ callback is executed before each test case, when it fails, the test case is not executed and the
 * failure is reported. The _afterEach_ callback is the executed just after the test case callback, unless
 * the _beforeEach_ callback reported a failure.
 *
 * == Asserting
 *
 * Vertx Unit provides the _test_ object for doing assertions in test cases. The _test_ object provides the usual
 * methods when dealing with assertions.
 *
 * === assertEquals
 *
 * Assert two objects are equals, works for _basic_ types or _json_ types.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_01}
 * ----
 *
 * There is also an overloaded version for providing a message:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_02}
 * ----
 *
 * Usually each assertion provides an overloaded version.
 *
 * === assertNotEquals
 *
 * The counter part of _assertEquals_.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_03}
 * ----
 *
 * === assertTrue and assertFalse
 *
 * Asserts the value of a boolean expression.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_04}
 * ----
 *
 * === Failing
 *
 * Last but not least, _test_ provides a _fail_ method that will throw an assertion error:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asserting_05}
 * ----
 *
 * == Asynchronous testing
 *
 * The previous examples supposed that test cases were terminated after their respective callbacks, this is the
 * default behavior of a test case callback. Often it is desirable to terminate the test after the test case
 * callback, for instance:
 *
 * .The Async object asynchronously completes the test case
 * [source,$lang]
 * ----
 * {@link examples.Examples#async_01}
 * ----
 * <1> The callback exits but the test case is not terminated
 * <2> The event callback from the bus terminates the test
 *
 * Creating an {@link io.vertx.ext.unit.Async} object with the {@link io.vertx.ext.unit.Test#async()} method marks the
 * executed test case as non terminated. The test case terminates when the {@link io.vertx.ext.unit.Async#complete()}
 * method is invoked.
 *
 * NOTE: When the `complete` callback is not invoked, the test case fails after a certain timeout.
 *
 * Several `Async` objects can be created during the same test case, all of them must be _completed_ to terminate
 * the test.
 *
 * .Several Async objects provide coordination
 * [source,$lang]
 * ----
 * {@link examples.Examples#async_02}
 * ----
 *
 * Async objects can also be used in _before_ or _after_ callbacks, it can be very convenient in a _before_ callback
 * to implement a setup that depends on one or several asynchronous results:
 *
 * .Async start an http server before test cases
 * [source,$lang]
 * ----
 * {@link examples.Examples#async_03}
 * ----
 *
 * [[reporting]]
 * == Running
 *
 * When a test suite is created, it won't be executed until the {@link io.vertx.ext.unit.TestSuite#run} method
 * is called.
 *
 * .Running a test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_01}
 * ----
 *
 * When the testsuite is executed, it will use the current Vert.x event loop for the steps of the test suite,
 * that is the context object returned by `io.vertx.core.Vertx#currentContext`. When such context does not
 * exist, the test suite is executed synchronously in the current thread.
 *
 * The test suite can also be ran with a specified `Vertx` instance:
 *
 * .Provides a Vertx instance to run the test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_02}
 * ----
 *
 * This execution uses the `Context` provided by the `Vertx` instance for runnings the steps of the test suite.
 *
 * === Test suite completion
 *
 * No assumptions can be made about when the test suite will be completed, and if some code needs to be executed
 * after the test suite, it should either be in the test suite _after_ callback or as callback of the
 * {@link io.vertx.ext.unit.TestCompletion}:
 *
 * .Test suite execution callback
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_03}
 * ----
 *
 * The `TestCompletion` object provides also a {@link io.vertx.ext.unit.TestCompletion#resolve} method that
 * takes a `Future` object, this `Future` will be notified of the test suite execution:
 *
 * .Resolving the start Future with the test suite
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_04}
 * ----
 *
 * This allow to easily create a _test_ verticle whose deployment is the test suite execution, allowing the
 * code that deploys it to be easily aware of the success or failure.
 *
 * === Time out
 *
 * The test cases of a test suite must execute before a certain timeout is reached. The default timeout is
 * of _2 minutes_, it can be changed using _test options_:
 *
 * .Setting the test suite timeout
 * [source,$lang]
 * ----
 * {@link examples.Examples#running_05}
 * ----
 *
 * == Reporting
 *
 * == Junit integration
 *
 * todo
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@GenModule(name = "vertx-unit")
@Document(fileName = "index.adoc")
package io.vertx.ext.unit;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;