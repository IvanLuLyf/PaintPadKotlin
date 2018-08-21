import java.awt.Color
import java.awt.Graphics
import java.io.PrintWriter
import java.util.Scanner
import sun.swing.SwingUtilities2.drawRect


class Rectangle : Shape {

    constructor(rectangle: Rectangle) : super(rectangle)

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
            g.fillRect(left, top, width, height)
        }
        g.color = lineColor
        g.drawRect(left, top, width, height)
        drawController(g)
    }

    override fun isContained(x: Int, y: Int): Boolean {
        val left = Math.min(startPoint.x, endPoint.x)
        val top = Math.min(startPoint.y, endPoint.y)
        val right = Math.max(startPoint.x, endPoint.x)
        val bottom = Math.max(startPoint.y, endPoint.y)
        return if (fillStyle == 0) {
            contain(x, y, left - 3, top - 3, right + 3, bottom + 3) && !contain(x, y, left + 3, top + 3, right - 3, bottom - 3)
        } else {
            contain(x, y, left, top, right, bottom)
        }
    }

    override fun Output(pw: PrintWriter) {
        pw.print("R ")
        super.Output(pw)
        pw.println()
    }

    override fun toString(): String {
        return "Rectangle : " + super.toString()
    }

}