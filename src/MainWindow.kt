import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.ItemListener
import java.util.Vector

import javax.swing.ButtonGroup
import javax.swing.JButton
import javax.swing.JColorChooser
import javax.swing.JFileChooser
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JScrollPane
import javax.swing.JSplitPane
import javax.swing.UIManager
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder

class MainWindow : JFrame() {
    private val fileChooser: JFileChooser = JFileChooser()
    private val shapes: Vector<Shape> = Vector()

    private val mnuNew_Click = ActionListener {
        shapes.clear()
        title = "画图"
        panelCanvas.repaint()
    }

    private val mnuOpen_Click = ActionListener {
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            title = "画图 - " + file.absolutePath
            panelCanvas.loadFile(file)
        }
    }

    private val mnuSave_Click = ActionListener {
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            title = "画图 - " + file.absolutePath
            panelCanvas.saveFile(file)
        }
    }

    private val mnuExit_Click = ActionListener { dispose() }

    private val selectLineColor = ActionListener {
        val color = JColorChooser.showDialog(this, "选择线条颜色", btnLineColor.background)
        if (color != null) {
            btnLineColor.background = color
            panelCanvas.setLineColor(color)
        }
    }

    private val setLineWidth = ActionListener {
        val w = JOptionPane.showInputDialog("输入线宽", 1)
        try {
            val width = Integer.parseInt(w)
            panelCanvas.setLineWidth(width)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private val mnuAbout_Click = ActionListener {
        JOptionPane.showMessageDialog(this, "画图 Ver 1.0", "关于画图", JOptionPane.PLAIN_MESSAGE)
    }

    private val selectShape = ItemListener {
        val sel = it.getItemSelectable()
        if (sel == btnArrow) {
            panelCanvas.setType(DrawPanel.NONE)
        } else if (sel == btnLine) {
            panelCanvas.setType(DrawPanel.LINE)
        } else if (sel == btnRectangle) {
            panelCanvas.setType(DrawPanel.RECTANGLE)
        } else if (sel == btnOval) {
            panelCanvas.setType(DrawPanel.OVAL)
        }
    }

    private val menuBar: JMenuBar = JMenuBar().apply {
        add(JMenu("文件").apply {
            add(JMenuItem("新建").apply {
                addActionListener(mnuNew_Click)
            })
            add(JMenuItem("打开").apply {
                addActionListener(mnuOpen_Click)
            })
            add(JMenuItem("保存").apply {
                addActionListener(mnuSave_Click)
            })
            add(JMenuItem("退出").apply {
                addActionListener(mnuExit_Click)
            })
        })
        add(JMenu("帮助").apply {
            add(JMenuItem("关于").apply {
                addActionListener(mnuAbout_Click)
            })
        })
    }

    private val btnArrow: JRadioButton = JRadioButton("鼠标").apply {
        isSelected = true
        addItemListener(selectShape)
    }

    private val btnLine: JRadioButton = JRadioButton("直线").apply {
        addItemListener(selectShape)
    }

    private val btnRectangle: JRadioButton = JRadioButton("矩形").apply {
        addItemListener(selectShape)
    }

    private val btnOval: JRadioButton = JRadioButton("椭圆").apply {
        addItemListener(selectShape)
    }

    private val groupShape: ButtonGroup = ButtonGroup().apply {
        add(btnArrow)
        add(btnLine)
        add(btnRectangle)
        add(btnOval)
    }

    private val btnLineColor: JButton = JButton("线条颜色").apply {
        addActionListener(selectLineColor)
    }

    private val panelCanvas: DrawPanel = DrawPanel().apply {
        background = Color.WHITE
        bounds = Rectangle(100, 50, 500, 400)
        border = LineBorder(Color.BLACK)
    }

    private val splitPane: JSplitPane = JSplitPane().apply {
        leftComponent = JPanel(FlowLayout()).apply {
            add(JPanel(GridLayout(0, 1, 0, 0)).apply {
                border = TitledBorder("工具")
                add(btnArrow)
                add(btnLine)
                add(btnRectangle)
                add(btnOval)
                add(btnLineColor)
            })
        }
        rightComponent = JScrollPane(JPanel(null).apply {
            add(panelCanvas)
            background = Color.GRAY
            preferredSize = Dimension(600, 400)
        })
    }

    init {
        title = "画图"
        defaultCloseOperation = DISPOSE_ON_CLOSE
        setSize(800, 600)
        jMenuBar = menuBar
        add(splitPane)
    }
}

fun main(args: Array<String>) {
    try {
        UIManager.setLookAndFeel(javax.swing.plaf.nimbus.NimbusLookAndFeel::class.java.name)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val window = MainWindow()
    window.isVisible = true
}