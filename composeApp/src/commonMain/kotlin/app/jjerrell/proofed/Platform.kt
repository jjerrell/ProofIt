package app.jjerrell.proofed

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform