import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import java.io.File
import java.io.PrintWriter
import java.util.*
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JPopupMenu
import javax.swing.JSeparator


class DrawPanel() : JPanel() {

    private var shapes: Vector<Shape> = Vector()

    var shapeSelect: Shape? = null
    var shapeTemp: Shape? = null
    private var preX: Int = 0
    private var preY: Int = 0

    var hasChange = false

    var type: Int = NONE
        set(value) {
            field = value
            shapeSelect?.selected = false
        }

    var lineColor: Color = Color.BLACK
        set(value) {
            field = value
            shapeSelect?.lineColor = lineColor
            repaint()
        }

    var fillStyle: Int = 1
        set(value) {
            field = value
            shapeSelect?.fillStyle = fillStyle
            repaint()
        }

    var fillColor: Color = Color.WHITE
        set(value) {
            field = value
            shapeSelect?.fillColor = fillColor
            repaint()
        }

    var fillColor2: Color = Color.WHITE
        set(value) {
            field = value
            shapeSelect?.fillColor2 = fillColor2
            repaint()
        }

    var lineWidth = 1
        set(value) {
            field = value
            shapeSelect?.width = lineWidth
            repaint()
        }

    private val popupMenu: JPopupMenu = JPopupMenu().apply {
        add(JMenuItem("剪切").apply { addActionListener { cut() } })
        add(JMenuItem("复制").apply { addActionListener { copy() } })
        add(JMenuItem("粘贴").apply { addActionListener { paste() } })
        add(JSeparator())
        add(JMenuItem("置顶").apply { addActionListener { goTop() } })
        add(JMenuItem("置底").apply { addActionListener { goBottom() } })
        add(JMenuItem("上移").apply { addActionListener { goUp() } })
        add(JMenuItem("下移").apply { addActionListener { goDown() } })
    }

    private val mouseMotionListener = object : MouseMotionAdapter() {

        override fun mouseDragged(e: MouseEvent) {
            if (type == NONE) {

            } else {
                val x = e.x
                var y = e.y
                if (e.isShiftDown) {
                    val p0 = shapes.lastElement().startPoint
                    val width = x - p0.x
                    val height = y - p0.y
                    y = y - height + width
                }
                shapes.lastElement().endPoint.x = x
                shapes.lastElement().endPoint.y = y
                hasChange = true
            }
            repaint()
        }
    }

    private val mouseListener = object : MouseAdapter() {

        override fun mouseReleased(e: MouseEvent) {
            if (e.button == MouseEvent.BUTTON3) {
                popupMenu.show(this@DrawPanel, e.x, e.y)
            } else {
                if (type == NONE) {
                    hasChange = true
                    shapeSelect?.move(e.x - preX, e.y - preY)
                }
                repaint()
            }
        }

        override fun mousePressed(e: MouseEvent) {
            val x = e.x
            val y = e.y
            preX = x;
            preY = y;
            when (type) {
                NONE -> {
                    shapeSelect?.selected = false
                    shapeSelect = null
                    for (i in shapes.size - 1 downTo 0) {
                        if (shapes[i].isContained(x, y)) {
                            shapes[i].selected = true
                            shapeSelect = shapes[i]
                            repaint()
                            break
                        }
                    }
                }
                LINE -> {
                    hasChange = true
                    shapes.add(Line(lineColor, lineWidth, x, y, x, y))
                }
                RECTANGLE -> {
                    hasChange = true
                    shapes.add(Rectangle(lineColor, fillColor, fillColor2, fillStyle, lineWidth, x, y, x, y))
                }
                OVAL -> {
                    hasChange = true
                    shapes.add(Oval(lineColor, fillColor, fillColor2, fillStyle, lineWidth, x, y, x, y))
                }
            }
        }
    }

    init {
        this.shapes = Vector()
        background = Color.WHITE
        addMouseListener(mouseListener)
        addMouseMotionListener(mouseMotionListener)
    }

    fun newFile() {
        hasChange = false
        shapes.clear()
        repaint()
    }

    override fun paint(arg0: Graphics?) {
        super.paint(arg0)
        for (shape in shapes) {
            shape.draw(arg0!!)
        }
    }

    fun loadFile(file: File) {
        hasChange = false
        shapes.clear()
        val scanner = Scanner(file)
        setSize(scanner.nextInt(), scanner.nextInt())
        background = Color(scanner.nextInt())
        while (scanner.hasNext()) {
            val sType = scanner.next()
            when (sType) {
                "L" -> shapes.add(Line(scanner))
                "R" -> shapes.add(Rectangle(scanner))
                "O" -> shapes.add(Oval(scanner))
                else -> {
                }
            }
        }
        repaint()
    }

    fun saveFile(file: File) {
        hasChange = false
        val printWriter = PrintWriter(file)
        printWriter.printf("%d %d ", width, height)
        printWriter.println(background.rgb)
        for (s in shapes) {
            s.Output(printWriter)
        }
        printWriter.close()
    }

    fun cut() {
        if (shapeSelect != null) {
            hasChange = true
            shapeTemp = shapeSelect
            shapes.remove(shapeSelect)
            shapeSelect = null
            repaint()
        }
    }

    fun copy() {
        if (shapeSelect != null) {
            shapeTemp = copyShape(shapeSelect!!)
        }
    }

    fun paste() {
        if (shapeTemp != null) {
            hasChange = true
            shapeSelect?.selected = false
            val shapeNew = copyShape(shapeTemp!!)
            if (shapeNew != null) {
                shapeNew.selected = true
                shapeSelect = shapeNew
                shapes.add(shapeNew)
            }
            repaint()
        }
    }

    fun goTop() {
        if (shapeSelect != null) {
            hasChange = true
            shapes.remove(shapeSelect)
            shapes.add(shapeSelect)
            repaint()
        }
    }

    fun goUp() {
        if (shapeSelect != null) {
            hasChange = true
            val i = shapes.indexOf(shapeSelect)
            if (i < shapes.size - 1) {
                shapes.remove(shapeSelect)
                shapes.insertElementAt(shapeSelect, i + 1)
            }
            repaint()
        }
    }

    fun goBottom() {
        if (shapeSelect != null) {
            hasChange = true
            shapes.remove(shapeSelect)
            shapes.insertElementAt(shapeSelect, 0)
            repaint()
        }
    }

    fun goDown() {
        if (shapeSelect != null) {
            hasChange = true
            val i = shapes.indexOf(shapeSelect)
            if (i > 0) {
                shapes.remove(shapeSelect)
                shapes.insertElementAt(shapeSelect, i - 1)
            }
            repaint()
        }
    }

    private fun copyShape(shape: Shape): Shape? {
        var shapeNew: Shape? = null
        when (shape) {
            is Line -> shapeNew = Line(shape)
            is Rectangle -> shapeNew = Rectangle(shape)
            is Oval -> shapeNew = Oval(shape)
        }
        return shapeNew
    }

    companion object {
        const val NONE = 0
        const val LINE = 1
        const val RECTANGLE = 2
        const val OVAL = 3
    }
}
