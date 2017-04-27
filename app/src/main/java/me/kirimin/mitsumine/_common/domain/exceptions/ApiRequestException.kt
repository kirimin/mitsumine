package me.kirimin.mitsumine._common.domain.exceptions

class ApiRequestException(e: String, val code: Int = 0) : Exception(e)