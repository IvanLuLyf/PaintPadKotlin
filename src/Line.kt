import java.awt.*
import java.io.*
import java.util.Scanner

class Line : Shape {

    constructor(c: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) : super(c, width, x1, y1, x2, y2)

    constructor(line: Line) : super(line)

    constructor(scanner: Scanner) : super(scanner)

    override fun draw(g: Graphics) {
        super.draw(g)
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y)
        if (selected) {
            g.color = Color.BLACK;
            drawSmallRec(g, startPoint.x, startPoint.y);
            drawSmallRec(g, endPoint.x, endPoint.y);
        }
    }

    override fun isContained(x: Int, y: Int): Boolean {
        val a: Double = distance(x, y, startPoint.x, startPoint.y)
        val b: Double = distance(startPoint.x, startPoint.y, endPoint.x, endPoint.y)
        val c: Double = distance(x, y, endPoint.x, endPoint.y)
        val d: Double
        d = Math.sqrt(a * a - (a * a + b * b - c * c) * (a * a + b * b - c * c) / (4.0 * b * b))
        return d < 3
    }

    override fun Output(pw: PrintWriter) {
        pw.print("L ")
        super.Output(pw)
        pw.println()
    }

    override fun toString(): String {
        return "Line : " + super.toString()
    }
}
