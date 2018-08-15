import java.awt.*
import java.io.*
import java.util.Scanner

class Line : Shape {

    constructor(c: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) : super(c, width, x1, y1, x2, y2) {}

    constructor(line: Line) : super(line) {}

    constructor(scanner: Scanner) : super(scanner) {}

    override fun draw(g: Graphics) {
        super.draw(g)
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y)
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
