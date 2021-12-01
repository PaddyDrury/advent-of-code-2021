package util



inline fun <reified T> loadInputFromResource(resource: String): List<String> = T::class.java.getResource(resource).readText().lines()