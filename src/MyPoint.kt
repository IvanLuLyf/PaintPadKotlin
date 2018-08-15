import java.awt.Point

class MyPoint : Point {

    constructor(x: Int, y: Int) : super(x, y) {}

    constructor(p: MyPoint) : super(p.x, p.y) {}

    override fun move(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    fun rotate(alpha: kotlin.Double) {
        val newX = (x * Math.cos(alpha) - y * Math.sin(alpha)).toInt()
        val newY = (x * Math.sin(alpha) + y * Math.cos(alpha)).toInt()
        x = newX
        y = newY
    }

    override fun toString(): String {
        return "{X = " + x.toString() + ", Y = " + y.toString() + "}"
    }
}
