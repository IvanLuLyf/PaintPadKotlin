import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionAdapter
import java.awt.event.MouseMotionListener
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.util.Scanner
import java.util.Vector

import javax.swing.JPanel

class DrawPanel : JPanel {

    private var shapes: Vector<Shape>? = null
    private var type = NONE
    private var lineColor = Color.BLACK

    private var lineWidth = 1

    private val mouseMotionListener = object : MouseMotionAdapter() {

        override fun mouseDragged(e: MouseEvent?) {
            if (type == NONE) {

            } else {
                val x = e!!.x
                var y = e.y
                if (e.isShiftDown) {
                    val p0 = shapes!!.lastElement().startPoint
                    val width = x - p0.x
                    val height = y - p0.y
                    y = y - height + width

                }
                shapes!!.lastElement().endPoint.x = x
                shapes!!.lastElement().endPoint.y = y
            }
            repaint()
        }
    }

    private val mouseListener = object : MouseAdapter() {

        override fun mousePressed(e: MouseEvent) {
            val x = e.x
            val y = e.y
            when (type) {
                NONE -> {
                }
                LINE -> shapes!!.add(Line(lineColor, lineWidth, x, y, x, y))
                RECTANGLE -> shapes!!.add(Rectangle(lineColor, lineWidth, x, y, x, y))
                OVAL -> shapes!!.add(Oval(lineColor, lineWidth, x, y, x, y))
            }
        }
    }

    constructor() {
        this.shapes = Vector()
        init()
    }

    constructor(shapes: Vector<Shape>) {
        this.shapes = shapes
        init()
    }

    private fun init() {
        background = Color.WHITE
        addMouseListener(mouseListener)
        addMouseMotionListener(mouseMotionListener)
    }

    override fun paint(arg0: Graphics?) {
        super.paint(arg0)
        for (shape in shapes!!) {
            shape.draw(arg0!!)
        }
    }

    fun setType(type: Int) {
        this.type = type
    }

    fun setLineWidth(lineWidth: Int) {
        this.lineWidth = lineWidth
    }

    fun setLineColor(lineColor: Color) {
        this.lineColor = lineColor
    }

    fun loadFile(file: File) {
        shapes!!.clear()
        val scanner = Scanner(file)
        setSize(scanner.nextInt(), scanner.nextInt())
        background = Color(scanner.nextInt())
        while (scanner.hasNext()) {
            val sType = scanner.next()
            when (sType) {
                "L" -> shapes!!.add(Line(scanner))
                "R" -> shapes!!.add(Rectangle(scanner))
                "O" -> shapes!!.add(Oval(scanner))
                else -> {
                }
            }
        }
        repaint()
    }

    fun saveFile(file: File) {
        val printWriter = PrintWriter(file)
        printWriter.printf("%d %d ", width, height)
        printWriter.println(background.rgb)
        for (s in shapes!!) {
            s.Output(printWriter)
        }
        printWriter.close()
    }

    companion object {
        const val NONE = 0
        const val LINE = 1
        const val RECTANGLE = 2
        const val OVAL = 3
    }
}
