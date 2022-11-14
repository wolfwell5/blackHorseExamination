package com.example.dealcontracts.utils

fun readTextFile(file: String): String {
  try {
    return FileUtils::class.java.getResource(file).readText()
  } catch (e: Throwable) {
    println(file)
    throw e
  }
}

private class FileUtils
