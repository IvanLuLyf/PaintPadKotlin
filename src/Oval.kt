import java.awt.Color
import java.awt.Graphics
import java.io.PrintWriter
import java.util.Scanner

class Oval : Shape {

    constructor(oval: Oval) : super(oval) {}

    constructor(c: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) : super(c, width, x1, y1, x2, y2) {}

    constructor(scanner: Scanner) : super(scanner) {}

    override fun draw(g: Graphics) {
        super.draw(g)
        val left = Math.min(startPoint.x, endPoint.x)
        val top = Math.min(startPoint.y, endPoint.y)
        val width = Math.abs(startPoint.x - endPoint.x)
        val height = Math.abs(startPoint.y - endPoint.y)
        g.drawOval(left, top, width, height)
    }

    override fun Output(pw: PrintWriter) {
        pw.print("O ")
        super.Output(pw)
        pw.println()
    }

    override fun toString(): String {
        return "Oval : " + super.toString()
    }
}
