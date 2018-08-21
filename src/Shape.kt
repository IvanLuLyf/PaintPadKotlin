import java.awt.*
import java.io.PrintWriter
import java.util.Scanner
import java.util.Vector


open class Shape {

    var fillStyle: Int = 1
    var lineColor: Color
    var fillColor: Color
    var fillColor2: Color

    var width: Int = 0
    var selected: Boolean = false
    var startPoint: MyPoint
    var endPoint: MyPoint
    var points = Vector<MyPoint>()

    constructor(shape: Shape) {
        this.lineColor = shape.lineColor
        this.fillColor = shape.fillColor
        this.fillColor2 = shape.fillColor2
        this.fillStyle = shape.fillStyle
        this.width = shape.width
        this.startPoint = MyPoint(shape.startPoint)
        this.endPoint = MyPoint(shape.endPoint)
        for (p in shape.points) {
            this.points.add(MyPoint(p.x, p.y))
        }
    }

    constructor(color: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        this.lineColor = color
        this.fillStyle = 0
        this.fillColor = Color.WHITE
        this.fillColor2 = Color.WHITE
        this.width = width
        this.startPoint = MyPoint(x1, y1)
        this.endPoint = MyPoint(x2, y2)
    }

    constructor(lineColor: Color, fillColor: Color, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        this.lineColor = lineColor
        this.fillStyle = 1
        this.fillColor = fillColor
        this.fillColor2 = Color.WHITE
        this.width = width
        this.startPoint = MyPoint(x1, y1)
        this.endPoint = MyPoint(x2, y2)
    }

    constructor(lineColor: Color, fillColor: Color, fillColor2: Color, fillStyle: Int, width: Int, x1: Int, y1: Int, x2: Int, y2: Int) {
        this.lineColor = lineColor
        this.fillStyle = fillStyle
        this.fillColor = fillColor
        this.fillColor2 = fillColor2
        this.width = width
        this.startPoint = MyPoint(x1, y1)
        this.endPoint = MyPoint(x2, y2)
    }

    constructor(scanner: Scanner) {
        startPoint = MyPoint(scanner.nextInt(), scanner.nextInt())
        endPoint = MyPoint(scanner.nextInt(), scanner.nextInt())
        width = scanner.nextInt()
        lineColor = Color(scanner.nextInt())
        fillStyle = scanner.nextInt()
        if (fillStyle > 0) {
            fillColor = Color(scanner.nextInt())
        } else {
            fillColor = Color.WHITE
        }
        if (fillStyle > 1) {
            fillColor2 = Color(scanner.nextInt())
        } else {
            fillColor2 = Color.WHITE
        }
        val pNum = scanner.nextInt()
        for (i in 0 until pNum) {
            points.add(MyPoint(scanner.nextInt(), scanner.nextInt()))
        }
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


    protected fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
        return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)).toDouble())
    }

    protected fun contain(x: Int, y: Int, left: Int, top: Int, right: Int, bottom: Int): Boolean {
        return x in (left + 1)..(right - 1) && y > top && y < bottom
    }

    open fun isContained(x: Int, y: Int): Boolean {
        return false
    }

    protected fun initFill(g: Graphics, left: Int, top: Int, width: Int, height: Int): Boolean {
        if (fillStyle == 0) {
            return false
        } else if (fillStyle == 1) {
            g.color = fillColor
        } else if (fillStyle > 1) {
            var paint: Paint? = null
            when (fillStyle) {
                2 // 水平
                -> paint = GradientPaint(left.toFloat(), top.toFloat(), fillColor, left.toFloat(), (top + height).toFloat(), fillColor2)
                3 // 水平反
                -> paint = GradientPaint(left.toFloat(), top.toFloat(), fillColor2, left.toFloat(), (top + height).toFloat(), fillColor)
                4 // 垂直
                -> paint = GradientPaint(left.toFloat(), top.toFloat(), fillColor, (left + width).toFloat(), top.toFloat(), fillColor2)
                5 // 垂直反
                -> paint = GradientPaint(left.toFloat(), top.toFloat(), fillColor2, (left + width).toFloat(), top.toFloat(), fillColor)
                6 // 左上右下
                -> paint = GradientPaint(left.toFloat(), top.toFloat(), fillColor, (left + width).toFloat(), (top + height).toFloat(), fillColor2)
                7 // 左上右下反
                -> paint = GradientPaint(left.toFloat(), top.toFloat(), fillColor2, (left + width).toFloat(), (top + height).toFloat(), fillColor)
                8 // 左下右上
                -> paint = GradientPaint(left.toFloat(), (top + height).toFloat(), fillColor, (left + width).toFloat(), top.toFloat(), fillColor2)
                9 // 左下右上反
                -> paint = GradientPaint(left.toFloat(), (top + height).toFloat(), fillColor2, (left + width).toFloat(), top.toFloat(), fillColor)
            }
            (g as Graphics2D).paint = paint
        }
        return true
    }

    protected fun drawController(g: Graphics) {
        if (selected) {
            (g as Graphics2D).stroke = BasicStroke(1f)
            drawSmallRec(g, startPoint.x, startPoint.y)
            drawSmallRec(g, endPoint.x, startPoint.y)
            drawSmallRec(g, startPoint.x, endPoint.y)
            drawSmallRec(g, endPoint.x, endPoint.y)
            drawSmallRnd(g, (startPoint.x + endPoint.x) / 2, startPoint.y)
            drawSmallRnd(g, startPoint.x, (startPoint.y + endPoint.y) / 2)
            drawSmallRnd(g, endPoint.x, (startPoint.y + endPoint.y) / 2)
            drawSmallRnd(g, (startPoint.x + endPoint.x) / 2, endPoint.y)
        }
    }

    protected fun drawSmallRec(g: Graphics, x: Int, y: Int) {
        g.color = Color.BLUE
        g.fillRect(x - 3, y - 3, 7, 7)
        g.color = Color.BLACK
        g.drawRect(x - 3, y - 3, 7, 7)
    }

    protected fun drawSmallRnd(g: Graphics, x: Int, y: Int) {
        g.color = Color.BLUE
        g.fillOval(x - 4, y - 4, 9, 9)
        g.color = Color.BLACK
        g.drawOval(x - 4, y - 4, 9, 9)
    }

    override fun toString(): String {
        return ("{Color : {R : " + lineColor.red + ",G : " + lineColor.green + ",B : " + lineColor.blue
                + "},Width : " + width.toString() + ",Points : " + points.toString() + "}")
    }

    open fun Output(pw: PrintWriter) {
        pw.printf("%d %d %d %d %d %d %d ", startPoint.x, startPoint.y, endPoint.x, endPoint.y, width,
                lineColor.rgb, fillStyle)
        if (fillStyle > 0) {
            pw.print(fillColor.rgb.toString() + " ")
        }
        if (fillStyle > 1) {
            pw.print(fillColor2.rgb.toString() + " ")
        }
        pw.print(points.size.toString() + " ")
        for (p in points) {
            pw.printf("%d %d ", p.x, p.y)
        }
    }
}
