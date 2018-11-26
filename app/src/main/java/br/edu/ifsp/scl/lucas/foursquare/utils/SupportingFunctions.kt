package br.edu.ifsp.scl.lucas.foursquare.utils

inline fun <T:Any, R> whenNotNull(input: T?, callback: (T)->R): R? = input?.let(callback)

