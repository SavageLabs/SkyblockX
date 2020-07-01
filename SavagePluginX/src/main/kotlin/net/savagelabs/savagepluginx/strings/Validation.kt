package net.savagelabs.savagepluginx.strings

fun String.isAlphaNumeric() = chars().allMatch(Character::isLetterOrDigit)