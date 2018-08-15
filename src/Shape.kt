import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.io.PrintWriter
import java.util.Scanner
import java.util.Vector

open class Shape {
    var lineColor: Color
    var width: Int = 0
    var startPoint: MyPoint
    var endPoint: MyPoint
    var points = Vector<MyPoint>()

    constructor(shape: Shape) {
        this.lineColor = shape.lineColor
        this.width = shape.width
        this.startPoint = MyPoint(shape.startPoint)
        this.endPoint = MyPoint(shape.endPoint)
        this.points.add(startPoint)
        this.points.add(endPoint)
    }

    constructor(color: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        this.lineColor = color
        this.width = width
        this.startPoint = MyPoint(x1, y1)
        this.endPoint = MyPoint(x2, y2)
        this.points.add(startPoint)
        this.points.add(endPoint)
    }

    constructor(scanner: Scanner) {
        startPoint = MyPoint(scanner.nextInt(), scanner.nextInt())
        endPoint = MyPoint(scanner.nextInt(), scanner.nextInt())
        width = scanner.nextInt()
        lineColor = Color(scanner.nextInt())
        this.points.add(startPoint)
        this.points.add(endPoint)
    }

    fun rotate(d: Double) {
        startPoint.rotate(d)
        endPoint.rotate(d)
        for (p in points) {
            p.rotate(d)
        }
    }

    fun move(dx: Int, dy: Int) {
        startPoint.move(dx, dy)
        endPoint.move(dx, dy)
        for (p in points) {
            p.move(dx, dy)
        }
    }

    open fun draw(g: Graphics) {
        g.color = this.lineColor
        (g as Graphics2D).stroke = BasicStroke(width.toFloat())
    }

    override fun toString(): String {
        return ("{Color : {R : " + lineColor.red + ",G : " + lineColor.green + ",B : " + lineColor.blue
                + "},Width : " + width.toString() + ",Points : " + points.toString() + "}")
    }

    open fun Output(pw: PrintWriter) {
        pw.printf("%d %d %d %d %d %d ", startPoint.x, startPoint.y, endPoint.x, endPoint.y, width,
                lineColor.rgb)
    }
}
