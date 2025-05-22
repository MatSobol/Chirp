package com.utility

import de.mkammerer.argon2.Argon2
import de.mkammerer.argon2.Argon2Factory

object Argon {
    private val argon2: Argon2 = Argon2Factory.create();
    fun hash(password: String) : String {
        return argon2.hash(10, 65536, 1, password.toCharArray());
    }
    fun verify(hash: String, password: String) : Boolean {
        return argon2.verify(hash, password.toCharArray())
    }
}