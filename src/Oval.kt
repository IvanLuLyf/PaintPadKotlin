import java.awt.Color
import java.awt.Graphics
import java.io.PrintWriter
import java.util.Scanner

class Oval : Shape {

    constructor(oval: Oval) : super(oval) {}

    constructor(c: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) : super(c, width, x1, y1, x2, y2)

    constructor(c: Color, f: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) : super(c, f, width, x1, y1, x2, y2)

    constructor(c: Color, f: Color, f2: Color, style: Int, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) : super(c, f, f2, style, width, x1, y1, x2, y2)

    constructor(scanner: Scanner) : super(scanner)

    override fun draw(g: Graphics) {
        super.draw(g)
        val left = Math.min(startPoint.x, endPoint.x)
        val top = Math.min(startPoint.y, endPoint.y)
        val width = Math.abs(startPoint.x - endPoint.x)
        val height = Math.abs(startPoint.y - endPoint.y)
        if (initFill(g, left, top, width, height)) {
            g.fillOval(left, top, width, height)
        }
        g.color = lineColor
        g.drawOval(left, top, width, height)
        drawController(g)
    }

    override fun isContained(x: Int, y: Int): Boolean {
        if (fillStyle == 0) {
            val x0: Int = (startPoint.x + endPoint.x) / 2
            val y0: Int = (startPoint.y + endPoint.y) / 2
            val a: Int = Math.abs(startPoint.x - endPoint.x) / 2
            val b: Int = Math.abs(startPoint.y - endPoint.y) / 2
            val d: Int
            val e: Int
            val c: Double
            val d1: Double
            val d2: Double
            c = Math.sqrt(Math.abs(a * a - b * b).toDouble())
            if (a >= b) {
                d1 = distance(x, y, x0 - c.toInt(), y0)
                d2 = distance(x, y, x0 + c.toInt(), y0)
            } else {
                d1 = distance(x, y, x0, y0 - c.toInt())
                d2 = distance(x, y, x0, y0 + c.toInt())
            }
            e = if (a > b) a else b
            d = Math.abs((d1 + d2) / 2 - e).toInt()
            return d < 3
        } else {
            val x0 = (startPoint.x + endPoint.x) / 2.0
            val y0 = (startPoint.y + endPoint.y) / 2.0
            val da = Math.abs(startPoint.x - endPoint.x) / 2.0
            val db = Math.abs(startPoint.y - endPoint.y) / 2.0
            return (x - x0) * (x - x0) / (da * da) + (y - y0) * (y - y0) / (db * db) < 1 && (x - x0) * (x - x0) / (da * da) + (y - y0) * (y - y0) / (db * db) >= 0
        }
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
