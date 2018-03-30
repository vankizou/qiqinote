/**
 * Created by vanki on 2018/1/17 18:17.
 */

fun main(args: Array<String>) {
    A().B().aa();
}

class A {
    private val a : Int = 1;

    inner class B {
        fun aa() = a;
    }
}