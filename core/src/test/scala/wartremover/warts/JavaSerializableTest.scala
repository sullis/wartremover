package org.wartremover
package test

import java.io.Serializable
import org.scalatest.FunSuite
import org.wartremover.warts.JavaSerializable


object Foo extends Serializable

class JavaSerializableTest extends FunSuite with ResultAssertions {
  test("java.io.Serializable can't be inferred") {
    val result = WartTestTraverser(JavaSerializable) {
      // String is not a subtype of scala.Serializable, but is of java.io.Serializable
      // Foo is a subtype of scala.Serializable, which is a subtype of java.io.Serializable
      // so scala should infer List[java.io.Serializable]
      List("foo", Foo)
    }
    assertError(result)("Inferred type containing Serializable")
  }
  test("Serializable wart obeys SuppressWarnings") {
    val result = WartTestTraverser(JavaSerializable) {
      @SuppressWarnings(Array("org.wartremover.warts.JavaSerializable"))
      val foo = List("foo", Foo)
    }
    assertEmpty(result)
  }
}
