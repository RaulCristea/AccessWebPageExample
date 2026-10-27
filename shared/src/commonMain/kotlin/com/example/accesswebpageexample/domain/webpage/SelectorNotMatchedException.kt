package com.example.accesswebpageexample.domain.webpage

class SelectorNotMatchedException(val selector: String) : Exception("No element matches \"$selector\"")
