fun String.substringBetween(after: String, before: String) =
    substringBefore(before).substringAfter(after)
