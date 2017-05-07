package me.kirimin.mitsumine._common.domain.extensions

import java.text.Normalizer

fun String.normalizeToNFC() = Normalizer.normalize(this, Normalizer.Form.NFC)!!